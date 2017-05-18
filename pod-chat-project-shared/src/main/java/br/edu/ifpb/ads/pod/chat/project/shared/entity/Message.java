package br.edu.ifpb.ads.pod.chat.project.shared.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 16:55:40
 */
public class Message implements Serializable, Comparable<Message> {
    
    private String id;
    private Long createdTime;
    private String text;
    private String userFromId;
    private String chatId;

    public Message() {
    }

    public Message(Long createdTime, String text, String userFromId, String chatId) {
        this.createdTime = createdTime;
        this.text = text;
        this.userFromId = userFromId;
        this.chatId = chatId;
    }
    
    public LocalDateTime getCreatedLocalDateTime(){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTime), ZoneId.systemDefault());
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserFromId() {
        return userFromId;
    }

    public void setUserFromId(String userFromId) {
        this.userFromId = userFromId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Message o) {
        return this.createdTime.compareTo(o.getCreatedTime());
    }
    
}
