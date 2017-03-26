package com.example.c2c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import com.google.inject.Singleton;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;

@Singleton
public class Client {
	private final DiscoveryClient discoveryClient;
	private AtomicInteger next = new AtomicInteger(0);
	private final Set<String> blackList = new CopyOnWriteArraySet<>();

	@Inject
	public Client(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	public String get() throws IOException {
		return getResponse(5);
	}

	String getResponse(int limit) throws IOException {
		if (limit <= 0) {
			return "ERROR";
		}
		InstanceInfo instance = getInstance();

		if (blackList.contains(instance.getInstanceId())) {
			// Retry
			return getResponse(limit - 1);
		}

		URL url = new URL(String.format("http://%s:%d", instance.getHostName(),
				instance.getPort()));
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() != 200) {
				Thread.sleep(1000);
				// Retry
				return getResponse(limit - 1);
			}
			return readContent(conn);
		}
		catch (Exception e) {
			blackList.add(instance.getInstanceId());
			instance.setOverriddenStatus(InstanceInfo.InstanceStatus.DOWN);
			// Retry
			return getResponse(limit - 1);
		}
	}

	String readContent(HttpURLConnection conn) throws IOException {
		StringBuilder sb = new StringBuilder("Response: ");
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream()))) {
			String input;
			while ((input = br.readLine()) != null) {
				sb.append(input);
			}
		}
		return sb.toString();
	}

	InstanceInfo getInstance() {
		List<InstanceInfo> instances = discoveryClient.getInstancesByVipAddress("backend",
				false);
		instances.forEach(i -> {
			System.out.println("==============================");
			System.out.println("id      =" + i.getInstanceId());
			System.out.println("hostname=" + i.getHostName());
			System.out.println("port    =" + i.getPort());
			System.out.println("VIP     =" + i.getVIPAddress());
		});
		int i = next.getAndIncrement();
		if (i >= instances.size()) {
			next.set(0);
			i = 0;
		}
		return instances.get(i);
	}
}
