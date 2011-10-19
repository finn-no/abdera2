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
package org.apache.abdera2.protocol.server.impl;

import java.io.IOException;
import org.apache.abdera2.Abdera;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.common.protocol.AbstractCollectionAdapter;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.ResponseContextException;
import org.apache.abdera2.model.Collection;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.parser.Parser;
import org.apache.abdera2.protocol.server.AtompubCollectionAdapter;
import org.apache.abdera2.protocol.server.AtompubMediaCollectionAdapter;
import org.apache.abdera2.protocol.server.context.AtompubRequestContext;
import org.apache.abdera2.protocol.server.context.FOMResponseContext;
import org.apache.abdera2.protocol.server.model.AtompubCategoriesInfo;
import org.apache.abdera2.protocol.server.model.AtompubCollectionInfo;
import org.joda.time.DateTime;

/**
 * Base CollectionAdapter implementation that provides a number of helper utility methods for adapter implementations.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractAtompubCollectionAdapter 
  extends AbstractCollectionAdapter
  implements AtompubCollectionAdapter, 
             AtompubMediaCollectionAdapter,
             AtompubCollectionInfo {

      public String[] getAccepts(RequestContext request) {
        return new String[] {"application/atom+xml;type=entry"};
      }
    
    public AtompubCategoriesInfo[] getCategoriesInfo(RequestContext request) {
        return null;
    }
    
    public <S extends ResponseContext>S getCategories(RequestContext request) {
        return null;
    }
  
    /**
     * Creates the ResponseContext for a newly created entry. By default, a BaseResponseContext is returned. The
     * Location, Content-Location, Etag and status are set appropriately.
     */
    protected <S extends ResponseContext>S buildCreateEntryResponse(String link, Entry entry) {
        FOMResponseContext<Entry> rc = new FOMResponseContext<Entry>(entry);
        rc.setLocation(link);
        rc.setContentLocation(rc.getLocation().toString());
        rc.setEntityTag(AbstractAtompubProvider.calculateEntityTag(entry));
        rc.setStatus(201);
        return (S)rc;
    }

    /**
     * Creates the ResponseContext for a newly created entry. By default, a BaseResponseContext is returned. The
     * Location, Content-Location, Etag and status are set appropriately.
     */
    protected <S extends ResponseContext>S buildPostMediaEntryResponse(String link, Entry entry) {
        return (S)buildCreateEntryResponse(link, entry);
    }

    /**
     * Creates the ResponseContext for a GET entry request. By default, a BaseResponseContext is returned. The Entry
     * will contain an appropriate atom:source element and the Etag header will be set.
     */
    protected <S extends ResponseContext>S buildGetEntryResponse(RequestContext request, Entry entry)
        throws ResponseContextException {
        Feed feed = createFeedBase(request);
        entry.setSource(feed.getAsSource());
        Document<Entry> entry_doc = entry.getDocument();
        FOMResponseContext<Document<Entry>> rc = new FOMResponseContext<Document<Entry>>(entry_doc);
        rc.setEntityTag(AbstractAtompubProvider.calculateEntityTag(entry));
        return (S)rc;
    }

    /**
     * Creates the ResponseContext for a GET feed request. By default, a BaseResponseContext is returned. The Etag
     * header will be set.
     */
    protected <S extends ResponseContext>S buildGetFeedResponse(Feed feed) {
        Document<Feed> document = feed.getDocument();
        FOMResponseContext<Document<Feed>> rc = new FOMResponseContext<Document<Feed>>(document);
        rc.setEntityTag(AbstractAtompubProvider.calculateEntityTag(document.getRoot()));
        return (S)rc;
    }

    /**
     * Create the base feed for the requested collection.
     */
    protected Feed createFeedBase(RequestContext request) throws ResponseContextException {
        Factory factory = AbstractAtompubProvider.getAbdera(request).getFactory();
        Feed feed = factory.newFeed();
        feed.setId(getId(request));
        feed.setTitle(getTitle(request));
        feed.addLink("");
        feed.addLink("", "self");
        feed.addAuthor(getAuthor(request));
        feed.setUpdated(DateTime.now());
        return feed;
    }

    /**
     * Retrieves the FOM Entry object from the request payload.
     */
    protected Entry getEntryFromRequest(RequestContext request) throws ResponseContextException {
        Abdera abdera = AbstractAtompubProvider.getAbdera(request);
        Parser parser = abdera.getParser();

        Document<Entry> entry_doc;
        try {
            AtompubRequestContext context = (AtompubRequestContext) request;
            entry_doc = (Document<Entry>)context.getDocument(parser).clone();
        } catch (ParseException e) {
            throw new ResponseContextException(400, e);
        } catch (IOException e) {
            throw new ResponseContextException(500, e);
        }
        if (entry_doc == null) {
            return null;
        }
        return entry_doc.getRoot();
    }

    public Collection asCollectionElement(RequestContext request) {
        Collection collection = AbstractAtompubProvider.getAbdera(request).getFactory().newCollection();
        collection.setHref(getHref(request));
        collection.setTitle(getTitle(request));
        collection.setAccept(getAccepts(request));
        for (AtompubCategoriesInfo catsinfo : getCategoriesInfo(request)) {
            collection.addCategories(catsinfo.asCategoriesElement(request));
        }
        return collection;
    }
}
