package com.ec.recauctionec.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ec.recauctionec.services.StorageImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StorageImageImpl implements StorageImage {

    private final Cloudinary cloudinary;

    @Override
    public List<String> storageMultiImage(MultipartFile[] files) {
        List<String> data = new ArrayList<>();
        for (MultipartFile file : files) {
            data.add(uploadFile(file));
        }
        return data;
    }

    public String uploadFile(MultipartFile multipartFile) {
        try {
            Map r = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String filename = (String) r.get("secure_url");
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
