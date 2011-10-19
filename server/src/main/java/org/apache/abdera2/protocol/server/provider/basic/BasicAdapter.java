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
package org.apache.abdera2.protocol.server.provider.basic;

import java.util.logging.Logger;

import javax.activation.MimeType;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.parser.Parser;
import org.apache.abdera2.protocol.server.context.AtompubRequestContext;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.protocol.server.provider.managed.FeedConfiguration;
import org.apache.abdera2.protocol.server.provider.managed.ManagedCollectionAdapter;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.ProviderHelper;
import org.apache.abdera2.common.protocol.Target;
import org.joda.time.DateTime;

/**
 * The BasicAdapter provides a simplistic interface for working with Atompub collections with a restricted set of
 * options/features. The idea of the basic adapter is to make it easy to provide a minimally capable Atompub server
 */
@SuppressWarnings("unchecked")
public abstract class BasicAdapter extends ManagedCollectionAdapter {

    public static Logger logger = Logger.getLogger(BasicAdapter.class.getName());

    protected BasicAdapter(Abdera abdera, FeedConfiguration config) {
        super(abdera,config);
    }

    public String getProperty(String key) throws Exception {
        Object val = config.getProperty(key);
        if (val == null) {
            logger.warning("Cannot find property " + key + "in Adapter properties file for feed " + config.getFeedId());
            throw new RuntimeException();
        }
        if (val instanceof String)
            return (String)val;
        throw new RuntimeException();
    }

    protected Feed createFeed() throws Exception {
        Feed feed = abdera.newFeed();
        feed.setId(config.getFeedUri());
        feed.setTitle(config.getFeedTitle());
        feed.setUpdated(DateTime.now());
        feed.addAuthor(config.getFeedAuthor());
        return feed;
    }

    protected void addEditLinkToEntry(Entry entry) throws Exception {
        if (AbstractAtompubProvider.getEditUriFromEntry(entry) == null) {
            entry.addLink(entry.getId().toString(), "edit");
        }
    }

    protected void setEntryIdIfNull(Entry entry) throws Exception {
        // if there is no id in Entry, assign one.
        if (entry.getId() != null) {
            return;
        }
        String uuidUri = abdera.getFactory().newUuidUri();
        String[] segments = uuidUri.split(":");
        String entryId = segments[segments.length - 1];
        entry.setId(createEntryIdUri(entryId));
    }

    protected String createEntryIdUri(String entryId) throws Exception {
        return config.getFeedUri() + "/" + entryId;
    }

    private <S extends ResponseContext>S createOrUpdateEntry(RequestContext context, boolean createFlag) {
        try {
            AtompubRequestContext request = (AtompubRequestContext) context;
            MimeType mimeType = request.getContentType();
            String contentType = mimeType == null ? null : mimeType.toString();
            if (contentType != null && !MimeTypeHelper.isAtom(contentType) && !MimeTypeHelper.isXml(contentType))
                return (S)ProviderHelper.notsupported(request);
            Abdera abdera = request.getAbdera();
            Parser parser = abdera.getParser();
            Entry inputEntry = (Entry)request.getDocument(parser).getRoot();
            Target target = request.getTarget();
            String entryId = !createFlag ? target.getParameter(BasicProvider.PARAM_ENTRY) : null;
            Entry newEntry = createFlag ? createEntry(inputEntry) : updateEntry(entryId, inputEntry);
            if (newEntry != null) {
                Document<Entry> newEntryDoc = newEntry.getDocument();
                String loc = newEntry.getEditLinkResolvedHref().toString();
                return (S)AbstractAtompubProvider.returnBase(newEntryDoc, createFlag ? 201 : 200, null).setLocation(loc);
            } else {
                return (S)ProviderHelper.notfound(request);
            }
        } catch (Exception e) {
            return (S)ProviderHelper.servererror(context, e.getMessage(), e);
        }
    }

    public <S extends ResponseContext>S postItem(RequestContext request) {
        return (S)createOrUpdateEntry(request, true);
    }

    public <S extends ResponseContext>S deleteItem(RequestContext request) {
        Target target = request.getTarget();
        String entryId = target.getParameter(BasicProvider.PARAM_ENTRY);
        try {
            return (S)(deleteEntry(entryId) ? ProviderHelper.nocontent() : ProviderHelper.notfound(request));
        } catch (Exception e) {
            return (S)ProviderHelper.servererror(request, e.getMessage(), e);
        }
    }

    public <S extends ResponseContext>S putItem(RequestContext request) {
        return (S)createOrUpdateEntry(request, false);
    }

    public <S extends ResponseContext>S getItem(RequestContext request) {
        Target target = request.getTarget();
        String entryId = target.getParameter(BasicProvider.PARAM_ENTRY);
        try {
            Entry entry = getEntry(entryId);
            return (S)(entry != null ? AbstractAtompubProvider.returnBase(entry.getDocument(), 200, null) : ProviderHelper
                .notfound(request));
        } catch (Exception e) {
            return (S)ProviderHelper.servererror(request, e.getMessage(), e);
        }
    }

    public <S extends ResponseContext>S getItemList(RequestContext request) {
        try {
            Feed feed = getFeed();
            return feed != null ? (S)AbstractAtompubProvider.returnBase(feed.getDocument(), 200, null) : 
              (S)ProviderHelper.notfound(request);
        } catch (Exception e) {
            return (S)ProviderHelper.servererror(request, e.getMessage(), e);
        }
    }

    public <S extends ResponseContext>S extensionRequest(RequestContext request) {
        return (S)ProviderHelper.notallowed(request, ProviderHelper.getDefaultMethods(request));
    }

    public <S extends ResponseContext>S getCategories(RequestContext request) {
        return (S)ProviderHelper.notfound(request);
    }

    public abstract Feed getFeed() throws Exception;

    public abstract Entry getEntry(Object entryId) throws Exception;

    public abstract Entry createEntry(Entry entry) throws Exception;

    public abstract Entry updateEntry(Object entryId, Entry entry) throws Exception;

    public abstract boolean deleteEntry(Object entryId) throws Exception;

}
