package com.barmagli.helper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class HashHelper {
    
    /**
     * hash string with md5 to 32 char
     * 
     * @param str
     * @return
     */
    public static String stringHash(String str) {
        String myHash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        return myHash;
    }

    public static boolean stringVerify(String str, String strHashed){
        String hashed = HashHelper.stringHash(str);
        return hashed.equals(strHashed);
    }
}

