package com.ec.recauctionec.controller;

import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.service.UserService;
import com.ec.recauctionec.variable.Router;
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

    //Upload Avatar
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public ResponseEntity uploadAvatar(@RequestParam String username,
                                       @RequestParam MultipartFile multipartFile, Model model) {
        try {
            //upload file
            String filename = multipartFile.getOriginalFilename();
            File file = new File(getUploadFolder(), filename);
            multipartFile.transferTo(file);
            //update user avatar
            User user = userService.findByUsername(username);
            user.setAvatar(filename);
            userService.updateUser(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public File getUploadFolder() {
        File folderUpload = new File("/uploads");
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }
        return folderUpload;
    }
}
