package org.xbmlz.next;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.xbmlz.next.common.config.ApplicationProperties;

/**
 * @author chenxc
 * @date 2023/04/21
 * @since 1.0.0
 */
@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class SpringBootNextApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootNextApplication.class, args);
    }
}
