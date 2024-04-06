package io.gitee.xuchenoak.limejapidocs.runner.util;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sun.org.apache.regexp.internal.RE;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ResCodeEnum;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;

/**
 * ID生成器工具类
 */
public class IdUtils {


    /**
     * Id加密秘钥
     */
    private static final byte[] encryptKey = "xuchenoak6666999".getBytes();

    /**
     * 加密Id
     * @param id
     * @return
     */
    public static String encryptId(Long id) {
        return SecureUtil.aes(encryptKey).encryptHex(String.valueOf(id));
    }

    /**
     * 解密Id
     * @param id
     * @return
     */
    public static Long decryptId(String id) {
        try {
            String decryptStr = SecureUtil.aes(encryptKey).decryptStr(id, CharsetUtil.CHARSET_UTF_8);
            if (StringUtils.isBlank(decryptStr)) {
                return null;
            }
            return Long.parseLong(decryptStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密Id
     * @param id
     * @return
     */
    public static Long decryptIdOrExc(String id) {
        Long res = decryptId(id);
        if (res == null) {
            CusExc.e(ResCodeEnum.NotFound, StrUtil.format("Id：{} 不存在", id));
        }
        return res;
    }

    /**
     * 解密Id
     * @param id
     * @return
     */
    public static String decryptStrId(String id) {
        try {
            return SecureUtil.aes(encryptKey).decryptStr(id, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            return null;
        }
    }


}
