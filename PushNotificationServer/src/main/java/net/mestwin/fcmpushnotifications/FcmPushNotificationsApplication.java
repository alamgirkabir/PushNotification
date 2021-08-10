package net.mestwin.fcmpushnotifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("net.mestwin.fcmpushnotifications")
public class FcmPushNotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FcmPushNotificationsApplication.class, args);
    }

}
