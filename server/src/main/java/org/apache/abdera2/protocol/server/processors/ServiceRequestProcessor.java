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
import java.util.Collection;
import java.util.Date;

import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.http.EntityTag;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.CollectionAdapter;
import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.common.protocol.RequestProcessor;
import org.apache.abdera2.common.protocol.WorkspaceInfo;
import org.apache.abdera2.common.protocol.WorkspaceManager;
import org.apache.abdera2.protocol.server.context.StreamWriterResponseContext;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.protocol.server.model.AtompubCategoriesInfo;
import org.apache.abdera2.protocol.server.model.AtompubCategoryInfo;
import org.apache.abdera2.protocol.server.model.AtompubCollectionInfo;
import org.apache.abdera2.writer.StreamWriter;

/**
 * {@link org.apache.AtompubRequestProcessor.protocol.server.RequestProcessor} implementation which processes requests for service
 * documents.
 */
@SuppressWarnings("unchecked")
public class ServiceRequestProcessor 
  implements RequestProcessor {

    public <S extends ResponseContext> S process(
        RequestContext request, WorkspaceManager workspaceManager,
        CollectionAdapter collectionAdapter) {
      return (S)this.processService(request, workspaceManager);
    }
  
    private <S extends ResponseContext>S processService(
        RequestContext context, 
        WorkspaceManager workspaceManager) {
        String method = context.getMethod();
        if (method.equalsIgnoreCase("GET")) {
            return (S)this.getServiceDocument(context, workspaceManager);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <S extends ResponseContext>S getServiceDocument(
        final RequestContext request, 
        final WorkspaceManager workspaceManager) {
 
        return (S)new StreamWriterResponseContext(
            AbstractAtompubProvider.getAbdera(request)) {

          // JIRA: https://issues.apache.org/jira/browse/ABDERA-255
          @Override
          public EntityTag getEntityTag() {
            EntityTag etag = workspaceManager.getEntityTag();
            return etag != null ? etag : super.getEntityTag();
          }

          // JIRA: https://issues.apache.org/jira/browse/ABDERA-255
          @Override
          public Date getLastModified() {
            Date lm = workspaceManager.getLastModified();
            return lm != null ? lm : super.getLastModified();
          }

            protected void writeTo(StreamWriter sw) throws IOException {
                sw.startDocument().startService();
                for (WorkspaceInfo wi : workspaceManager.getWorkspaces(request)) {
                    sw.startWorkspace().writeTitle(wi.getTitle(request));
                    Collection<CollectionInfo> collections = wi.getCollections(request);
                    if (collections != null) {
                        for (CollectionInfo c : collections) {
                            AtompubCollectionInfo ci = (AtompubCollectionInfo) c;
                            sw.startCollection(ci.getHref(request)).writeTitle(ci.getTitle(request)).writeAccepts(ci
                                .getAccepts(request));
                            AtompubCategoriesInfo[] catinfos = ci.getCategoriesInfo(request);
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
