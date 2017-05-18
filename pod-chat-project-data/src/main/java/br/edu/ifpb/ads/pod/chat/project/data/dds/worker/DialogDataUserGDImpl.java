package br.edu.ifpb.ads.pod.chat.project.data.dds.worker;

import br.edu.ifpb.ads.pod.chat.project.data.datafacade.GoogleDriveDataFacade;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataUser;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
import br.edu.ifpb.ads.pod.chat.project.shared.exeption.NotPreparedTransationExeption;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 04:55:31
 */
public class DialogDataUserGDImpl implements DialogDataUser {

    private List<User> usersTransational;
    private boolean inTransation;
    private GoogleDriveDataFacade gddf;
    private Gson gson;

    private String fileName = "users.txt";

    public DialogDataUserGDImpl(GoogleDriveDataFacade gddf) {
        usersTransational = new ArrayList<>();
        inTransation = false;
        gson = new Gson();
        this.gddf = gddf;
    }

    @Override
    public User login(String login, String password) throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<User> us = gson.fromJson(json, new TypeToken<ArrayList<User>>() {
            }.getType());

            for (User u : us) {
                if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                    return u;
                }
            }

        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public User persist(User object) throws RemoteException {

        if (inTransation) {
            usersTransational.add(object);
            return object;
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public User find(String id) throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<User> us = gson.fromJson(json, new TypeToken<ArrayList<User>>() {
            }.getType());

            for (User u : us) {
                if (u.getLogin().equals(id)) {
                    return u;
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
            usersTransational.remove(new User(id, ""));
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public List<User> listAll() throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<User> us = gson.fromJson(json, new TypeToken<ArrayList<User>>() {
            }.getType());

            return us;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public void prepare() throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<User> us = gson.fromJson(json, new TypeToken<ArrayList<User>>() {
            }.getType());
            usersTransational = us;
            inTransation = true;
        } catch (FileNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public void commit() throws RemoteException {
        if (inTransation) {
            try {
                gddf.updateMetada(fileName, gson.toJson(usersTransational));
                inTransation = false;
                usersTransational = new ArrayList<>();
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
            usersTransational = new ArrayList<>();
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

}
