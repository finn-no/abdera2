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

import java.io.Serializable;

import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.BasicWorkspaceInfo;
import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.model.Workspace;
import org.apache.abdera2.protocol.server.model.AtompubCollectionInfo;
import org.apache.abdera2.protocol.server.model.AtompubWorkspaceInfo;

public class SimpleWorkspaceInfo 
  extends BasicWorkspaceInfo
  implements AtompubWorkspaceInfo, Serializable {

    private static final long serialVersionUID = -8459688584319762878L;

    public SimpleWorkspaceInfo() {
    }

    public SimpleWorkspaceInfo(String title) {
        super(title);
    }

    public Workspace asWorkspaceElement(RequestContext request) {
        Workspace workspace = AbstractAtompubProvider.getAbdera(request).getFactory().newWorkspace();
        workspace.setTitle(title);
        for (CollectionInfo c : this.collections) {
            AtompubCollectionInfo collection = (AtompubCollectionInfo) c;
            workspace.addCollection(collection.asCollectionElement(request));
        }
        return workspace;
    }

}
