package br.edu.ifpb.ads.pod.chat.project.shared.exeption;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 18:24:07
 */
public class AlreadyRegistredUserExeption extends RemoteException {

    public AlreadyRegistredUserExeption() {
        super("This login are already in use, please try another.");
    }
    
}
