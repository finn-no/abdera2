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
package org.apache.abdera2.protocol.server.processors;

import java.io.IOException;
import java.util.Map;

import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.protocol.CollectionAdapter;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.common.protocol.WorkspaceInfo;
import org.apache.abdera2.common.protocol.WorkspaceManager;
import org.apache.abdera2.protocol.server.context.StreamWriterResponseContext;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.protocol.server.model.AtompubCategoriesInfo;
import org.apache.abdera2.protocol.server.model.AtompubCategoryInfo;
import org.apache.abdera2.protocol.server.model.AtompubCollectionInfo;
import org.apache.abdera2.protocol.server.multipart.MultipartRelatedCollectionInfo;
import org.apache.abdera2.writer.StreamWriter;

/**
 * {@link org.apache.AtompubRequestProcessor.protocol.server.RequestProcessor} implementation which processes requests for service
 * documents. It writes multipart/related accept attributes when is enabled.
 */
public class MultipartRelatedServiceRequestProcessor 
  extends ServiceRequestProcessor {

    protected MultipartRelatedServiceRequestProcessor(
      WorkspaceManager workspaceManager, 
      CollectionAdapter adapter) {
        super(workspaceManager, adapter);
    }

    protected ResponseContext getServiceDocument(
      final RequestContext request, 
      final WorkspaceManager workspaceManager) {
        return new StreamWriterResponseContext(
            AbstractAtompubProvider.getAbdera(request)) {

            @Override
            protected void writeTo(StreamWriter sw) throws IOException {
                sw.startDocument().startService();
                for (WorkspaceInfo wi : workspaceManager.getWorkspaces(request)) {
                    sw.startWorkspace().writeTitle(wi.getTitle(request));
                    Iterable<CollectionInfo> collections = wi.getCollections(request);

                    if (collections != null) {
                        for (CollectionInfo c : collections) {
                            AtompubCollectionInfo ci = (AtompubCollectionInfo) c;
                            sw.startCollection(ci.getHref(request)).writeTitle(ci.getTitle(request));
                            if (ci instanceof MultipartRelatedCollectionInfo) {
                                MultipartRelatedCollectionInfo multipartCi = (MultipartRelatedCollectionInfo)ci;
                                for (Map.Entry<String, String> accept : multipartCi.getAlternateAccepts(request)
                                    .entrySet()) {
                                    sw.startElement(Constants.ACCEPT);
                                    if (accept.getValue() != null && accept.getValue().length() > 0) {
                                        sw.writeAttribute(Constants.LN_ALTERNATE, accept.getValue());
                                    }
                                    sw.writeElementText(accept.getKey()).endElement();
                                }
                            } else {
                                sw.writeAccepts(ci.getAccepts(request));
                            }
                            Iterable<AtompubCategoriesInfo> catinfos = ci.getCategoriesInfo(request);
                            if (catinfos != null) {
                                for (AtompubCategoriesInfo catinfo : catinfos) {
                                    String cathref = catinfo.getHref(request);
                                    if (cathref != null) {
                                        sw.startCategories().writeAttribute("href",
                                                                            request.getTargetBasePath() + cathref)
                                            .endCategories();
                                    } else {
                                        sw.startCategories(catinfo.isFixed(request), catinfo.getScheme(request));
                                        for (AtompubCategoryInfo cat : catinfo) {
                                            sw.writeCategory(cat.getTerm(request), cat.getScheme(request), cat
                                                .getLabel(request));
                                        }
                                        sw.endCategories();
                                    }
                                }
                            }
                            sw.endCollection();
                        }
                    }
                    sw.endWorkspace();
                }
                sw.endService().endDocument();
            }
        }.setStatus(200).setContentType(Constants.APP_MEDIA_TYPE);
    }

}
