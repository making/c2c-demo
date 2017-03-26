package am.ik.eureka;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.discovery.DefaultEurekaClientConfig;

public class EurekaClientConfigImpl extends DefaultEurekaClientConfig {
	private static final Logger log = LoggerFactory
			.getLogger(EurekaClientConfigImpl.class);

	@Override
	public List<String> getEurekaServerServiceUrls(String myZone) {
		String envKey = "EUREKA_CLIENT_SERVICE_URL_" + myZone.toUpperCase();
		List<String> eurekaServerServiceUrls = Optional.ofNullable(System.getenv(envKey))
				.map(s -> Arrays.asList(s.split(",")))
				.orElseGet(() -> super.getEurekaServerServiceUrls(myZone));
		log.debug("getEurekaServerServiceUrls({})={}", myZone, eurekaServerServiceUrls);
		return eurekaServerServiceUrls;
	}

	@Override
	public int getRegistryFetchIntervalSeconds() {
		int seconds = ofNullable(getenv("EUREKA_CLIENT_REGISTRY_FETCH_INTERVAL_SECONDS"))
				.map(Integer::parseInt).orElseGet(super::getRegistryFetchIntervalSeconds);
		log.debug("getRegistryFetchIntervalSeconds()={}", seconds);
		return seconds;
	}

	@Override
	public int getInstanceInfoReplicationIntervalSeconds() {
		int seconds = ofNullable(
				getenv("EUREKA_CLIENT_INSTANCE_INFO_REPLICATION_INTERVAL_SECONDS"))
						.map(Integer::parseInt)
						.orElseGet(super::getInstanceInfoReplicationIntervalSeconds);
		log.debug("getInstanceInfoReplicationIntervalSeconds()={}", seconds);
		return seconds;
	}
}
