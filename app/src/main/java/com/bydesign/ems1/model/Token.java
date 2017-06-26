package com.bydesign.ems1.model;

/**
 * Created by user on 5/31/2016.
 */
public class Token {


   static  String Token = null;
    static String url=null;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String urli) {
       url = urli;
    }

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }
}
