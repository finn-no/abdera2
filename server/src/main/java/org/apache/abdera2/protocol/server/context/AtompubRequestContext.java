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
package org.apache.abdera2.protocol.server.context;

import java.io.IOException;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.parser.Parser;
import org.apache.abdera2.parser.ParserOptions;
import org.apache.abdera2.protocol.server.AtompubProvider;
import org.apache.abdera2.common.Localizer;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.BaseRequestContextWrapper;
import org.apache.abdera2.common.protocol.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class AtompubRequestContext 
  extends BaseRequestContextWrapper {

    private final static Log log = LogFactory.getLog(AtompubRequestContext.class);

    protected Document<?> document;
    
    public AtompubRequestContext(RequestContext parent) {
      super(parent);
    }

    public Abdera getAbdera() {
      Provider provider = getProvider();
      return provider instanceof AtompubProvider ? 
        ((AtompubProvider)provider).getAbdera() : 
        Abdera.getInstance();
    }

    public synchronized <T extends Element> Document<T> getDocument() throws ParseException, IOException {
        log.debug(Localizer.get("PARSING.REQUEST.DOCUMENT"));
        if (document == null)
            document = getDocument(getAbdera().getParser());
        return (Document<T>)document;
    }

    public synchronized <T extends Element> Document<T> getDocument(Parser parser) throws ParseException, IOException {
        log.debug(Localizer.get("PARSING.REQUEST.DOCUMENT"));
        if (parser == null)
            parser = getAbdera().getParser();
        if (parser == null)
            throw new IllegalArgumentException("No Parser implementation was provided");
        if (document == null)
            document = getDocument(parser, parser.getDefaultParserOptions());
        return (Document<T>)document;
    }

    public synchronized <T extends Element> Document<T> getDocument(ParserOptions options) throws ParseException,
        IOException {
        log.debug(Localizer.get("PARSING.REQUEST.DOCUMENT"));
        if (document == null)
            document = getDocument(getAbdera().getParser(), options);
        return (Document<T>)document;
    }

    public synchronized <T extends Element> Document<T> getDocument(Parser parser, ParserOptions options)
        throws ParseException, IOException {
        log.debug(Localizer.get("PARSING.REQUEST.DOCUMENT"));
        if (parser == null)
            parser = getAbdera().getParser();
        if (parser == null)
            throw new IllegalArgumentException("No Parser implementation was provided");
        if (document == null) {
            document = parser.parse(getInputStream(), getResolvedUri().toString(), options);
        }
        return (Document<T>)document;
    }

    public boolean isAtom() {
        try {
            return MimeTypeHelper.isAtom(getContentType().toString());
        } catch (Exception e) {
            return false;
        }
    }
    
}
