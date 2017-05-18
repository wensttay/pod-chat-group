package br.edu.ifpb.ads.pod.chat.project.shared.dialog;

import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 17:57:38
 */
public interface DialogClient extends Remote {

    public void notify(Notification n)
            throws RemoteException;
    
    // To use when other people try a login with a logged account
    public boolean ping() 
            throws RemoteException; 
    
}
