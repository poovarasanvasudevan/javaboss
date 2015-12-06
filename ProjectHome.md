This is a simple Java wrapper for the [Yahoo! Search BOSS](http://developer.yahoo.com/search/boss/) API.

It allows web, image and news searching and returns the results as simple POJOs.

For simplicity and minimal dependencies it uses java.net.HTTPURLConnection to send requests to BOSS.  This can be easily extended to use another http implementation (e.g. Apache Commons HttpClient)

It supports paging the results as well as the optional BOSS filters (where applicable) for filtering out pornographic and hate related materials.

Example usage :

```

WebSearch ws = new WebSearch();

ws.search("lolcats");

System.out.println("Total hits : " + ws.getTotalResults());

List<WebSearchResult> results = ws.getResults();

for(WebSearchResult result : results){
     System.out.println(result.getTitle());
}

```

The produced library is dependant upon org.json.jar for parsing the returned results, for ease of use this gets bundled into the boss.jar

The only other dependency is on junit.

In order to use this you will need a Yahoo! BOSS API key, which you can get from [Yahoo! BOSS](http://developer.yahoo.com/search/boss/)

You will need to enter your API KEY into the BOSSSearch.java source before building the library for use in your own projects.