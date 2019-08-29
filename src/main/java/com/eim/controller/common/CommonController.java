package com.eim.controller.common;

import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.StoreInfoService;
import com.eim.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "通用接口管理")
@RestController
@RequestMapping("/common/")
public class CommonController {
    @Autowired
    private StoreInfoService storeInfoService;
    @Autowired
    private UploadService uploadService;

    @ApiOperation("获取省市区及门店")
    @GetMapping("getByType.do")
    public ResultTemplate getAll(@RequestParam int type, @RequestParam(required = false) String parm, @RequestParam(required = false) Integer activeId) {
        Map<String, Object> map;
        if (null == activeId || activeId == 0) {
            //获取全部省市区及门店
            map = storeInfoService.getAllStore(type, parm);
        } else {
            //获取参与活动的省市区及门店
            map = storeInfoService.getStoreLimit(type, parm, activeId);
        }
        return ResultTemplate.success(map);
    }

    @ApiOperation("上传图片")
    @PostMapping("upload.do")
    public ResultTemplate upload(@RequestParam MultipartFile file, HttpServletRequest request) {
        if (null == file) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        String baseUrl = request.getSession().getServletContext().getRealPath("");
        baseUrl = baseUrl.replaceAll("\\\\", "/");
        String imgUrl = uploadService.uploadImg(file, baseUrl);

        return ResultTemplate.success(imgUrl);
    }
}
