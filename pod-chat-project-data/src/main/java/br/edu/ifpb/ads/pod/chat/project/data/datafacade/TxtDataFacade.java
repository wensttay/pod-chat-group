package br.edu.ifpb.ads.pod.chat.project.data.datafacade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 08:01:55
 */
public class TxtDataFacade implements DataFacade {

    private File file;
    private String filePath;

    public TxtDataFacade() {
        this.filePath = System.getProperty("user.dir");
        initService();
    }

    private void initService() {
        file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public boolean checkIfExistData(String fileName) {
        return new File(geratePathByName(fileName)).exists();
    }

    @Override
    public void createANewFile(String fileName, String content) throws FileNotFoundException {
        
        createIfNotExistData(fileName);
        try {
            File newNotification = new File(geratePathByName(fileName));
            newNotification.createNewFile();
            FileOutputStream fos = new FileOutputStream(newNotification);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileNotFoundException();
        }

    }

    @Override
    public void createIfNotExistData(String fileName) throws FileNotFoundException {
        if(!checkIfExistData(fileName)){
            new File(geratePathByName(fileName)).mkdirs();
        }
    }

    @Override
    public void updateMetada(String oldFileName, String newContent) throws FileNotFoundException {
        if (checkIfExistData(oldFileName)) {
            new File(geratePathByName(oldFileName)).delete();
        }

        createANewFile(oldFileName, newContent);
    }

    @Override
    public String getContent(String fileName) throws FileNotFoundException {
        try {
            FileInputStream fis = new FileInputStream(geratePathByName(fileName));
            byte[] bytes = IOUtils.toByteArray(fis);
            return new String(bytes);
        } catch (IOException ex) {
            throw new FileNotFoundException();
        }
        
    }
    
    private String geratePathByName(String fileName){
        return filePath + File.separator + fileName;
    }

}
