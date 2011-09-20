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
import java.io.OutputStream;
import org.apache.abdera2.common.protocol.BaseResponseContextWrapper;
import org.apache.abdera2.protocol.server.AtompubResponseContext;
import org.apache.abdera2.writer.Writer;

public class ResponseContextWrapper 
  extends BaseResponseContextWrapper implements AtompubResponseContext {

    public ResponseContextWrapper(AtompubResponseContext response) {
        super(response);
    }

    private AtompubResponseContext getResponse() {
      return (AtompubResponseContext)response;
    }
    
    public AtompubResponseContext setWriter(Writer writer) {
        getResponse().setWriter(writer);
        return this;
    }

    public void writeTo(OutputStream out, Writer writer) throws IOException {
        getResponse().writeTo(out, writer);
    }

    public void writeTo(java.io.Writer javaWriter, Writer abderaWriter) throws IOException {
        getResponse().writeTo(javaWriter, abderaWriter);
    }
}
