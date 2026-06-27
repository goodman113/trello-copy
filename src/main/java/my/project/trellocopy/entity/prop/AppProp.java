package my.project.trellocopy.entity.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class AppProp {
    private AwsS3 awsS3;

    @Getter @Setter
    public static class AwsS3 {
        private String bucketName;
        private String endpoint;
        private String accessKey;
        private String secretKey;
    }
}