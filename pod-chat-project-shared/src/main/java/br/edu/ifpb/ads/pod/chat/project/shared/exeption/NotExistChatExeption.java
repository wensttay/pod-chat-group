package br.edu.ifpb.ads.pod.chat.project.shared.exeption;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 17/05/2017, 18:36:48
 */
public class NotExistChatExeption extends RemoteException {

    public NotExistChatExeption() {
        super("No exist any Chat with this id.");
    }

}
