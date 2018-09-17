package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "blocked_ip", indexes = {@Index(columnList = "ip")})
public class BlockedIp {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column()
    private Date created = new Date();

    @Column()
    private String ip;

    @Column()
    private String reason;

    public BlockedIp() {
    }

    public BlockedIp(String ip, String reason) {
        this.ip = ip;
        this.reason = reason;
    }
}
