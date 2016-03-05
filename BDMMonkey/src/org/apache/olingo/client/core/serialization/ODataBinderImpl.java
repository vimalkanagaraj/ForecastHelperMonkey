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
package org.apache.olingo.client.core.serialization;

import java.io.StringWriter;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.client.api.EdmEnabledODataClient;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.data.ResWrap;
import org.apache.olingo.client.api.data.ServiceDocument;
import org.apache.olingo.client.api.data.ServiceDocumentItem;
import org.apache.olingo.client.api.domain.ClientAnnotatable;
import org.apache.olingo.client.api.domain.ClientAnnotation;
import org.apache.olingo.client.api.domain.ClientCollectionValue;
import org.apache.olingo.client.api.domain.ClientComplexValue;
import org.apache.olingo.client.api.domain.ClientDeletedEntity.Reason;
import org.apache.olingo.client.api.domain.ClientDelta;
import org.apache.olingo.client.api.domain.ClientDeltaLink;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientInlineEntity;
import org.apache.olingo.client.api.domain.ClientInlineEntitySet;
import org.apache.olingo.client.api.domain.ClientLink;
import org.apache.olingo.client.api.domain.ClientLinkType;
import org.apache.olingo.client.api.domain.ClientLinked;
import org.apache.olingo.client.api.domain.ClientOperation;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.api.domain.ClientServiceDocument;
import org.apache.olingo.client.api.domain.ClientValuable;
import org.apache.olingo.client.api.domain.ClientValue;
import org.apache.olingo.client.api.serialization.ODataBinder;
import org.apache.olingo.client.api.serialization.ODataSerializerException;
import org.apache.olingo.client.core.domain.ClientAnnotationImpl;
import org.apache.olingo.client.core.domain.ClientDeletedEntityImpl;
import org.apache.olingo.client.core.domain.ClientDeltaLinkImpl;
import org.apache.olingo.client.core.domain.ClientPropertyImpl;
import org.apache.olingo.client.core.uri.URIUtils;
import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.data.Annotation;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.DeletedEntity;
import org.apache.olingo.commons.api.data.Delta;
import org.apache.olingo.commons.api.data.DeltaLink;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Linked;
import org.apache.olingo.commons.api.data.Operation;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.Valuable;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmBindingTarget;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmElement;
import org.apache.olingo.commons.api.edm.EdmEntityContainer;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmSchema;
import org.apache.olingo.commons.api.edm.EdmStructuredType;
import org.apache.olingo.commons.api.edm.EdmTerm;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.geo.Geospatial;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.core.edm.EdmTypeInfo;
import org.apache.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODataBinderImpl implements ODataBinder {

	/**
	 * Logger.
	 */
	protected static final Logger LOG = LoggerFactory.getLogger(ODataBinderImpl.class);

	protected final ODataClient client;

	public ODataBinderImpl(final ODataClient client) {
		this.client = client;
	}

	@Override
	public boolean add(final ClientEntity entity, final ClientProperty property) {
		return entity.getProperties().add(property);
	}

	protected boolean add(final ClientEntitySet entitySet, final ClientEntity entity) {
		return entitySet.getEntities().add(entity);
	}

	@Override
	public ClientServiceDocument getODataServiceDocument(final ServiceDocument resource) {
		final ClientServiceDocument serviceDocument = new ClientServiceDocument();

		for (ServiceDocumentItem entitySet : resource.getEntitySets()) {
			serviceDocument.getEntitySets().put(entitySet.getName(),
					URIUtils.getURI(resource.getBaseURI(), entitySet.getUrl()));
		}
		for (ServiceDocumentItem functionImport : resource.getFunctionImports()) {
			serviceDocument.getFunctionImports().put(
					functionImport.getName() == null ? functionImport.getUrl() : functionImport.getName(),
					URIUtils.getURI(resource.getBaseURI(), functionImport.getUrl()));
		}
		for (ServiceDocumentItem singleton : resource.getSingletons()) {
			serviceDocument.getSingletons().put(singleton.getName() == null ? singleton.getUrl() : singleton.getName(),
					URIUtils.getURI(resource.getBaseURI(), singleton.getUrl()));
		}
		for (ServiceDocumentItem sdoc : resource.getRelatedServiceDocuments()) {
			serviceDocument.getRelatedServiceDocuments().put(sdoc.getName() == null ? sdoc.getUrl() : sdoc.getName(),
					URIUtils.getURI(resource.getBaseURI(), sdoc.getUrl()));
		}

		return serviceDocument;
	}

	private void updateValuable(final Valuable propertyResource, final ClientValuable odataValuable) {
		final Object propertyValue = getValue(odataValuable.getValue());
		if (odataValuable.hasPrimitiveValue()) {
			propertyResource.setType(odataValuable.getPrimitiveValue().getTypeName());
			propertyResource.setValue(propertyValue instanceof Geospatial ? ValueType.GEOSPATIAL : ValueType.PRIMITIVE,
					propertyValue);
		} else if (odataValuable.hasEnumValue()) {
			propertyResource.setType(odataValuable.getEnumValue().getTypeName());
			propertyResource.setValue(ValueType.ENUM, propertyValue);
		} else if (odataValuable.hasComplexValue()) {
			propertyResource.setType(odataValuable.getComplexValue().getTypeName());
			propertyResource.setValue(ValueType.COMPLEX, propertyValue);
		} else if (odataValuable.hasCollectionValue()) {
			final ClientCollectionValue<ClientValue> collectionValue = odataValuable.getCollectionValue();
			propertyResource.setType(collectionValue.getTypeName());
			final ClientValue value = collectionValue.iterator().hasNext() ? collectionValue.iterator().next() : null;
			ValueType valueType = ValueType.COLLECTION_PRIMITIVE;
			if (value == null) {
				valueType = ValueType.COLLECTION_PRIMITIVE;
			} else if (value.isPrimitive()) {
				valueType = value.asPrimitive().toValue() instanceof Geospatial ? ValueType.COLLECTION_GEOSPATIAL
						: ValueType.COLLECTION_PRIMITIVE;
			} else if (value.isEnum()) {
				valueType = ValueType.COLLECTION_ENUM;
			} else if (value.isComplex()) {
				valueType = ValueType.COLLECTION_COMPLEX;
			}
			propertyResource.setValue(valueType, propertyValue);
		}
	}

	private void annotations(final ClientAnnotatable odataAnnotatable, final Annotatable annotatable) {
		for (ClientAnnotation odataAnnotation : odataAnnotatable.getAnnotations()) {
			final Annotation annotation = new Annotation();

			annotation.setTerm(odataAnnotation.getTerm());
			annotation.setType(odataAnnotation.getValue().getTypeName());
			updateValuable(annotation, odataAnnotation);

			annotatable.getAnnotations().add(annotation);
		}
	}

	@Override
	public EntityCollection getEntitySet(final ClientEntitySet odataEntitySet) {
		final EntityCollection entitySet = new EntityCollection();

		entitySet.setCount(odataEntitySet.getCount());

		final URI next = odataEntitySet.getNext();
		if (next != null) {
			entitySet.setNext(next);
		}

		for (ClientEntity entity : odataEntitySet.getEntities()) {
			entitySet.getEntities().add(getEntity(entity));
		}

		entitySet.setDeltaLink(odataEntitySet.getDeltaLink());
		annotations(odataEntitySet, entitySet);
		return entitySet;
	}

	protected void links(final ClientLinked odataLinked, final Linked linked) {
		// -------------------------------------------------------------
		// Append navigation links (handling inline entity / entity set as well)
		// -------------------------------------------------------------
		// handle navigation links
		for (ClientLink link : odataLinked.getNavigationLinks()) {
			// append link
			LOG.debug("Append navigation link\n{}", link);
			linked.getNavigationLinks().add(getLink(link));
		}
		// -------------------------------------------------------------

		// -------------------------------------------------------------
		// Append association links
		// -------------------------------------------------------------
		for (ClientLink link : odataLinked.getAssociationLinks()) {
			LOG.debug("Append association link\n{}", link);
			linked.getAssociationLinks().add(getLink(link));
		}
		// -------------------------------------------------------------

		for (Link link : linked.getNavigationLinks()) {
			final ClientLink odataLink = odataLinked.getNavigationLink(link.getTitle());
			if (!(odataLink instanceof ClientInlineEntity) && !(odataLink instanceof ClientInlineEntitySet)) {
				annotations(odataLink, link);
			}
		}
	}

	@Override
	public Entity getEntity(final ClientEntity odataEntity) {
		final Entity entity = new Entity();

		entity.setType(odataEntity.getTypeName() == null ? null : odataEntity.getTypeName().toString());

		// -------------------------------------------------------------
		// Add edit and self link
		// -------------------------------------------------------------
		final URI odataEditLink = odataEntity.getEditLink();
		if (odataEditLink != null) {
			final Link editLink = new Link();
			editLink.setTitle(entity.getType());
			editLink.setHref(odataEditLink.toASCIIString());
			editLink.setRel(Constants.EDIT_LINK_REL);
			entity.setEditLink(editLink);
		}

		if (odataEntity.isReadOnly()) {
			final Link selfLink = new Link();
			selfLink.setTitle(entity.getType());
			selfLink.setHref(odataEntity.getLink().toASCIIString());
			selfLink.setRel(Constants.SELF_LINK_REL);
			entity.setSelfLink(selfLink);
		}
		// -------------------------------------------------------------

		links(odataEntity, entity);

		// -------------------------------------------------------------
		// Append edit-media links
		// -------------------------------------------------------------
		for (ClientLink link : odataEntity.getMediaEditLinks()) {
			LOG.debug("Append edit-media link\n{}", link);
			entity.getMediaEditLinks().add(getLink(link));
		}
		// -------------------------------------------------------------

		if (odataEntity.isMediaEntity()) {
			entity.setMediaContentSource(odataEntity.getMediaContentSource());
			entity.setMediaContentType(odataEntity.getMediaContentType());
			entity.setMediaETag(odataEntity.getMediaETag());
		}

		for (ClientProperty property : odataEntity.getProperties()) {
			entity.getProperties().add(getProperty(property));
		}

		entity.setId(odataEntity.getId());
		annotations(odataEntity, entity);
		return entity;
	}

	@Override
	public Link getLink(final ClientLink link) {
		final Link linkResource = new Link();
		linkResource.setRel(link.getRel());
		linkResource.setTitle(link.getName());
		linkResource.setHref(link.getLink() == null ? null : link.getLink().toASCIIString());
		linkResource.setType(link.getType().toString());
		linkResource.setMediaETag(link.getMediaETag());

		if (link instanceof ClientInlineEntity) {
			// append inline entity
			final ClientEntity inlineEntity = ((ClientInlineEntity) link).getEntity();
			LOG.debug("Append in-line entity\n{}", inlineEntity);

			linkResource.setInlineEntity(getEntity(inlineEntity));
		} else if (link instanceof ClientInlineEntitySet) {
			// append inline entity set
			final ClientEntitySet InlineEntitySet = ((ClientInlineEntitySet) link).getEntitySet();
			LOG.debug("Append in-line entity set\n{}", InlineEntitySet);

			linkResource.setInlineEntitySet(getEntitySet(InlineEntitySet));
		}

		return linkResource;
	}

	@Override
	public Property getProperty(final ClientProperty property) {

		final Property propertyResource = new Property();
		propertyResource.setName(property.getName());
		updateValuable(propertyResource, property);
		annotations(property, propertyResource);

		return propertyResource;
	}

	protected Object getValue(final ClientValue value) {
		Object valueResource = null;
		if (value == null) {
			return null;
		}
		if (value.isEnum()) {
			valueResource = value.asEnum().getValue();
		} else if (value.isPrimitive()) {
			valueResource = value.asPrimitive().toValue();
		} else if (value.isComplex()) {
			List<Property> complexProperties = new ArrayList<Property>();
			for (final ClientProperty propertyValue : value.asComplex()) {
				complexProperties.add(getProperty(propertyValue));
			}
			final ComplexValue lcValueResource = new ComplexValue();
			lcValueResource.getValue().addAll(complexProperties);
			annotations(value.asComplex(), lcValueResource);
			links(value.asComplex(), lcValueResource);
			valueResource = lcValueResource;

		} else if (value.isCollection()) {
			final ClientCollectionValue<? extends ClientValue> _value = value.asCollection();
			ArrayList<Object> lcValueResource = new ArrayList<Object>();

			for (final ClientValue collectionValue : _value) {
				lcValueResource.add(getValue(collectionValue));
			}
			valueResource = lcValueResource;
		}
		return valueResource;
	}

	private void odataAnnotations(final Annotatable annotatable, final ClientAnnotatable odataAnnotatable) {
		for (Annotation annotation : annotatable.getAnnotations()) {
			FullQualifiedName fqn = null;
			if (client instanceof EdmEnabledODataClient) {
				final EdmTerm term = ((EdmEnabledODataClient) client).getCachedEdm()
						.getTerm(new FullQualifiedName(annotation.getTerm()));
				if (term != null) {
					fqn = term.getType().getFullQualifiedName();
				}
			}

			if (fqn == null && annotation.getType() != null) {
				final EdmTypeInfo typeInfo = new EdmTypeInfo.Builder().setTypeExpression(annotation.getType()).build();
				if (typeInfo.isPrimitiveType()) {
					fqn = typeInfo.getPrimitiveTypeKind().getFullQualifiedName();
				}
			}

			final ClientAnnotation odataAnnotation = new ClientAnnotationImpl(annotation.getTerm(),
					getODataValue(fqn, annotation, null, null));
			odataAnnotatable.getAnnotations().add(odataAnnotation);
		}
	}

	@Override
	public ClientEntitySet getODataEntitySet(final ResWrap<EntityCollection> resource) {
		if (LOG.isDebugEnabled()) {
			final StringWriter writer = new StringWriter();
			try {
				client.getSerializer(ContentType.JSON).write(writer, resource.getPayload());
			} catch (final ODataSerializerException e) {
				LOG.debug("EntitySet -> ODataEntitySet:\n{}", writer.toString());
			}
			writer.flush();
			LOG.debug("EntitySet -> ODataEntitySet:\n{}", writer.toString());
		}

		final URI base = resource.getContextURL() == null ? resource.getPayload().getBaseURI()
				: ContextURLParser.parse(resource.getContextURL()).getServiceRoot();

		final URI next = resource.getPayload().getNext();

		final ClientEntitySet entitySet = next == null ? client.getObjectFactory().newEntitySet()
				: client.getObjectFactory().newEntitySet(URIUtils.getURI(base, next.toASCIIString()));

		if (resource.getPayload().getCount() != null) {
			entitySet.setCount(resource.getPayload().getCount());
		}

		for (Entity entityResource : resource.getPayload().getEntities()) {
			add(entitySet, getODataEntity(
					new ResWrap<Entity>(resource.getContextURL(), resource.getMetadataETag(), entityResource)));
		}

		if (resource.getPayload().getDeltaLink() != null) {
			entitySet.setDeltaLink(URIUtils.getURI(base, resource.getPayload().getDeltaLink()));
		}
		odataAnnotations(resource.getPayload(), entitySet);

		return entitySet;
	}

	protected void odataNavigationLinks(final EdmType edmType, final Linked linked, final ClientLinked odataLinked,
			final String metadataETag, final URI base) {
		for (Link link : linked.getNavigationLinks()) {
			final String href = link.getHref();
			final String title = link.getTitle();
			final Entity inlineEntity = link.getInlineEntity();
			final EntityCollection inlineEntitySet = link.getInlineEntitySet();
			if (inlineEntity == null && inlineEntitySet == null) {
				ClientLinkType linkType = null;
				if (edmType instanceof EdmStructuredType) {
					final EdmNavigationProperty navProp = ((EdmStructuredType) edmType).getNavigationProperty(title);
					if (navProp != null) {
						linkType = navProp.isCollection() ? ClientLinkType.ENTITY_SET_NAVIGATION
								: ClientLinkType.ENTITY_NAVIGATION;
					}
				}
				if (linkType == null) {
					linkType = link.getType() == null ? ClientLinkType.ENTITY_NAVIGATION
							: ClientLinkType.fromString(link.getRel(), link.getType());
				}

				odataLinked.addLink(linkType == ClientLinkType.ENTITY_NAVIGATION
						? client.getObjectFactory().newEntityNavigationLink(title, URIUtils.getURI(base, href))
						: client.getObjectFactory().newEntitySetNavigationLink(title, URIUtils.getURI(base, href)));
			} else if (inlineEntity != null) {
				odataLinked.addLink(
						createODataInlineEntity(inlineEntity, URIUtils.getURI(base, href), title, metadataETag));
			} else {
				odataLinked.addLink(createODataInlineEntitySet(inlineEntitySet,
						href == null ? null : URIUtils.getURI(base, href), title, metadataETag));
			}
		}

		for (ClientLink link : odataLinked.getNavigationLinks()) {
			if (!(link instanceof ClientInlineEntity) && !(link instanceof ClientInlineEntitySet)) {
				odataAnnotations(linked.getNavigationLink(link.getName()), link);
			}
		}
	}

	private ClientInlineEntity createODataInlineEntity(final Entity inlineEntity, final URI uri, final String title,
			final String metadataETag) {
		return new ClientInlineEntity(uri, ClientLinkType.ENTITY_NAVIGATION, title,
				getODataEntity(new ResWrap<Entity>(inlineEntity.getBaseURI() == null ? null : inlineEntity.getBaseURI(),
						metadataETag, inlineEntity)));
	}

	private ClientInlineEntitySet createODataInlineEntitySet(final EntityCollection inlineEntitySet, final URI uri,
			final String title, final String metadataETag) {
		return new ClientInlineEntitySet(uri, ClientLinkType.ENTITY_SET_NAVIGATION, title,
				getODataEntitySet(new ResWrap<EntityCollection>(
						inlineEntitySet.getBaseURI() == null ? null : inlineEntitySet.getBaseURI(), metadataETag,
						inlineEntitySet)));
	}

	private EdmEntityType findEntityType(final String entitySetOrSingletonOrType, final EdmEntityContainer container) {

		EdmEntityType type = null;

		final String firstToken = StringUtils.substringBefore(entitySetOrSingletonOrType, "/");
		EdmBindingTarget bindingTarget = container.getEntitySet(firstToken);
		if (bindingTarget == null) {
			bindingTarget = container.getSingleton(firstToken);
		}
		if (bindingTarget != null) {
			type = bindingTarget.getEntityType();
		}

		if (entitySetOrSingletonOrType.indexOf('/') != -1) {
			final String[] splitted = entitySetOrSingletonOrType.split("/");
			if (splitted.length > 1) {
				for (int i = 1; i < splitted.length && type != null; i++) {
					final EdmNavigationProperty navProp = type.getNavigationProperty(splitted[i]);
					if (navProp == null) {
						type = null;
					} else {
						type = navProp.getType();
					}
				}
			}
		}

		return type;
	}

	/**
	 * Infer type name from various sources of information including Edm and
	 * context URL, if available.
	 *
	 * @param candidateTypeName
	 *            type name as provided by the service
	 * @param contextURL
	 *            context URL
	 * @param metadataETag
	 *            metadata ETag
	 * @return Edm type information
	 */
	private EdmType findType(final String candidateTypeName, final ContextURL contextURL, final String metadataETag) {
		EdmType type = null;

		if (client instanceof EdmEnabledODataClient) {
			final Edm edm = ((EdmEnabledODataClient) client).getEdm(metadataETag);
			if (StringUtils.isNotBlank(candidateTypeName)) {
				type = edm.getEntityType(new FullQualifiedName(candidateTypeName));
			}
			if (type == null && contextURL != null) {
				if (contextURL.getDerivedEntity() == null) {
					for (EdmSchema schema : edm.getSchemas()) {
						final EdmEntityContainer container = schema.getEntityContainer();
						if (container != null) {
							final EdmEntityType entityType = findEntityType(contextURL.getEntitySetOrSingletonOrType(),
									container);

							if (entityType != null) {
								if (contextURL.getNavOrPropertyPath() == null) {
									type = entityType;
								} else {
									final EdmNavigationProperty navProp = entityType
											.getNavigationProperty(contextURL.getNavOrPropertyPath());

									type = navProp == null ? entityType : navProp.getType();
								}
							}
						}
					}
					if (type == null) {
						type = new EdmTypeInfo.Builder().setEdm(edm)
								.setTypeExpression(contextURL.getEntitySetOrSingletonOrType()).build().getType();
					}
				} else {
					type = edm.getEntityType(new FullQualifiedName(contextURL.getDerivedEntity()));
				}
			}
		}

		return type;
	}

	private ClientLink createLinkFromNavigationProperty(final Property property, final String propertyTypeName) {
		if (property.isCollection()) {
			EntityCollection inlineEntitySet = new EntityCollection();
			for (final Object inlined : property.asCollection()) {
				Entity inlineEntity = new Entity();
				inlineEntity.setType(propertyTypeName);
				inlineEntity.getProperties().addAll(((ComplexValue) inlined).getValue());
				inlineEntitySet.getEntities().add(inlineEntity);
			}
			return createODataInlineEntitySet(inlineEntitySet, null, property.getName(), null);
		} else {
			Entity inlineEntity = new Entity();
			inlineEntity.setType(propertyTypeName);
			inlineEntity.getProperties().addAll(property.asComplex().getValue());
			return createODataInlineEntity(inlineEntity, null, property.getName(), null);
		}
	}

	@Override
	public ClientEntity getODataEntity(final ResWrap<Entity> resource) {
		if (LOG.isDebugEnabled()) {
			final StringWriter writer = new StringWriter();
			try {
				client.getSerializer(ContentType.JSON).write(writer, resource.getPayload());
			} catch (final ODataSerializerException e) {
				LOG.debug("EntityResource -> ODataEntity:\n{}", writer.toString());
			}
			writer.flush();
			LOG.debug("EntityResource -> ODataEntity:\n{}", writer.toString());
		}

		final ContextURL contextURL = ContextURLParser.parse(resource.getContextURL());
		final URI base = resource.getContextURL() == null ? resource.getPayload().getBaseURI()
				: contextURL.getServiceRoot();
		final EdmType edmType = findType(resource.getPayload().getType(), contextURL, resource.getMetadataETag());
		FullQualifiedName typeName = null;
		if (resource.getPayload().getType() == null) {
			if (edmType != null) {
				typeName = edmType.getFullQualifiedName();
			}
		} else {
			typeName = new FullQualifiedName(resource.getPayload().getType());
		}

		final ClientEntity entity = resource.getPayload().getSelfLink() == null
				? client.getObjectFactory().newEntity(typeName)
				: client.getObjectFactory().newEntity(typeName,
						URIUtils.getURI(base, resource.getPayload().getSelfLink().getHref()));

		if (StringUtils.isNotBlank(resource.getPayload().getETag())) {
			entity.setETag(resource.getPayload().getETag());
		}

		if (resource.getPayload().getEditLink() != null) {
			entity.setEditLink(URIUtils.getURI(base, resource.getPayload().getEditLink().getHref()));
		}

		for (Link link : resource.getPayload().getAssociationLinks()) {
			entity.addLink(client.getObjectFactory().newAssociationLink(link.getTitle(),
					URIUtils.getURI(base, link.getHref())));
		}

		odataNavigationLinks(edmType, resource.getPayload(), entity, resource.getMetadataETag(), base);

		for (Link link : resource.getPayload().getMediaEditLinks()) {
			entity.addLink(
					client.getObjectFactory().newMediaEditLink(link.getTitle(), URIUtils.getURI(base, link.getHref())));
		}

		for (Operation op : resource.getPayload().getOperations()) {
			ClientOperation operation = new ClientOperation();
			operation.setTarget(URIUtils.getURI(base, op.getTarget()));
			operation.setTitle(op.getTitle());
			operation.setMetadataAnchor(op.getMetadataAnchor());
			entity.getOperations().add(operation);
		}

		if (resource.getPayload().isMediaEntity()) {
			entity.setMediaEntity(true);
			entity.setMediaContentSource(URIUtils.getURI(base, resource.getPayload().getMediaContentSource()));
			entity.setMediaContentType(resource.getPayload().getMediaContentType());
			entity.setMediaETag(resource.getPayload().getMediaETag());
		}

		for (final Property property : resource.getPayload().getProperties()) {
			EdmType propertyType = null;
			if (edmType instanceof EdmEntityType) {
				final EdmElement edmProperty = ((EdmEntityType) edmType).getProperty(property.getName());
				if (edmProperty != null) {
					propertyType = edmProperty.getType();
					if (edmProperty instanceof EdmNavigationProperty && !property.isNull()) {
						final String propertyTypeName = propertyType.getFullQualifiedName()
								.getFullQualifiedNameAsString();
						entity.addLink(createLinkFromNavigationProperty(property, propertyTypeName));
						continue;
					}
				}
			}
			add(entity, getODataProperty(propertyType, property));
		}

		entity.setId(resource.getPayload().getId());
		odataAnnotations(resource.getPayload(), entity);

		return entity;
	}

	@Override
	public ClientProperty getODataProperty(final ResWrap<Property> resource) {
		final Property payload = resource.getPayload();
		final EdmTypeInfo typeInfo = buildTypeInfo(ContextURLParser.parse(resource.getContextURL()),
				resource.getMetadataETag(), payload.getName(), payload.getType());

		final ClientProperty property = new ClientPropertyImpl(payload.getName(),
				getODataValue(typeInfo == null ? null : typeInfo.getFullQualifiedName(), payload,
						resource.getContextURL(), resource.getMetadataETag()));
		odataAnnotations(payload, property);

		return property;
	}

	private EdmTypeInfo buildTypeInfo(final ContextURL contextURL, final String metadataETag, final String propertyName,
			final String propertyType) {

		FullQualifiedName typeName = null;
		final EdmType type = findType(null, contextURL, metadataETag);
		if (type instanceof EdmStructuredType) {
			final EdmProperty edmProperty = ((EdmStructuredType) type).getStructuralProperty(propertyName);
			if (edmProperty != null) {
				typeName = edmProperty.getType().getFullQualifiedName();
			}
		}
		if (typeName == null && type != null) {
			typeName = type.getFullQualifiedName();
		}

		return buildTypeInfo(typeName, propertyType);
	}

	private EdmTypeInfo buildTypeInfo(final FullQualifiedName typeName, final String propertyType) {
		EdmTypeInfo typeInfo = null;
		if (typeName == null) {
			if (propertyType != null) {
				typeInfo = new EdmTypeInfo.Builder().setTypeExpression(propertyType).build();
			}
		} else {
			if (propertyType == null
					|| propertyType.equals(EdmPrimitiveTypeKind.String.getFullQualifiedName().toString())) {
				typeInfo = new EdmTypeInfo.Builder().setTypeExpression(typeName.toString()).build();
			} else if (isPrimiteveType(typeName)) {
				// Inheritance is not allowed for primitve types, so we use the
				// type given by the EDM
				typeInfo = new EdmTypeInfo.Builder().setTypeExpression(typeName.toString()).build();
			} else {
				typeInfo = new EdmTypeInfo.Builder().setTypeExpression(propertyType).build();
			}
		}
		return typeInfo;
	}

	private boolean isPrimiteveType(final FullQualifiedName typeName) {
		try {
			return EdmPrimitiveTypeKind.valueOfFQN(typeName) != null;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	protected ClientProperty getODataProperty(final EdmType type, final Property resource) {
		final EdmTypeInfo typeInfo = buildTypeInfo(type == null ? null : type.getFullQualifiedName(),
				resource.getType());

		final ClientProperty property = new ClientPropertyImpl(resource.getName(),
				getODataValue(typeInfo == null ? null : typeInfo.getFullQualifiedName(), resource, null, null));
		odataAnnotations(resource, property);

		return property;
	}

	protected ClientValue getODataValue(final FullQualifiedName type, final Valuable valuable, final URI contextURL,
			final String metadataETag) {

		// fixes enum values treated as primitive when no type information is
		// available
		if (client instanceof EdmEnabledODataClient && type != null) {
			final EdmEnumType edmType = ((EdmEnabledODataClient) client).getEdm(metadataETag).getEnumType(type);
			if (!valuable.isCollection() && valuable.isPrimitive() && edmType != null) {
				valuable.setValue(ValueType.ENUM, valuable.asPrimitive());
			}
		}

		ClientValue value = null;

		if (valuable.isCollection()) {
			value = client.getObjectFactory()
					.newCollectionValue(type == null ? null : "Collection(" + type.toString() + ")");

			for (Object _value : valuable.asCollection()) {
				final Property fake = new Property();
				fake.setValue(valuable.getValueType().getBaseType(), _value);
				value.asCollection().add(getODataValue(type, fake, contextURL, metadataETag));
			}
		} else if (valuable.isEnum()) {
			value = client.getObjectFactory().newEnumValue(type == null ? null : type.toString(),
					valuable.asEnum().toString());
		} else if (valuable.isComplex()) {
			final ClientComplexValue lcValue = client.getObjectFactory()
					.newComplexValue(type == null ? null : type.toString());

			EdmComplexType edmType = null;
			if (client instanceof EdmEnabledODataClient && type != null) {
				edmType = ((EdmEnabledODataClient) client).getEdm(metadataETag).getComplexType(type);
			}

			for (Property property : valuable.asComplex().getValue()) {
				EdmType edmPropertyType = null;
				if (edmType != null) {
					final EdmElement edmProp = edmType.getProperty(property.getName());
					if (edmProp != null) {
						edmPropertyType = edmProp.getType();
					}
				}
				lcValue.add(getODataProperty(edmPropertyType, property));
			}

			odataNavigationLinks(edmType, valuable.asComplex(), lcValue, metadataETag, contextURL);
			odataAnnotations(valuable.asComplex(), lcValue);

			value = lcValue;
		} else {
			if (valuable.isGeospatial()) {
				value = client.getObjectFactory().newPrimitiveValueBuilder().setValue(valuable.asGeospatial())
						.setType(type == null || EdmPrimitiveTypeKind.Geography.getFullQualifiedName().equals(type)
								|| EdmPrimitiveTypeKind.Geometry.getFullQualifiedName().equals(type)
										? valuable.asGeospatial().getEdmPrimitiveTypeKind()
										: EdmPrimitiveTypeKind.valueOfFQN(type.toString()))
						.build();
			} else if (valuable.isPrimitive() || valuable.getValueType() == null) {
				// fixes non-string values treated as string when no type
				// information is available at de-serialization level
				if (type != null && !EdmPrimitiveTypeKind.String.getFullQualifiedName().equals(type)
						&& EdmPrimitiveType.EDM_NAMESPACE.equals(type.getNamespace())
						&& valuable.asPrimitive() instanceof String) {

					final EdmPrimitiveType primitiveType = EdmPrimitiveTypeFactory
							.getInstance(EdmPrimitiveTypeKind.valueOf(type.getName()));
					final Class<?> returnType = primitiveType.getDefaultType().isAssignableFrom(Calendar.class)
							? Timestamp.class : primitiveType.getDefaultType();
					try {
						valuable.setValue(valuable.getValueType(),
								primitiveType.valueOfString(valuable.asPrimitive().toString(), null, null,
										Constants.DEFAULT_PRECISION, Constants.DEFAULT_SCALE, null, returnType));
					} catch (EdmPrimitiveTypeException e) {
						throw new IllegalArgumentException(e);
					}
				}

				value = client.getObjectFactory().newPrimitiveValueBuilder().setValue(valuable.asPrimitive())
						.setType(type == null || !EdmPrimitiveType.EDM_NAMESPACE.equals(type.getNamespace()) ? null
								: EdmPrimitiveTypeKind.valueOfFQN(type.toString()))
						.build();
			} else if (valuable.isComplex()) {
				final ClientComplexValue cValue = client.getObjectFactory()
						.newComplexValue(type == null ? null : type.toString());

				if (!valuable.isNull()) {
					EdmComplexType edmType = null;
					if (client instanceof EdmEnabledODataClient && type != null) {
						edmType = ((EdmEnabledODataClient) client).getEdm(metadataETag).getComplexType(type);
					}

					for (Property property : valuable.asComplex().getValue()) {
						EdmType edmPropertyType = null;
						if (edmType != null) {
							final EdmElement edmProp = edmType.getProperty(property.getName());
							if (edmProp != null) {
								edmPropertyType = edmProp.getType();
							}
						}

						cValue.add(getODataProperty(edmPropertyType, property));
					}
				}

				value = cValue;
			}
		}

		return value;
	}

	@Override
	public ClientDelta getODataDelta(final ResWrap<Delta> resource) {
		final URI base = resource.getContextURL() == null ? resource.getPayload().getBaseURI()
				: ContextURLParser.parse(resource.getContextURL()).getServiceRoot();

		final URI next = resource.getPayload().getNext();

		final ClientDelta delta = next == null ? client.getObjectFactory().newDelta()
				: client.getObjectFactory().newDelta(URIUtils.getURI(base, next.toASCIIString()));

		if (resource.getPayload().getCount() != null) {
			delta.setCount(resource.getPayload().getCount());
		}

		if (resource.getPayload().getDeltaLink() != null) {
			delta.setDeltaLink(URIUtils.getURI(base, resource.getPayload().getDeltaLink()));
		}

		for (Entity entityResource : resource.getPayload().getEntities()) {
			add(delta, getODataEntity(
					new ResWrap<Entity>(resource.getContextURL(), resource.getMetadataETag(), entityResource)));
		}
		for (DeletedEntity deletedEntity : resource.getPayload().getDeletedEntities()) {
			final ClientDeletedEntityImpl impl = new ClientDeletedEntityImpl();
			impl.setId(URIUtils.getURI(base, deletedEntity.getId()));
			impl.setReason(Reason.valueOf(deletedEntity.getReason().name()));

			delta.getDeletedEntities().add(impl);
		}

		odataAnnotations(resource.getPayload(), delta);

		for (DeltaLink link : resource.getPayload().getAddedLinks()) {
			final ClientDeltaLink impl = new ClientDeltaLinkImpl();
			impl.setRelationship(link.getRelationship());
			impl.setSource(URIUtils.getURI(base, link.getSource()));
			impl.setTarget(URIUtils.getURI(base, link.getTarget()));

			odataAnnotations(link, impl);

			delta.getAddedLinks().add(impl);
		}
		for (DeltaLink link : resource.getPayload().getDeletedLinks()) {
			final ClientDeltaLink impl = new ClientDeltaLinkImpl();
			impl.setRelationship(link.getRelationship());
			impl.setSource(URIUtils.getURI(base, link.getSource()));
			impl.setTarget(URIUtils.getURI(base, link.getTarget()));

			odataAnnotations(link, impl);

			delta.getDeletedLinks().add(impl);
		}

		return delta;
	}
}
