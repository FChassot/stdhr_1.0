package hesso.mas.stdhb.Client.Gui.Search.Handler;

import android.os.Bundle;
import android.os.Message;

/**
 * Created by chf on 10.12.2016.
 *
 * Thread qui à envie de faire un traitement de longue durée et
 * d'interagire avec l'UI
 */
public class SearchThread extends Thread {

    // SearchThread connait mSearchHandler
    private SearchHandler mSearchHandler;

    public SearchThread(SearchHandler aSearchHandler) {
        this.mSearchHandler = aSearchHandler;
    }

    private boolean mContinuer = true;

    public void run() {
        Message lMessage = null;

        Bundle lBundle = new Bundle();

        int i=0;

        //while (mContinuer){

            // Permet d'obtenir du Handler un Message dans lequel on va «glisser» les informations
            // à transmettre (à la fonction handleMessage).
            lMessage = mSearchHandler.obtainMessage();

            lBundle.putString("clef", "i="+i);
            lMessage.setData(lBundle);

            // permet à un Thread partageant ce Handler (avec un autre Thread (E.g.
            // ThreadCreateur)) de déposer (FIFO) un Message dans la file de Messages.
            // Généralement cette méthode sera appelée à partir du run() (de cet autre
            //Thread).
            mSearchHandler.sendMessage(lMessage);
            //try { Thread.sleep(10000L); } catch (InterruptedException e) {}
            i++;
        //}
    }

}