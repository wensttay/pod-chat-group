package br.edu.ifpb.ads.pod.chat.project.server;

import static br.edu.ifpb.ads.pod.chat.project.shared.dialog.Configs.*;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataChat;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataMessage;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataNotification;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataUser;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 03:41:00
 */
public class Main {

    public static DialogServerImpl dialogServerImpl;
    public static DialogDataUser dialogDataUser;
    public static DialogDataChat dialogDataChat;
    public static DialogDataMessage dialogDataMessage;
    public static DialogDataNotification dialogDataNotification;

    public static void main(String[] args) {
        try {
            Registry registryData = LocateRegistry.getRegistry(PORT_DATA);
            dialogDataUser = (DialogDataUser) registryData.lookup(DIALOG_DATA_USER);
            dialogDataChat = (DialogDataChat) registryData.lookup(DIALOG_DATA_CHAT);
            dialogDataMessage = (DialogDataMessage) registryData.lookup(DIALOG_DATA_MESSAGE);
            dialogDataNotification = (DialogDataNotification) registryData.lookup(DIALOG_DATA_NOTIFICATION);

            dialogServerImpl = new DialogServerImpl(dialogDataUser,
                    dialogDataChat,
                    dialogDataMessage,
                    dialogDataNotification);
            
            Registry registryServer = LocateRegistry.getRegistry(PORT_SERVER);
            registryServer.bind(DIALOG_SERVER, dialogServerImpl);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        dialogServerImpl.notifyAllSubscribers();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        System.out.println("ERRO MEU: falha ao notificar todos usuarios");
                    }
                }
            }, 1000, 1000);
            
        } catch (RemoteException | NotBoundException | AlreadyBoundException ex) {
            ex.printStackTrace();
            System.out.println("ERRO MEU: falha ao iniciar o servidor.");
        }
    }
}
