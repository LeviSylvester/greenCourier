package com.sda.sylvester.greenCourier.dao;

import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ConsignmentDao {

    public Consignment get(int idConsignment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Consignment c = session.get(Consignment.class, idConsignment);
        session.close();

        return c;
    }

    public Consignment save(Consignment consignment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(consignment);
        transaction.commit();
        session.close();

        return consignment;
    }

    public Consignment delete(Consignment consignment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(consignment);
        transaction.commit();
        session.close();

        return consignment;
    }

    public List<Consignment> getAllConsignments(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from Consignment"); // e.g. "where status == Status.AWAITING_RESPONSE"
        List<Consignment> consignmentList = query.list();

        session.close();
        return consignmentList;
    }

    public void deleteAllConsignments(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("delete from Consignment");
        query.executeUpdate();
        transaction.commit();

        session.close();
    }
}