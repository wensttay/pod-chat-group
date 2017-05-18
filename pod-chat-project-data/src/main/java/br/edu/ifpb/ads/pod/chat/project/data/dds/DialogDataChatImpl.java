package br.edu.ifpb.ads.pod.chat.project.data.dds;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataChat;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Chat;
import br.edu.ifpb.ads.pod.chat.project.shared.exeption.FailToPrepareTransationException;
import br.edu.ifpb.ads.pod.chat.project.shared.exeption.OfflineDialogDataWorker;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 04:25:31
 */
public class DialogDataChatImpl extends UnicastRemoteObject implements DialogDataChat {

    private Map<String, DialogDataChat> dialogDataChats;

    public DialogDataChatImpl() throws RemoteException {
        dialogDataChats = new HashMap<>();
    }

    @Override
    public List<String> listChatAllIds() throws RemoteException {

        for (DialogDataChat value : dialogDataChats.values()) {
            try {
                return value.listChatAllIds();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Exeption duranting listChat");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public List<String> listChatByUser(String userId) throws RemoteException {

        for (DialogDataChat value : dialogDataChats.values()) {
            try {
                return value.listChatByUser(userId);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Exeption duranting listChatByUser");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public boolean subscribe(String userId, String chatId) throws RemoteException {

        prepare();
        try {
            for (DialogDataChat value : dialogDataChats.values()) {
                value.subscribe(userId, chatId);
            }
            commit();
        } catch (Exception ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Exeption duranting subscribe a User on Chat");
            return false;
        }

        return true;
    }

    @Override
    public void unsubscribe(String userId, String chatId) throws RemoteException {

        prepare();
        try {
            for (DialogDataChat value : dialogDataChats.values()) {
                value.unsubscribe(userId, chatId);
            }
            commit();
        } catch (Exception ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Exeption duranting unsubcribe a User on Chat");
        }
    }

    @Override
    public Chat persist(Chat object) throws RemoteException {

        prepare();
        Chat chat = null;
        try {
            for (DialogDataChat value : dialogDataChats.values()) {
                chat = value.persist(object);
            }
            commit();
        } catch (Exception ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Exeption duranting unsubcribe a User on Chat");
            return null;
        }

        return chat;
    }

    @Override
    public Chat find(String id) throws RemoteException {

        for (DialogDataChat value : dialogDataChats.values()) {
            try {
                return value.find(id);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Exeption duranting listChat");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void remove(String id) throws RemoteException {

        prepare();
        try {
            for (DialogDataChat value : dialogDataChats.values()) {
                value.remove(id);
            }
            commit();
        } catch (Exception ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Exeption duranting unsubcribe a User on Chat");
        }

    }

    @Override
    public List<Chat> listAll() throws RemoteException {

        for (DialogDataChat value : dialogDataChats.values()) {
            try {
                return value.listAll();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Exeption duranting listChat");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void unsubscribeByUser(String id) throws RemoteException {
        prepare();
        try {
            for (DialogDataChat value : dialogDataChats.values()) {
                value.unsubscribeByUser(id);
            }
            commit();
        } catch (Exception ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Exeption duranting unsubcribe a User on Chat");
        }
    }

    @Override
    public void prepare() throws RemoteException {
        try {
            for (DialogDataChat value : dialogDataChats.values()) {
                value.prepare();
            }
        } catch (RemoteException ex) {
            rollback();
            throw new FailToPrepareTransationException();
        }
    }

    @Override
    public void commit() throws RemoteException {
        for (DialogDataChat value : dialogDataChats.values()) {
            value.commit();
        }
    }

    @Override
    public void rollback() throws RemoteException {
        for (DialogDataChat value : dialogDataChats.values()) {
            value.rollback();
        }
    }

    public Map<String, DialogDataChat> getDialogDataChats() {
        return Collections.unmodifiableMap(dialogDataChats);
    }

    public DialogDataChat addDialogDataChat(String key, DialogDataChat dialogDataChat) {
        return dialogDataChats.put(key, dialogDataChat);
    }

    public DialogDataChat removeDialogDataChat(String key) {
        return dialogDataChats.remove(key);
    }
}
