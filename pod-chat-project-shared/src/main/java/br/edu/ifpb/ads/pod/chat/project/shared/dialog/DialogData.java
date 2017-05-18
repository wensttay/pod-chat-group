/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.ads.pod.chat.project.shared.dialog;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 19:01:05
 */
public interface DialogData<T extends Serializable> extends Remote, Transationable {
    
    public T persist(T object) 
            throws RemoteException;

    public T find(String id) 
            throws RemoteException;

    public void remove(String id) 
            throws RemoteException;

    public List<T> listAll() 
            throws RemoteException;
    
}
