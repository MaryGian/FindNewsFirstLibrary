package services;

import Exception.NewsApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.NewsInfo;
import model.ipApi.Error;
import model.ipApi.IpResults;
import model.newsapi.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class NewsApiService {
    private final String API_KEY;
    private final String API_URL;
    private final String API_KEY_FOR_IP;
    private final String API_URL_FOR_IP;


    public NewsApiService(String API_KEY, String API_URL, String API_KEY_FOR_IP, String API_URL_FOR_IP) {
        this.API_KEY = API_KEY;
        this.API_URL = API_URL;
        this.API_KEY_FOR_IP = API_KEY_FOR_IP;
        this.API_URL_FOR_IP = API_URL_FOR_IP;
    }


    public List<NewsInfo> getYourTopHeadlines(String country, String category)
            throws NewsApiException {
        NewsResults totalResults = getApiData("top-headlines", country, category, null, null, null, null, null);
        // the object totalResult it takes as a list the fields of NewsResults that have been returned

        List<NewsInfo> newsInfoList = new ArrayList<>(totalResults.getArticles().size()); // initializes the width
        // we want only the data about articles so from the list of the total results we use the method .getArticles
        // which retrieves the nested lists of articles (every list that we take using .getArticles is an article object)
        for (Article theArticles : totalResults.getArticles()) {
            newsInfoList.add(new NewsInfo(theArticles)); // for each articles' details we create an object NewsInfo
            // and we add them to the returning list which we will finally get
        }
        return newsInfoList;
    }

    public List<NewsInfo> getEverything(String language, String sources, String from_date, String to_date, String parameterq)
            throws NewsApiException {
        NewsResults totalResults = getApiData("everything", null, null, language, sources, from_date, to_date, parameterq);
        List<NewsInfo> newsInfoList = new ArrayList<>(totalResults.getArticles().size());
        for (Article theArticles : totalResults.getArticles()) {
            newsInfoList.add(new NewsInfo(theArticles));
        }
        return newsInfoList;


    }


    // getSources returns a string to use when we want category from endpoint everything
    // so here, getSources returns a comma-separated string of all the sources of a specific category
    // we can't use getApiData for getting the results because endpoint sources returns a different type of Json objects
    public String getSources(String language, String category) throws NewsApiException{

        String sourcesToString;
        StringBuilder stringBuilder= new StringBuilder();
        final URIBuilder uribuilder;
        try {
            uribuilder = new URIBuilder(API_URL).setPathSegments("v2","top-headlines","sources")
                    .addParameter("apiKey", API_KEY);

            if (language != null && !language.isEmpty()) {
                uribuilder.addParameter("language", language);
            }
            if (category!=null && !language.isEmpty()) {
                uribuilder.addParameter("category", category);
            }
            final URI uri = uribuilder.build(); // build the final uri

            final HttpGet getRequest = new HttpGet(uri); // create a uri request
            final CloseableHttpClient httpclient = HttpClients.createDefault(); // create a http client to be able to get the request

            try (CloseableHttpResponse response = httpclient.execute(getRequest)) {
                final HttpEntity entity = response.getEntity();
                final ObjectMapper mapper = new ObjectMapper(); // mapper object to serialize the  entity we get

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    ErrorResponse errorResponse = mapper.readValue(entity.getContent(), ErrorResponse.class);
                    if (!errorResponse.getStatus().equals("ok") )
                        throw new NewsApiException("Error occurred on API call: " + errorResponse.getMessage());
                }

                NewsResultsSourcesEndpoint totalResults=mapper.readValue(entity.getContent(), NewsResultsSourcesEndpoint.class);

                ArrayList<String> listOfSources= new ArrayList<>();
                for (SourceSourceEndpoint source:totalResults.getSources()) {
                    if (!source.getId().equals(null) && !listOfSources.contains(source.getId())) {
                        listOfSources.add(source.getId());  // Do not put multiple times the same source
                        stringBuilder.append(source.getId());
                        stringBuilder.append(",");
                    }
                }
                if (stringBuilder.length()>0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                sourcesToString=stringBuilder.toString();
                return sourcesToString;
            } catch (IOException e) {
                throw new NewsApiException("Error requesting data from the News API.", e);
            }

        } catch (URISyntaxException e) {
            throw new NewsApiException("Something went wrong with the connection", e);
        }catch ( IndexOutOfBoundsException M){
            throw new NewsApiException("You have no results "+M.getMessage());
        }


    }




    // getApiData is a method which creates the uri and the hhtp client and returns the serialized results for the News api
    private NewsResults getApiData(String api_function, String country, String category, String language,
                                   String sources, String from_date, String to_date, String parameterq)
            throws NewsApiException {

        final URIBuilder uribuilder;
        try {
            uribuilder = new URIBuilder(API_URL).setPathSegments("v2", api_function)
                    .addParameter("apiKey", API_KEY);

            //  you either choose top-headlines searching by country and category or choose everything and search from-to date
            //  by language and by source

            switch (api_function) {

                //for each case empty queries will be excluding
                case "top-headlines":
                    if (country != null && !country.isEmpty()) {
                        uribuilder.addParameter("country", country);
                    } else {
                        // if no country is chosen it will choose the country based on the ip address using the method
                        // getIpData()
                        uribuilder.addParameter("country", getIpData());
                    }
                    if (category != null && !category.isEmpty())
                        uribuilder.addParameter("category", category);
                    break;
                case "everything":
                    if (sources != null && !sources.isEmpty()) {
                        uribuilder.addParameter("sources", sources);
                    }
                    if (from_date != null && !from_date.isEmpty()) {
                        uribuilder.addParameter("from", from_date);
                    }
                    if (to_date != null && !to_date.isEmpty()) {
                        uribuilder.addParameter("to", to_date);
                    }
                    if (language != null && !language.isEmpty()) {
                        uribuilder.addParameter("language", language);
                    }
                    if (parameterq != null && !parameterq.isEmpty()) {
                        uribuilder.addParameter("q", parameterq);
                    }
                    break;

            }


            final URI uri = uribuilder.build(); // build the final uri

            final HttpGet getRequest = new HttpGet(uri); // create a uri request
            final CloseableHttpClient httpclient = HttpClients.createDefault(); // create a http client to be able to get the request

            try (CloseableHttpResponse response = httpclient.execute(getRequest)) {
                final HttpEntity entity = response.getEntity();
                final ObjectMapper mapper = new ObjectMapper(); // mapper object to serialize the  entity we get

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    ErrorResponse errorResponse = mapper.readValue(entity.getContent(), ErrorResponse.class);
                    if (errorResponse.getStatus().equals("ok") )
                        throw new NewsApiException("Error occurred on API call: " + errorResponse.getMessage());
                }

                return mapper.readValue(entity.getContent(), NewsResults.class);  // the results are  NewsResult objects
            } catch (IOException e) {
                throw new NewsApiException("Error requesting data from the News API.", e);
            }

        } catch (URISyntaxException e) {
            throw new NewsApiException("Something went wrong with the connection", e);
        }

    }







