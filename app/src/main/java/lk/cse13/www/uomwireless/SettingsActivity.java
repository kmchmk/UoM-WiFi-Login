package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends AppCompatActivity {
    private EditText indexbox;
    private EditText passwordbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        indexbox = (EditText) findViewById(R.id.indexbox);
        passwordbox = (EditText) findViewById(R.id.passwordbox);
        SharedPreferences settings = getSharedPreferences("index_password", MODE_PRIVATE);
        indexbox.setText(settings.getString("index", ""));
        passwordbox.setText(settings.getString("password", ""));
        Log.i("qqq", Integer.toString(MODE_PRIVATE));
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
}

