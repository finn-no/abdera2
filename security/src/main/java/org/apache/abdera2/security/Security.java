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
package org.apache.abdera2.security;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.Discover;
import org.apache.abdera2.util.Configuration;

/**
 * The AbderaSecurity class provides the entry point for using XML Digital Signatures and XML Encryption with Abdera.
 */
public class Security {

    private final Abdera abdera;
    private final Encryption encryption;
    private final Signature signature;

    public Security() {
        this(Abdera.getInstance());
    }

    public Security(Abdera abdera) {
        this.abdera = abdera;
        this.encryption = newEncryption();
        this.signature = newSignature();
    }

    public Security(Configuration config) {
        this(new Abdera(config));
    }

    private Abdera getAbdera() {
        return abdera;
    }

    /**
     * Acquire a new XML Encryption provider instance
     */
    public Encryption newEncryption() {
        return (Encryption)Discover.locate(Encryption.class,
                                           "org.apache.abdera2.security.xmlsec.XmlEncryption",
                                           getAbdera());
    }

    /**
     * Acquire a shared XML Encryption provider instance
     */
    public Encryption getEncryption() {
        return encryption;
    }

    /**
     * Acquire a new XML Digital Signature provider instance
     */
    public Signature newSignature() {
        return (Signature)Discover.locate(Signature.class,
                                          "org.apache.abdera2.security.xmlsec.XmlSignature",
                                          getAbdera());
    }

    /**
     * Acquire a shared XML Digital Signature provider instance
     */
    public Signature getSignature() {
        return signature;
    }

}
