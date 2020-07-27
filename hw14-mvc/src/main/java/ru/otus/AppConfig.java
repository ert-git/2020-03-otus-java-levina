package ru.otus;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.hibernate.HibernateUtils;
import ru.otus.model.AddressDataSet;
import ru.otus.model.PhoneDataSet;
import ru.otus.model.User;

@Configuration
public class AppConfig {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory(
                HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, PhoneDataSet.class, AddressDataSet.class);
    }
}
