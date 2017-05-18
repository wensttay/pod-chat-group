package br.edu.ifpb.ads.pod.chat.project.data.dds.worker;

import br.edu.ifpb.ads.pod.chat.project.data.datafacade.GoogleDriveDataFacade;
import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogDataChat;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Chat;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
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
 * @date 18/05/2017, 04:47:21
 */
public class DialogDataChatGDImpl implements DialogDataChat {

    private List<Chat> chatsTransational;
    private boolean inTransation;
    private GoogleDriveDataFacade gddf;
    private Gson gson;
    //
    // Colocar informações no CONFIGS
    //
    private String fileName = "chats.txt";

    public DialogDataChatGDImpl(GoogleDriveDataFacade gddf) {
        chatsTransational = new ArrayList<>();
        inTransation = false;
        gson = new Gson();
        this.gddf = gddf;
    }

    @Override
    public List<String> listChatAllIds() throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<Chat> chats = gson.fromJson(json, new TypeToken<ArrayList<Chat>>() {
            }.getType());
            List<String> chatIds = new ArrayList<>();

            for (Chat chat : chats) {
                chatIds.add(chat.getName());
            }
            return chatIds;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<String> listChatByUser(String userId) throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<Chat> chats = gson.fromJson(json, new TypeToken<ArrayList<Chat>>() {
            }.getType());
            List<String> chatIds = new ArrayList<>();

            for (Chat chat : chats) {
                if (chat.getUsers().contains(new User(userId, ""))) {
                    chatIds.add(chat.getName());
                }
            }

            return chatIds;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean subscribe(String userId, String chatId) throws RemoteException {
        if (inTransation) {
            for (Chat chat : chatsTransational) {
                if(chat.getName().equals(chatId)){
                    return chat.addUser(new User(userId, ""));
                }
            }
        } else {
            throw new NotPreparedTransationExeption();
        }
        
        return false;
    }

    @Override
    public void unsubscribe(String userId, String chatId) throws RemoteException {
        if (inTransation) {
            for (Chat chat : chatsTransational) {
                if(chat.getName().equals(chatId)){
                    chat.removeUser(new User(userId, ""));
                    break;
                }
            }
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public Chat persist(Chat object) throws RemoteException {
        if (inTransation) {
            chatsTransational.add(object);
            return object;
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public Chat find(String id) throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            List<Chat> cs = gson.fromJson(json, new TypeToken<ArrayList<Chat>>() {
            }.getType());
            for (Chat c : cs) {
                if (c.getName().equals(id)) {
                    return c;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage(), ex);
        }
        
        return null;
    }

    @Override
    public void remove(String id) throws RemoteException {
        if (inTransation) {
            chatsTransational.remove(new Chat(id));
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public List<Chat> listAll() throws RemoteException {
        try {
            String json = gddf.getContent(fileName);
            return gson.fromJson(json, new TypeToken<ArrayList<Chat>>() {
            }.getType());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public void unsubscribeByUser(String id) throws RemoteException {
        if (inTransation) {
            for (Chat chat : chatsTransational) {
                chat.removeUser(new User(id, ""));
            }
        } else {
            throw new NotPreparedTransationExeption();
        }
    }

    @Override
    public void prepare() throws RemoteException {
        chatsTransational = listAll();
        inTransation = true;
    }

    @Override
    public void commit() throws RemoteException {
        if (inTransation) {
            try {
                gddf.updateMetada(fileName, gson.toJson(chatsTransational));
                
                inTransation = false;
                chatsTransational = new ArrayList<>();
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
            chatsTransational = new ArrayList<>();
            inTransation = false;
        }
    }
}
