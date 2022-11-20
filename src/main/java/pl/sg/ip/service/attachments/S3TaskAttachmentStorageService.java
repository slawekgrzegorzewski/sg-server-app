package pl.sg.ip.service.attachments;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.sg.application.configuration.Configuration;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static java.util.Optional.empty;

@Component
@Profile("https")
public class S3TaskAttachmentStorageService implements TaskAttachmentStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(S3TaskAttachmentStorageService.class);

    private final Configuration configuration;

    @Value("${aws.intellectual-property-task.attachments.bucket-name}")
    private String bucketName;

    public S3TaskAttachmentStorageService(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Collection<String> listFiles(int intellectualPropertyId, int taskId) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try {
            return createClient().listObjects(bucketName, prefix)
                    .getObjectSummaries()
                    .stream()
                    .map(S3ObjectSummary::getKey)
                    .collect(Collectors.toList());
        } catch (AmazonServiceException e) {
            LOG.warn("Unable to list files under directory " + prefix + " due to an exception.", e);
            return List.of();
        }
    }

    @Override
    public Optional<InputStream> getFile(int intellectualPropertyId, int taskId, String fileName) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try {
            S3Object s3Object = createClient().getObject(bucketName, prefix + fileName);
            return of(s3Object.getObjectContent());
        } catch (AmazonServiceException e) {
            LOG.warn("Unable to get file " + fileName + " from directory " + prefix + " due to an exception.", e);
            return empty();
        }
    }

    @Override
    public boolean putFile(int intellectualPropertyId, int taskId, String fileName, InputStream fileStream) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try {
            createClient().putObject(bucketName, prefix + fileName, fileStream, new ObjectMetadata());
            return true;
        } catch (AmazonServiceException e) {
            LOG.warn("Unable to put file " + fileName + " under directory " + prefix + " due to an exception.", e);
            return false;
        }
    }

    @Override
    public boolean deleteFile(int intellectualPropertyId, int taskId, String fileName) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try {
            createClient().deleteObject(bucketName, prefix + fileName);
            return true;
        } catch (AmazonServiceException e) {
            LOG.warn("Unable to delete file " + fileName + " from directory " + prefix + " due to an exception.", e);
            return false;
        }
    }

    private AmazonS3 createClient() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(configuration.getAWSAccessKeyId(), configuration.getAWSSecretAccessKey())))
                .withRegion(configuration.getAWSRegion())
                .build();
    }

    private String buildPrefix(int intellectualPropertyId, int taskId) {
        return String.format("%s/%s/", intellectualPropertyId, taskId);
    }
}
