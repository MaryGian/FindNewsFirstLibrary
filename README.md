# FindNewsFirstLibrary
This is a java library for finding news through NewsApi. Note that is a maven project. Before you use it you have to register to NewsAPI and Abstract API
and receive your primaries keys.
At the class NewsApi you write your api keys at the appropiate place. (NewsApi key goes to API_KEY field and Abstract Api key goes to API_KEY_FOR_IP)
After that the library is ready.
If you want to use the library for your own project read about its use bellow.

Using FindNewsFirstLibrary:
The object you have to create to access the library is NewsApiService objectName = NewsApi.getNewsApiService().
Now the object that you have create, gives you access to all the methods you will need.
Methods:
1. getEverything(String language, String sources, String from_date, String to_date, String parameterq)->list\<NewsInfo>
  Uses the endpoint Everything.
  It receives as arguments the language at ISO ALPHA code, the source of the article, the date from and to for the published articles you want
  and a keyword for the search (parameterq). Because of api limitations you need to give not empty fields for either parameterq or language or source or all of the above
  in order to get results without any errors. This method returns a list of NewsInfo objects. Iterate over the list using a NewsInfo object to have access to the
  getters of the articles such as getTitle etc. The available getters are: getTitle,getContent,getDate,getUrl_page,getAuthors
  
2. getYourTopHeadlines(String country, String category)->List\<NewsInfo>
  Uses the endpoint Top-Headlines.
  It receives as arguments the country in ISO ALPHA code and the category of the article which can be one pf these:     business,entertainment,general,health,science,sports,technology. If the argument for country is empty the method uses the country of the user, through ip address, for the search.
  This method returns a list of NewsInfo objects. Iterate over the list using a NewsInfo object to have access to the
  getters of the articles such as getTitle etc.
  
3. getSources(String language, String category)->String
  Uses the endpoint Top-headlines\Sources.
  The category can be one of the above options and language as we mention must be in form of ISO ALPHA.
  It returns a string with all the sources comma seperated, which sources write about ay the specified category and particulary language.
  
4. getIpData()->String 
  This method returns the country code for users IP address.
  
  


