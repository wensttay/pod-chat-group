package br.edu.ifpb.ads.pod.chat.project.data.dds.worker;

import br.edu.ifpb.ads.pod.chat.project.data.datafacade.GoogleDriveDataFacade;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataMessage;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
import br.edu.ifpb.ads.pod.chat.project.shared.exeption.NotPreparedTransationExeption;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 04:54:30
 */
public class DialogDataMessageGDImpl implements DialogDataMessage {

    private List<Message> messagesTransational;
    private boolean inTransation;
    private GoogleDriveDataFacade gddf;
    private Gson gson;

    private String fileName = "messages.txt";

    public DialogDataMessageGDImpl(GoogleDriveDataFacade gddf) {
        messagesTransational = new ArrayList<>();
        inTransation = false;
        gson = new Gson();
        this.gddf = gddf;
    }

    @Override
    public void removeByUser(String userId) throws RemoteException {
        if (inTransation) {
            List<Message> aux = new ArrayList<>();
            Collections.copy(aux, messagesTransational);
            for (Message message : aux) {
                if (message.getUserFromId().equals(userId)) {
                    messagesTransational.remove(message);
                }
            }
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public List<Message> listMessageByUser(String userId) throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<Message> msgs = gson.fromJson(json, new TypeToken<ArrayList<Message>>() {
            }.getType());
            List<Message> messByUser = new ArrayList<>();

            for (Message msg : msgs) {
                if (msg.getUserFromId().equals(userId)) {
                    messByUser.add(msg);
                }
            }

            return messByUser;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Message> listMessageByUserChat(String userId, String chatId) throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<Message> msgs = gson.fromJson(json, new TypeToken<ArrayList<Message>>() {
            }.getType());
            List<Message> messByUser = new ArrayList<>();

            for (Message msg : msgs) {
                if (msg.getUserFromId().equals(userId) && msg.getChatId().equals(chatId)) {
                    messByUser.add(msg);
                }
            }

            return messByUser;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public Message persist(Message object) throws RemoteException {
        if (inTransation) {
            messagesTransational.add(object);
            return object;
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public Message find(String id) throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<Message> msgs = gson.fromJson(json, new TypeToken<ArrayList<Message>>() {
            }.getType());

            for (Message msg : msgs) {
                if (msg.getId().equals(id)) {
                    return msg;
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
            messagesTransational.remove(new Message(id));
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public List<Message> listAll() throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<Message> msgs = gson.fromJson(json, new TypeToken<ArrayList<Message>>() {
            }.getType());

            return msgs;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public void prepare() throws RemoteException {
        messagesTransational = listAll();
        inTransation = true;
    }

    @Override
    public void commit() throws RemoteException {
        if (inTransation) {
            try {
                gddf.updateMetada(fileName, gson.toJson(messagesTransational));

                inTransation = false;
                messagesTransational = new ArrayList<>();
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
            inTransation = false;
            messagesTransational = new ArrayList<>();
        }
    }

}
