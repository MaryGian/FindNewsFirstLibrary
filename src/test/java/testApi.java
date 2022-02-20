import model.NewsInfo;
import org.junit.Assert;
import org.junit.Test;
import services.NewsApiService;
import Exception.NewsApiException;
import java.net.URISyntaxException;
import java.util.List;


public class testApi {
    @Test
    public void testYourTopHeadlinesAPI() throws NewsApiException {
        final NewsApiService newsSearchService = NewsApi.getNewsApiService(); // initialize and create object NewsApiService
        final List<NewsInfo> results = newsSearchService.getYourTopHeadlines("gr","business");
        Assert.assertFalse(results.isEmpty());
        results.forEach(System.out::println);
    }

    @Test
    public void testYourTopHeadlinesWrongValuesAPI() throws NewsApiException {
        final NewsApiService newsSearchService = NewsApi.getNewsApiService(); // initialize and create object NewsApiService
        final List<NewsInfo> results = newsSearchService.getYourTopHeadlines("","");
     //   Assert.assertFalse(results.isEmpty());
        results.forEach(System.out::println);
    }

    @Test
    public void testEverythingAPI() throws NewsApiException {
        final NewsApiService newsSearchService = NewsApi.getNewsApiService(); // initialize and create object NewsApiService
        final List<NewsInfo> results = newsSearchService.getEverything("en","bbc-news",null,null,null);
        Assert.assertFalse(results.isEmpty());
        results.forEach(System.out::println);
    }

    @Test
    public void testEverythingWrongValuesAPI() throws NewsApiException {
        final NewsApiService newsSearchService = NewsApi.getNewsApiService(); // initialize and create object NewsApiService
        final List<NewsInfo> results = newsSearchService.getEverything("eng","bbc-news",null,null,null);
        // Assert.assertFalse(results.isEmpty());
        results.forEach(System.out::println);
    }

    @Test
    public void testSources() throws NewsApiException{
        final  NewsApiService newsApiService = NewsApi.getNewsApiService();
        System.out.println(newsApiService.getSources("en","science"));
    }

    @Test
    public void testSourcesWrongValues() throws NewsApiException{
        final  NewsApiService newsApiService = NewsApi.getNewsApiService();
        System.out.println(newsApiService.getSources("endav",""));
    }

    }


