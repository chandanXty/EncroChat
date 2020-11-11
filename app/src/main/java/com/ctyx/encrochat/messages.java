package com.ctyx.encrochat;

public class messages {

    private String message;
    private Boolean seen;
    private long time;
    private String type;
    private String from;

    public messages(String message,Boolean seen,Long time,String type,String from){
        this.message=message;
        this.seen=seen;
        this.time=time;
        this.type=type;
        this.from=from;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {return from;}

    public void setFrom() {this.from=from;}

    public messages(){

    }
}
