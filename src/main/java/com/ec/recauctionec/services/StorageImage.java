package com.ec.recauctionec.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageImage {
    List<String> storageMultiImage(MultipartFile[] files);
}
