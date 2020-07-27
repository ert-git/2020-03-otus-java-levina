package ru.otus.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface DatabaseSession {

    void close();

    Transaction getTransaction();

    Session getHibernateSession();

}
