package br.edu.ifpb.ads.pod.chat.project.shared.exeption;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 22:17:05
 */
public class NotPreparedTransationExeption extends RemoteException {

    public NotPreparedTransationExeption() {
        super("There are not prepared DialogData");
    }

}
