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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 07:12:56
 */
public class DropBoxDataFacade implements DataFacade {

    private String DROPBOX_ACCOUNT_TOKEN = "";
    private DbxClientV2 dbClient;

    public DropBoxDataFacade() {
        //
        // Armazenar variaveis em Configs ...
        //
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        dbClient = new DbxClientV2(config, DROPBOX_ACCOUNT_TOKEN);
    }

    private Metadata getMetadataByFilename(String fileName) throws FileNotFoundException, DbxException {
        try {
            Metadata metadata = dbClient.files().getMetadata(File.separator + fileName);
            return metadata;
        } catch (GetMetadataErrorException ex) {
            throw new FileNotFoundException("The file "
                    + fileName + " doesn't exists!");
        }
    }

    @Override
    public boolean checkIfExistData(String fileName) {
        try {
            getMetadataByFilename(fileName);
        } catch (FileNotFoundException | DbxException ex) {
            return true;
        }
        return false;
    }

    @Override
    public void createIfNotExistData(String fileName) {
        if (!checkIfExistData(fileName)) {
            createANewFile(fileName, new Gson().toJson(new ArrayList<>()));
        }
    }

    @Override
    public void createANewFile(String fileName, String content) {
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
            getMetadataByFilename(oldFileName);
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
            getMetadataByFilename(fileName);
            InputStream inputStream = dbClient.files().download(File.separator + fileName)
                    .getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return new String(bytes);

        } catch (IOException | DbxException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }

}
