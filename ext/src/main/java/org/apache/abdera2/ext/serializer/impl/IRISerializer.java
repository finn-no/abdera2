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
package org.apache.abdera2.ext.serializer.impl;

import javax.xml.namespace.QName;

import org.apache.abdera2.ext.serializer.Conventions;
import org.apache.abdera2.ext.serializer.ObjectContext;
import org.apache.abdera2.ext.serializer.SerializationContext;
import org.apache.abdera2.writer.StreamWriter;

public class IRISerializer extends ElementSerializer {

    public IRISerializer(QName qname) {
        super(qname);
    }

    public IRISerializer() {
        super(null);
    }

    protected void init(Object source,
                        ObjectContext objectContext,
                        SerializationContext context,
                        Conventions conventions) {
        QName qname = this.qname != null ? this.qname : getQName(objectContext.getAccessor());
        StreamWriter sw = context.getStreamWriter();
        sw.startElement(qname);
    }

    protected void process(Object source,
                           ObjectContext objectContext,
                           SerializationContext context,
                           Conventions conventions) {
        writeAttributes(source, objectContext, context, conventions);
        writeTextValue(source, objectContext, context, conventions);
    }
}
