package com.boyue.common.test;

import com.boyue.common.auth.entity.Payload;
import com.boyue.common.auth.entity.UserInfo;
import com.boyue.common.auth.utils.JwtUtils;
import com.boyue.common.auth.utils.RsaUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 14:38
 * @Author: Jacky
 * @Description: rsa测试工具
 */
public class RsaTest {
    /**
     * 公钥存储位置
     */
    private final String publicKeyPath = "D:\\GraduationProject\\BoYue\\ssh\\id_rsa.pub";
    /**
     * 私钥存储位置
     */
    private final String privateKeyPath = "D:\\GraduationProject\\BoYue\\ssh\\id_rsa";

    /**
     * 生成公钥和私钥
     * @throws Exception 异常抛出
     */
    @Test
    public void createKey() throws Exception {
        //生成密钥对
        RsaUtils.generateKey(publicKeyPath,privateKeyPath,"BoYue_shopping",2048);

        //读取公钥
        PublicKey publicKey = RsaUtils.getPublicKey(publicKeyPath);
        System.out.println(publicKey);

        System.out.println("=============");
        //读取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        System.out.println(privateKey);
    }

    /**
     * 测试生成和验证token
     */
    @Test
    public void tokenTest(){
        try {
            //获取私钥
            PrivateKey privateKey = RsaUtils.getPrivateKey(privateKeyPath);
            //使用jwt生成token
            //构建用户数据
            UserInfo userInfo = new UserInfo();
            userInfo.setId(101L);
            userInfo.setUsername("jacky");
            userInfo.setRole("admin");
            String token = JwtUtils.generateTokenExpireInMinutes(userInfo, privateKey, 3);
            System.out.println(token);
            System.out.println("=====================================");

            //验证token
            //获取公钥
            PublicKey publicKey = RsaUtils.getPublicKey(publicKeyPath);
            //解析token
            Payload<? extends UserInfo> payload = JwtUtils.getInfoFromToken(token, publicKey, userInfo.getClass());

            String id = payload.getId();
            System.out.println("id="+id);
            UserInfo user = payload.getUserInfo();
            System.out.println("user："+user);
            Date expiration = payload.getExpiration();
            System.out.println("date:"+expiration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
