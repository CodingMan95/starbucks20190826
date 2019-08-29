package com.eim.service.impl;

import com.eim.service.UploadService;
import com.eim.util.UploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {

    @Override
    public String uploadImg(MultipartFile file, String uploadUrl) {
        String imgUrl = UploadUtil.uploadImg(file, uploadUrl + "/my_imgs/", "/my_imgs/");
        return imgUrl;
    }
}
