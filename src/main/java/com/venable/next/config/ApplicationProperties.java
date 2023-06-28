package com.venable.next.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author chenxc
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Security security = new Security();

    private final CorsConfiguration cors = new CorsConfiguration();

    private final Cache cache = new Cache();

    private final Logging logging = new Logging();

    @Data
    public static class Logging {

        private final boolean useJsonFormat = false;

        private final Logstash logstash = new Logstash();

        @Data
        public static class Logstash {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 5000;

            private int ringBufferSize = 128;
        }
    }

    @Data
    public static class Cache {

        private final Caffeine caffeine = new Caffeine();

        @Data
        public static class Caffeine {

            private int timeToLiveSeconds = 3600;

            private long maxEntries = 100;
        }
    }


    @Data
    public static class Security {

        private String contentSecurityPolicy = "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";

        private String permissionsPolicy = "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()";

        private final Authentication authentication = new Authentication();

        @Data
        public static class Authentication {

            private String[] whitelist = new String[0];

            private final Jwt jwt = new Jwt();


            @Data
            public static class Jwt {

                private String secret = null;

                private String base64Secret = null;

                /**
                 * Token is valid for 30 minutes
                 */
                private long tokenValidityInSeconds = 1800;

                /**
                 * Token is valid for 30 days
                 */
                private long tokenValidityInSecondsForRememberMe = 2592000;
            }
        }
    }
}
