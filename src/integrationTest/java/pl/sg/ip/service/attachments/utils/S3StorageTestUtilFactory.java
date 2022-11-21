package pl.sg.ip.service.attachments.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.sg.application.configuration.Configuration;

@Component
@Profile("S3Storage")
public class S3StorageTestUtilFactory implements StorageTestUtilFactory {

    private final String bucketName;
    private final Configuration configuration;

    public S3StorageTestUtilFactory(
            @Value("${aws.intellectual-property-task.attachments.bucket-name}") String bucketName,
            Configuration configuration) {
        this.bucketName = bucketName;
        this.configuration = configuration;
    }

    public StorageTestUtil create(int intellectualPropertyId, int taskId) {
        return new S3StorageTestUtil(
                intellectualPropertyId, taskId,
                configuration.getAWSAccessKeyId(),
                configuration.getAWSSecretAccessKey(),
                configuration.getAWSRegion(),
                bucketName
        );
    }
}
