package com.alexkirillov.alitabot.models.logging;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//TODO !!!Test Concept!!!
public class MessageLog {
    private final String messageDate;
    private final String user_name;
    private final long telegram_user_id;
    private final String user_first_name;
    private final String user_last_name;
    private final String message_in;
    private final String response;

    public MessageLog(String user_name, long telegram_user_id, String user_first_name, String user_last_name, String message_in, String response) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.messageDate = dateFormat.format(new Date());
        this.user_name = user_name;
        this.telegram_user_id = telegram_user_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.message_in = message_in;
        this.response = response;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public String getUser_name() {
        return user_name;
    }

    public long getUser_id() {
        return telegram_user_id;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public String getMessage_in() {
        return message_in;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString(){
        return "-----------------------------------"+ messageDate + "From: "+user_name+", ID: "+telegram_user_id
                +"Cred: "+ user_first_name+" "+user_last_name
                + "User's Message: '"+message_in+"'"+"Bot Response Code: '"+response+"'";
    }

    public DBObject toDBObject(){
        return new BasicDBObject("messageDate", messageDate).append("username", user_name)
                                .append("userid", telegram_user_id).append("firstname", user_first_name)
                                .append("lastname", user_last_name);
    }
}
