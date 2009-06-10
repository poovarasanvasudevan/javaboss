/*
 * Copyright 2008 Jellymold.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jellymold.boss;

import com.jellymold.boss.util.BOSSException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * Encapsulates a Yahoo! BOSS web search.
 */
public class WebSearch extends BOSSSearch {

    protected static String endPoint = "/ysearch/web/v1/";

    private Set<WebSearchFilter> webSearchFilters = new HashSet<WebSearchFilter>();

    private List<WebSearchResult> resultWebs;

    /**
     *
     * Performs a search based upon the values of
     * the searchString and filters (if any) for this
     * instance
     *
     * @return  - HTTP response code
     */
    public int search() {
        return search(getSearchString(), getFilters());
    }

    /**
     *
     * Performs a search based upon the filters (if any) for this
     * instance and the searched string passed to this method.
     *
     * Sets the value of search string for this instance.
     *
     * @param search - the string to search with
     * @return - HTTP response code
     */
    public int search(String search) {
        return search(search, getFilters());
    }

    /**
     * Performs a search based on the values of parameters search and filters
     *
     * @param search - search string
     * @param webSearchFilters - set of filters
     * @return - HTTP response code
     * @throws BOSSException runtime exception
     */
    public int search(String search, Set<WebSearchFilter> webSearchFilters) throws BOSSException {

        this.setSearchString(search);

        String params = "appid=" + getAppKey();

        if (webSearchFilters != null) {
            this.setFilters(webSearchFilters);
            for (WebSearchFilter f : webSearchFilters) {
                params += "&filter=" + f.getUrlPart();
            }
        }

        try {

            setResponseCode(getHttpRequest().sendGetRequest(getServer() + endPoint + URLEncoder.encode(this.getSearchString()) + "?" + params));
            if(HTTP_OK==getResponseCode()){
                JSONObject searchResults = new JSONObject(getHttpRequest().getResponseBody()).getJSONObject("ysearchresponse");
                this.parseResults(searchResults);
            }

        } catch (JSONException e) {
            setResponseCode(500);
            throw new BOSSException("JSON Exception parsing news search results", e);
        } catch (IOException ioe) {
            setResponseCode(500);
            throw new BOSSException("IO Exception", ioe);
        }

        return getResponseCode();

    }

    protected void parseResults(JSONObject jobj) throws JSONException {

        if (jobj != null) {

            
            setResponseCode(jobj.getInt("responsecode"));
            if (jobj.has("nextpage")) setNextPage(jobj.getString("nextpage"));
            if (jobj.has("prevpage")) setPrevPage(jobj.getString("prevpage"));
            setTotalResults(jobj.getLong("totalhits"));
	    long count = jobj.getLong("count");
            setPagerCount(count);
            setPagerStart(jobj.getLong("start"));
            this.setResults(new ArrayList<WebSearchResult>(((int) count)));
            if(jobj.has("resultset_web")){
                JSONArray res = jobj.getJSONArray("resultset_web");
                for (int i = 0; i < res.length(); i++) {
                    JSONObject thisResult = res.getJSONObject(i);
                    WebSearchResult newResultWeb = new WebSearchResult();
                    newResultWeb.setDescription(thisResult.getString("abstract"));
                    newResultWeb.setClickUrl(thisResult.getString("clickurl"));
                    newResultWeb.setDate(thisResult.getString("date"));
                    newResultWeb.setTitle(thisResult.getString("title"));
                    newResultWeb.setDisplayUrl(thisResult.getString("dispurl"));
                    newResultWeb.setUrl(thisResult.getString("url"));
                    newResultWeb.setSize(thisResult.getLong("size"));
                    this.resultWebs.add(newResultWeb);
                }
            }

        }

    }


    public Set<WebSearchFilter> getFilters() {
        return webSearchFilters;
    }

    public void setFilters(Set<WebSearchFilter> webSearchFilters) {
        if(null!=webSearchFilters){
            this.webSearchFilters = webSearchFilters;
        }
    }

    public List<WebSearchResult> getResults() {
        return resultWebs;
    }

    public void setResults(List<WebSearchResult> resultWebs) {
        if(null!=resultWebs){
            this.resultWebs = resultWebs;
        }
    }

     /**
     *
     * @return - the end point for an web search
     */
    public static String getEndPoint() {
        return endPoint;
    }

    /**
     * Allows you to override the web search endpoint
     *
     * Defaults to /ysearch/web/v1/
     *
     * @param endPoint - the new end point for an image search
     */
    public static void setEndPoint(String endPoint) {
        if(null!=endPoint){
            WebSearch.endPoint = endPoint;
        }
    }

    /**
     * Inner class (enum) representing the various filter options.
     * used for convenience.
     */
    public static enum WebSearchFilter {

        PORN,HATE;

        private String[] names = {"PORN", "HATE"};

        private String[] labels = {"Porn", "Hate"};

        private String[] urlParts = {"-porn", "-hate"};

        public String toString() {
            return getName();
        }

        public String getName() {
            return names[this.ordinal()];
        }

        public String getLabel() {
            return labels[this.ordinal()];
        }

        public String getUrlPart() {
            return urlParts[this.ordinal()];
        }

        public static Map<String, String> getChoices() {
            Map<String, String> choices = new LinkedHashMap<String, String>();
            for (WebSearchFilter webSearchFilter : WebSearchFilter.values()) {
                choices.put(webSearchFilter.getName(), webSearchFilter.getLabel());
            }
            return choices;
        }

    }
}
