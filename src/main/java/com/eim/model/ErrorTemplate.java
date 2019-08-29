package com.eim.model;

import lombok.Data;

@Data
public class ErrorTemplate {
    private boolean status;//状态：false

    private int errorCode;//错误码

    private String message;//错误信息
}
