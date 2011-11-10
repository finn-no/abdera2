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
package org.apache.abdera2.activities.protocol.managed;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.common.protocol.RequestContext;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;

public class FeedConfiguration extends Configuration implements CollectionInfo {
    public static final String PROP_NAME_ADAPTER_CLASS = "adapterClassName";
    public static final String PROP_SUB_URI_NAME = "subUri";
    public static final String PROP_AUTHOR_NAME = "author";
    public static final String PROP_TITLE_NAME = "title";
    public static final String PROP_ACCEPTS = "accepts";
    public static final String PROP_ENTRY_TITLE_NAME = "entryTitle";
    public static final String PROP_FEED_CONFIG_LOCATION_NAME = "configFile";

    public static final String ENTRY_ELEM_NAME_ID = "id";
    public static final String ENTRY_ELEM_NAME_TITLE = "title";
    public static final String ENTRY_ELEM_NAME_CONTENT = "content";
    public static final String ENTRY_ELEM_NAME_AUTHOR = "author";
    public static final String ENTRY_ELEM_NAME_UPDATED = "updated";
    public static final String ENTRY_ELEM_NAME_LINK = "link";

    public static class Generator implements Supplier<FeedConfiguration> {

      String feedId;
      String subUri;
      String adapterClassName;
      String feedConfigLocation;
      ServerConfiguration serverConfiguration;
      String feedTitle = "unknown";
      String feedAuthor = "unknown";
      final ImmutableMap.Builder<Object,Object> optionalProperties =
        ImmutableMap.builder();
      CollectionAdapterConfiguration adapterConfiguration;      
      
      public Generator id(String id) {
        this.feedId = id;
        return this;
      }
      
      public Generator subUri(String uri) {
        this.subUri = uri;
        return this;
      }
      
      public Generator adapter(String className) {
        this.adapterClassName = className;
        return this;
      }
      
      public Generator location(String location) {
        this.feedConfigLocation = location;
        return this;
      }
      
      public Generator serverConfig(ServerConfiguration config) {
        this.serverConfiguration = config;
        return this;
      }
      
      public Generator title(String title) {
        this.feedTitle = title;
        return this;
      }
      
      public Generator author(String author) {
        this.feedAuthor = author;
        return this;
      }
      
      public Generator set(Object key, Object val) {
        this.optionalProperties.put(key, val);
        return this;
      }
      
      public Generator setAll(Map<Object,Object> props) {
        this.optionalProperties.putAll(props);
        return this;
      }
      
      public Generator adapterConfif(CollectionAdapterConfiguration config) {
        this.adapterConfiguration = config;
        return this;
      }
      
      public FeedConfiguration get() {
        return new FeedConfiguration(this);
      }
      
    }
    
    public static Generator make() {
      return new Generator();
    }
    
    private final String feedId;
    private final String subUri;
    private final String adapterClassName;
    private final String feedConfigLocation;
    private final ServerConfiguration serverConfiguration;
    private final String feedTitle;
    private final String feedAuthor;
    private final ImmutableMap<Object, Object> optionalProperties;
    private final CollectionAdapterConfiguration adapterConfiguration;

    protected FeedConfiguration(Generator gen) {
      this.feedId = gen.feedId;
      this.subUri = gen.subUri;
      this.adapterClassName = gen.adapterClassName;
      this.feedConfigLocation = gen.feedConfigLocation;
      this.serverConfiguration = gen.serverConfiguration;
      this.feedTitle = gen.feedTitle;
      this.feedAuthor = gen.feedAuthor;
      this.optionalProperties = gen.optionalProperties.build();
      this.adapterConfiguration = gen.adapterConfiguration;
    }

    public static FeedConfiguration getFeedConfiguration(
        String feedId,
        Properties properties,
        ServerConfiguration serverConfiguration) {
        
        String author = "unknown";
        String title = "unknown";
        
        if (properties.containsKey(PROP_AUTHOR_NAME))
          author = Configuration.getProperty(properties, PROP_AUTHOR_NAME);
        if (properties.containsKey(PROP_TITLE_NAME))
          title = Configuration.getProperty(properties, PROP_TITLE_NAME);
        
        return FeedConfiguration
          .make()
            .id(feedId)
            .subUri(Configuration.getProperty(properties, PROP_SUB_URI_NAME))
            .adapter(Configuration.getProperty(properties, PROP_NAME_ADAPTER_CLASS))
            .location(Configuration.getProperty(properties, PROP_FEED_CONFIG_LOCATION_NAME))
            .serverConfig(serverConfiguration)
            .title(title)
            .author(author)
            .setAll(properties)
          .get();
      }

    public String getAdapterClassName() {
        return adapterClassName;
    }

    public String getFeedAuthor() {
        return feedAuthor;
    }

    public String getFeedConfigLocation() {
        return feedConfigLocation;
    }

    public String getFeedId() {
        return feedId;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public String getSubUri() {
        return subUri;
    }

    public String getFeedUri() {
        return serverConfiguration.getServerUri() + "/" + getSubUri();
    }

    public boolean hasProperty(String key) {
        return optionalProperties.containsKey(key);
    }

    public Object getProperty(String key) {
        return optionalProperties.get(key);
    }

    public CollectionAdapterConfiguration getAdapterConfiguration() {
        return adapterConfiguration;
    }
    
    public Iterable<String> getAccepts(RequestContext request) {
        Object accepts = optionalProperties.get(PROP_ACCEPTS);
        String[] arr = null;
        if (accepts == null || !(accepts instanceof String))
            arr = new String[] {"application/json"};
        else arr = ((String)accepts).split("\\s*,\\s*");
        return Arrays.<String>asList(arr);
    }

    public String getHref(RequestContext request) {
        return getFeedUri();
    }

    public String getTitle(RequestContext request) {
        return getFeedTitle();
    }

    public ServerConfiguration getServerConfiguration() {
        return adapterConfiguration.getServerConfiguration();
    }
}
