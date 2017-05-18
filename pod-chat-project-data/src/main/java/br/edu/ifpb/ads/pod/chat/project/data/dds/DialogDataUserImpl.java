package br.edu.ifpb.ads.pod.chat.project.data.dds;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataUser;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
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
 * @date 18/05/2017, 04:25:44
 */
public class DialogDataUserImpl extends UnicastRemoteObject implements DialogDataUser {

    private Map<String, DialogDataUser> dialogDataUsers;
    private DialogDataChatImpl dialogDataChatImpl;
    private DialogDataMessageImpl dialogDataMessageImpl;
    private DialogDataNotificationImpl dialogDataNotificationImpl;

    public DialogDataUserImpl() throws RemoteException {
        dialogDataUsers = new HashMap<>();
    }

    @Override
    public User login(String login, String password) throws RemoteException {

        for (DialogDataUser value : dialogDataUsers.values()) {
            try {
                return value.login(login, password);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falaha ao realizar login");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public User persist(User object) throws RemoteException {

        prepare();
        User user = null;
        try {
            for (DialogDataUser value : dialogDataUsers.values()) {
                user = value.persist(object);
            }
            commit();
        } catch (Exception ex) {
            rollback();
            ex.printStackTrace();
            System.out.println("Falaha ao persistir user");
            return null;
        }

        return user;
    }

    @Override
    public User find(String id) throws RemoteException {

        for (DialogDataUser value : dialogDataUsers.values()) {
            try {
                return value.find(id);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falaha ao realizar find");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void remove(String id) throws RemoteException {

        prepareAll();
        try {
            dialogDataNotificationImpl.removeByUser(id);
            dialogDataMessageImpl.removeByUser(id);
            dialogDataChatImpl.unsubscribeByUser(id);

            for (DialogDataUser value : dialogDataUsers.values()) {
                value.remove(id);
            }
            commitAll();
        } catch (Exception ex) {
            rollbackAll();
            ex.printStackTrace();
            System.out.println("Falha ao remover um user");
        }

    }

    @Override
    public List<User> listAll() throws RemoteException {
        for (DialogDataUser value : dialogDataUsers.values()) {
            try {
                return value.listAll();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Falaha ao realizar listall user");
            }
        }

        throw new OfflineDialogDataWorker();
    }

    @Override
    public void prepare() throws RemoteException {
        try {
            for (DialogDataUser value : dialogDataUsers.values()) {
                value.prepare();
            }
        } catch (RemoteException ex) {
            rollback();
            ex.printStackTrace();
            throw new FailToPrepareTransationException();
        }
    }

    public void prepareAll() throws RemoteException {
        try {
            dialogDataNotificationImpl.prepare();
            dialogDataMessageImpl.prepare();
            dialogDataChatImpl.prepare();

            for (DialogDataUser value : dialogDataUsers.values()) {
                value.prepare();
            }
        } catch (RemoteException ex) {
            rollbackAll();
            ex.printStackTrace();
            throw new FailToPrepareTransationException();
        }
    }

    @Override
    public void commit() throws RemoteException {
        for (DialogDataUser value : dialogDataUsers.values()) {
            value.commit();
        }
    }

    public void commitAll() throws RemoteException {
        dialogDataNotificationImpl.commit();
        dialogDataMessageImpl.commit();
        dialogDataChatImpl.commit();

        for (DialogDataUser value : dialogDataUsers.values()) {
            value.commit();
        }
    }

    @Override
    public void rollback() throws RemoteException {
        for (DialogDataUser value : dialogDataUsers.values()) {
            value.rollback();
        }
    }

    public void rollbackAll() throws RemoteException {
        dialogDataNotificationImpl.rollback();
        dialogDataMessageImpl.rollback();
        dialogDataChatImpl.rollback();

        for (DialogDataUser value : dialogDataUsers.values()) {
            value.rollback();
        }
    }

    public Map<String, DialogDataUser> getDialogDataMessages() {
        return Collections.unmodifiableMap(dialogDataUsers);
    }

    public DialogDataUser addDialogDataChat(String key, DialogDataUser dialogDataUser) {
        return dialogDataUsers.put(key, dialogDataUser);
    }

    public DialogDataUser removeDialogDataChat(String key) {
        return dialogDataUsers.remove(key);
    }

    public DialogDataChatImpl getDialogDataChatImpl() {
        return dialogDataChatImpl;
    }

    public void setDialogDataChatImpl(DialogDataChatImpl dialogDataChatImpl) {
        this.dialogDataChatImpl = dialogDataChatImpl;
    }

    public DialogDataMessageImpl getDialogDataMessageImpl() {
        return dialogDataMessageImpl;
    }

    public void setDialogDataMessageImpl(DialogDataMessageImpl dialogDataMessageImpl) {
        this.dialogDataMessageImpl = dialogDataMessageImpl;
    }

    public DialogDataNotificationImpl getDialogDataNotificationImpl() {
        return dialogDataNotificationImpl;
    }

    public void setDialogDataNotificationImpl(DialogDataNotificationImpl dialogDataNotificationImpl) {
        this.dialogDataNotificationImpl = dialogDataNotificationImpl;
    }

}
