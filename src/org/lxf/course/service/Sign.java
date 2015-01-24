package org.lxf.course.service;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;  

//生成JS-SDK权限验证的签名
public class Sign {
   /* public static void main(String[] args) {
        String jsapi_ticket = "bxLdikRXVbTPdHSM05e5u_qmRB2uLxdVrAyiBhY8lfIWvvquz0Xpjy-NtuhhQX_j6V96HwU6DK_rHVkE62EnNw";
        String url = "http://js.wwz114.cn/hotelms/";
        Map<String, String> ret = sign(jsapi_ticket, url);
        for (Map.Entry entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
    }*/

    public static Map<String, String> getSignature(String jsapi_ticket,String url){
    	Map<String, String> ret = sign(jsapi_ticket, url);
    	return ret;
    }
    
    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();  //签名的随机串
        String timestamp = create_timestamp();  //签名的时间戳
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
//        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

	//生成签名的随机串
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
	//生成签名的时间戳
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
