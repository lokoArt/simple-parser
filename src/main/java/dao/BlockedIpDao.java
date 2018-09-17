package dao;

import model.BlockedIp;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BlockedIpDao extends BaseDao {
    public BlockedIpDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void persist(BlockedIp blockedIp) {
        getCurrentSession().save(blockedIp);
    }

    public BlockedIp findByIp(String ip) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<BlockedIp> root = criteriaQuery.from(BlockedIp.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("ip"), ip));
        Query<BlockedIp> query = getCurrentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }
}
