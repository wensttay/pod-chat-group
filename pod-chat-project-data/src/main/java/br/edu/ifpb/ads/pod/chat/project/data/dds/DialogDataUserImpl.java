package br.edu.ifpb.ads.pod.chat.project.data.dds;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataUser;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
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
    
    public DialogDataUserImpl() throws RemoteException {
        dialogDataUsers = new HashMap<>();
    }

    @Override
    public User login(String login, String password) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User persist(User object) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User find(String id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> listAll() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepare() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commit() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void callback() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
