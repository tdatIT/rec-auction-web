package com.ec.recauctionec.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ec.recauctionec.service.StorageImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StorageImageImpl implements StorageImage {
    @Autowired
    Cloudinary cloudinary;

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

        /*Path uploadPath = Paths.get("/uploads");
        //upload file
        String filename = multipartFile.getOriginalFilename();

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            //update user avatar
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return "";
    }

}
