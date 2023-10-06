package DbFiller.Dao;

import org.hibernate.SessionFactory;

import java.util.List;

public interface DaoUtility <T>{

    public List<T> findAll(SessionFactory factory);
    public void save(SessionFactory factory,T t);
    public  T retrieve(SessionFactory factory,int id);
    public void update(SessionFactory factory,T t);
    public void delete(SessionFactory factory,int id);
}
