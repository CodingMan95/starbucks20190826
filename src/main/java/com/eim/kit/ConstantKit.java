package com.eim.kit;

/**
 * @author lpf
 * @description
 */
public final class ConstantKit {

    /**
     * 每天可游戏次数
     */
    public static final Integer PLAY_NUM = 5;

    /**
     * 返回码：请求错误
     */
    public static final Integer BAD_REQUEST = 400;
    public static final String FAIL = "fail";

    /**
     * 返回码：未授权
     */
    public static final Integer NO_TOKEN = 401;
    public static final String NO_LOGIN = "未授权登录";
    public static final String NO_TOKEN_MESSAGE = "用户token已失效，请重新登录";

    public static final String NO_USER = "用户不存在";
    public static final String ERROR_PASSWORD = "密码错误";

    public static final String CHANGE_ERROR = "修改失败";

    public static final String ACTIVITY_OVER = "活动已结束，无法修改活动状态";

    /**
     * 返回码：请求成功
     */
    public static final Integer SUCCESS_REQUEST = 0;
    public static final String SUCCESS = "success";

    /**
     * redis存储token设置的过期时间
     */
    public static final Integer TOKEN_EXPIRE_TIME = 60 * 30;

    /**
     * 设置可以重置token过期时间的时间界限
     */
    public static final Integer TOKEN_RESET_TIME = 60 * 30;

    /**
     * 存放登录用户模型Key的Request Key
     */
    public static final String REQUEST_CURRENT_KEY = "REQUEST_CURRENT_KEY";

    /**
     * 缺少参数
     */
    public static final String NO_PARAMETER = "缺少参数";

    /**
     * 缺少参数
     */
    public static final String USERINFO_FAIL = "用户信息获取失败";

    public static final String ERROR_CODE = "code已失效";

    public static final String HAVE_STORE = "店铺已存在，请勿重复添加";
    public static final String HAVE_STOREID = "店铺编号已存在";
    public static final String HAVE_STORENAME = "店铺名称已存在";

    public static final String COMBO_ORDERED = "该套餐已有人预约，无法删除";

    public static final int ORDER_FAIL = 0;
    public static final int ORDER_SUCCESS = 1;
    public static final int ORDER_OVER = 2;

    public static final int PAGE_LIMIT = 20;

    public static final int ADMIN_ROLE = 1;
    public static final int STORE_ROLE = 2;

}
