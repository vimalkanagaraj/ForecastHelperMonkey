
package com.helpermonkey.olingo;

import java.net.URI;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.olingo.client.core.http.AbstractHttpClientFactory;
import org.apache.olingo.commons.api.http.HttpMethod;

import com.helpermonkey.util.StaticMasterData;

public class CookieBasedHttpClientFactory extends AbstractHttpClientFactory {

	protected CloseableHttpClient httpclient = null;

	//public static CookieManager cookieMgr = null;
	public static CookieStore store = null;

	@Override
	public HttpClient create(final HttpMethod method, final URI uri) {
		 if(store == null){
			// Create a new static instance of the cookie store, first ever time
		    store = new BasicCookieStore();
		    //else
		    //all subsequent requests should not create a new static store instead
			//should transfer the cookie store and set it as part of the new httpClient instance, this way
		    //the cookie store is carried across multiple httpClient instance without losing the cookie
		 }

		PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();

		Registry<CookieSpecProvider> registry = RegistryBuilder.<CookieSpecProvider> create()
				.register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider(publicSuffixMatcher))
				.register(CookieSpecs.STANDARD, new RFC6265CookieSpecProvider(publicSuffixMatcher)).build();

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(null, AuthScope.ANY_PORT),
				new UsernamePasswordCredentials(StaticMasterData.LOGGED_IN_USERNAME, StaticMasterData.PASSWORD));
		
		httpclient = HttpClients.custom().setDefaultCookieSpecRegistry(registry).setDefaultCookieStore(store)
				.setDefaultRequestConfig(requestConfig).setDefaultCredentialsProvider(credsProvider).build();
		
		return httpclient;
	}

	@Override
	public void close(final HttpClient httpClient) {
		httpClient.getConnectionManager().shutdown();
	}

}
