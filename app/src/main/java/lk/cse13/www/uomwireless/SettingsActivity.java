package lk.cse13.www.uomwireless;

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

        indexbox = (EditText) findViewById(R.id.indexbox);
        passwordbox = (EditText) findViewById(R.id.passwordbox);
        SharedPreferences settings = getSharedPreferences("index_password", MODE_PRIVATE);
        indexbox.setText(settings.getString("index", ""));
        passwordbox.setText(settings.getString("password", ""));


        notificationCheckBox = (CheckBox) findViewById(R.id.enable_notification);
        toastCheckBox = (CheckBox) findViewById(R.id.enable_toast);

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        notificationCheckBox.setChecked(preferences.getBoolean("notification_enabled", true));
        toastCheckBox.setChecked(preferences.getBoolean("toast_enabled", true));


    }

    public void save(View view) {
        String index = indexbox.getText().toString();
        String password = passwordbox.getText().toString();

        SharedPreferences settings = getSharedPreferences("index_password", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("index", index);
        editor.putString("password", password);
        editor.commit();
        finish();
    }

    public void clearAll(View view) {
        indexbox.setText("");
        passwordbox.setText("");
        getSharedPreferences("index_password", MODE_PRIVATE).edit().clear().commit();
    }

    public void savePreferences(View v) {
        SharedPreferences settings = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("notification_enabled", notificationCheckBox.isChecked());
        editor.putBoolean("toast_enabled", toastCheckBox.isChecked());
        editor.commit();
    }
}

