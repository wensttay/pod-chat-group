package br.edu.ifpb.ads.pod.chat.project.data.datafacade;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 07:12:56
 */
public class DropBoxDataFacade implements DataFacade {

    private String DROPBOX_ACCOUNT_TOKEN = "BsoOqz8TTFAAAAAAAAABj5uEVF6u0qXCiRWlJGRtUXFxVNgreaoBvMpo3XrKLiaq";
    private DbxClientV2 dbClient;

    public DropBoxDataFacade() {
        initService();
    }

    private void initService() {
        //
        // Colocar informações no CONFIGS
        //
        DbxRequestConfig config = new DbxRequestConfig("dropbox/pod-chat-project", "en_US");
        dbClient = new DbxClientV2(config, DROPBOX_ACCOUNT_TOKEN);
    }
    
    private Metadata getFileByName(String fileName) throws FileNotFoundException, DbxException { 
        try {
            return dbClient.files().getMetadata(File.separator + fileName);
        } catch (GetMetadataErrorException ex) {
            throw new FileNotFoundException("The file "
                    + fileName + " doesn't exists!");
        }
    }

    @Override
    public boolean checkIfExistData(String fileName) throws FileNotFoundException {
        try {
            getFileByName(fileName);
        } catch (FileNotFoundException | DbxException ex) {
            return true;
        }
        return false;
    }

    @Override
    public void createIfNotExistData(String fileName) throws FileNotFoundException {
        if (!checkIfExistData(fileName)) {
            createANewFile(fileName, new Gson().toJson(Collections.EMPTY_LIST));
        }
    }

    @Override
    public void createANewFile(String fileName, String content) throws FileNotFoundException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
            try {
                dbClient.files().uploadBuilder(File.separator + fileName)
                        .uploadAndFinish(inputStream);

            } catch (DbxException | IOException ex) {
                throw new DbxException(ex.getMessage());
            }
        } catch (DbxException e) {
            e.printStackTrace();
            System.out.println("Falha ao criar o arquivo de nome " + fileName + " no dropbox");
        }
    }

    @Override
    public void updateMetada(String oldFileName, String newContent) throws FileNotFoundException {
        ByteArrayInputStream inputStream
                = new ByteArrayInputStream(newContent.getBytes());
        try {
            getFileByName(oldFileName);
            dbClient.files()
                    .uploadBuilder(File.separator + oldFileName)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream);

        } catch (IOException | DbxException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }

    @Override
    public String getContent(String fileName) throws FileNotFoundException {
        try {
            getFileByName(fileName);
            InputStream inputStream = dbClient.files().download(File.separator + fileName)
                    .getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return new String(bytes);

        } catch (IOException | DbxException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }

}
