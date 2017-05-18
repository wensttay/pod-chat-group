/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.ads.pod.chat.project.shared.dialog;

import br.edu.ifpb.ads.pod.chat.project.shared.entity.Chat;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 19:27:37
 */
public interface DialogDataChat extends DialogData<Chat> {

    public List<Chat> listByUser(String userId) 
            throws RemoteException;

    public boolean subscribe(String userId, String chatId) 
            throws RemoteException;

    public void unsubscribe(String userId) 
            throws RemoteException;
}
