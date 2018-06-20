package com.yxy.oa.encrypt;/**
 * Created by dell on 2018/2/27.
 */

import com.yxy.oa.entity.enums.SubsidiaryCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 加密工厂，负责针对不同类型的系统生成不同的密码
 **/
public class EncryptFactory {

    protected Logger logger = LoggerFactory.getLogger(EncryptFactory.class);

    private EncryptFactory() {
    }

    private static class Builder {
        public static final EncryptFactory ENCRYPT_FACTORY = new EncryptFactory();
    }

    public static EncryptFactory builder() {
        return Builder.ENCRYPT_FACTORY;
    }

    /**
     * 获取后端加密后的密码
     *
     * @param encryptType
     * @param password
     * @param salt
     * @return
     */
    public String produceBackPassword(String encryptType, String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return null;
        }
        IEncrypt encrypt = getEncryptInstance(encryptType);
        return encrypt.getBackPassword(password, salt);
    }

    /**
     * 获取前端加密后的密码，主要用在重置密码场合。
     *
     * @param encryptType
     * @param password
     * @param salt
     * @return
     */
    public String produceFrontPassword(String encryptType, String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return null;
        }
        IEncrypt encrypt = getEncryptInstance(encryptType);
        return encrypt.getFrontPassword(password, salt);
    }

    /**
     * 验证后台密码是否匹配，
     *
     * @param encryptType 加密类型
     * @param password    已经经过前端加密的密码
     * @param salt        盐
     * @param dbPassword  数据库的密码字符串
     * @return
     */
    public boolean verifyBackPassword(String encryptType, String password, String salt, String dbPassword) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(dbPassword)) {
            return false;
        }
        return produceBackPassword(encryptType, password, salt).equals(dbPassword);
    }

    /**
     * 获取适合子公司的密码
     * 接入各个子公司，因此前端的密码会包含各个子公司加密算法的合集，各个密码之间用分号隔开。
     * 如：md5(pwd);pdkdf2(pwd)，为了兼容有些子公司，做了一些处理，如果只传一个密码，第二个密码将会被默认为空
     * 主要用在子公司修改密码场合
     *
     * @param encryptType 子公司标识/加密类型，根据标识获取前端密码
     * @param password    已经经过前端加密的密码
     * @return
     */
    public String getFitPassword(String encryptType, String password) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        String[] pwds = new String[2];
        String[] encryptPasswords = password.split(";");
        if (encryptPasswords.length == 1) {
            pwds[0] = encryptPasswords[0];
            pwds[1] = "";
        } else if (encryptPasswords.length >= 2) {
            pwds[0] = encryptPasswords[0];
            pwds[1] = encryptPasswords[1];
        }
        String curPassword = null;
        if (SubsidiaryCodeEnum.YC.getCode().equals(encryptType)) {
            curPassword = encryptPasswords[1];
        } else {
            curPassword = encryptPasswords[0];
        }
        return curPassword;
    }


    /**
     * 获取加密算法实例
     *
     * @param encryptType
     * @return
     */
    private IEncrypt getEncryptInstance(String encryptType) {

        IEncrypt encrypt = null;
        logger.info("当前加密规则:" + encryptType);
        if (SubsidiaryCodeEnum.DJ.getCode().equals(encryptType)) {
            encrypt = new DjEncrypt();
        } else if (SubsidiaryCodeEnum.YC.getCode().equals(encryptType)) {
            encrypt = new YcEncrypt();
        } else if (SubsidiaryCodeEnum.YBF.getCode().equals(encryptType)) {
            encrypt = new YbfEncrypt();
        } else if (SubsidiaryCodeEnum.HT.getCode().equals(encryptType)) {
            encrypt = new HtEncrypt();
        } else {
            encrypt = new DefaultEncrypt();
        }

        logger.info("当前加密规则:" + encrypt);
        return encrypt;
    }


}
