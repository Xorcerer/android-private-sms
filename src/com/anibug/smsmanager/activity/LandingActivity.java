package com.anibug.smsmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.anibug.smsmanager.R;

public class LandingActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.landing);
    }

    public void ok(View v) {
        EditText inputName = (EditText) findViewById(R.id.landing_input_name);
        String name = inputName.getText().toString();
        inputName.getEditableText().clear();

        // FIXME: Make password configurable.
        if (!name.equals("password")) {
            TextView greeting = (TextView) findViewById(R.id.landing_greeting);
            greeting.setText("Hello " + name + " !");
            return;
        }

        Intent intent = new Intent(this, SmsManagerActivity.class);
        startActivity(intent);
    }
}