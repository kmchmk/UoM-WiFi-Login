package lk.cse13.www.uomwireless;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


public class SettingsActivity extends AppCompatActivity {
    private EditText indexbox;
    private EditText passwordbox;
    private CheckBox notificationCheckBox;
    private CheckBox toastCheckBox;
    private CheckBox vibrationCheckBox;
    private CheckBox soundCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MainActivity.loginScreenShowing = true;

        indexbox = (EditText) findViewById(R.id.indexbox);
        passwordbox = (EditText) findViewById(R.id.passwordbox);

        indexbox.setText(Operations.readFromFile("username"));
        passwordbox.setText(Operations.readFromFile("password"));


        notificationCheckBox = (CheckBox) findViewById(R.id.enable_notification);
        toastCheckBox = (CheckBox) findViewById(R.id.enable_toast);
        vibrationCheckBox = (CheckBox) findViewById(R.id.enable_vibration);
        soundCheckBox = (CheckBox) findViewById(R.id.enable_sound);

        notificationCheckBox.setChecked(Operations.getPreferences("notification_enabled"));
        vibrationCheckBox.setChecked(Operations.getPreferences("vibration_enabled"));
        soundCheckBox.setChecked(Operations.getPreferences("sound_enabled"));
        toastCheckBox.setChecked(Operations.getPreferences("toast_enabled"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.loginScreenShowing = false;
    }

    public void saveAndLogin(View view) {
        String index = indexbox.getText().toString();
        String password = passwordbox.getText().toString();

        Operations.writeToFile(index, "username");
        Operations.writeToFile(password, "password");
        Operations.toast("Saved");
        MainActivity.loginScreenShowing = false;
        new BackgroundLogin(0).execute();
        finish();
    }

    public void clearAll(View view) {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        indexbox.setText("");
                        passwordbox.setText("");
//        getSharedPreferences("index_password", MODE_PRIVATE).edit().clear().commit();
                        Operations.writeToFile("", "username");
                        Operations.writeToFile("", "password");
//                        new BackgroundLogout().execute();
                        Operations.toast("Deleted");
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void saveNotificationEnabled(View v) {
        boolean isNotificationEnabled = notificationCheckBox.isChecked();
        Operations.savePreferences("notification_enabled", notificationCheckBox.isChecked());

        if (isNotificationEnabled) {
            vibrationCheckBox.setEnabled(true);
            soundCheckBox.setEnabled(true);
        } else {
            vibrationCheckBox.setEnabled(false);
            soundCheckBox.setEnabled(false);
        }


    }

    public void saveVibrationEnabled(View v) {
        Operations.savePreferences("vibration_enabled", vibrationCheckBox.isChecked());
    }

    public void saveSoundEnabled(View v) {
        Operations.savePreferences("sound_enabled", soundCheckBox.isChecked());
    }

    public void saveToastEnabled(View v) {
        Operations.savePreferences("toast_enabled", toastCheckBox.isChecked());
    }
}

