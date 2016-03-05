package com.helpermonkey.excel.macro;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class DummyTemplateFor_SourceCode {

//	public String myCookie = null;
//	public String xRequestDigest = null;
//
//	public SPRevenueListParser revenueParser = null;

	public DummyTemplateFor_SourceCode() throws Exception {

		//This is the code to link 2 entities within Share Point
		// set the id in an URI form of the record that we plan to update at the entry tag level
		// URI revenueUri =
		// RevenueOlingoMonkey.client.newURIBuilder(MonkeyConstants.LIST_URL).appendEntitySetSegment("Revenue").appendKeySegment(new
		// Integer(resourceEntity.getId())).build();
		// resourceEntity.setId(revenueUri);

		
		/*  *** none of these default http stuff works with Apache HttpClient and olingo uses apache http client
		hence we need to override and provide a custom CookieBasedHttpClientFactory to create a custom 
		httpclient with apache cookie handling and basic auth credentials provider. *** */
//		java.net.CookieManager cm = new java.net.CookieManager(null, CookiePolicy.ACCEPT_ALL);
//		java.net.CookieHandler.setDefault(cm);
//		Authenticator.setDefault(new AuthenticatorMonkey());
//		loginIntoSP();

//		revenueParser = new SPRevenueListParser();
	}

//	public void loginIntoSP() throws Exception {
//		// URL loginUrl = new URL(BDMConstants.LIST_URL);
//		// HttpURLConnection connection = (HttpURLConnection)
//		// loginUrl.openConnection();
//		// connection.setRequestMethod("GET");
//		//
//		// connection.setRequestProperty("Accept",
//		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//		// connection.setRequestProperty("Accept-Encoding", "gzip, deflate,
//		// sdch");
//		// connection.setRequestProperty("Accept-Language",
//		// "en-GB,en-US;q=0.8,en;q=0.6");
//		// connection.setRequestProperty("Connection", "keep-alive");
//		//
//		// String authCode = AuthenticatorMonkey.DOMAIN +
//		// AuthenticatorMonkey.USERNAME + ":" + AuthenticatorMonkey.PASSWORD;
//		// byte[] encodedBytes = Base64.encodeBase64(authCode.getBytes());
//		// authCode = "BASIC " + encodedBytes.toString();
//		// connection.setRequestProperty("Authorization", authCode);
//
////		TrustManager trustmanager = new X509TrustManager() {
////
////			@Override
////			public X509Certificate[] getAcceptedIssuers() {
////				return null;
////			}
////
////			@Override
////			public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
////			}
////
////			@Override
////			public void checkClientTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
////			}
////		};
////
////		try {
////
////			DefaultHttpClient client = new DefaultHttpClient();
////
////			NTCredentials credentials = new NTCredentials(BDMConstants.USERNAME, BDMConstants.PASSWORD,
////					BDMConstants.HOST, BDMConstants.DOMAIN);
////			AuthScope scope = new AuthScope(BDMConstants.HOST, BDMConstants.PORT);
////
////			SSLContext sslcontext = SSLContext.getInstance("TLS");
////			sslcontext.init(null, new TrustManager[] { trustmanager }, null);
////
////			SSLSocketFactory sf = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
////			Scheme https = new Scheme("https", 443, sf);
////
////			client.getConnectionManager().getSchemeRegistry().register(https);
////			client.getCredentialsProvider().setCredentials(scope, credentials);
////
////			HttpGet request = new HttpGet(BDMConstants.LIST_URL);
////			HttpResponse response = client.execute(request);
////			InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
////			BufferedReader rd = new BufferedReader(reader);
////
////			String line = "";
////			String result = "";
////
////			while ((line = rd.readLine()) != null)
////				result += line;
////
////		} catch (Exception ex) {
////			ex.printStackTrace();
////		}
//
//		// System.out.println("HTTP Status Code:" +
//		// response.getStatusLine().getStatusCode());
//		// System.out.println(response.getFirstHeader("Set-Cookie").getValue());
//
//		// connection.setDoInput(true);
//		// connection.setReadTimeout(20000);
//		// connection.setInstanceFollowRedirects(true);
//
//		// //make the http connection and login into the sharepoint site
//		// connection.connect();
//		//
//		// //get the cookie from the response header and set it to the class
//		// level variable for using in subsequent http requests
//		// myCookie = connection.getHeaderField("Set-Cookie");
//		
//		HttpURLConnection connection = (HttpURLConnection) new URL(BDMConstants.LIST_URL).openConnection();
//		connection.setRequestMethod(BDMConstants.GET_METHOD);
//
//		//make the http connection and login into the sharepoint site
//		connection.connect();
//		
//		//get the cookie from the response header and set it to the class level variable for using in subsequent http requests
//		myCookie = connection.getHeaderField(BDMConstants.SET_COOKIE_HEADER);
//		
//		System.out.println("headers:" + connection.getHeaderFields());
//		System.out.println("Response Code:" + connection.getResponseCode());
//	}
//
//	public void getListMetadata() throws Exception {
//		// ODataClient client = ODataClientFactory.getClient();
//		//
//		// ODataServiceDocumentRequest req =
//		// client.getRetrieveRequestFactory().getServiceDocumentRequest(BDMConstants.LIST_URL);
//		// ODataRetrieveResponse<ClientServiceDocument> res = req.execute();
//		//
//		// ClientServiceDocument serviceDocument = res.getBody();
//		//
//		// Collection<String> entitySetNames =
//		// serviceDocument.getEntitySetNames();
//		// Map<String,URI> entitySets = serviceDocument.getEntitySets();
//		// Map<String,URI> singletons = serviceDocument.getSingletons();
//		// Map<String,URI> functionImports =
//		// serviceDocument.getFunctionImports();
//		// URI revenueUrl = serviceDocument.getEntitySetURI("Revenue");
//		//
//		// EdmMetadataRequest request
//		// =
//		// client.getRetrieveRequestFactory().getMetadataRequest(BDMConstants.LIST_URL);
//		// ODataRetrieveResponse<Edm> response = request.execute();
//		//
//		// Edm edm = response.getBody();
//		//
//		// List<EdmSchema> schemas = edm.getSchemas();
//		// for (EdmSchema schema : schemas) {
//		// String namespace = schema.getNamespace();
//		// for (EdmComplexType complexType : schema.getComplexTypes()) {
//		// FullQualifiedName name = complexType.getFullQualifiedName();
//		// }
//		// for (EdmEntityType entityType : schema.getEntityTypes()) {
//		// FullQualifiedName name = entityType.getFullQualifiedName();
//		// }
//		// }
//		//
//		// EdmEntityType customerType = edm.getEntityType(
//		// new FullQualifiedName("ListDataModel", "Revenue"));
//		// List<String> propertyNames = customerType.getPropertyNames();
//		// for (String propertyName : propertyNames) {
//		// EdmProperty property =
//		// customerType.getStructuralProperty(propertyName);
//		// FullQualifiedName typeName =
//		// property.getType().getFullQualifiedName();
//		// }
//	}
//
//	// URI productsUri = client.newURIBuilder(serviceRoot)
//	// .appendEntitySetSegment("Products").build();
//	//
//	// ODataEntity product = (...)
//	//
//	// ODataEntityCreateRequest<ODataEntity> req = client.getCUDRequestFactory()
//	// .getEntityCreateRequest(productsUri, product);
//	//
//	// ODataEntityCreateResponse<ODataEntity> res = req.execute();
//	//
//	// if (res.getStatusCode()==201) {
//	// // Created
//	// } }
//
//	public static void main(String[] args) throws Exception {
//		RevenueODataMonkey revOdataMonkey = new RevenueODataMonkey();
//		revOdataMonkey.getRevenueListData();
//	}
//
//	public void getRevenueListData() throws Exception {
//		ODataClient client = ODataClientFactory.getClient();
//		
//		ODataRetrieveResponse<ClientEntitySetIterator<ClientEntitySet, ClientEntity>> response = null;
//		ODataEntitySetIteratorRequest<ClientEntitySet, ClientEntity> iteratorReq = null;
//
//		try {
//			
//			client.getConfiguration().setHttpClientFactory(new CookieBasedHttpClientFactory());
//
//			 URI authUri =
//			 client.newURIBuilder(BDMConstants.LIST_URL).appendEntitySetSegment(BDMConstants.REVENUE_LIST_NAME).build();
//			 iteratorReq =
//			 client.getRetrieveRequestFactory().getEntitySetIteratorRequest(authUri);
//			
//			// String authCode = AuthenticatorMonkey.DOMAIN +
////			 AuthenticatorMonkey.USERNAME + ":" +
////			 AuthenticatorMonkey.PASSWORD;
//			// byte[] encodedBytes = Base64.encodeBase64(authCode.getBytes());
//			// authCode = "BASIC " + encodedBytes.toString();
//			// iteratorReq.addCustomHeader("Authorization", authCode);
//			
//			 response = iteratorReq.execute();
//			//
//			// TrustManager trustmanager = new X509TrustManager() {
//			//
//			// @Override
//			// public X509Certificate[] getAcceptedIssuers() {
//			// return null;
//			// }
//			//
//			// @Override
//			// public void checkServerTrusted(X509Certificate[]
//			// ax509certificate, String s) throws CertificateException {}
//			//
//			// @Override
//			// public void checkClientTrusted(X509Certificate[]
//			// ax509certificate, String s) throws CertificateException {}
//			// };
//			//
//			// String url = BDMConstants.REVENUE_LIST_URL;
//			// DefaultHttpClient client = new DefaultHttpClient();
//			//
//			// NTCredentials credentials = new
//			// NTCredentials(AuthenticatorMonkey.USERNAME,
//			// AuthenticatorMonkey.PASSWORD, BDMConstants.HOST,
//			// AuthenticatorMonkey.DOMAIN);
//			// AuthScope authScope = new
//			// AuthScope(BDMConstants.HOST,BDMConstants.PORT);
//			//
//			// SSLContext sslcontext = SSLContext.getInstance("TLS");
//			// sslcontext.init(null, new TrustManager[] { trustmanager }, null);
//			//
//			// SSLSocketFactory sf = new SSLSocketFactory(sslcontext,
//			// SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//			// Scheme https = new Scheme("https", 443, sf);
//			//
//			// client.getConnectionManager().getSchemeRegistry().register(https);
//			// client.getCredentialsProvider().setCredentials(scope,
//			// credentials);
//			//
//			// HttpGet request = new HttpGet(url);
//			// //request.addHeader("Accept", "application/xml");
//			// request.addHeader("Accept", "application/json;odata=verbose");
//			//
//			// HttpResponse response = client.execute(request);
//			//
//			//
//			//// httpClient.getCredentialsProvider().setCredentials(authScope,
//			// credentials);
//			////
//			//// HttpResponse httpResponse = httpClient.execute(getRequest);
//
//			iteratorReq = null;
//			URI revenueUri = client.newURIBuilder(BDMConstants.LIST_URL)
//					.appendEntitySetSegment(BDMConstants.REVENUE_LIST_NAME).appendKeySegment("809").build();
//
//			iteratorReq = client.getRetrieveRequestFactory().getEntitySetIteratorRequest(revenueUri);
//
//			//iteratorReq.addCustomHeader("Cookie", myCookie);
//
//			response = iteratorReq.execute();
//
//			ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = response.getBody();
//
//			while (iterator.hasNext()) {
//				ClientEntity revenue = iterator.next();
//				List<ClientProperty> properties = revenue.getProperties();
//				for (ClientProperty property : properties) {
//					String name = property.getName();
//					ClientValue value = property.getValue();
//					String valueType = value.getTypeName();
//					System.out.println("Value Data Type:" + valueType);
//					System.out.println("Name:" + name + " | Value:" + value.toString());
//				}
//			}
//		} catch (ODataClientErrorException ex) {
//			System.out.println("HTTP Status Code:" + response.getStatusCode());
//			// if (response.getStatusCode() >= 400) {
//			// // Handle error
//			// }
//			ex.printStackTrace();
//		}
//	}
}
