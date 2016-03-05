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
package org.apache.olingo.client.api.communication.response;

import org.apache.olingo.client.api.domain.ClientProperty;

/**
 * This class implements the response to an OData update entity property request.
 *
 * @see org.apache.olingo.client.api.communication.request.cud.ODataPropertyUpdateRequest
 */
public interface ODataPropertyUpdateResponse extends ODataResponse {

  /**
   * Gets updated object.
   *
   * @return updated object.
   */
  ClientProperty getBody();
}
