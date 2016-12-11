package hesso.mas.stdhb.DataAccess.Communication.Handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.Client.Tools.SpinnerHandler;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;

/**
 * Created by chf on 10.12.2016.
 *
 * A Handler allows you to send and process Message and Runnable objects associated with a thread's
 * MessageQueue. Each Handler instance is associated with a single thread and that thread's message
 * queue. When you create a new Handler, it is bound to the thread / message queue of the thread
 * that is creating it -- from that point on, it will deliver messages and runnables to that message
 * queue and execute them as they come out of the message queue.
 */
public class SearchHandler extends Handler {

    private Spinner mSpinner;
    private Context mContext;

    public SearchHandler(Spinner aSpinner, Context aContext) {

        this.mSpinner = aSpinner;
        this.mContext = aContext;
    }

    /*
     * toute sous classe de la classe Handler doit redéfinir cette méthode.
     * cette méthode sera invoquée dans/par le ThreadCreateur.
     * Si ce Thread est le Thread UI c'est lui qui modifiera l'IHM ...
     * possèdent une file de Messages
     * connaissent/mémorisent (à l'instanciation) le Thread qui l'a créé (ThreadCreateur).
     *
     * @param aMessage
     */
    public void handleMessage(Message aMessage) {

        List<CitizenDbObject> lCityZenObjects =
                aMessage.getData().getParcelableArrayList("CityZen Data");

        List<String> lItems = new ArrayList<>();

        for (CitizenDbObject lObjet : lCityZenObjects) {
            lItems.add(lObjet.GetValue("subject"));
        }

        SpinnerHandler.fillComboSubject(
                mSpinner,
                mContext,
                android.R.layout.simple_spinner_item,
                lItems);
    }
}
