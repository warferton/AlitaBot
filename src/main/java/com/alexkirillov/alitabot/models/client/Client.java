package com.alexkirillov.alitabot.models.client;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.UUID;

public class Client {
    private String user_name;
    private String first_name;
    private String last_name;
    private UUID user_id;

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public UUID getUserId() {
        return user_id;
    }

    public void setUserId(UUID user_id) {
        this.user_id = user_id;
    }

    public Client(String user_name, String first_name, String last_name, UUID user_id) {
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Client{" +
                "user_name='" + user_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }

    public DBObject toDBObject(){
        return new BasicDBObject("username", user_name).append("firstname", first_name)
                .append("lastname", last_name).append("userid", user_id);
    }
}
