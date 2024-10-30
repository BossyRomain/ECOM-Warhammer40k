package com.warhammer.ecom.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EntityScan(basePackages = { "com.warhammer.ecom.model" })
@ComponentScan(basePackages = { "com.warhammer.ecom.repository", "com.warhammer.ecom.service", "com.warhammer.ecom.controller" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.warhammer.ecom.repository" })
@EnableWebMvc
@EnableSpringDataWebSupport
public class AppConfig {
}
