package am.ik.eureka;

import static com.netflix.appinfo.InstanceInfo.InstanceStatus.UP;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Singleton
public class EurekaRegistrar {
	private static final Logger log = LoggerFactory.getLogger(EurekaRegistrar.class);

	@Inject
	public EurekaRegistrar(ApplicationInfoManager applicationInfoManager,
			EurekaClient eurekaClient, InstanceInfo instanceInfo) {
		log.info("Registering to Eureka Server");
		// applicationInfoManager.setInstanceStatus(STARTING);
		log.info("Application Name = {}", instanceInfo.getAppName());
		log.info("Port = {}", instanceInfo.getPort());
		log.info("VIP = {}", instanceInfo.getVIPAddress());
		log.info("Metadata = {}", instanceInfo.getMetadata());
		log.info("Set instance status to UP");
		applicationInfoManager.setInstanceStatus(UP);
		log.info("Registered");
	}
}
