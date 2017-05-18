package br.edu.ifpb.ads.pod.chat.project.server;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogClient;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataChat;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataMessage;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataNotification;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataUser;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogServer;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
import br.edu.ifpb.ads.pod.chat.project.shared.exeption.AlreadyLoggedUserExeption;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 03:35:12
 */
public class DialogServerImpl extends UnicastRemoteObject implements DialogServer {

    private DialogDataUser dialogDataUser;
    private DialogDataChat dialogDataChat;
    private DialogDataMessage dialogDataMessage;
    private DialogDataNotification dialogDataNotification;
    private HashMap<String, DialogClient> onlineUsers;

    public DialogServerImpl(DialogDataUser dialogDataUser,
            DialogDataChat dialogDataChat,
            DialogDataMessage dialogDataMessage,
            DialogDataNotification dialogDataNotification) throws RemoteException {

        this.dialogDataUser = dialogDataUser;
        this.dialogDataChat = dialogDataChat;
        this.dialogDataMessage = dialogDataMessage;
        this.dialogDataNotification = dialogDataNotification;
        onlineUsers = new HashMap<>();
    }

    public DialogServerImpl() throws RemoteException {
    }

    @Override
    public void register(User u) throws RemoteException {
        dialogDataUser.persist(u);
    }

    @Override
    public void unRegister(String userId) throws RemoteException {
        dialogDataUser.remove(userId);
    }

    @Override
    public User login(String login, String password, DialogClient dialogClient) throws RemoteException {

        DialogClient get = onlineUsers.get(login);
        try {
            get.ping();
        } catch (RemoteException e) {
            throw new AlreadyLoggedUserExeption();
        }

        onlineUsers.put(login, dialogClient);
        return dialogDataUser.login(login, password);
    }

    @Override
    public void logout(String userId) throws RemoteException {
        onlineUsers.remove(userId);
    }

    @Override
    public List<String> listChats() throws RemoteException {
        return dialogDataChat.listChatAllIds();
    }

    @Override
    public List<String> listChatsByUser(String userId) throws RemoteException {
        return dialogDataChat.listChatByUser(userId);
    }

    @Override
    public void subscribe(String userId, String chatId) throws RemoteException {
        dialogDataChat.subscribe(userId, chatId);
    }

    @Override
    public void unSubscribe(String userId, String chatId) throws RemoteException {
        dialogDataChat.unsubscribe(userId, chatId);
    }

    @Override
    public void publish(Message m) throws RemoteException {
        dialogDataMessage.persist(m);
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public List<Message> listMessagesByUserChat(String userId, String chatId) throws RemoteException {
        return dialogDataMessage.listMessageByUserChat(userId, chatId);
    }

    @Override
    public List<Notification> listNotificationsByUserChat(String userId, String chatId) throws RemoteException {
        return dialogDataNotification.listNotificationByUserChat(userId, chatId);
    }

    @Override
    public void setAlreadyDelivered(String notificationId) throws RemoteException {
        dialogDataNotification.setAlreadyDelivered(notificationId);
    }

    @Override
    public void notifyAllSubscribers() throws RemoteException {
        for (String userId : onlineUsers.keySet()) {
            List<Notification> ns = dialogDataNotification.listNotificationByUser(userId);
            for (Notification n : ns) {
                DialogClient dialogClient = onlineUsers.get(userId);
                if (dialogClient != null) {
                    try {
                        dialogClient.notify(n);
                    } catch (RemoteException ex) {
                        System.out.println("Falha ao tentar notificar cliente: " + ns);
                        // talvez remover o cliente???
                        break;
                    }
                }
            }
        }
    }

}
