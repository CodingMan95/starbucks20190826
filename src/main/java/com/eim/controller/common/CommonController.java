package com.eim.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eim.entity.StoreInfo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.StoreInfoService;
import com.eim.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    public ResultTemplate getAll(@RequestParam int type, @RequestParam(required = false) String parm, @RequestParam(required = false) Integer activeId, @RequestParam(required = false) String cityName) {
        Map<String, Object> map;
        if (null == activeId || activeId == 0) {
            //获取全部省市区及门店
            map = storeInfoService.getAllStore(type, parm, cityName);
        } else {
            //获取参与活动的省市区及门店
            map = storeInfoService.getStoreLimit(type, parm, cityName, activeId);
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

    @ApiOperation("校验门店编号是否存在")
    @GetMapping("verifyStore.do")
    public ResultTemplate verifyStore(@RequestParam String storeId) {
        int count = storeInfoService.count(new QueryWrapper<StoreInfo>().eq("store_id", storeId));
        if (count == 0) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.HAVE_STOREID);
    }

    @ApiOperation("校验门店名称是否存在")
    @GetMapping("verifyStoreName.do")
    public ResultTemplate verifyStoreName(@RequestParam String storeName) {
        int count = storeInfoService.count(new QueryWrapper<StoreInfo>().eq("store_name", storeName));
        if (count == 0) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.HAVE_STORENAME);
    }

    @ApiOperation("查询全省/市全部门店")
    @GetMapping("getStoreByType.do")
    public ResultTemplate getStoreByType(@RequestParam int type, @RequestParam String parm) {
        if (StringUtils.isEmpty(parm)) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        List<StoreInfo> store = storeInfoService.getStoreByType(type, parm);
        return ResultTemplate.success(store);
    }

    @ApiOperation("通过门店列表获取门店对应id列表 ")
    @ApiImplicitParam(name = "nameList", value = "例子：{\"武汉江夏中百广场店\";\"光谷德国风情街店\";\"南昌铜锣湾广场店\"}", paramType = "query")
    @PostMapping("getIdList.do")
    public ResultTemplate getIdList(@RequestParam String nameList) {
        if (StringUtils.isEmpty(nameList)) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }

        if (nameList.startsWith("{") && nameList.endsWith("}") && nameList.contains("\"")) {
            nameList = nameList.substring(1, nameList.length() - 1).replaceAll("\"", "");
            String[] names = nameList.split(";");

            List<StoreInfo> storeList = storeInfoService.getStoreList(names);

            return ResultTemplate.success(storeList);
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.ERROR_STYLE);
    }

}
