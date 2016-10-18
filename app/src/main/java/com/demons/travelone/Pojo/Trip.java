package com.demons.travelone.Pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinjal on 30/8/16.
 */
public class Trip {
    String description;
    String uname,to,from,medium;

    public Trip() {
    }

    public Trip(String uname, String to, String from, String medium,String description) {
        this.uname = uname;
        this.to = to;
        this.from = from;
        this.medium = medium;
        this.description= description;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uname", uname);
        result.put("to", to);
        result.put("from", from);
        result.put("medium", medium);
        result.put("description",description);
        return result;
    }
}
