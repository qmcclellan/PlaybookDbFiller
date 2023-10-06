package DbFiller.Dao;

import DbFiller.Entity.Formation;
import DbFiller.Entity.Scheme;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SchemeDao implements DaoUtility<Scheme>{

    private Session session;

    private Transaction trans;


    @Override
    public List<Scheme> findAll(SessionFactory factory) {

        session = factory.getCurrentSession();

        List<Scheme> schemes = null;

        try{

            session.beginTransaction();

            schemes = session.createQuery("from Scheme ", Scheme.class).getResultList();

        }catch(HibernateException exc){
            exc.printStackTrace();

            session.close();

        }

        session.close();

        return schemes;
    }

    public List<Scheme> findAllByFormation(SessionFactory factory, Formation formation) {

        session = factory.getCurrentSession();

        List<Scheme> schemes = null;

        try {

            Transaction t = session.beginTransaction();

            Query query= session.createQuery("from Scheme where formation = :formation", Scheme.class);

            query.setParameter("formation",formation);

            schemes = query.getResultList();

            t.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            session.close();
        }
        session.close();

        return schemes;
    }

    @Override
    public void save(SessionFactory factory, Scheme scheme) {

        session = factory.getCurrentSession();

        try{

            trans = session.beginTransaction();

            session.persist(scheme);

            trans.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

        session.close();
    }

    public Scheme saveFlush(SessionFactory factory, Scheme scheme) {

        session = factory.getCurrentSession();

        try{

            trans = session.beginTransaction();

            session.persist(scheme);

            trans.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

        session.close();

        return  scheme;
    }

    @Override
    public Scheme retrieve(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        Scheme scheme = null;

        try{

            session.beginTransaction();

        scheme = session.get(Scheme.class, id);

        session.close();

        }catch(HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        return scheme;
    }

    @Override
    public void update(SessionFactory factory,Scheme scheme) {

        session = factory.getCurrentSession();

        try{

           trans = session.beginTransaction();

            session.merge(scheme);

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

        Scheme scheme = null;

        try{
            trans = session.beginTransaction();

            scheme = session.get(Scheme.class,id);

            session.remove(scheme);

            trans.commit();
        }catch(HibernateException exc){

            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

        session.close();
    }

    public void saveAndFlush(SessionFactory factory, Scheme scheme) {
    }
}
