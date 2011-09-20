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
package org.apache.abdera2.common.protocol;


public interface CollectionAdapter {

    /**
     * Post a new entry to the collection
     */
    <S extends ResponseContext>S postItem(RequestContext request);

    /**
     * Delete an entry from the collection
     */
    <S extends ResponseContext>S deleteItem(RequestContext request);

    /**
     * Get an entry from the collection
     */
    <S extends ResponseContext>S getItem(RequestContext request);

    /**
     * Get metadata for an entry from the collection
     */
    <S extends ResponseContext>S headItem(RequestContext request);

    /**
     * Get options for an entry from the collection
     */
    <S extends ResponseContext>S optionsItem(RequestContext request);

    /**
     * Update an existing entry
     */
    <S extends ResponseContext>S putItem(RequestContext request);

    /**
     * Get the collections Atom feed document
     */
    <S extends ResponseContext>S getItemList(RequestContext request);

    /**
     * Any request that is not covered by the postEntry, deleteEntry, etc methods will be passed on to the
     * extensionRequest method. This provides an Adapter with the ability to support Atompub protocol extensions.
     */
    <S extends ResponseContext>S extensionRequest(RequestContext request);

}
