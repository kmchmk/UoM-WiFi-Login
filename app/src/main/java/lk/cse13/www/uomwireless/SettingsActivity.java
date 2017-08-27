package lk.cse13.www.uomwireless;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        notificationCheckBox.setChecked(preferences.getBoolean("notification_enabled", true));
        toastCheckBox.setChecked(preferences.getBoolean("toast_enabled", true));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.loginScreenShowing = false;
    }

    public void saveAndLogin(View view) {
        String index = indexbox.getText().toString();
        String password = passwordbox.getText().toString();

//        SharedPreferences settings = getSharedPreferences("index_password", MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("index", index);
//        editor.putString("password", password);
//        editor.commit();
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

    public void savePreferences(View v) {
        SharedPreferences settings = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("notification_enabled", notificationCheckBox.isChecked());
        editor.putBoolean("toast_enabled", toastCheckBox.isChecked());
        editor.commit();
    }
}

