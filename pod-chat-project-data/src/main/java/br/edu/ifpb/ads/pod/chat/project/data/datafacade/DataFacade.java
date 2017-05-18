/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.ads.pod.chat.project.data.datafacade;

import java.io.FileNotFoundException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 07:28:30
 */
public interface DataFacade{
    
    public boolean checkIfExistData(String fileName) throws FileNotFoundException;

    public void createANewFile(String fileName, String content) throws FileNotFoundException;
    
    public void createIfNotExistData(String fileName) throws FileNotFoundException;
    
    public void updateMetada(String oldFileName, String newContent) throws FileNotFoundException;
    
    public String getContent(String fileName) throws FileNotFoundException;
}
