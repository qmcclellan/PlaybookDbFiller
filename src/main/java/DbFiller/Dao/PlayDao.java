package DbFiller.Dao;

import DbFiller.Entity.Play;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PlayDao implements DaoUtility<Play>{

    private Session session;

    private Transaction trans;


    @Override
    public List<Play> findAll(SessionFactory factory) {

        session = factory.getCurrentSession();

        List<Play> plays = null;

        try{

            session.beginTransaction();

            plays = session.createQuery("from Play", Play.class).getResultList();

        }catch(HibernateException exc){
            exc.printStackTrace();

            session.close();
        }

        session.close();

        return plays;
    }

    @Override
    public void save(SessionFactory factory,Play play) {

        session = factory.getCurrentSession();

        try{
            trans = session.beginTransaction();

            session.persist(play);

            trans.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

        session.close();

    }

    @Override
    public Play retrieve(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        Play play = null;

        try{
            session.beginTransaction();

            play = session.get(Play.class,id);
        }catch(HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        session.close();

        return play;
    }

    @Override
    public void update(SessionFactory factory,Play play) {

        session = factory.getCurrentSession();

        try{
            trans = session.beginTransaction();

            session.merge(play);

            trans.commit();
        }catch(HibernateException exc){

            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

        session.close();
    }

    @Override
    public void delete(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        Play play = null;

        try {

            trans = session.beginTransaction();

            play = session.get(Play.class,id);

            session.remove(play);

            trans.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

        session.close();
    }
}
