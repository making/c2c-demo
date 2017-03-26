package com.example.c2c;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.DiscoveryClient;

@Singleton
public class BackendServlet extends HttpServlet {
	private final DiscoveryClient discoveryClient;
	private final EurekaInstanceConfig config;

	@Inject
	public BackendServlet(DiscoveryClient discoveryClient, EurekaInstanceConfig config) {
		this.discoveryClient = discoveryClient;
		this.config = config;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		pw.println("Hello from " + config.getHostName(false) + ":"
				+ config.getNonSecurePort());
		pw.flush();
	}
}
