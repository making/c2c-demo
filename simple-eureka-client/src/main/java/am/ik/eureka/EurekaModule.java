package am.ik.eureka;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.jersey.Jersey1DiscoveryClientOptionalArgs;

public class EurekaModule extends AbstractModule {
	@Override
	protected void configure() {
		// need to eagerly initialize
		bind(ApplicationInfoManager.class).asEagerSingleton();

		bind(EurekaInstanceConfig.class).to(EurekaInstanceConfigImpl.class)
				.in(Scopes.SINGLETON);
		bind(EurekaClientConfig.class).to(EurekaClientConfigImpl.class)
				.in(Scopes.SINGLETON);

		// this is the self instanceInfo used for registration purposes
		bind(InstanceInfo.class).toProvider(EurekaConfigBasedInstanceInfoProvider.class)
				.in(Scopes.SINGLETON);

		bind(EurekaClient.class).to(DiscoveryClient.class).in(Scopes.SINGLETON);

		// Default to the jersey1 discovery client optional args
		bind(AbstractDiscoveryClientOptionalArgs.class)
				.to(Jersey1DiscoveryClientOptionalArgs.class).in(Scopes.SINGLETON);

		bind(EurekaRegistrar.class).asEagerSingleton();
	}
}
