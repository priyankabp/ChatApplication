package com.example.priyankaphapale.chatapp.model;

/**
 * Created by m_alrajab on 4/3/17.
 */

public class ChatMessageItem {

    private String msgBody;
    private String senderName;
    private String timeStamp;
    private String photoUrl;


    public ChatMessageItem(){

    }


    // this is today design
    public ChatMessageItem(String msgBody, String senderName, String time, String u) {
        this.msgBody = msgBody;
        this.senderName = senderName;
        this.timeStamp=time;
        this.photoUrl=u;
    }


    public String getMsgBody() {
        return msgBody;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


}
