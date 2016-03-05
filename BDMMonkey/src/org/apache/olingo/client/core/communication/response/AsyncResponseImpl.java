/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.client.core.communication.response;

import java.util.Collection;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.batch.ODataBatchLineIterator;
import org.apache.olingo.client.api.communication.response.AsyncResponse;
import org.apache.olingo.client.core.communication.request.batch.ODataBatchController;

/**
 * Abstract representation of an OData response.
 */
public class AsyncResponseImpl extends AbstractODataResponse implements AsyncResponse {

  public AsyncResponseImpl(final ODataClient odataClient, final HttpClient httpClient,
          final HttpResponse res) {

    super(odataClient, httpClient, res);
  }

  /**
   * Constructor to be used inside a batch item.
   */
  public AsyncResponseImpl(
          final Map.Entry<Integer, String> responseLine,
          final Map<String, Collection<String>> headers,
          final ODataBatchLineIterator batchLineIterator,
          final String boundary) {

    super(null, null, null);

    if (hasBeenInitialized) {
      throw new IllegalStateException("Request already initialized");
    }

    this.hasBeenInitialized = true;

    this.batchInfo = new ODataBatchController(batchLineIterator, boundary);

    this.statusCode = responseLine.getKey();
    this.statusMessage = responseLine.getValue();
    this.headers.putAll(headers);
  }
}
