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
package test;

import com.jellymold.boss.NewsSearch;
import com.jellymold.boss.NewsSearchResult;
import junit.framework.TestCase;

import java.util.List;

public class NewsSearchTest extends TestCase {

    public NewsSearchTest(){

    }

    public void setUp(){
        //nothing to do
    }

    public void tearDown(){
        //nothing to do
    }

    public void testNextPage(){
        NewsSearch ws = new NewsSearch();

        //if no search has been done should return a 500
        assertEquals(500,ws.getNextPage());

        //do simple search that's bound to return something
        ws.search("obama");

        List<NewsSearchResult> results = ws.getResults();

        //should return a 200
        assertEquals(200,ws.getNextPage());

        List<NewsSearchResult> results2 = ws.getResults();

        //page 2 shouldn't be the same as page 1
        assertFalse(results.equals(results2));
    }

    public void testPreviousPage(){
        NewsSearch ws = new NewsSearch();

        //if no search has been done should return a 500
        assertEquals(500,ws.getPreviousPage());

        //do simple search that's bound to return something
        ws.search("obama");

        List<NewsSearchResult> results = ws.getResults();

        //should return a 200
        assertEquals(200,ws.getNextPage());

        //should return a 200
        assertEquals(200,ws.getPreviousPage());

        List<NewsSearchResult> results2 = ws.getResults();

        //page 2 shouldn't be the same as page 1
        assertFalse(results.equals(results2));
    }
}
