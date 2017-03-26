package am.ik.eureka;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.PropertiesInstanceConfig;

public class EurekaInstanceConfigImpl extends PropertiesInstanceConfig {
	private static final Logger log = LoggerFactory
			.getLogger(EurekaInstanceConfigImpl.class);

	@Override
	public String getAppname() {
		String appname = super.getAppname();
		log.debug("getAppname()={}", appname);
		return appname;
	}

	@Override
	public int getNonSecurePort() {
		int nonSecurePort = getPort()
				.orElseGet(() -> ofNullable(getenv("EUREKA_INSTANCE_NON_SECURE_PORT"))
						.map(Integer::parseInt).orElseGet(super::getNonSecurePort));
		log.debug("getNonSecurePort()={}", nonSecurePort);
		return nonSecurePort;
	}

	private Optional<Integer> getPort() {
		return ofNullable(getenv("PORT")).map(Integer::valueOf);
	}

	@Override
	public String getInstanceId() {
		String instanceId = ofNullable(getenv("INSTANCE_GUID"))
				.orElseGet(this::getInstanceId);
		log.debug("getInstanceId()={}", instanceId);
		return instanceId;
	}

	@Override
	public String getHostName(boolean refresh) {
		String cfInstanceInternalIp = System.getenv("CF_INSTANCE_INTERNAL_IP");
		String hostname = ofNullable(getenv("EUREKA_INSTANCE_HOSTNAME"))
				.orElseGet(() -> cfInstanceInternalIp == null ? super.getHostName(refresh)
						: cfInstanceInternalIp);
		log.debug("getHostName({})={}", refresh, hostname);
		return hostname;
	}

	@Override
	public String getVirtualHostName() {
		String virtualHostName = ofNullable(getenv("EUREKA_INSTANCE_VIRTUAL_HOST_NAME"))
				.orElseGet(this::getAppname);
		log.debug("getVirtualHostName()={}", virtualHostName);
		return virtualHostName;
	}

	@Override
	public int getLeaseRenewalIntervalInSeconds() {
		int seconds = ofNullable(
				getenv("EUREKA_INSTANCE_LEASE_RENEWAL_INTERVAL_IN_SECONDS"))
						.map(Integer::parseInt)
						.orElseGet(super::getLeaseRenewalIntervalInSeconds);
		log.debug("getLeaseRenewalIntervalInSeconds()={}", seconds);
		return seconds;
	}

	@Override
	public int getLeaseExpirationDurationInSeconds() {
		int seconds = ofNullable(
				getenv("EUREKA_INSTANCE_LEASE_EXPIRATION_DURATION_IN_SECONDS"))
						.map(Integer::parseInt)
						.orElseGet(super::getLeaseExpirationDurationInSeconds);
		log.debug("getLeaseExpirationDurationInSeconds()={}", seconds);
		return seconds;
	}

	@Override
	public boolean isInstanceEnabledOnit() {
		return true;
	}
}
