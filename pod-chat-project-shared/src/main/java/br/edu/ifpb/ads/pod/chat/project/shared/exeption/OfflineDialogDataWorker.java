package br.edu.ifpb.ads.pod.chat.project.shared.exeption;

import java.rmi.RemoteException;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 05:38:08
 */
public class OfflineDialogDataWorker extends RemoteException {

    public OfflineDialogDataWorker() {
        super("All workers to dis Dialog Data aren't found");
    }
    
}
