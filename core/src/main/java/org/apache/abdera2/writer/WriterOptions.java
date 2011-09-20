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
package org.apache.abdera2.writer;

import org.apache.abdera2.common.io.Compression.CompressionCodec;

public interface WriterOptions extends Cloneable {

    /**
     * When writing, use the specified compression codecs
     */
    CompressionCodec[] getCompressionCodecs();

    /**
     * When writing, use the specified compression codecs
     */
    WriterOptions setCompressionCodecs(CompressionCodec... codecs);

    Object clone() throws CloneNotSupportedException;

    /**
     * The character encoding to use for the output
     */
    String getCharset();

    /**
     * The character encoding to use for the output
     */
    WriterOptions setCharset(String charset);

    /**
     * True if the writer should close the output stream or writer when finished
     */
    boolean getAutoClose();

    /**
     * True if the writer should close the output stream or writer when finished
     */
    WriterOptions setAutoClose(boolean autoclose);
}
