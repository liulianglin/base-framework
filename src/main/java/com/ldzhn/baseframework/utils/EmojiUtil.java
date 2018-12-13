package com.ldzhn.baseframework.utils;/**
 * Created by admin on 2018/6/21.
 */

import com.vdurmont.emoji.EmojiParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @description:表情包处理工具
 * @author:xlwang
 * @create:2018/6/21
 */
public class EmojiUtil {

    private Logger logger = LogManager.getLogger(EmojiUtil.class);

    public EmojiUtil() {
    }

    /**
     * 将表情符号转为字符
     * @param input
     * @return
     */
    public static String toAliases(String input) {
        return EmojiParser.parseToAliases(input);
    }

    /**
     * 将字符转为表情符号
     * @param input
     * @return
     */
    public static String toUnicode(String input) {
        return EmojiParser.parseToUnicode(input);
    }

}
