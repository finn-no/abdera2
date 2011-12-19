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
import java.io.InputStream;
import java.util.List;

import javax.activation.MimeType;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.text.UrlEncoding;
import org.apache.abdera2.common.text.CharUtils.Profile;
import org.apache.abdera2.common.date.DateTimes;
import org.apache.abdera2.model.Content;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.model.Person;
import org.apache.abdera2.model.Text;
import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.util.MorePredicates;
import org.apache.abdera2.common.http.EntityTag;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.EmptyResponseContext;
import org.apache.abdera2.common.protocol.MediaResponseContext;
import org.apache.abdera2.common.protocol.ProviderHelper;
import org.apache.abdera2.common.protocol.ResponseContextException;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.google.common.base.Function;

/**
 * By extending this class it becomes easy to build Collections which are backed by a set of entities - such as a
 * database row, domain objects, or files.
 * 
 * @param <T> The entity that this is backed by.
 */
public abstract class AbstractEntityCollectionAdapter<T> 
  extends AbstractAtompubCollectionAdapter {

  private final static Log log = LogFactory.getLog(AbstractEntityCollectionAdapter.class);

  public AbstractEntityCollectionAdapter(String href) {
    super(href);
    putHandler(TargetType.TYPE_COLLECTION,"POST",handlePost());
    putHandler(TargetType.TYPE_COLLECTION,"GET",getItemList());
    putHandler(TargetType.TYPE_COLLECTION,"HEAD",getItemList());
    putHandler(TargetType.TYPE_MEDIA,"PUT",putMedia());
    putHandler(TargetType.TYPE_MEDIA,"DELETE",deleteMedia());
    putHandler(TargetType.TYPE_MEDIA, "HEAD", headMedia());
    putHandler(TargetType.TYPE_MEDIA, "GET", getMedia());
    putHandler(TargetType.TYPE_ENTRY,"DELETE",deleteItem());
    putHandler(TargetType.TYPE_ENTRY,"GET",getItem());
    putHandler(TargetType.TYPE_ENTRY,"HEAD",headItem());
    putHandler(TargetType.TYPE_ENTRY,"PUT",putItem());
  }
  
    /**
     * Create a new entry
     * 
     * @param title The title of the entry (assumes that type="text")
     * @param id The value of the atom:id element
     * @param summary The summary of the entry
     * @param updated The value of the atom:updated element
     * @param authors Listing of atom:author elements
     * @param context The content of the entry
     * @param request The request context
     */
    public abstract T postEntry(String title,
                                IRI id,
                                String summary,
                                DateTime updated,
                                List<Person> authors,
                                Content content,
                                RequestContext request) throws ResponseContextException;

    Function<RequestContext,ResponseContext> postMedia() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return createMediaEntry(input);
        }
      };
    }
    
    private Function<RequestContext,ResponseContext> putMedia() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            String id = getResourceName(input);
            T entryObj = getEntry(id, input);
            putMedia(entryObj, input.getContentType(), input.getSlug(), input.getInputStream(), input);
            return new EmptyResponseContext(200);
          } catch (IOException e) {
              return new EmptyResponseContext(500);
          } catch (ResponseContextException e) {
              return createErrorResponse(e);
          }
        }
      };
    }

    /**
     * Update a media resource. By default this method is not allowed. Implementations must override this method to
     * support media resource updates
     * 
     * @param entryObj
     * @param contentType The mime-type of the media resource
     * @param slug The value of the Slug request header
     * @param inputStream An input stream providing access to the request payload
     * @param request The request context
     */
    public void putMedia(T entryObj, MimeType contentType, String slug, InputStream inputStream, RequestContext request)
        throws ResponseContextException {
        throw new ResponseContextException(ProviderHelper.notallowed(request));
    }

    Function<RequestContext,ResponseContext> postItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return createNonMediaEntry(input);
        }
      };
    }
    
    private Function<RequestContext,ResponseContext> handlePost() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return AbstractAtompubProvider.IS_ATOM.apply(input) ?
            postItem().apply(input) :
            postMedia().apply(input);
        }
      };
    }
    
    protected String getLink(T entryObj, IRI feedIri, RequestContext request) throws ResponseContextException {
        return getLink(entryObj, feedIri, request, false);
    }

    protected String getLink(T entryObj, IRI feedIri, RequestContext request, boolean absolute)
        throws ResponseContextException {
        return getLink(getName(entryObj), entryObj, feedIri, request, absolute);
    }

    protected String getLink(String name, T entryObj, IRI feedIri, RequestContext request) {
        return getLink(name, entryObj, feedIri, request, false);
    }

    protected String getLink(String name, T entryObj, IRI feedIri, RequestContext request, boolean absolute) {
        feedIri = feedIri.trailingSlash();
        IRI entryIri = feedIri.resolve(UrlEncoding.encode(name, Profile.PATH));

        if (absolute) {
            entryIri = request.getResolvedUri().resolve(entryIri);
        }

        String link = entryIri.toString();

        String qp = getQueryParameters(entryObj, request);
        if (qp != null && !"".equals(qp)) {
            StringBuilder sb = new StringBuilder();
            sb.append(link).append("?").append(qp);
            link = sb.toString();
        }

        return link;
    }

    protected String getQueryParameters(T entryObj, RequestContext request) {
        return null;
    }

    /**
     * Post a new media resource to the collection. By default, this method is not supported. Implementations must
     * override this method to support posting media resources
     * 
     * @param mimeType The mime-type of the resource
     * @param slug The value of the Slug header
     * @param inputStream An InputStream providing access to the request payload
     * @param request The request context
     */
    public T postMedia(MimeType mimeType, String slug, InputStream inputStream, RequestContext request)
        throws ResponseContextException {
        throw new UnsupportedOperationException();
    }
    
    private Function<RequestContext,ResponseContext> deleteItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          String id = getResourceName(input);
          if (id != null) {
              try {
                  deleteEntry(id, input);
              } catch (ResponseContextException e) {
                  return createErrorResponse(e);
              }
              return new EmptyResponseContext(204);
          } else {
            return new EmptyResponseContext(404);
          }
        }
      };
    }

    /**
     * Delete an entry
     * 
     * @param resourceName The entry to delete
     * @param request The request context
     */
    public abstract void deleteEntry(String resourceName, RequestContext request) throws ResponseContextException;

    private Function<RequestContext,ResponseContext> deleteMedia() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          String resourceName = getResourceName(input);
          if (resourceName != null) {
              try {
                  deleteMedia(resourceName, input);
              } catch (ResponseContextException e) {
                  return createErrorResponse(e);
              }
              return new EmptyResponseContext(204);
          } else {
              return new EmptyResponseContext(404);
          }
        }
      };
    }

    /**
     * Delete a media resource. By default this method is not supported. Implementations must override this method to
     * support deleting media resources
     */
    public void deleteMedia(String resourceName, RequestContext request) throws ResponseContextException {
        throw new ResponseContextException(ProviderHelper.notsupported(request));
    }

    /**
     * Get the authors for an entry. By default this returns null. Implementations must override in order to providing a
     * listing of authors for an entry
     */
    public List<Person> getAuthors(T entry, RequestContext request) throws ResponseContextException {
        return null;
    }

    /**
     * Get the content for the entry.
     */
    public abstract Object getContent(T entry, RequestContext request) throws ResponseContextException;

    // GET, POST, PUT, DELETE

    /**
     * Get the content-type for the entry. By default this operation is not supported.
     */
    public String getContentType(T entry) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the listing of entries requested
     */
    public abstract Iterable<T> getEntries(RequestContext request) throws ResponseContextException;

    private Function<RequestContext,ResponseContext> getItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            Entry entry = getEntryFromCollectionProvider(input);
            if (entry != null) {
                return buildGetEntryResponse(input, entry);
            } else {
                return new EmptyResponseContext(404);
            }
        } catch (ResponseContextException e) {
            return createErrorResponse(e);
        }
        }
      };
    }
   
    /**
     * Get a specific entry
     * 
     * @param resourceName The entry to get
     * @param request The request context
     */
    public abstract T getEntry(String resourceName, RequestContext request) throws ResponseContextException;

    private Function<RequestContext,ResponseContext> headItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            String resourceName = getResourceName(input);
            T entryObj = getEntry(resourceName, input);
            if (entryObj != null) {
                return buildHeadEntryResponse(input, resourceName, getUpdated(entryObj));
            } else {
                return new EmptyResponseContext(404);
            }
        } catch (ResponseContextException e) {
            return createErrorResponse(e);
        }
        }
      };
    }
    
    private Function<RequestContext,ResponseContext> headMedia() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            String resourceName = getResourceName(input);
            T entryObj = getEntry(resourceName, input);

            if (entryObj != null) {
                return buildHeadEntryResponse(input, resourceName, getUpdated(entryObj));
            } else {
                return new EmptyResponseContext(404);
            }
        } catch (ResponseContextException e) {
            return createErrorResponse(e);
        }
        }
      };
    }

    public Function<RequestContext,ResponseContext> getItemList() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            Feed feed = createFeedBase(input);
            addFeedDetails(feed, input);
            return buildGetFeedResponse(feed);
        } catch (ResponseContextException e) {
            return createErrorResponse(e);
        }
        }
      };
    }


    /**
     * Adds the selected entries to the Feed document. By default, this will set the feed's atom:updated element to the
     * current date and time
     */
    protected void addFeedDetails(Feed feed, RequestContext request) throws ResponseContextException {
        feed.setUpdated(DateTime.now());

        Iterable<T> entries = getEntries(request);
        if (entries != null) {
            for (T entryObj : entries) {
                Entry e = feed.addEntry();

                IRI feedIri = new IRI(getFeedIriForEntry(entryObj, request));
                addEntryDetails(request, e, feedIri, entryObj);

                if (isMediaEntry(entryObj)) {
                    addMediaContent(feedIri, e, entryObj, request);
                } else {
                    addContent(e, entryObj, request);
                }
            }
        }
    }

    private IRI getFeedIRI(T entryObj, RequestContext request) {
        String feedIri = getFeedIriForEntry(entryObj, request);
        return new IRI(feedIri).trailingSlash();
    }

    /**
     * Gets the UUID for the specified entry.
     * 
     * @param entry
     * @return
     */
    public abstract String getId(T entry) throws ResponseContextException;

    private Function<RequestContext,ResponseContext> getMedia() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            String resource = getResourceName(input);
            T entryObj = getEntry(resource, input);
            if (entryObj == null) {
                return new EmptyResponseContext(404);
            }
            return buildGetMediaResponse(resource, entryObj);
        } catch (ParseException pe) {
            return new EmptyResponseContext(415);
        } catch (ClassCastException cce) {
            return new EmptyResponseContext(415);
        } catch (ResponseContextException e) {
            return e.getResponseContext();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return new EmptyResponseContext(400);
        }
        }
      };
    }
    
    /**
     * Creates a ResponseContext for a GET media request. By default, this returns a MediaResponseContext containing the
     * media resource. The last-modified header will be set.
     */
    protected ResponseContext buildGetMediaResponse(String id, T entryObj) throws ResponseContextException {
        DateTime updated = getUpdated(entryObj);
        return new MediaResponseContext(getMediaStream(entryObj), updated, 200)
          .setContentType(getContentType(entryObj))
          .setEntityTag(EntityTag.generate(id, DateTimes.format(updated)));
    }

    /**
     * Get the name of the media resource. By default this method is unsupported. Implementations must override.
     */
    public String getMediaName(T entry) throws ResponseContextException {
        throw new UnsupportedOperationException();
    }

    /**
     * Get an input stream for the media resource. By default this method is unsupported. Implementations must override.
     */
    public InputStream getMediaStream(T entry) throws ResponseContextException {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the name of the entry resource (used to construct links)
     */
    public abstract String getName(T entry) throws ResponseContextException;

    /**
     * Get the title fo the entry
     */
    public abstract String getTitle(T entry) throws ResponseContextException;

    /**
     * Get the value to use in the atom:updated element
     */
    public abstract DateTime getUpdated(T entry) throws ResponseContextException;

    /**
     * True if this entry is a media-link entry. By default this always returns false. Implementations must override to
     * support media link entries
     */
    public boolean isMediaEntry(T entry) throws ResponseContextException {
        return false;
    }

    private Function<RequestContext,ResponseContext> putItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            String id = getResourceName(input);
            T entryObj = getEntry(id, input);

            if (entryObj == null) {
                return new EmptyResponseContext(404);
            }

            Entry orig_entry =
                getEntryFromCollectionProvider(entryObj, new IRI(getFeedIriForEntry(entryObj, input)), input);
            if (orig_entry != null) {

                MimeType contentType = input.getContentType();
                if (contentType != null && !MimeTypeHelper.isAtom(contentType.toString()))
                    return new EmptyResponseContext(415);

                Entry entry = getEntryFromRequest(input);
                if (entry != null) {
                    if (!entry.getId().equals(orig_entry.getId()))
                        return new EmptyResponseContext(409);

                    if (!MorePredicates.VALID_ENTRY.apply(entry))
                        return new EmptyResponseContext(400);

                    putEntry(entryObj, entry.getTitle(), DateTime.now(), entry.getAuthors(), entry.getSummary(), entry
                        .getContentElement(), input);
                    return new EmptyResponseContext(204);
                } else {
                    return new EmptyResponseContext(400);
                }
            } else {
                return new EmptyResponseContext(404);
            }
        } catch (ResponseContextException e) {
            return createErrorResponse(e);
        } catch (ParseException pe) {
            return new EmptyResponseContext(415);
        } catch (ClassCastException cce) {
            return new EmptyResponseContext(415);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return new EmptyResponseContext(400);
        }
        }
      };
    }
    
    /**
     * Get the Feed IRI
     */
    protected String getFeedIriForEntry(T entryObj, RequestContext request) {
        return getHref(request);
    }

    /**
     * Update an entry.
     * 
     * @param entry The entry to update
     * @param title The new title of the entry
     * @param updated The new value of atom:updated
     * @param authors To new listing of authors
     * @param summary The new summary
     * @param content The new content
     * @param request The request context
     */
    public abstract void putEntry(T entry,
                                  String title,
                                  DateTime updated,
                                  List<Person> authors,
                                  String summary,
                                  Content content,
                                  RequestContext request) throws ResponseContextException;

    /**
     * Adds the atom:content element to an entry
     */
    protected void addContent(Entry e, T doc, RequestContext request) throws ResponseContextException {
        Object content = getContent(doc, request);

        if (content instanceof Content) {
            e.setContentElement((Content)content);
        } else if (content instanceof String) {
            e.setContent((String)content);
        }
    }

    /**
     * Add the details to an entry
     * 
     * @param request The request context
     * @param e The entry
     * @param feedIri The feed IRI
     * @param entryObj
     */
    protected String addEntryDetails(RequestContext request, Entry e, IRI feedIri, T entryObj)
        throws ResponseContextException {
        String link = getLink(entryObj, feedIri, request);

        e.addLink(link, "edit");
        e.setId(getId(entryObj));
        e.setTitle(getTitle(entryObj));
        e.setUpdated(getUpdated(entryObj));

        List<Person> authors = getAuthors(entryObj, request);
        if (authors != null) {
            for (Person a : authors) {
                e.addAuthor(a);
            }
        }

        Text t = getSummary(entryObj, request);
        if (t != null) {
            e.setSummaryElement(t);
        }
        return link;
    }

    /**
     * Get the summary of the entry. By default this returns null.
     */
    public Text getSummary(T entry, RequestContext request) throws ResponseContextException {
        return null;
    }

    /**
     * Add media content details to a media-link entry
     * 
     * @param feedIri The feed iri
     * @param entry The entry object
     * @param entryObj
     * @param request The request context
     */
    protected String addMediaContent(IRI feedIri, Entry entry, T entryObj, RequestContext request)
        throws ResponseContextException {
        String name = getMediaName(entryObj);

        IRI mediaIri = new IRI(getLink(name, entryObj, feedIri, request));
        String mediaLink = mediaIri.toString();
        entry.setContent(mediaIri, getContentType(entryObj));
        entry.addLink(mediaLink, "edit-media");

        return mediaLink;
    }

    /**
     * Create a media entry
     * 
     * @param request The request context
     */
    protected ResponseContext createMediaEntry(RequestContext request) {
        try {
            T entryObj = postMedia(request.getContentType(), request.getSlug(), request.getInputStream(), request);

            IRI feedUri = getFeedIRI(entryObj, request);

            Entry entry = AbstractAtompubProvider.getAbdera(request).getFactory().newEntry();
            addEntryDetails(request, entry, feedUri, entryObj);
            addMediaContent(feedUri, entry, entryObj, request);

            String location = getLink(entryObj, feedUri, request, true);
            return buildPostMediaEntryResponse(location, entry);
        } catch (UnsupportedOperationException e) {
          return UNSUPPORTED_TYPE.apply(request);
        } catch (IOException e) {
            return new EmptyResponseContext(500);
        } catch (ResponseContextException e) {
            return createErrorResponse(e);
        }
    }

    /**
     * Create a regular entry
     * 
     * @param request The request context
     */
    protected ResponseContext createNonMediaEntry(RequestContext request) {
        try {
            Entry entry = getEntryFromRequest(request);
            if (entry != null) {
                if (!MorePredicates.VALID_ENTRY.apply(entry))
                    return new EmptyResponseContext(400);

                entry.setUpdated(DateTime.now());

                T entryObj =
                    postEntry(entry.getTitle(), entry.getId(), entry.getSummary(), entry.getUpdated(), entry
                        .getAuthors(), entry.getContentElement(), request);

                entry.getIdElement().setValue(getId(entryObj));

                IRI feedUri = getFeedIRI(entryObj, request);

                String link = getLink(entryObj, feedUri, request);
                entry.addLink(link, "edit");

                String location = getLink(entryObj, feedUri, request, true);
                return buildCreateEntryResponse(location, entry);
            } else {
                return new EmptyResponseContext(400);
            }
        } catch (ResponseContextException e) {
            return createErrorResponse(e);
        }
    }

    protected Entry getEntryFromCollectionProvider(RequestContext request) throws ResponseContextException {
        String id = getResourceName(request);
        T entryObj = getEntry(id, request);

        if (entryObj == null) {
            return null;
        }

        IRI feedIri = new IRI(getFeedIriForEntry(entryObj, request));
        return getEntryFromCollectionProvider(entryObj, feedIri, request);
    }

    Entry getEntryFromCollectionProvider(T entryObj, IRI feedIri, RequestContext request)
        throws ResponseContextException {
        Abdera abdera = AbstractAtompubProvider.getAbdera(request);
        Factory factory = abdera.getFactory();
        Entry entry = factory.newEntry();

        return buildEntry(entryObj, entry, feedIri, request);
    }

    /**
     * Build the entry from the source object
     * 
     * @param entryObj The source object
     * @param entry The entry to build
     * @param feedIri The feed IRI
     * @param request The request context
     */
    private Entry buildEntry(T entryObj, Entry entry, IRI feedIri, RequestContext request)
        throws ResponseContextException {
        addEntryDetails(request, entry, feedIri, entryObj);

        if (isMediaEntry(entryObj)) {
            addMediaContent(feedIri, entry, entryObj, request);
        } else {
            addContent(entry, entryObj, request);
        }

        return entry;
    }

}
