package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.List;

public class Chat {

    private List<Message> messages;

    public Chat(){}

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
