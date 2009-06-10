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

import com.jellymold.boss.WebSearch;
import junit.framework.TestCase;

public class SearchTest extends TestCase {

    public SearchTest(){

    }

    public void setUp(){
        //nothing to do
    }

    public void tearDown(){
        //nothing to do
    }

    public void testDuffAppKey(){
        WebSearch ws = new WebSearch();
        ws.setAppKey("I am a totally duff'd and invalid app key"); 
        assertEquals(400, ws.search("duff beer"));
        ws.setAppKey("12345");
        assertEquals(403, ws.search("duff beer"));
        //ws.setAppKey("ENTER A REAL API KEY HERE AND UNCOMMENT THESE LINES TO RUN THIS TEST");
        //assertEquals(200, ws.search("duff beer"));
    }

}
