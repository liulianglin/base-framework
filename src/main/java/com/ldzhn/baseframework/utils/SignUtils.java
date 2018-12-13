package com.ldzhn.baseframework.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by rocky on 2018/5/24.
 * <p>
 * http接口签名相关工具类
 */
public class SignUtils {


    /**
     * 加签方法
     *
     * @param paramValues
     * @param ignoreParamNames
     * @param appSecret
     * @param signType
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String makeSign(Map<String, String> paramValues, List<String> ignoreParamNames, String appSecret, String signType) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        List<String> paramNames = new ArrayList<>(paramValues.size());
        paramNames.addAll(paramValues.keySet());
        if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
            for (String ignoreParamName : ignoreParamNames) {
                paramNames.remove(ignoreParamName);
            }
        }
        Collections.sort(paramNames);

        sb.append(appSecret).append("&");
        for (String paramName : paramNames) {
            final String pValue = paramValues.get(paramName);
            //空值不参与加签
            if (StringUtils.isBlank(pValue)) {
                continue;
            }
            sb.append(paramName).append("=").append(pValue).append("&");
        }
        sb.append(appSecret);

        MessageDigest md = MessageDigest.getInstance(signType);
        return byte2hex(md.digest(sb.toString().getBytes("UTF-8")));
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map<String, String> map = Maps.newHashMap();
        map.put("a", "1");
        map.put("b", "2");
        List<String> list = Lists.newArrayList();
        list.add("a");
        String str = SignUtils.makeSign(map, list, "aaa", "MD5");
        System.out.println(str);
    }
}
