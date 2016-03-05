/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.client.core.edm.xml;

import java.io.IOException;

import org.apache.olingo.commons.api.edm.provider.CsdlSingleton;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ClientCsdlSingleton.SingletonDeserializer.class)
class ClientCsdlSingleton extends CsdlSingleton {

  private static final long serialVersionUID = 1656749615107151921L;

  static class SingletonDeserializer extends AbstractClientCsdlEdmDeserializer<ClientCsdlSingleton> {
    @Override
    protected ClientCsdlSingleton doDeserialize(final JsonParser jp, final DeserializationContext ctxt)
            throws IOException {
      final ClientCsdlSingleton singleton = new ClientCsdlSingleton();

      for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
        final JsonToken token = jp.getCurrentToken();
        if (token == JsonToken.FIELD_NAME) {
          if ("Name".equals(jp.getCurrentName())) {
            singleton.setName(jp.nextTextValue());
          } else if ("Type".equals(jp.getCurrentName())) {
            singleton.setType(jp.nextTextValue());
          } else if ("NavigationPropertyBinding".equals(jp.getCurrentName())) {
            jp.nextToken();
            singleton.getNavigationPropertyBindings().add(
                    jp.readValueAs(ClientCsdlNavigationPropertyBinding.class));
          } else if ("Annotation".equals(jp.getCurrentName())) {
            jp.nextToken();
            singleton.getAnnotations().add(jp.readValueAs(ClientCsdlAnnotation.class));
          }
        }
      }

      return singleton;
    }
  }
}
