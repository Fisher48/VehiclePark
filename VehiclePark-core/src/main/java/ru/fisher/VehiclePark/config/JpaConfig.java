package ru.fisher.VehiclePark.config;//package ru.fisher.VehiclePark.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.Database;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableJpaRepositories(
//        basePackages = "ru.fisher.VehiclePark.repositories.jpa",
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef = "transactionManager"
//)
//@EnableTransactionManagement
//public class JpaConfig {
//
//    @Autowired
//    private Environment env;
//
//    // 1. Определяем DataSource
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .url(env.getProperty("spring.datasource.url"))
//                .username(env.getProperty("spring.datasource.username"))
//                .password(env.getProperty("spring.datasource.password"))
//                .driverClassName(env.getProperty("spring.datasource.driver-class-name"))
//                .build();
//    }
//
////    // 2. Настраиваем EntityManagerFactory
////    @Bean
////    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
////        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
////        em.setDataSource(dataSource());
////        em.setPackagesToScan("ru.fisher.VehiclePark.entity"); // Укажите ваш пакет с entity
////
////        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
////        em.setJpaVendorAdapter(vendorAdapter);
////
////        // Дополнительные свойства Hibernate
////        Properties properties = new Properties();
////        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
////        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
////        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
////        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
////
////        em.setJpaProperties(properties);
////
////        return em;
////    }
//
//    // 3. Настраиваем TransactionManager
//    @Bean
//    @Primary
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(emf);
//        return transactionManager;
//    }
//
//
//}