// getIpData returns, based on the IP address, the country code as a string.
// choosing the parameter (field= country_code) the json has only the country code in and
    public String getIpData() throws NewsApiException {
        final URIBuilder uribuilder;

        try {
            uribuilder = new URIBuilder(API_URL_FOR_IP).setPathSegments("v1").addParameter("api_key", API_KEY_FOR_IP)
                    .addParameter("fields", "country_code");
            final URI uri = uribuilder.build(); // build the final uri like for the getApiData

            final HttpGet getRequest = new HttpGet(uri);
            final CloseableHttpClient httpclient = HttpClients.createDefault();

            try (CloseableHttpResponse response = httpclient.execute(getRequest)) {
                final HttpEntity entity = response.getEntity();
                final ObjectMapper mapper = new ObjectMapper();
                    // until here same as before
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    Error errorForIp = mapper.readValue(entity.getContent(), Error.class);
                    if (errorForIp.getCode() != "200") // this code means that everything is ok
                        throw new NewsApiException("Error occurred on IP API call: " + errorForIp.getCode());
                }

                return mapper.readValue(entity.getContent(), IpResults.class).getCountryCode();
            } catch (IOException e) {
                throw new NewsApiException("Error requesting data from the News API.", e);
            }

        } catch (URISyntaxException e) {
            throw new NewsApiException("Unable to connect with Ip Api", e);
        }
    }
}