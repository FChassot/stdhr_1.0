package hesso.mas.stdhb.Client.Gui.Intro;

import hesso.mas.stdhb.Client.Gui.Radar.RadarActivity;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 16.12.2016.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * Created by chf on 11.06.2016.
 *
 * This is an introductions' activity
 */
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_intro);

        // Find the imageView that was identified by an id attribute
        ImageView imageView = (ImageView) findViewById(R.id.logo);
        imageView.setImageResource(R.mipmap.ic_logo2);

        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);

        imageView.startAnimation(myFadeInAnimation);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View detailsView = layoutInflater.inflate(R.layout.organisation_details_popup_layout, null);

                final PopupWindow popupWindow = new PopupWindow(detailsView, 1200, 900);

                Button dismissButton = (Button) detailsView.findViewById(R.id.dismiss_button);

                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        myFadeInAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(IntroActivity.this, RadarActivity.class);

                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
                System.exit(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        IntroActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
