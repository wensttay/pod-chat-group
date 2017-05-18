package br.edu.ifpb.ads.pod.chat.project.data.datafacade;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import com.google.api.services.drive.model.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 08:01:15
 */
public class GoogleDriveDataFacade implements DataFacade {

    private Drive googleDrive;
    private String mimeType;

    public GoogleDriveDataFacade() throws FileNotFoundException, IOException, GeneralSecurityException {
        this.mimeType = "text/json";
        initService();
    }

    private void initService() throws FileNotFoundException, IOException, GeneralSecurityException {
        //
        // Colocar informações no CONFIGS
        //
        FileInputStream input = getResource("google/pod-chat-project.json");
        GoogleCredential credentials = GoogleCredential
                .fromStream(input)
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/drive.file"));

        googleDrive = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                new JacksonFactory(), credentials)
                .setApplicationName("pod-chat-project")
                .build();
    }

    private FileInputStream getResource(String path) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(path);
        java.io.File file = new java.io.File(url.getFile());
        return new FileInputStream(file);
    }

    private File getFileByName(String fileName) throws FileNotFoundException {
        try {

            String query = "name = \'" + fileName + "\'";
            Drive.Files.List list = googleDrive.files().list();
            FileList fileList = list.setQ(query).execute();
            List<File> files = fileList.getFiles();

            if (!files.isEmpty()) {
                return files.get(0);
            }

            throw new IOException();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileNotFoundException(ex.getMessage());
        }
    }

    @Override
    public boolean checkIfExistData(String fileName) throws FileNotFoundException {
        try {

            String query = "name = \'" + fileName + "\'";
            Drive.Files.List list = googleDrive.files().list();
            FileList fileList = list.setQ(query).execute();
            List<File> files = fileList.getFiles();

            if (files.isEmpty()) {
                return false;
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileNotFoundException(ex.getMessage());
        }
    }

    @Override
    public void createANewFile(String fileName, String content) throws FileNotFoundException {
        File file = new File();
        file.setName(fileName);
        file.setMimeType(mimeType);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        InputStreamContent inputContent = new InputStreamContent(mimeType, inputStream);

        try {
            googleDrive.files().create(file, inputContent).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileNotFoundException(ex.getMessage());
        }

    }

    @Override
    public void createIfNotExistData(String fileName) throws FileNotFoundException {
        if (!checkIfExistData(fileName)) {
            createANewFile(fileName, new Gson().toJson(Collections.EMPTY_LIST));
        }
    }

    @Override
    public void updateMetada(String oldFileName, String newContent) throws FileNotFoundException {
        try {
            File targetFile = getFileByName(oldFileName);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(newContent.getBytes());
            InputStreamContent inputStreamContent = new InputStreamContent(mimeType, inputStream);

            googleDrive.files()
                    .update(targetFile.getId(), null, inputStreamContent)
                    .execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileNotFoundException(ex.getMessage());
        }
    }

    @Override
    public String getContent(String fileName) throws FileNotFoundException {
        try {
            File file = getFileByName(fileName);

            if (file == null) {
                return "";
            }

            Drive.Files.Get got = googleDrive.files().get(file.getId());
            InputStream input = got.executeMediaAsInputStream();
            byte[] bytes = IOUtils.toByteArray(input);
            return new String(bytes);

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileNotFoundException(ex.getMessage());
        }
    }
}
