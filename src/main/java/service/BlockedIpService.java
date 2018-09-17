package service;

import dao.BlockedIpDao;
import model.BlockedIp;
import org.hibernate.SessionFactory;

import java.util.List;

public class BlockedIpService {
    private static BlockedIpDao blockedIpDao;

    public BlockedIpService(SessionFactory sessionFactory) {
        blockedIpDao = new BlockedIpDao(sessionFactory);
    }

    public void blockIps(List<Object[]> ips) {
        try {
            blockedIpDao.openCurrentSessionwithTransaction();

            for (Object[] ip : ips) {
                if (blockedIpDao.findByIp((String) ip[0]) == null) {
                    String reason = String.format("Throttle reached %d", ip[1]);
                    BlockedIp blockedIp = new BlockedIp((String) ip[0], reason);
                    blockedIpDao.persist(blockedIp);
                }
            }
            blockedIpDao.commitTransaction();
        } finally {
            blockedIpDao.closeCurrentSession();
        }
    }
}
