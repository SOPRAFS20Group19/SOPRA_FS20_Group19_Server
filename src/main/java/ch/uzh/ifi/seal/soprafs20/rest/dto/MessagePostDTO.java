package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class MessagePostDTO {

    private int senderId;
    private String content;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
