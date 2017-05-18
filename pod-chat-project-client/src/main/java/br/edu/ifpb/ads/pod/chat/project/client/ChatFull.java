package br.edu.ifpb.ads.pod.chat.project.client;

import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 23:01:22
 */
public class ChatFull implements Comparable<ChatFull> {

    private String name;
    private List<Message> messages;
    private List<Notification> notifications;
    private boolean subcribed;

    public ChatFull(String name) {
        this.name = name;
        messages = new ArrayList<>();
        notifications = new ArrayList<>();
        subcribed = false;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.name);
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
        final ChatFull other = (ChatFull) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ChatFull o) {
        if (this.isSubcribed()) {
            return -1;
        }

        return this.getName().compareTo(o.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
    public void addAllMessages(Collection<Message> ms) {
        messages.addAll(ms);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
    
    public int getNotificationCount(){
        return notifications.size();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }
    
    public void addAllNotifications(Collection<Notification> ns) {
        notifications.addAll(ns);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
    }

    public boolean isSubcribed() {
        return subcribed;
    }

    public void setSubcribed(boolean subcribed) {
        this.subcribed = subcribed;
    }

    @Override
    public String toString() {
        return "-> " + name + (subcribed == true ? "(" + getNotificationCount() + " News)": "(Unsubscribed)");
    }

    
}
