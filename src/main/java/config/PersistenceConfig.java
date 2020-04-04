package config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dialect.CustomMySQLDialect;

@Configuration
@EnableTransactionManagement // for @Transactional
@PropertySource("classpath:database.properties")
@ComponentScan({"dao", "services"}) 
public class PersistenceConfig {
	
	
    private @Autowired Environment env; // env.getProperty("...")
	
	
    /**
     * JpaTransactionManager implements PlatformTransactionManager
     */
	@Bean
	public JpaTransactionManager getJpaTransactionManager() {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(getEntityManagerFactoryBean().getObject());
		return tm;
	}

	
	@Bean
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
		emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		emFactory.setDataSource(getDataSource());
		emFactory.setPersistenceUnitName("YOUS-JPA");
		emFactory.setPackagesToScan("entities");
		emFactory.setJpaProperties(jpaProperties());
		return emFactory;
	}
	
    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", CustomMySQLDialect.class);
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.generate_statistics", true);
        return properties;        
    }
    
	
    @Bean
	public DataSource getDataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
	    dataSource.setUrl(env.getProperty("jdbc.url"));
	    dataSource.setUsername(env.getProperty("jdbc.username"));
	    dataSource.setPassword(env.getProperty("jdbc.password"));
	    return dataSource;
	}

}