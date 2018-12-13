package com.ldzhn.baseframework.exception;

/**
 * @Author : llliu
 * @Despriction : 系统业务异常统一封装处理
 * @Date : create on 2018/8/1 17:33
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1240015972592825169L;
    private boolean logged;
    private Integer errorCode = 0; //错误代码 默认为0
    private String errorDetailMsg; //错误详情信息


    /**
     * 业务层受检异常
     *
     * @param errMsg 异常消息
     * @param logged 是否已记录异常
     */
    public BusinessException(String errMsg, boolean logged) {
        super(errMsg);
        this.logged = logged;
        this.errorDetailMsg = errMsg;
    }

    /**
     * 业务层受检异常
     *
     * @param errMsg    异常消息
     * @param logged    是否已记录异常
     * @param errorCode 错误代码
     */
    public BusinessException(Integer errorCode, String errMsg, boolean logged) {
        super(errMsg);
        this.logged = logged;
        this.errorCode = errorCode;
        this.errorDetailMsg = errMsg;
    }

    /**
     * 业务层受检异常
     *
     * @param errorCode      错误编码
     * @param errMsg         错误简要信息
     * @param errorDetailMsg 错误详情信息
     * @param logged         是否需要打印日志
     */
    public BusinessException(Integer errorCode, String errMsg, String errorDetailMsg, boolean logged) {
        super(errMsg);
        this.logged = logged;
        this.errorCode = errorCode;
        this.errorDetailMsg = errorDetailMsg;
    }

    /**
     * 业务层受检异常
     *
     * @param errMsg    异常消息
     * @param throwable 异常堆栈
     * @param logged    是否已记录异常
     */
    public BusinessException(String errMsg, Throwable throwable, boolean logged) {
        super(errMsg, throwable);
        this.logged = logged;
        this.errorDetailMsg = errMsg;
    }

    /**
     * 业务层受检异常
     *
     * @param errorCode 错误代码
     * @param errMsg    异常消息
     * @param throwable 异常堆栈
     * @param logged    是否已记录异常
     */
    public BusinessException(Integer errorCode, String errMsg, Throwable throwable, boolean logged) {
        super(errMsg, throwable);
        this.logged = logged;
        this.errorCode = errorCode;
        this.errorDetailMsg = errMsg;
    }

    /**
     * 业务层受检异常
     *
     * @param errorCode      错误代码
     * @param errMsg         异常消息
     * @param errorDetailMsg 异常详情信息
     * @param throwable      异常堆栈
     * @param logged         是否已记录异常
     */
    public BusinessException(Integer errorCode, String errMsg, String errorDetailMsg, Throwable throwable, boolean logged) {
        super(errMsg, throwable);
        this.logged = logged;
        this.errorCode = errorCode;
        this.errorDetailMsg = errorDetailMsg;
    }


    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetailMsg() {
        return errorDetailMsg;
    }

    public void setErrorDetailMsg(String errorDetailMsg) {
        this.errorDetailMsg = errorDetailMsg;
    }
}