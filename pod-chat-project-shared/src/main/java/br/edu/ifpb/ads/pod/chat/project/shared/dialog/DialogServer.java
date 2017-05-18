/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.ads.pod.chat.project.shared.dialog;

import br.edu.ifpb.ads.pod.chat.project.shared.entity.Chat;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 17:59:46
 */
public interface DialogServer extends Remote {

    public void register(User u)
            throws RemoteException;

    public void unRegister(String userId)
            throws RemoteException;

    public User login(String login, String password, DialogClient dialogClient)
            throws RemoteException;

    public void logout(String userId)
            throws RemoteException;

    public List<String> listChats()
            throws RemoteException;

    public List<String> listChatsByUser(String userId)
            throws RemoteException;
    
    public void subscribe(String userId, String chatId)
            throws RemoteException;

    public void unSubscribe(String userId, String chatId)
            throws RemoteException;

    public void publish(Message m)
            throws RemoteException;
    
    public boolean ping()
            throws RemoteException;
    
    public List<Message> listMessagesByUserChat(String userId, String chatId)
            throws RemoteException;
    
    public List<Notification> listNotificationsByUserChat(String userId, String chatId)
            throws RemoteException;
    
    public void setAlreadyDelivered(String notificationId)
            throws RemoteException;

    public void notifyAllSubscribers()
            throws RemoteException;
}
