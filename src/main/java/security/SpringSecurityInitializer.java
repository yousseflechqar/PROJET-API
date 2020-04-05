package security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;


/**
 * In Spring, security is implemented using DelegatingFilterProxy. 
 * To register it, with spring container in Java configuration, you shall use AbstractSecurityWebApplicationInitializer.
 * 
 * The spring will detect the instance of this class during application startup, 
 * and register the DelegatingFilterProxy to use the springSecurityFilterChain before any other registered Filter. 
 * It also register a ContextLoaderListener.
 * @author Yous
 *
 */

public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {}
