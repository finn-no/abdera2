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
package org.apache.abdera2.ext.media;


import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.model.ElementWrapper;
import org.apache.abdera2.common.anno.QName;
import org.apache.abdera2.common.iri.IRI;
import static org.apache.abdera2.ext.media.MediaConstants.*;

@QName(value=LN_CATEGORY, 
    ns=MEDIA_NS,
    pfx=MEDIA_PREFIX)
public class MediaCategory extends ElementWrapper {

    public MediaCategory(Element internal) {
        super(internal);
    }

    public MediaCategory(Factory factory) {
        super(factory, CATEGORY);
    }

    public IRI getScheme() {
        String scheme = getAttributeValue("scheme");
        return (scheme != null) ? new IRI(scheme) : null;
    }

    public void setScheme(String scheme) {
        if (scheme != null)
            setAttributeValue("scheme", (new IRI(scheme)).toString());
        else
            removeAttribute(SCHEME);
    }

    public String getLabel() {
        return getAttributeValue("label");
    }

    public void setLabel(String label) {
        if (label != null)
            setAttributeValue("label", label);
        else
            removeAttribute(LABEL);
    }
}
