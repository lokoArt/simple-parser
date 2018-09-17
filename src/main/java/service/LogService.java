package service;

import dao.LogDao;
import model.Log;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

public class LogService {
    private static LogDao logDao;

    public LogService(SessionFactory sessionFactory) {
        logDao = new LogDao(sessionFactory);
    }

    public void persist(Log entity) {
        try {
            logDao.openCurrentSessionwithTransaction();
            logDao.persist(entity);
            logDao.commitTransaction();
        } finally {
            logDao.closeCurrentSession();
        }
    }

    public List<Object[]> getThrottlingIps(Date startDate, Duration duration, long threshold) {
        try {
            long secondsToAdd = duration == Duration.hourly ? 60 * 60 * 1000 : 60 * 60 * 24 * 1000;
            Date endDate = new Date(startDate.getTime() + secondsToAdd);
            logDao.openCurrentSessionwithTransaction();
            List<Object[]> ips = logDao.getThrottlingIps(startDate, endDate, threshold);
            return ips;
        } finally {
            logDao.closeCurrentSession();
        }

    }
}
