/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 *
 * An SSL socket factory used to bypass self-signed certificate limitations on
 * web servers. Note that accepting self signed certificates is a security
 * vulnerability and you should therefore know the validity of the server you
 * are connecting to before proceeding.
 *
 * This code was sourced from:
 *
 * http://www.javaexample.net/2011/12/23/bypass-self-signed-certificate-on-
 * httpclient/
 *
 * @author rwhitear@cisco.com
 *
 */
public class UntrustedSSLSocketFactory implements ProtocolSocketFactory {

	/**
	 * Get the trusdt
	 *
	 * @return
	 */
	private static TrustManager[] getTrustManager() {

		TrustManager[] trustAllCerts = new TrustManager[] {

				// Anonymous inner class to build 'trustless' manager - allow
				// everything!
				new X509TrustManager() {
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
						// Do nothing (no checks - all permissive)
					}

					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
						// Do nothing (no checks - all permissive)
					}
				}
		};
		return trustAllCerts;
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		TrustManager[] trustAllCerts = UntrustedSSLSocketFactory.getTrustManager();
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
			return socketFactory.createSocket(host, port);
		}
		catch (Exception ex) {
			throw new UnknownHostException("Problems to connect " + host + ex.toString());
		}
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
			throws IOException, UnknownHostException {
		TrustManager[] trustAllCerts = UntrustedSSLSocketFactory.getTrustManager();
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
			return socketFactory.createSocket(host, port, clientHost, clientPort);
		}
		catch (Exception ex) {
			throw new UnknownHostException("Problems to connect " + host + ex.toString());
		}

	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort,
			HttpConnectionParams arg4) throws IOException, UnknownHostException, ConnectTimeoutException {
		TrustManager[] trustAllCerts = UntrustedSSLSocketFactory.getTrustManager();
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
			return socketFactory.createSocket(host, port);
		}
		catch (Exception ex) {
			throw new UnknownHostException("Problems to connect " + host + ex.toString());
		}
	}
}
