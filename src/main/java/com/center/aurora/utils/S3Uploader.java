package com.center.aurora.utils;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Utils s3Utils;

    public String upload(MultipartFile multipartFile, String dirName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        String storeFileName = createStoreFileName(dirName);

        try {
            s3Utils.storeFile(multipartFile.getInputStream(), objectMetadata, storeFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s3Utils.getStoreFileUrl(storeFileName);
    }

    public void deleteFile(String storeFileUrl,String dirname) {
        String storeFileName = extractStoreFileName(storeFileUrl);
        s3Utils.deleteStoreFile(storeFileName, dirname);
    }

    public String extractStoreFileName(String storeFileUrl) {
        int pos = storeFileUrl.lastIndexOf("/");
        return storeFileUrl.substring(pos + 1);
    }

    private String createStoreFileName(String dirName) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss_");
        double dValue = Math.random();
        int iValue = (int)(dValue * 10000);
        String date = sdf.format(cal.getTime());
        return dirName + "/" + date + Integer.toString(iValue);
    }
}
