package com.ldzhn.baseframework.constants;

/**
 * @Author : llliu
 * @Despriction : 接口请求相关核心常量
 * @Date : create on 2018/8/1 17:33
 */
public class CoreConstants {

    /**
     * 成功code
     */
    public static final int REQUEST_SUCCESS_CODE = 0;
    /**
     * 参数错误
     */
    public static final int REQUEST_ERROR_PARAMS = -1;

    /**
     * 程序异常code
     */
    public static final int REQUEST_PROGRAM_ERROR_CODE = -100;

    /**
     * 鉴权失败
     */
    public static final int REQUEST_ERROR_AUTHENTICATION = -200;
    /**
     * 授权失败
     */
    public static final int REQUEST_ERROR_AUTHORIZE = -201;
    /**
     * 非法访问
     */
    public static final int REQUEST_ERROR_ILLEGALLY = -202;
    /**
     * IP拒绝code
     */
    public static final int REQUEST_REFUSE_IP_ERROR_CODE = -60;

    /**
     * 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
     * 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
     * 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
     * 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
     * 406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
     * 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
     * 422 Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
     * 500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
     */
    private static final int INVALID_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int NOT_ACCEPTABLE = 406;
    private static final int GONE = 410;
    private static final int UNPROCESABLE = 422;
    private static final int INTERNAL_SERVER_ERROR = 500;

}