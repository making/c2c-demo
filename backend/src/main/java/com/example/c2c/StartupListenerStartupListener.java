package com.example.c2c;

import static com.netflix.appinfo.InstanceInfo.InstanceStatus.DOWN;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.netflix.appinfo.ApplicationInfoManager;

import am.ik.eureka.EurekaModule;

@WebListener
public class StartupListenerStartupListener extends GuiceServletContextListener {
	Injector injector = Guice.createInjector(new EurekaModule(), new ServletModule() {
		@Override
		protected void configureServlets() {
			serve("/").with(BackendServlet.class);
		}
	});

	@Override
	protected Injector getInjector() {
		return injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		//
		ServletContext ctx = servletContextEvent.getServletContext();
		FilterRegistration fr = ctx.addFilter("Guice Filter", GuiceFilter.class);
		fr.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		injector.getInstance(ApplicationInfoManager.class).setInstanceStatus(DOWN);
		super.contextDestroyed(servletContextEvent);
	}
}
