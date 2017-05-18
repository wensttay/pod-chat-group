package br.edu.ifpb.ads.pod.chat.project.data.dds;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataNotification;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
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
 * @date 18/05/2017, 04:26:03
 */
public class DialogDataNotificationImpl extends UnicastRemoteObject implements DialogDataNotification {

    private Map<String, DialogDataNotification> dialogDataNotifications;

    public DialogDataNotificationImpl() throws RemoteException {
        dialogDataNotifications = new HashMap<>();
    }

    @Override
    public void removeByUser(String userId) throws RemoteException {
        prepare();
        try {
            for (DialogDataNotification value : dialogDataNotifications.values()) {
                value.removeByUser(userId);
            }
            commit();
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falha ao remover by user");
        }
    }

    @Override
    public List<Notification> listNotificationByUser(String userId) throws RemoteException {
        for (Map.Entry<String, DialogDataNotification> entry : dialogDataNotifications.entrySet()) {
            try {
                if (entry.getKey().equals("dropBox")) {
                    return entry.getValue().listNotificationByUser(userId);
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ao listar notificações by user");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public List<Notification> listNotificationByUserChat(String userId, String chatId) throws RemoteException {
        for (Map.Entry<String, DialogDataNotification> entry : dialogDataNotifications.entrySet()) {
            try {
                if (entry.getKey().equals("dropBox")) {
                    return entry.getValue().listNotificationByUserChat(userId, chatId);
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ao listar notificações by user chat");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void setAlreadyDelivered(String notificationId) throws RemoteException {
        prepare();
        try {
            for (DialogDataNotification value : dialogDataNotifications.values()) {
                value.setAlreadyDelivered(notificationId);
            }
            commit();
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falha ao setar como already delivered uma notificacao");
        }
    }

    @Override
    public Notification persist(Notification object) throws RemoteException {
        prepare();
        Notification notification = null;
        try {
            for (DialogDataNotification value : dialogDataNotifications.values()) {
                notification = value.persist(object);
            }
            commit();
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falha persistir uma notificacao");
            return null;
        }

        return notification;
    }

    @Override
    public Notification find(String id) throws RemoteException {
        for (Map.Entry<String, DialogDataNotification> entry : dialogDataNotifications.entrySet()) {
            try {
                if (entry.getKey().equals("dropBox")) {
                    return entry.getValue().find(id);
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ao find notification");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void remove(String id) throws RemoteException {
        prepare();
        try {
            for (DialogDataNotification value : dialogDataNotifications.values()) {
                value.remove(id);
            }
            commit();
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falha remover uma notificacao");
        }
    }

    @Override
    public List<Notification> listAll() throws RemoteException {
        for (Map.Entry<String, DialogDataNotification> entry : dialogDataNotifications.entrySet()) {
            try {
                if (entry.getKey().equals("dropBox")) {
                    return entry.getValue().listAll();
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ao listar all");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void prepare() throws RemoteException {
        try {
            for (DialogDataNotification value : dialogDataNotifications.values()) {
                value.prepare();
            }
        } catch (RemoteException ex) {
            rollback();
            throw new FailToPrepareTransationException();
        }
    }

    @Override
    public void commit() throws RemoteException {
        for (DialogDataNotification value : dialogDataNotifications.values()) {
            value.commit();
        }
    }

    @Override
    public void rollback() throws RemoteException {
        for (DialogDataNotification value : dialogDataNotifications.values()) {
            value.rollback();
        }
    }

    public Map<String, DialogDataNotification> getDialogDataMessages() {
        return Collections.unmodifiableMap(dialogDataNotifications);
    }

    public DialogDataNotification addDialogDataChat(String key, DialogDataNotification dialogDataNotification) {
        return dialogDataNotifications.put(key, dialogDataNotification);
    }

    public DialogDataNotification removeDialogDataChat(String key) {
        return dialogDataNotifications.remove(key);
    }

}
