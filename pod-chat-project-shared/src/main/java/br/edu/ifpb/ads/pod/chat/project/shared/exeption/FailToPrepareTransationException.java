package br.edu.ifpb.ads.pod.chat.project.shared.exeption;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 05:49:36
 */
public class FailToPrepareTransationException extends RemoteException {

    public FailToPrepareTransationException() {
        System.out.println("Fail to prepare a transation.");
    }

}
