package com.fav.daengnyang.global.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fav.daengnyang.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fav.daengnyang.global.exception.ErrorCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RequiredArgsConstructor
@Service
@Slf4j
public class AwsService {
    @Value("${aws.s3.bucket}")
    private String name;

    private final AmazonS3 s3Client;

    public String uploadFile(MultipartFile file, Long memberId) {
        try{
            File fileObj = convertMultiPartFileToFile(file);
            String originalFilename = file.getOriginalFilename();

            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }

            //memberId로 랜덤
            String uniqueFileName = generateFileName(String.valueOf(memberId), extension);

            s3Client.putObject(new PutObjectRequest(name, uniqueFileName, fileObj));
            fileObj.delete();
            return uniqueFileName;

        }catch (Exception e) {
            log.error(e.getMessage());

            throw new CustomException(ErrorCode.FAILED_CONVERT_FILE);
        }
    }

    public String getImageUrl(String userUrl) {
        URL url = s3Client.getUrl(name, userUrl);
        return "" + url;
    }

    private String generateFileName(String memberId, String extension) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(memberId.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString() + extension;
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(ErrorCode.FAILED_CONVERT_FILE);
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
