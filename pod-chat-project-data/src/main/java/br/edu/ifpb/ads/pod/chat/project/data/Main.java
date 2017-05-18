package br.edu.ifpb.ads.pod.chat.project.data;

import br.edu.ifpb.ads.pod.chat.project.data.dds.DialogDataChatImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.DialogDataMessageImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.DialogDataNotificationImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.DialogDataUserImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.worker.DialogDataChatGDImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.worker.DialogDataMessageGDImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.worker.DialogDataNotificationDBImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.worker.DialogDataNotificationTXTImpl;
import br.edu.ifpb.ads.pod.chat.project.data.dds.worker.DialogDataUserGDImpl;
import static br.edu.ifpb.ads.pod.chat.project.shared.dialog.Configs.*;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataChat;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataMessage;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataNotification;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataUser;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 05:00:43
 */
public class Main {
   
    public static DialogDataUserImpl dialogDataUser;
    public static DialogDataChatImpl dialogDataChat;
    public static DialogDataMessageImpl dialogDataMessage;
    public static DialogDataNotificationImpl dialogDataNotification;
    
    public static void main(String[] args) {
        try {
            dialogDataUser = new DialogDataUserImpl();
            dialogDataChat = new DialogDataChatImpl();
            dialogDataMessage = new DialogDataMessageImpl();
            dialogDataNotification = new DialogDataNotificationImpl();
            
            DialogDataUser dataUserGD = new DialogDataUserGDImpl();
            DialogDataChat dataChatGD = new DialogDataChatGDImpl();
            DialogDataMessage dataMessageGD = new DialogDataMessageGDImpl();
            DialogDataNotification dataNotificationTXT = new DialogDataNotificationTXTImpl();
            DialogDataNotification dataNotificationDB = new DialogDataNotificationDBImpl();
           
            dialogDataUser.addDialogDataChat("googleDrive", dataUserGD);
            dialogDataChat.addDialogDataChat("googleDrive", dataChatGD);
            dialogDataMessage.addDialogDataChat("googleDrive", dataMessageGD);
            dialogDataNotification.addDialogDataChat("txt", dataNotificationTXT);
            dialogDataNotification.addDialogDataChat("dropBox", dataNotificationDB);
            
            dialogDataUser.setDialogDataChatImpl(dialogDataChat);
            dialogDataUser.setDialogDataMessageImpl(dialogDataMessage);
            dialogDataUser.setDialogDataNotificationImpl(dialogDataNotification);
            
            Registry registry = LocateRegistry.getRegistry(PORT_DATA);
            
            registry.bind(DIALOG_DATA_USER, dialogDataUser);
            registry.bind(DIALOG_DATA_CHAT, dialogDataChat);
            registry.bind(DIALOG_DATA_MESSAGE, dialogDataMessage);
            registry.bind(DIALOG_DATA_NOTIFICATION, dialogDataNotification);
            
        } catch (RemoteException | AlreadyBoundException ex) {
            ex.printStackTrace();
            System.out.println("ERRO MEU: falha o instanciar os diagos");
        }
        
    }

}
