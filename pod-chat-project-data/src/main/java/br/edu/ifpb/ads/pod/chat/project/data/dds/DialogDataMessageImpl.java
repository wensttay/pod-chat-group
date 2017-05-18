package br.edu.ifpb.ads.pod.chat.project.data.dds;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataMessage;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
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
 * @date 18/05/2017, 04:28:36
 */
public class DialogDataMessageImpl extends UnicastRemoteObject implements DialogDataMessage {

    private Map<String, DialogDataMessage> dialogDataMessages;

    public DialogDataMessageImpl() throws RemoteException {
        dialogDataMessages = new HashMap<>();
    }

    @Override
    public void removeByUser(String userId) throws RemoteException {
        prepare();
        try {
            for (DialogDataMessage value : dialogDataMessages.values()) {
                value.removeByUser(userId);
            }
            commit();
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falha ao tentar remover by user");
        }
    }

    @Override
    public List<Message> listMessageByUser(String userId) throws RemoteException {
        for (DialogDataMessage value : dialogDataMessages.values()) {
            try {
                return value.listMessageByUser(userId);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ou listar messagebyUser");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public List<Message> listMessageByUserChat(String userId, String chatId) throws RemoteException {
        for (DialogDataMessage value : dialogDataMessages.values()) {
            try {
                return value.listMessageByUserChat(userId, chatId);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ou listar messagebyUserChat");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public Message persist(Message object) throws RemoteException {
        prepare();
        Message message = null;
        try {
            for (DialogDataMessage value : dialogDataMessages.values()) {
                message = value.persist(object);
            }
            commit();
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falha ao persistir");
            return null;
        }
        return message;
    }

    @Override
    public Message find(String id) throws RemoteException {
        for (DialogDataMessage value : dialogDataMessages.values()) {
            try {
                return value.find(id);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ao find");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void remove(String id) throws RemoteException {
        prepare();
        try {
            for (DialogDataMessage value : dialogDataMessages.values()) {
                value.remove(id);
            }
            commit();
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falha ao tentar remover");
        }
    }

    @Override
    public List<Message> listAll() throws RemoteException {
        for (DialogDataMessage value : dialogDataMessages.values()) {
            try {
                return value.listAll();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falha ou listar messagebyUser");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void prepare() throws RemoteException {
        try {
            for (DialogDataMessage value : dialogDataMessages.values()) {
                value.prepare();
            }
        } catch (RemoteException ex) {
            rollback();
            throw new FailToPrepareTransationException();
        }
    }

    @Override
    public void commit() throws RemoteException {
        for (DialogDataMessage value : dialogDataMessages.values()) {
            value.commit();
        }
    }

    @Override
    public void rollback() throws RemoteException {
        for (DialogDataMessage value : dialogDataMessages.values()) {
            value.rollback();
        }
    }

    public Map<String, DialogDataMessage> getDialogDataMessages() {
        return Collections.unmodifiableMap(dialogDataMessages);
    }

    public DialogDataMessage addDialogDataChat(String key, DialogDataMessage dialogDataMessage) {
        return dialogDataMessages.put(key, dialogDataMessage);
    }

    public DialogDataMessage removeDialogDataChat(String key) {
        return dialogDataMessages.remove(key);
    }

}
