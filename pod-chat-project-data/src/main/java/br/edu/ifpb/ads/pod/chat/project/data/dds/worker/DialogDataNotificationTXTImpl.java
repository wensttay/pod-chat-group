package br.edu.ifpb.ads.pod.chat.project.data.dds.worker;

import br.edu.ifpb.ads.pod.chat.project.data.datafacade.DropBoxDataFacade;
import br.edu.ifpb.ads.pod.chat.project.data.datafacade.TxtDataFacade;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataNotification;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import br.edu.ifpb.ads.pod.chat.project.shared.exeption.NotPreparedTransationExeption;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 04:46:25
 */
public class DialogDataNotificationTXTImpl implements DialogDataNotification {

    private List<Notification> notificationsTransational;
    private boolean inTransation;
    private TxtDataFacade tdf;
    private Gson gson;
    //
    // Colocar informações no CONFIGS
    //
    private String fileName = "notifications.txt";

    public DialogDataNotificationTXTImpl(TxtDataFacade tdf) {
        notificationsTransational = new ArrayList<>();
        inTransation = false;
        this.tdf = tdf;
        gson = new Gson();
    }

    @Override
    public void removeByUser(String userId) throws RemoteException {
        if (inTransation) {
            notificationsTransational.remove(new Notification(userId));
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public List<Notification> listNotificationByUser(String userId) throws RemoteException {
        try {
            String json = tdf.getContent(fileName);
            List<Notification> ns = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());

            List<Notification> nsUser = new ArrayList<>();
            for (Notification notification : nsUser) {
                if (notification.getMessage().getUserFromId().equals(userId)) {
                    nsUser.add(notification);
                }
            }
            return nsUser;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Notification> listNotificationByUserChat(String userId, String chatId) throws RemoteException {

        try {
            String json = tdf.getContent(fileName);
            List<Notification> ns = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());

            List<Notification> nsUser = new ArrayList<>();
            for (Notification notification : nsUser) {
                if (notification.getMessage().getUserFromId().equals(userId)
                        && notification.getMessage().getChatId().equals(chatId)) {
                    nsUser.add(notification);
                }
            }
            return nsUser;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setAlreadyDelivered(String notificationId) throws RemoteException {
        if (inTransation) {
            for (Notification notification : notificationsTransational) {
                if (notification.getId().equals(notificationId)) {
                    notification.setDelivered(true);
                    break;
                }
            }
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public Notification persist(Notification object) throws RemoteException {
        if (inTransation) {
            notificationsTransational.add(object);
            return object;
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public Notification find(String id) throws RemoteException {
        try {
            String json = tdf.getContent(fileName);
            List<Notification> ns = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());

            for (Notification notification : ns) {
                if (notification.getId().equals(id)) {
                    return notification;
                }
            }

        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }

        return null;
    }

    @Override
    public void remove(String id) throws RemoteException {
        if (inTransation) {
            notificationsTransational.remove(new Notification(id));
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public List<Notification> listAll() throws RemoteException {
        try {
            String json = tdf.getContent(fileName);
            List<Notification> ns = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            return ns;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public void prepare() throws RemoteException {
        notificationsTransational = listAll();
        inTransation = true;
    }

    @Override
    public void commit() throws RemoteException {
        if (inTransation) {
            try {
                tdf.updateMetada(fileName, gson.toJson(notificationsTransational));
                inTransation = false;
            } catch (FileNotFoundException ex) {
                throw new RemoteException(ex.getMessage(), ex);
            }
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public void rollback() throws RemoteException {
        if (inTransation) {
            notificationsTransational = new ArrayList<>();
            inTransation = false;
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

}
