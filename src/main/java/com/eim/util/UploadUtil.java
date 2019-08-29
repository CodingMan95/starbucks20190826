package com.eim.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class UploadUtil {

    /**
     * 上传图片工具类
     *
     * @param multipartFile 上传的文件
     * @param upload_url    上传路径
     * @return
     */
    public static String uploadImg(MultipartFile multipartFile, String upload_url, String save_url) {

        if (!multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            String houzui = FilenameUtils.getExtension(fileName);
            String name = new Date().getTime() + "." + houzui;

            File file = new File(upload_url);
            String Url = upload_url + name;

            File file1 = new File(Url);

            if (!file.exists()) {
                file.mkdirs();
            }

            try {
                multipartFile.transferTo(file1);
                return save_url + name;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * base64编码转MultipartFile文件
     *
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(baseStrs[1]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
