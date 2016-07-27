/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.ucsd.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.cisco.ukidcv.ucsd.http.HttpConnection.httpMethod;
import com.cisco.ukidcv.ucsd.http.HttpConnection.httpProtocol;

/**
 * Provides some demonstrations of how to connect to services in UCS Director
 *
 * @author Matt Day
 *
 */
public class HttpConnectionExample {
	public static void main(String[] args) throws ClientProtocolException, IOException {

		// Example one : simple way to connect to cisco.com via a proxy
		ProxySettings p = new ProxySettings("proxy.esl.cisco.com", 80);
		HttpConnection c = new HttpConnection("cisco.com", "/", httpProtocol.HTTPS, 443, httpMethod.GET, p);
		c.execute();
		System.out.println(c.getResponse());

		// Example two : connect to an invalid server via a proxy
		c = new HttpConnection("invalid.mrsmiggins.net", "/", httpProtocol.HTTPS, 443, httpMethod.GET, p);
		c.allowUntrustedCertificates(true);
		c.execute();
		System.out.println(c.getResponse());

		// Example three : direct connection
		p.setEnabled(false);
		c = new HttpConnection("www.cisco.com", "/", httpProtocol.HTTPS, 443, httpMethod.GET, p);
		c.allowUntrustedCertificates(true);
		c.execute();
		System.out.println(c.getResponse());
	}
}
