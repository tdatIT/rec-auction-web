package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.services.UserService;
import com.ec.recauctionec.data.variable.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping(value = Router.UPLOAD)
public class FileUploadsController {
    @Autowired
    private UserService userService;

    public File getUploadFolder() {
        File folderUpload = new File("/uploads");
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }
        return folderUpload;
    }
}
