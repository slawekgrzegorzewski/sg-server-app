package pl.sg.ip.service.attachments.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class S3StorageTestUtil implements StorageTestUtil {

    public static String NOT_INITIALIZED = "SET_TO_REAL_VALUE_WHEN_TESTING_S3";

    private final int intellectualPropertyId;
    private final int taskId;
    private final String bucketName;
    private final AmazonS3 s3Client;

    public S3StorageTestUtil(int intellectualPropertyId, int taskId, String awsAccessKeyId, String awsSecretAccessKey, String awsRegion, String bucketName) {
        this.bucketName = bucketName;
        if (List.of(awsAccessKeyId, awsSecretAccessKey, awsRegion).contains(NOT_INITIALIZED)) {
            throw new RuntimeException("You need to provide correct values");
        }

        this.intellectualPropertyId = intellectualPropertyId;
        this.taskId = taskId;
        s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey))).withRegion(awsRegion).build();
    }

    @Override
    public boolean checkFileExistenceInStorage(String fileName) {
        return s3Client.doesObjectExist(bucketName, getFullFileName(fileName));
    }

    @Override
    public void clearStorage() {
        if (s3Client.doesBucketExistV2(bucketName)) {
            ObjectListing objectListing = s3Client.listObjects(bucketName);
            objectListing.getObjectSummaries().forEach(s3ObjectSummary -> s3Client.deleteObject(bucketName, s3ObjectSummary.getKey()));
            s3Client.deleteBucket(bucketName);
        }
    }

    @Override
    public void initStorage() {
        s3Client.createBucket(bucketName);
    }

    @Override
    public String readFileContentFromStorage(String fileName) throws IOException {
        try (S3ObjectInputStream objectContent = s3Client.getObject(bucketName, getFullFileName(fileName)).getObjectContent()) {
            return new String(objectContent.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Override
    public void putResourceInStorage(String resource, String withName) {
        s3Client.putObject(bucketName, getFullFileName(withName), S3StorageTestUtil.class.getResourceAsStream(resource), new ObjectMetadata());
    }

    private String getPrefix() {
        return String.format("%s/%s/", intellectualPropertyId, taskId);
    }

    @NotNull
    private String getFullFileName(String fileName) {
        return getPrefix() + fileName;
    }
}
