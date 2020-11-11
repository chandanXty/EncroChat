package com.ctyx.encrochat;

public class Conversation {

    public boolean seen;
    public long timestamp;

    public Conversation(){

    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Conversation(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
}
