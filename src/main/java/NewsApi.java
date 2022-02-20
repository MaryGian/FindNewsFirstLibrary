import services.NewsApiService;

public class NewsApi {

    public static NewsApiService getNewsApiService(){
        return new NewsApiService("1e9acc98b0ee47bf8ad291d4219b68e7","https://newsapi.org","6d12f5f089fc4c90b1a36388c9ed21a9","https://ipgeolocation.abstractapi.com");
    }
    // this class and method create like a constructor the object of the class NewsApiService  and initializes the values of each
    // field (basically it uses the constructor NewsApiService at the return statement to create the object)

}
