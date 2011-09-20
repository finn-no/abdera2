/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.examples.ext;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.templates.MapContext;
import org.apache.abdera2.common.templates.Template;
import org.apache.abdera2.ext.history.FeedPagingHelper;
import org.apache.abdera2.model.Feed;

public class Paging {

    private static final Template template = new Template("feed{?page}");
  
    private static String gen(int page) {
      MapContext mc = new MapContext();
      if (page > -1)
        mc.put("page", page);
      return template.expand(mc);
    }
    
    public static void main(String... args) throws Exception {

        Abdera abdera = Abdera.getInstance();
        Feed feed = abdera.newFeed();
        

        // Set/Get the paging links
        FeedPagingHelper.setCurrent(feed, gen(-1));
        FeedPagingHelper.setNext(feed, gen(3));
        FeedPagingHelper.setPrevious(feed, gen(1));
        FeedPagingHelper.setFirst(feed, gen(-1));
        FeedPagingHelper.setLast(feed, gen(10));
        FeedPagingHelper.setNextArchive(feed, gen(3));
        FeedPagingHelper.setPreviousArchive(feed, gen(1));

        System.out.println(FeedPagingHelper.getCurrent(feed));
        System.out.println(FeedPagingHelper.getNext(feed));
        System.out.println(FeedPagingHelper.getPrevious(feed));
        System.out.println(FeedPagingHelper.getFirst(feed));
        System.out.println(FeedPagingHelper.getLast(feed));
        System.out.println(FeedPagingHelper.getNextArchive(feed));
        System.out.println(FeedPagingHelper.getPreviousArchive(feed));

        // Set/Get the archive flag
        FeedPagingHelper.setArchive(feed, true);
        if (FeedPagingHelper.isArchive(feed))
            System.out.println("archive feed!");

        // Set/Get the complete flag
        FeedPagingHelper.setComplete(feed, true);
        if (FeedPagingHelper.isComplete(feed))
            System.out.println("complete feed!");

    }

}
