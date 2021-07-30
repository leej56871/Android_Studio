package com.example.chattingapplication;

import java.io.Serializable;

public class ChatData implements Serializable {
    private String msg = "";
    private String nickname;
    private String email;
    private String format;
    private String url = " ";

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public java.lang.String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setFormat(String format){this.format = format;}
    public String getFormat(){return this.format;}
    public String getUrl() {return this.url;}
    public void setUrl(String url){this.url = url;}
}
