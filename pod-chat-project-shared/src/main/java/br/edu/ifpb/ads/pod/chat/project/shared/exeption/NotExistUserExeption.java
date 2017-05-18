package br.edu.ifpb.ads.pod.chat.project.shared.exeption;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 18:28:29
 */
public class NotExistUserExeption extends RemoteException {

    public NotExistUserExeption(String s) {
        super(s);
    }
    
}
