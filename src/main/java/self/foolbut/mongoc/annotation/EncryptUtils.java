package self.foolbut.mongoc.annotation;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptUtils {

    public static String encoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        MessageDigest md5=MessageDigest.getInstance("MD5");
        byte[] encryped = md5.digest(str.getBytes("utf-8"));
        String newstr=org.apache.commons.codec.binary.Base64.encodeBase64String(encryped);
        return newstr;
    }
    
    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {  
        byte[ ] keyBytes=org.apache.commons.codec.binary.Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");  
        return keyFactory.generatePrivate(keySpec);  
    } 
    
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException{  
        byte[ ] keyBytes=org.apache.commons.codec.binary.Base64.decodeBase64(publicKey.getBytes());  
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");  
        return keyFactory.generatePublic(keySpec);    
    }  

    public static byte[] encrypt(byte[] content, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{  
        Cipher cipher=Cipher.getInstance("RSA");//javaĬ��"RSA"="RSA/ECB/PKCS1Padding"  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        return cipher.doFinal(content);  
    }
    
    public static byte[] encrypt(byte[] content, String  publicKeyStr) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        PublicKey publicKey = EncryptUtils.getPublicKey(publicKeyStr);
        Cipher cipher=Cipher.getInstance("RSA");//javaĬ��"RSA"="RSA/ECB/PKCS1Padding"  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);  
    }
    
    public static byte[] decryptByPrivateKey(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException   {  
        byte[ ] content =org.apache.commons.codec.binary.Base64.decodeBase64(data);
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        return cipher.doFinal(content);
    } 
}
