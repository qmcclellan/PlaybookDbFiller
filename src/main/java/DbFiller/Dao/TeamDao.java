package DbFiller.Dao;

import DbFiller.Entity.Team;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TeamDao implements DaoUtility<Team> {

    private Session session;

    private Transaction trans;

    @Override
    public List<Team> findAll(SessionFactory factory) {

        session = factory.getCurrentSession();

        List<Team> teams = null;

        try{

           Transaction t =  session.beginTransaction();

            teams = session.createQuery("from Team", Team.class).getResultList();

            t.commit();
        }catch(HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        return teams;
    }
    @Override
    public void save(SessionFactory factory, Team team) {

        session = factory.getCurrentSession();


        try{

            trans = session.beginTransaction();

            session.persist(team);

            trans.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

    }

    public void merge(SessionFactory factory, Team team) {

        session = factory.getCurrentSession();


        try{

            trans = session.beginTransaction();

            session.merge(team);

            trans.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

    }


    public void saveAll(SessionFactory factory,List<Team> team) {

        session = factory.getCurrentSession();


        try{

            trans = session.beginTransaction();

            for ( int i=0; i<100000; i++ ) {

                System.out.println("Items in queue : " + i);

                session.persist(team);

                if( i % 10 == 0 ) { // Same as the JDBC batch size
                    //flush a batch of inserts and release memory:
                    session.flush();
                    session.clear();
                }
            }


            trans.commit();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }

        session.close();
    }

    @Override
    public Team retrieve(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        Team team = null;

        try{

            session.beginTransaction();

            team = session.get(Team.class,id);
        }catch (HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        session.close();

        return team;
    }

    @Override
    public void update(SessionFactory factory,Team team) {

        session = factory.getCurrentSession();

        try{

            trans = session.beginTransaction();

            session.merge(team);

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

        Team team = null;

        try{

            trans = session.beginTransaction();

            session.get(Team.class, id);

            session.remove(team);

            session.close();

        }catch(HibernateException exc){
            exc.printStackTrace();

            trans.rollback();

            session.close();
        }
            session.close();
    }
}
