/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifpb.ads.pod.chat.project.shared.dialog;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 22:19:52
 */
public interface Transationable {
    
    public void prepare() 
            throws RemoteException;
    
    public void commit() 
            throws RemoteException;
    
    public void rollback() 
            throws RemoteException;
}
