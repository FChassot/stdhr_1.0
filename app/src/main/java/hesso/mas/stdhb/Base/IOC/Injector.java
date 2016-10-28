package hesso.mas.stdhb.Base.IOC;

/**
 * Created by chf on 05.10.2016.
 */
public enum Injector {
    INSTANCE;

    /*ApplicationComponent applicationComponent;*/

    private Injector(){
    }

    void initializeApplicationComponent(CustomApplication aCustomApplication) {
        /*ApplicationComponent aCustomApplication = DaggerApplicationComponent.builder()
                .appContextModule(new AppContextModule(customApplication))
                .build();
        this.applicationComponent = applicationComponent;*/

    }

    /*public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }*/
}