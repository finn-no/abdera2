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
package org.apache.abdera2.ext.thread;

import org.apache.abdera2.common.anno.QName;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.model.ElementWrapper;
import static org.apache.abdera2.ext.thread.ThreadConstants.*;

@QName(value=LN_TOTAL,ns=THR_NS,pfx=THR_PREFIX)
public class Total extends ElementWrapper {

    public Total(Element internal) {
        super(internal);
    }

    public Total(Factory factory) {
        super(factory, ThreadConstants.THRTOTAL);
    }

    public int getValue() {
        String val = getText();
        return (val != null) ? Integer.parseInt(val) : -1;
    }

    public void setValue(int value) {
        setText(String.valueOf(value));
    }

}
