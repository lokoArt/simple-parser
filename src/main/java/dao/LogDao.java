package dao;

import model.Log;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class LogDao extends BaseDao {
    public LogDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void persist(Log entity) {
        getCurrentSession().save(entity);
    }

    public List<Object[]> getThrottlingIps(Date start, Date end, long threshold) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<Log> logRoot = criteriaQuery.from(Log.class);

        criteriaQuery.select(logRoot).where(criteriaBuilder.between(logRoot.get("timestamp"), start, end));

        Expression<String> groupByIpExp  = logRoot.get("ip").as(String.class);
        Expression<Long> countExp = criteriaBuilder.count(groupByIpExp);

        CriteriaQuery<Log> select = criteriaQuery.multiselect(groupByIpExp, countExp);
        criteriaQuery.groupBy(groupByIpExp);
        criteriaQuery.having(criteriaBuilder.gt(criteriaBuilder.count(logRoot), threshold));
        criteriaQuery.orderBy(criteriaBuilder.desc(countExp));

        Query query = getCurrentSession().createQuery(select);
        return query.getResultList();
    }
}
