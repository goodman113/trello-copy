package my.project.trellocopy.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.prop.AppProp;
import my.project.trellocopy.exception.RestException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final MinioClient minioClient;
    private final AppProp prop;

    @PostConstruct
    public void init() {

        try {
            String policy = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": \"*\",\n" +
                    "      \"Action\": [\"s3:GetObject\"],\n" +
                    "      \"Resource\": [\"arn:aws:s3:::" + prop.getAwsS3().getBucketName() + "/*\"]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(prop.getAwsS3().getBucketName())
                            .config(policy)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error while setting up Minio client", e);
        }
    }


    public String uploadFile(MultipartFile multipart) {
        try {
            String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipart.getOriginalFilename());
            String mimeType = detectMimeType(multipart.getBytes());

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            File file = new File(System.getProperty("java.io.tmpdir"), fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipart.getBytes());
            }

            try (InputStream imageStream = new FileInputStream(file)) {
                PutObjectArgs data = PutObjectArgs.builder()
                        .bucket(prop.getAwsS3().getBucketName())
                        .object(fileName)
                        .stream(imageStream, file.length(), -1)
                        .contentType(mimeType)
                        .build();
                minioClient.putObject(data);

                return "https://" + prop.getAwsS3().getEndpoint() + "/" + prop.getAwsS3().getBucketName() + "/" + fileName;
            }
        } catch (Exception e) {
            log.error("Error while uploading file: {}", e.getMessage());
            throw RestException.restThrow(ErrorType.ERROR_SAVING_FILE);
        }
    }


    private String detectMimeType(byte[] bytes) {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            log.error("Could not detect mime type: {}", e.getMessage());
            throw RestException.restThrow(ErrorType.FILE_TYPE_ERROR);
        }
    }


    public void deleteFiles(List<String> images) {
        for (String filePath : images) {
            try {
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(prop.getAwsS3().getBucketName())
                                .object(fileName)
                                .build()
                );
            } catch (Exception e) {
                log.error("Error while deleting file: {}", e.getMessage());
            }
        }
    }

    public InputStream getFileStream(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(prop.getAwsS3().getBucketName())
                .object(fileName)
                .build();
        return minioClient.getObject(args);
    }

    @SneakyThrows
    public String getContentType(String fileName) {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                .bucket(prop.getAwsS3().getBucketName())
                .object(fileName)
                .build();
        StatObjectResponse stat = minioClient.statObject(statObjectArgs);
        String contentType = stat.contentType();
        return contentType != null ? contentType : "application/octet-stream";
    }

    public String getPresignedUrl(String objectName, int expiryInMinutes) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(prop.getAwsS3().getBucketName())
                .object(objectName)
                .expiry(expiryInMinutes * 60)  // seconds
                .build();
        return minioClient.getPresignedObjectUrl(args);
    }
}