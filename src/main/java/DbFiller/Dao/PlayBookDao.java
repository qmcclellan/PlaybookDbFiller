package DbFiller.Dao;

import DbFiller.Entity.PlayBook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PlayBookDao implements DaoUtility<PlayBook> {

    private Session session;

    private Transaction trans;

    @Override
    public List<PlayBook> findAll(SessionFactory factory) {

        session = factory.getCurrentSession();

        List<PlayBook> playBooks = null;

        try{

            session.beginTransaction();

            playBooks = session.createQuery("from PlayBook ", PlayBook.class).getResultList();


        }catch(HibernateException exc){
            exc.printStackTrace();
            session.close();
        }

        session.close();

        return playBooks;
    }


    public List<PlayBook> findAllByPlayType(SessionFactory factory, String playType) {

        session = factory.getCurrentSession();

        List<PlayBook> playBooks = null;

        try{

            session.beginTransaction();

           Query query = session.createQuery("from PlayBook p where p.type =:playType", PlayBook.class);

           query.setParameter("playType", playType);

           playBooks = query.getResultList();
        }catch(HibernateException exc){
            exc.printStackTrace();
            session.close();
        }

        session.close();

        return playBooks;
    }


    @Override
    public void save(SessionFactory factory,PlayBook playBook) {

        session = factory.getCurrentSession();

        try{

            trans = session.beginTransaction();

            session.persist(playBook);

            trans.commit();
        }catch(HibernateException exc){

            exc.printStackTrace();

            trans.rollback();

            session.close();
        }
        session.close();
    }

    @Override
    public PlayBook retrieve(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

        PlayBook playBook = null;

        try{

            session.beginTransaction();

            playBook = session.get(PlayBook.class, id);

        }catch(HibernateException exc){

            exc.printStackTrace();

            session.close();
        }

        session.close();

        return playBook;
    }

    @Override
    public void update(SessionFactory factory,PlayBook playBook) {

        session = factory.getCurrentSession();

        try {

            trans = session.beginTransaction();

            session.merge(playBook);

            trans.commit();

        } catch (HibernateException exc) {

            exc.printStackTrace();

            session.close();
        }
    }
    @Override
    public void delete(SessionFactory factory,int id) {

        session = factory.getCurrentSession();

            PlayBook playBook = null;
            try{

                trans = session.beginTransaction();

                playBook = session.get(PlayBook.class, id);

                session.remove(playBook);

                trans.commit();
            }catch(HibernateException exc){
                exc.printStackTrace();

                trans.rollback();

                session.close();
            }

            session.close();
        }

    }

