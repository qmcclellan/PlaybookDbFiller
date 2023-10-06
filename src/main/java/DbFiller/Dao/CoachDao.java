package DbFiller.Dao;

import DbFiller.Entity.Coach;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CoachDao implements DaoUtility<Coach>{

    private Session session;

    private Transaction trans;


    @Override
    public List<Coach> findAll(SessionFactory factory) {

        session = factory.getCurrentSession();

        List<Coach> coachList = null;
        try{

            session.beginTransaction();

           coachList= session.createQuery("from Coach", Coach.class).getResultList();

        }catch(HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        return coachList;
    }

    @Override
    public void save(SessionFactory factory,Coach coach) {

        session = factory.getCurrentSession();

        try{
            trans = session.beginTransaction();

            session.persist(coach);

            trans.commit();

        }catch(HibernateException exc){
            trans.rollback();

            exc.printStackTrace();

            session.close();
        }

        session.close();
    }

    @Override
    public Coach retrieve(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        Coach coach = null;

        try{

            session.beginTransaction();

            coach = session.get(Coach.class,id);

        }catch(HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        session.close();

        return coach;
    }

    @Override
    public void update(SessionFactory factory,Coach coach) {

        session = factory.getCurrentSession();

        try{

            trans = session.beginTransaction();

            session.merge(coach);

            trans.commit();

        }catch(HibernateException exc){

            trans.rollback();

            exc.printStackTrace();

            session.close();

        }

        session.close();

    }

    @Override
    public void delete(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        Coach coach = null;

        try{

            trans = session.beginTransaction();

            coach = session.get(Coach.class, id);

            session.remove(coach);

            trans.commit();

        }catch(HibernateException exc){

            trans.rollback();

            exc.printStackTrace();

            session.close();

        }

        session.close();
    }
}
