package net.codejava.notification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailNotificationScheduler {

    private final JavaMailSender javaMailSender;
    private final Map<String, ServerStatus> serverStatusMap;
    private final DateTimeFormatter dateTimeFormatter;
    Map<String, Integer> downCountMap = new HashMap<>();

    @Autowired
    public EmailNotificationScheduler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.serverStatusMap = new HashMap<>();
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void sendEmailNotification() {
        String recipient = "decisionservernotify@gmail.com"; // Replace with the recipient email address
        String subject = "Server Downtime Notification";

        // Check the server status and update downtime count
        Map<String, ServerStatus> downServers = getDownServers();

        // Send email only if there are down servers
        if (!downServers.isEmpty()) {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(recipient);
                helper.setSubject(subject);

                // Create the HTML content for the email
                StringBuilder emailContent = new StringBuilder();
                emailContent.append("<h2>Server Downtime Notification</h2>");
                emailContent.append("<table border='1' cellpadding='10'>");
                emailContent.append("<tr><th>Server Name</th><th>Last Checked Time</th><th>Last Up Time</th><th>Downtime Count</th></tr>");
                for (ServerStatus serverStatus : downServers.values()) {
                    emailContent.append("<tr>");
                    emailContent.append("<td>").append(serverStatus.getName()).append("</td>");
                    emailContent.append("<td>").append(formatDateTime(serverStatus.getLastCheckedTime())).append("</td>");
                    emailContent.append("<td>").append(serverStatus.getLastUpTime() != null ? formatDateTime(serverStatus.getLastUpTime()) : "Null").append("</td>");
                    emailContent.append("<td>").append(downCountMap.get(serverStatus.getName())).append("</td>");
                    emailContent.append("</tr>");
                }
                emailContent.append("</table>");

                // Set the HTML content as the email body
                helper.setText(emailContent.toString(), true);

                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, ServerStatus> getDownServers() {
        Map<String, ServerStatus> downServers = new HashMap<>();
        


        try (BufferedReader br = new BufferedReader(new FileReader("servers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String url = parts[1].trim();
                try {
                    URL serverUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
                    if (conn instanceof HttpsURLConnection) {
                        // Disable SSL certificate validation
                        ((HttpsURLConnection) conn).setSSLSocketFactory(createTrustAllSSLSocketFactory());
                        ((HttpsURLConnection) conn).setHostnameVerifier((hostname, session) -> true);
                    }

                    conn.setRequestMethod("HEAD");
                    int responseCode = conn.getResponseCode();
                    boolean isUp = (200 <= responseCode && responseCode <= 399);

                    if (!isUp) {
                        LocalDateTime lastCheckedTime = LocalDateTime.now();
                        LocalDateTime lastUpTime = null; // Initialize as null

                        // Check if the server was previously down and get the previous downtime count
                        ServerStatus serverStatus = serverStatusMap.get(name);
                        if (serverStatus != null && serverStatus.getLastUpTime() != null) {
                            // Server was previously up and is now down
                            lastUpTime = serverStatus.getLastCheckedTime();
                            serverStatus.incrementDowntimeCount();
                        } else {
                            // Server is newly down
                            ServerStatus newServerStatus = new ServerStatus(name, lastCheckedTime, lastUpTime);
                            newServerStatus.incrementDowntimeCount();
                            incrementValue(downCountMap, name);
                            downServers.put(name, newServerStatus);
                        }

                        // Update the server status map
                        serverStatusMap.put(name, new ServerStatus(name, lastCheckedTime, lastUpTime));
                    } else {
                        // Server is up, remove it from the status map
                        serverStatusMap.remove(name);
                        decrementValue(downCountMap,name);
                    }
                } catch (MalformedURLException e) {
                    // Handle MalformedURLException
                    e.printStackTrace();
                } catch (IOException e) {
                    // Handle IOException
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }

        return downServers;
    }

    private SSLSocketFactory createTrustAllSSLSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    private static class ServerStatus {
        private final String name;
        private final LocalDateTime lastCheckedTime;
        private final LocalDateTime lastUpTime;
        private int downtimeCount;

        public ServerStatus(String name, LocalDateTime lastCheckedTime, LocalDateTime lastUpTime) {
            this.name = name;
            this.lastCheckedTime = lastCheckedTime;
            this.lastUpTime = lastUpTime;
            this.downtimeCount = 0;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getLastCheckedTime() {
            return lastCheckedTime;
        }

        public LocalDateTime getLastUpTime() {
            return lastUpTime;
        }

        public int getDowntimeCount() {
            return downtimeCount;
        }

        public void incrementDowntimeCount() {
            downtimeCount++;
        }
    }
    
    public static void incrementValue(Map<String, Integer> map, String key) {
        // Retrieve the current value
        int value = map.getOrDefault(key, 0);
        
        // Increment the value
        value++;
        
        // Put the updated value back into the map
        map.put(key, value);
    }
    
    public static void decrementValue(Map<String, Integer> map, String key) {
        // Retrieve the current value
        int value = map.getOrDefault(key, 0);

        // Check if the value is greater than 0 before decrementing
        if (value > 0) {
            // Decrement the value
            value--;

            // Put the updated value back into the map
            map.put(key, value);
        }
    }

}
