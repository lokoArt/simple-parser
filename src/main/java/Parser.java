import model.Log;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import picocli.CommandLine;
import service.BlockedIpService;
import service.Duration;
import service.LogService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@CommandLine.Command(name = "parser", mixinStandardHelpOptions = true, version = "Parser 0.1")
public class Parser implements Runnable {
    @CommandLine.Option(names = {"-a", "--accessLog"}, description = "Log file to process.")
    private String accessLog;

    // Picocli doesn's support formatting, so accept as string and format it
    @CommandLine.Option(names = {"-s", "--startDate"}, required = true, description = "Start date")
    private String startDateStr;
    private Date startDate;

    @CommandLine.Option(names = {"-d", "--duration"}, required = true, description = "Enum values: ${COMPLETION-CANDIDATES}")
    private Duration duration;

    @CommandLine.Option(names = {"-t", "--threshold"}, required = true, description = "Maximum number of requests per IP")
    private int threshold = 100;

    private LogService logService;
    private BlockedIpService blockedIpService;

    private void formatCommandLineArguments() {
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse(startDateStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void initHibernate() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate_cfg.xml").build();

        try {
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            logService = new LogService(sessionFactory);
            blockedIpService = new BlockedIpService(sessionFactory);
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void destroyHibernate() {

    }

    private void processLogLine(final String line) throws ParseException {
        String[] logParams = line.split("\\|");

        Date timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logParams[0]);

        Log log = new Log(timestamp, logParams[1], logParams[2], Integer.parseInt(logParams[3]), logParams[4]);

        logService.persist(log);
    }

    private void parseLogFile() {
        try (Stream<String> stream = Files.lines(Paths.get(accessLog))) {

            stream.forEach(line -> {
                try {
                    processLogLine(line);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void blockIps() {
        List<Object[]> ips = logService.getThrottlingIps(startDate, duration, threshold);
        blockedIpService.blockIps(ips);

        ips.forEach((v) -> System.out.println(v[0]));
    }

    public void run() {
        formatCommandLineArguments();

        initHibernate();
        if (accessLog != null) {
            parseLogFile();
        }

        blockIps();

        System.exit(0);
    }

    public static void main(String[] args) {
        CommandLine.run(new Parser(), System.out, args);
    }
}
