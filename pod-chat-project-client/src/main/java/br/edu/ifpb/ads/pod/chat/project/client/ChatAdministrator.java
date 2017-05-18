package br.edu.ifpb.ads.pod.chat.project.client;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogClient;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogServer;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 22:49:13
 */
public class ChatAdministrator extends UnicastRemoteObject
        implements Serializable, DialogClient {

    private List<ChatFull> chatsFull;
    private List<Message> offlineMessages;
    private DialogServer dialogServer;
    private User loggedUser;
    private boolean isLooged;

    public ChatAdministrator() throws RemoteException {
    }

    public ChatAdministrator(boolean isLooged) throws RemoteException {
        chatsFull = new ArrayList<>();
        offlineMessages = new ArrayList<>();
        isLooged = isLooged;
    }

    @Override
    public void notify(Notification n) throws RemoteException {

        ChatFull chatFull = findById(n.getMessage().getChatId());
        if (chatFull != null) {
            chatFull.addNotification(n);
        }

    }

    @Override
    public boolean ping() throws RemoteException {
        if (isLooged) {
            return true;
        } else {
            return false;
        }
    }

    public void sendMessage(String text, String chatId) {
        Message message = new Message(System.currentTimeMillis(),
                text, loggedUser.getLogin(), chatId);
        try {
            dialogServer.publish(message);

            ChatFull findById = findById(chatId);
            findById.addMessage(message);
        } catch (RemoteException ex) {
            ex.printStackTrace();

            System.out.println("ERRO MEU: ADD TO OFFLINE MENSAGE LIST");
            offlineMessages.add(message);
        }
    }

    public ChatFull findById(String chatId) {
        for (ChatFull cf : chatsFull) {
            if (cf.getName().equals(chatId)) {
                return cf;
            }
        }
        return null;
    }

    public void reloadChatsFull() {
        try {
            if (loggedUser != null) {
                String login = loggedUser.getLogin();
                List<String> allCs = dialogServer.listChats();
                List<String> myCs = dialogServer.listChatsByUser(login);

                for (String cs : allCs) {
                    if (findById(cs) == null) {
                        ChatFull csf = new ChatFull(cs);

                        if (myCs.contains(cs)) {
                            csf.setSubcribed(true);

                            csf.addAllMessages(dialogServer.listMessagesByUserChat(login, cs));
                            csf.addAllNotifications(dialogServer.listNotificationsByUserChat(login, cs));
                        }

                        chatsFull.add(csf);
                    }
                }
            } else {
                System.out.println("Usuario ainda tá null mano?");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.out.println("ERRO MEU: AO DAR RELOAD NOS CHATS");
        }
    }

    public void subscribeIntoChat(String chatId) {
        try {
            dialogServer.subscribe(loggedUser.getLogin(), chatId);
            ChatFull csf = findById(chatId);
            String login = loggedUser.getLogin();

            if (csf != null) {
                csf.setSubcribed(true);
                csf.addAllMessages(dialogServer.listMessagesByUserChat(login, chatId));
                csf.addAllNotifications(dialogServer.listNotificationsByUserChat(login, chatId));
            }

        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.out.println("ERRO MEU: Não consegui subscrever num chat");
        }
    }

    public void visualizeNotifications(String chatId) {

        ChatFull chatFull = findById(chatId);

        if (chatFull != null) {
            List<Notification> ns = new ArrayList<>();
            Collections.copy(ns, chatFull.getNotifications());

            for (Notification n : ns) {
                try {
                    dialogServer.setAlreadyDelivered(n.getId());
                    chatFull.removeNotification(n);
                    chatFull.addMessage(n.getMessage());
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    System.out.println("ERRO MEU: Não consegui setar "
                            + "uma notificação como Delivered");
                }
            }
        }
    }

    public List<ChatFull> getChatsFull() {
        return Collections.unmodifiableList(chatsFull);
    }

    public DialogServer getDialogServer() {
        return dialogServer;
    }

    public void setDialogServer(DialogServer dialogServer) {
        this.dialogServer = dialogServer;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public List<Message> getOfflineMessages() {
        return Collections.unmodifiableList(offlineMessages);
    }

    public void removeOfflineMessage(Message m) {
        offlineMessages.remove(m);
    }
}
