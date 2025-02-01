package org.example.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private final String sender;
    private final String content;
    private final String timestamp;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return timestamp + " " + sender + ": " + content;
    }
}
