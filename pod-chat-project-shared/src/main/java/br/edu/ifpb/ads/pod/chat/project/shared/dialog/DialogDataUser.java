/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.ads.pod.chat.project.shared.dialog;

import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 19:15:38
 */
public interface DialogDataUser extends DialogData<User> {

    public User login(String login, String password) 
            throws RemoteException;

}
