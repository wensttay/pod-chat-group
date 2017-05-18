package br.edu.ifpb.ads.pod.chat.project.shared.exeption;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 18:34:54
 */
public class AlreadyLoggedUserExeption extends RemoteException {

    public AlreadyLoggedUserExeption() {
        super("This user are already logged from another section.");
    }

}
