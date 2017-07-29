package lk.cse13.www.uomwireless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends AppCompatActivity {
    private EditText indexbox;
    private EditText passwordbox;
    private Operations operations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        operations = new Operations();
        indexbox = (EditText) findViewById(R.id.indexbox);
        passwordbox = (EditText) findViewById(R.id.passwordbox);
        Button saveLoginButton = (Button) findViewById(R.id.save_button);
        Button clearAll = (Button) findViewById(R.id.clear_all);

        indexbox.setText(operations.readFromFile("ind"));
        passwordbox.setText(operations.readFromFile("psd"));

        saveLoginButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String index = indexbox.getText().toString();
                        String password = passwordbox.getText().toString();
                        operations.writeToFile(index,"ind");
                        operations.writeToFile(password,"psd");
                        finish();
                    }
                });

        clearAll.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        indexbox.setText("");
                        passwordbox.setText("");
                        operations.writeToFile("","ind");
                        operations.writeToFile("","psd");
                    }
                });
    }
}

