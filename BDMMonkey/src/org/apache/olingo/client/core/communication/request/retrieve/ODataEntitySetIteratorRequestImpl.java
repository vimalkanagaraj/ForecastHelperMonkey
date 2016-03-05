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
package org.apache.olingo.client.core.communication.request.retrieve;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetIteratorRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.commons.api.format.ContentType;

/**
 * This class implements an OData EntitySet query request.
 */
public class ODataEntitySetIteratorRequestImpl<ES extends ClientEntitySet, E extends ClientEntity>
        extends AbstractODataRetrieveRequest<ClientEntitySetIterator<ES, E>>
        implements ODataEntitySetIteratorRequest<ES, E> {

  private ClientEntitySetIterator<ES, E> entitySetIterator = null;

  /**
   * Private constructor.
   *
   * @param odataClient client instance getting this request
   * @param query query to be executed.
   */
  public ODataEntitySetIteratorRequestImpl(final ODataClient odataClient, final URI query) {
    super(odataClient, query);
  }

  @Override
  public ContentType getDefaultFormat() {
    return odataClient.getConfiguration().getDefaultPubFormat();
  }

  @Override
  public ODataRetrieveResponse<ClientEntitySetIterator<ES, E>> execute() {
    final HttpResponse res = doExecute();
    return new ODataEntitySetIteratorResponseImpl(odataClient, httpClient, res);
  }

  /**
   * Response class about an ODataEntitySetIteratorRequest.
   */
  protected class ODataEntitySetIteratorResponseImpl extends AbstractODataRetrieveResponse {

    private ODataEntitySetIteratorResponseImpl(final ODataClient odataClient, final HttpClient httpClient,
            final HttpResponse res) {

      super(odataClient, httpClient, res);
    }

    @Override
    public ClientEntitySetIterator<ES, E> getBody() {
      if (entitySetIterator == null) {
    	  /* Vimal fixing the DateTime Primitive issue
    	   * basically Olingo does not support data type 'DateTime', it supports the data type as defined in
    	   * 'EdmPrimitiveTypeKind' but SP 2010 provides date value only in 'DateTime' as edm type, hence
    	   * reading the entire response and replacing the type from 'DateTime' to 'Date' so olingo can 
    	   * consider date fields also as primitive fields and move forward, if not olingo considers these
    	   * as complex type and encounters null pointer in many places and does not function at all. 
    	   */
		StringWriter strWriter = new StringWriter();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getRawResponse()));
			String content = "";
			while((content = reader.readLine()) != null){
				content = content.replace("Edm.DateTime", "Edm.Date");
				content = StringUtils.replacePattern(content, "T..:..:..", "");
				strWriter.write(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	  
        entitySetIterator = new ClientEntitySetIterator<ES, E>(
                odataClient, new ByteArrayInputStream(strWriter.toString().getBytes()), ContentType.parse(getContentType()));
      }
      return entitySetIterator;
    }
  }
}
