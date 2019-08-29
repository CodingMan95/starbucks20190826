package com.eim.controller.applet;

import com.eim.entity.ActivityInfo;
import com.eim.entity.StoreInfo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.ActivityInfoService;
import com.eim.service.StoreInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "小程序-活动管理")
@RestController
@RequestMapping("/activity/")
public class ActivityController {
    @Autowired
    private ActivityInfoService infoService;
    @Autowired
    private StoreInfoService storeInfoService;

    @ApiOperation("首页活动列表展示")
    @GetMapping("list.do")
    public ResultTemplate activityList(@RequestParam String city) {
        if (StringUtils.isEmpty(city)) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        List<ActivityInfo> infoList = infoService.getActivityByCity(city);
        return ResultTemplate.success(infoList);
    }

    @ApiOperation("活动详情")
    @GetMapping("detail.do")
    public ResultTemplate activityDetail(@RequestParam Integer activeId) {
        if (null == activeId || activeId == 0) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        ActivityInfo activityDetail = infoService.getActivityDetail(activeId);
        if (null == activityDetail) {
            return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
        }
        return ResultTemplate.success(activityDetail);
    }

    @ApiIgnore
    @ApiOperation("可选报名门店")
    @GetMapping("store.do")
    public ResultTemplate storeDetail(@RequestParam Integer activeId, @RequestParam String city) {
        if (null == activeId || activeId == 0 || StringUtils.isEmpty(city)) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        List<StoreInfo> storeInfoList = storeInfoService.selectByIdSet(city, activeId);
        if (null == storeInfoList) {
            return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
        }
        return ResultTemplate.success(storeInfoList);
    }

}
