package br.edu.ifpb.ads.pod.chat.project.shared.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 16:56:49
 */
public class Notification implements Serializable {

    private String id;
    private Message message;
    private String userToId;
    private boolean delivered;

    public Notification(String id) {
        this.id = id;
    }

    public Notification(Message message, String userToId) {
        this.message = message;
        this.userToId = userToId;
        delivered = false;
    }
    
    public Notification(Message message, String userToId, boolean delivered) {
        this.message = message;
        this.userToId = userToId;
        this.delivered = delivered;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getUserToId() {
        return userToId;
    }

    public void setUserToId(String userToId) {
        this.userToId = userToId;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Notification other = (Notification) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
