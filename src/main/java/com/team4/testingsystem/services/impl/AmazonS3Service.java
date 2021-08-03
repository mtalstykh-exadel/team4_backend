package com.team4.testingsystem.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.services.FilesService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Profile("release")
@Service
public class AmazonS3Service implements FilesService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.s3.bucket-name}")
    private String bucketName;

    @Autowired
    public AmazonS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public void save(String fileName, Resource file) throws FileSavingFailedException {
        Path tempFilePath;
        try {
            tempFilePath = createTempFile(fileName);
        } catch (IOException e) {
            throw new FileSavingFailedException();
        }

        File tempFile = new File(tempFilePath.toString());

        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            amazonS3.putObject(bucketName, fileName, tempFile);
        } catch (IOException | AmazonServiceException e) {
            throw new FileSavingFailedException();
        } finally {
            tempFile.deleteOnExit();
        }
    }

    @Override
    public Resource load(String fileName) throws FileLoadingFailedException {
        try {
            S3Object data = amazonS3.getObject(bucketName, fileName);
            S3ObjectInputStream objectContent = data.getObjectContent();

            return new ByteArrayResource(IOUtils.toByteArray(objectContent));
        } catch (IOException | AmazonServiceException e) {
            throw new FileLoadingFailedException();
        }
    }

    @Override
    public void delete(String fileName) throws FileDeletingFailedException {
        try {
            amazonS3.deleteObject(bucketName, fileName);
        } catch (AmazonServiceException e) {
            throw new FileDeletingFailedException();
        }
    }

    public Path createTempFile(String fileName) throws IOException {
        return Files.createTempFile(null, fileName);
    }
}
