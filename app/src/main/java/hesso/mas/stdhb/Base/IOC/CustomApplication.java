package hesso.mas.stdhb.Base.IOC;

import android.app.Application;

/**
 * Created by chf on 05.10.2016.
 */
public class CustomApplication
        extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.initializeApplicationComponent(this);
    }
}