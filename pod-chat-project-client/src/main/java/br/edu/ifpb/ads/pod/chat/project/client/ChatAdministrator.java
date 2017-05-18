package br.edu.ifpb.ads.pod.chat.project.client;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogClient;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogServer;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 22:49:13
 */
public class ChatAdministrator implements DialogClient, Runnable {

    private final List<ChatFull> chatsFull;
    private final DialogServer dialogServer;
    private final User loggedUser;

    public ChatAdministrator(DialogServer dialogServer, User loggedUser) throws RemoteException {
        this.dialogServer = dialogServer;
        this.loggedUser = loggedUser;
        chatsFull = new ArrayList<>();
        reloadChatsFull();
    }

    public ChatFull findById(String chatId) {
        for (ChatFull cf : chatsFull) {
            if (cf.getName().equals(chatId)) {
                return cf;
            }
        }
        return null;
    }

    private void reloadChatsFull() throws RemoteException {
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

    @Override
    public void notify(Notification n) throws RemoteException {

        ChatFull chatFull = findById(n.getMessage().getChatId());

        if (chatFull != null) {
            chatFull.addNotification(n);
        }

    }

    @Override
    public boolean ping() throws RemoteException {
        // Only to test connection already registred
        return true;
    }

    @Override
    public void run() {
        try {
            reloadChatsFull();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.out.println("ERRO MEU: AO DAR RELOAD NOS CHATS");
        }
    }

}
