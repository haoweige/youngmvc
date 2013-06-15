package org.young.listener;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.young.util.StaticConstant;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * ContextLoaderListener is used to init young mvc
 * 
 * @author haoweige@126.com
 */
public class ContextLoaderListener implements ServletContextListener {

	private Logger logger = Logger.getLogger(ContextLoaderListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Young destroyed successfully!");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		setRoot(context);
		initYoung();
		initLog4j();
		initFreemarker(context);
		logger.info("Young initialized successfully!");
	}

	private void setRoot(ServletContext context) {
		String root = context.getRealPath("/");
		StaticConstant.WEB_ROOT = root.replaceAll("\\\\", "/");
	}

	private void initYoung() {
		String path = StaticConstant.WEB_ROOT + "/WEB-INF/young.properties";
		StaticConstant.TEMPLATE_ROOT = getConfig(path, "template.root");
		StaticConstant.BASE_PATH = getConfig(path, "base.path");
	}

	private void initLog4j() {
		String log4j = StaticConstant.WEB_ROOT + "/WEB-INF/log4j.properties";
		PropertyConfigurator.configure(log4j);
	}

	private void initFreemarker(ServletContext context) {
		Configuration config = new Configuration();
		config.setDefaultEncoding("UTF-8");
		config.setServletContextForTemplateLoading(context,
				StaticConstant.TEMPLATE_ROOT);
		TemplateExceptionHandler handler = TemplateExceptionHandler.HTML_DEBUG_HANDLER;
		config.setTemplateExceptionHandler(handler);
		config.setLocale(Locale.CHINA);
		context.setAttribute(StaticConstant.TEMPLATE_CONFIG, config);
	}

	private String getConfig(String path, String name) {
		try {
			CompositeConfiguration compConfig = new CompositeConfiguration();
			PropertiesConfiguration propConfig = new PropertiesConfiguration(
					path);
			compConfig.addConfiguration(propConfig);
			return compConfig.getString(name);
		} catch (ConfigurationException e) {
			logger.error(e);
		}
		return null;
	}

}
