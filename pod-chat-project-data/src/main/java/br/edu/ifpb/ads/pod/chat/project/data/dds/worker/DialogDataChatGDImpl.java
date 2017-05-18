package br.edu.ifpb.ads.pod.chat.project.data.dds.worker;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataChat;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Chat;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 04:47:21
 */
public class DialogDataChatGDImpl implements DialogDataChat {

    @Override
    public List<String> listChatAllIds() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> listChatByUser(String userId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean subscribe(String userId, String chatId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsubscribe(String userId, String chatId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Chat persist(Chat object) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Chat find(String id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Chat> listAll() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsubscribeByUser(String id) throws RemoteException {
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
    public void rollback() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
