package config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import security.WebSecurityConfig;




/**
 * 
 * SpringServletContainerInitializer which implements ServletContainerInitializer is bootstrapped automatically by any Servlet 3.0 container.
 * 
 * Servlet 3.0 ServletContainerInitializer designed to support code-based configuration of the servlet container using Spring's WebApplicationInitializer SPI 
 * as opposed to (or possibly in combination with) the traditional web.xml-based approach.
 * 
 * SpringServletContainerInitializer has to find the right class implementing WebApplicationInitializer
 * 
 * AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer
 * 
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
    	return new Class<?>[] { PersistenceConfig.class, WebSecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}


