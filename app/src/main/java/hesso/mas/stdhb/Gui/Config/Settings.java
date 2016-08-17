package hesso.mas.stdhb.Gui.Config;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.widget.Button;

import hesso.mas.stdhbtests.R;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Add a button to the header list.
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Some action");
            setListFooter(button);
        }
    }
}
