package com.eim.model;

import com.eim.kit.ConstantKit;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultTemplate {
    @ApiModelProperty(value = "返回状态")
    public boolean status;

    @ApiModelProperty(value = "返回状态码")
    public Integer errorCode;

    @ApiModelProperty(value = "正确、错误信息")
    public String message;

    @ApiModelProperty(value = "返回数据")
    public Object data;


    public ResultTemplate(boolean status, Integer errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * 失败
     */
    public static ResultTemplate error(int code, String message) {
        ResultTemplate resultTemplate = new ResultTemplate(false, code, message);
        return resultTemplate;
    }

    /**
     * 成功：不带参数
     */
    public static ResultTemplate success() {
        ResultTemplate resultTemplate = new ResultTemplate(true, ConstantKit.SUCCESS_REQUEST, ConstantKit.SUCCESS);
        return resultTemplate;
    }

    /**
     * 成功：带参数
     */
    public static ResultTemplate success(Object data) {
        ResultTemplate resultTemplate = new ResultTemplate(true, ConstantKit.SUCCESS_REQUEST, ConstantKit.SUCCESS, data);
        return resultTemplate;
    }

}
