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
package org.apache.abdera2.test.server.custom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.RequestContext.Scope;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.ProviderHelper;
import org.apache.abdera2.common.protocol.ResponseContextException;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.text.UrlEncoding;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.protocol.server.context.AtompubRequestContext;
import org.apache.abdera2.protocol.server.context.FOMResponseContext;
import org.apache.abdera2.protocol.server.context.StreamWriterResponseContext;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubCollectionAdapter;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.writer.StreamWriter;
import org.joda.time.DateTime;

@SuppressWarnings("unchecked")
public class SimpleAdapter extends AbstractAtompubCollectionAdapter {

    @Override
    public String getAuthor(RequestContext request) throws ResponseContextException {
        return "Simple McGee";
    }

    @Override
    public String getId(RequestContext request) {
        return "tag:example.org,2008:feed";
    }

    public String getHref(RequestContext request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("collection", "feed");
        return request.urlFor(TargetType.TYPE_COLLECTION, params);
    }

    public String getTitle(RequestContext request) {
        return "A simple feed";
    }

    public ResponseContext extensionRequest(RequestContext request) {
        return ProviderHelper.notallowed(request, "Method Not Allowed", ProviderHelper.getDefaultMethods(request));
    }

    private Document<Feed> getFeedDocument(RequestContext context) throws ResponseContextException {
        Feed feed = (Feed)context.getAttribute(Scope.SESSION, "feed");
        if (feed == null) {
            feed = createFeedBase(context);
            feed.setBaseUri(getHref(context));
            context.setAttribute(Scope.SESSION, "feed", feed);
        }
        return feed.getDocument();
    }

    public ResponseContext getItemList(RequestContext request) {
        Document<Feed> feed;
        try {
            feed = getFeedDocument(request);
        } catch (ResponseContextException e) {
            return e.getResponseContext();
        }

        return AbstractAtompubProvider.returnBase(feed, 200, feed.getRoot().getUpdated()).setEntityTag(AbstractAtompubProvider
            .calculateEntityTag(feed.getRoot()));
    }

    public ResponseContext deleteItem(RequestContext request) {
        Entry entry = getAbderaEntry(request);
        if (entry != null)
            entry.discard();
        return ProviderHelper.nocontent();
    }

    public ResponseContext getItem(RequestContext request) {
        Entry entry = (Entry)getAbderaEntry(request);
        if (entry != null) {
            Feed feed = entry.getParentElement();
            entry = (Entry)entry.clone();
            entry.setSource(feed.getAsSource());
            Document<Entry> entry_doc = entry.getDocument();
            return AbstractAtompubProvider.returnBase(entry_doc, 200, entry.getEdited()).setEntityTag(AbstractAtompubProvider
                .calculateEntityTag(entry));
        } else {
            return ProviderHelper.notfound(request);
        }
    }
    
    public ResponseContext postItem(RequestContext context) {
        AtompubRequestContext request = (AtompubRequestContext) context;
        Abdera abdera = request.getAbdera();
        try {
            Document<Entry> entry_doc = (Document<Entry>)request.getDocument(abdera.getParser()).clone();
            if (entry_doc != null) {
                Entry entry = entry_doc.getRoot();
                if (!AbstractAtompubProvider.isValidEntry(entry))
                    return ProviderHelper.badrequest(request);
                setEntryDetails(request, entry, abdera.getFactory().newUuidUri());
                Feed feed = getFeedDocument(request).getRoot();
                feed.insertEntry(entry);
                feed.setUpdated(DateTime.now());
                FOMResponseContext<?> rc =
                    (FOMResponseContext<?>)AbstractAtompubProvider.returnBase(entry_doc, 201, entry.getEdited());
                return rc.setLocation(ProviderHelper.resolveBase(request).resolve(entry.getEditLinkResolvedHref())
                    .toString()).setContentLocation(rc.getLocation().toString()).setEntityTag(AbstractAtompubProvider
                    .calculateEntityTag(entry));
            } else {
                return ProviderHelper.badrequest(request);
            }
        } catch (ParseException pe) {
            return ProviderHelper.notsupported(request);
        } catch (ClassCastException cce) {
            return ProviderHelper.notsupported(request);
        } catch (Exception e) {
            return ProviderHelper.badrequest(request);
        }
    }

    private void setEntryDetails(RequestContext request, Entry entry, String id) {
        entry.setUpdated(DateTime.now());
        entry.setEdited(entry.getUpdated());
        entry.getIdElement().setValue(id);
        entry.addLink(getEntryLink(request, entry.getId().toASCIIString()), "edit");
    }

    private String getEntryLink(RequestContext request, String entryid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("collection", request.getTarget().getParameter("collection"));
        params.put("entry", entryid);
        return request.urlFor(TargetType.TYPE_ENTRY, params);
    }

    public ResponseContext putItem(RequestContext context) {
        AtompubRequestContext request = (AtompubRequestContext) context;
        Abdera abdera = request.getAbdera();
        Entry orig_entry = getAbderaEntry(request);
        if (orig_entry != null) {
            try {
                Document<Entry> entry_doc = (Document<Entry>)request.getDocument(abdera.getParser()).clone();
                if (entry_doc != null) {
                    Entry entry = entry_doc.getRoot();
                    if (!entry.getId().equals(orig_entry.getId()))
                        return ProviderHelper.conflict(request);
                    if (!AbstractAtompubProvider.isValidEntry(entry))
                        return ProviderHelper.badrequest(request);
                    setEntryDetails(request, entry, orig_entry.getId().toString());
                    orig_entry.discard();
                    Feed feed = getFeedDocument(request).getRoot();
                    feed.insertEntry(entry);
                    feed.setUpdated(DateTime.now());
                    return ProviderHelper.nocontent();
                } else {
                    return ProviderHelper.badrequest(request);
                }
            } catch (ParseException pe) {
                return ProviderHelper.notsupported(request);
            } catch (ClassCastException cce) {
                return ProviderHelper.notsupported(request);
            } catch (Exception e) {
                return ProviderHelper.badrequest(request);
            }
        } else {
            return ProviderHelper.notfound(request);
        }
    }

    private Entry getAbderaEntry(RequestContext request) {
        try {
            return getFeedDocument(request).getRoot().getEntry(getResourceName(request));
        } catch (Exception e) {
        }
        return null;
    }

    public String getResourceName(RequestContext request) {
        if (request.getTarget().getType() != TargetType.TYPE_ENTRY)
            return null;
        String[] segments = request.getUri().toString().split("/");
        return UrlEncoding.decode(segments[segments.length - 1]);
    }

    public ResponseContext getCategories(RequestContext context) {
      AtompubRequestContext request = (AtompubRequestContext) context;
        return new StreamWriterResponseContext(request.getAbdera()) {
            protected void writeTo(StreamWriter sw) throws IOException {
                sw.startDocument().startCategories(false).writeCategory("foo").writeCategory("bar")
                    .writeCategory("baz").endCategories().endDocument();
            }
        }.setStatus(200).setContentType(Constants.CAT_MEDIA_TYPE);
    }
}
