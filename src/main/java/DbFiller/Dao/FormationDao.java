package DbFiller.Dao;

import DbFiller.Entity.Formation;
import DbFiller.Entity.PlayBook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class FormationDao implements DaoUtility<Formation> {

    private Session session;

    private Transaction trans;


    @Override
    public List<Formation> findAll(SessionFactory factory) {

        session = factory.getCurrentSession();

        List<Formation> formations = null;

        try {


            formations = session.createQuery("from Formation ", Formation.class).getResultList();

        }catch(HibernateException exc){
            exc.printStackTrace();

            session.close();
        }
        session.close();

        return formations;
    }

    public List<Formation> findAllByPlaybook(SessionFactory factory, PlayBook playbook) {

        session = factory.getCurrentSession();

        List<Formation> formations = null;

        try {

           Transaction t = session.beginTransaction();


            Query query= session.createQuery("from Formation f where f.playBook = :playbook", Formation.class);

            query.setParameter("playbook",playbook);

            formations = query.getResultList();

            t.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            session.close();
        }
        session.close();

        return formations;
    }

    @Override
    public void save(SessionFactory factory,Formation formation) {

        session = factory.getCurrentSession();

        try{

            trans = session.beginTransaction();

            session.persist(formation);

            trans.commit();

        }catch(HibernateException exc){

            trans.rollback();

            exc.printStackTrace();

            session.close();
        }
            session.close();
    }

    @Override
    public Formation retrieve(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        Formation formation = null;

        try{
            session.beginTransaction();

            formation = session.get(Formation.class,id);
        }catch(HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        session.close();

        return formation;
    }

    @Override
    public void update(SessionFactory factory,Formation formation) {

             session = factory.getCurrentSession();

        try{
            trans = session.beginTransaction();

            session.merge(formation);

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

        Formation formation = null;

        try{
            trans = session.beginTransaction();

            formation = session.get(Formation.class, id);

            session.remove(formation);

            trans.commit();
        }catch(HibernateException exc){

            exc.printStackTrace();

            trans.rollback();

            session.close();
        }
            session.close();
    }
}
