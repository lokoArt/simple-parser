package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Log", indexes = {@Index(columnList = "ip,timestamp"), @Index(columnList = "ip"), @Index(columnList = "timestamp")})
public class Log {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column()
    private Date timestamp;

    @Column()
    private String ip;

    @Column()
    private String request;

    @Column()
    private Integer status;

    @Column()
    private String userAgent;

    public Log() {
    }

    public Log(Date timestamp, String ip, String request, Integer status, String userAgent) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
    }
}
