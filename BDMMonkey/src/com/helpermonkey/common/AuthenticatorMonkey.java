package com.helpermonkey.common;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.helpermonkey.util.StaticMasterData;

public class AuthenticatorMonkey extends Authenticator {

	public AuthenticatorMonkey() {

	}

	public PasswordAuthentication getPasswordAuthentication() {

		// return new PasswordAuthentication (DOMAIN + "\\" + USERNAME,
		// PASSWORD.toCharArray());
		return new PasswordAuthentication(StaticMasterData.LOGGED_IN_USERNAME, StaticMasterData.PASSWORD.toCharArray());
	}

	// sample code for http basic auth and handling cookies from the response
	// public void loginUsingBasicAuth(){
	// HttpURLConnection connection;
	// URL loginUrl = new URL(loginUrl);
	// connection = (HttpURLConnection) loginUrl.openConnection();
	// connection.setRequestMethod("GET");
	// connection.setRequestProperty("Accept",
	// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	// connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
	// connection.setRequestProperty("Accept-Language",
	// "en-GB,en-US;q=0.8,en;q=0.6");
	// connection.setRequestProperty("Connection", "keep-alive");
	//
	// String authCode = USERNAME + ":" + PASSWORD;
	// byte[] encodedBytes = Base64.encodeBase64(authCode.getBytes());
	// authCode = "BASIC " + encodedBytes.toString();
	// connection.addRequestProperty("Authorization", authCode);
	//
	// connection.connect();
	//
	// myCookie = connection.getHeaderField("Set-Cookie");
	//
	// //sample code for handling cookies
	// String headerName=null;
	// for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++)
	// {
	// if (headerName.equals("Set-Cookie")) {
	// String cookie = connection.getHeaderField(i);
	// }
	// cookie = cookie.substring(0, cookie.indexOf(";"));
	// String cookieName = cookie.substring(0, cookie.indexOf("="));
	// String cookieValue = cookie.substring(cookie.indexOf("=") + 1,
	// cookie.length());
	// }

}
