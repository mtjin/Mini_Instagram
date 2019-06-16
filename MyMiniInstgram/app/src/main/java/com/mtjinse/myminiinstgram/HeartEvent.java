package com.mtjinse.myminiinstgram;

public class HeartEvent {
    String id;
    boolean like;

    public HeartEvent(String id, boolean like) {
        this.id = id;
        this.like = like;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
