package com.anibug.smsmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.anibug.smsmanager.R;
import com.anibug.smsmanager.utils.ContentDisplayMode;
import com.anibug.smsmanager.utils.PreferenceConstants;
import com.anibug.smsmanager.utils.Session;

public class LandingActivity extends Activity {

    private SharedPreferences settings;
    private EditText inputName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.landing);

        settings = getSharedPreferences(PreferenceConstants.PREF_NAME, MODE_PRIVATE);
        boolean passwordRequired = settings.getBoolean(PreferenceConstants.PREF_PASSWORD_REQUIRED, true);

        if (!passwordRequired) {
            showConversationList();
            finish();
            return;
        }

        inputName = (EditText) findViewById(R.id.landing_input_name);
        inputName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ok(textView);
                return true;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void ok(View v) {
        String password = inputName.getText().toString(); // Actually, it is password.
        inputName.getEditableText().clear();

        if (password.equals("demo")) {
            showDemo();
            return;
        }

        if (passwordMatched(password)) {
            showConversationList();
            return;
        }

        TextView greeting = (TextView) findViewById(R.id.landing_greeting);
        greeting.setText("Hello " + password + " !");
        return;
    }

    private void showDemo() {
        Intent intent = new Intent(this, ConversationListActivity.class);
        Session session = new Session();
        session.setDisplayMode(ContentDisplayMode.DEMO);
        intent.putExtra(Session.INTENT_KEY, session);
        startActivity(intent);
    }

    private void showConversationList() {
        Intent intent = new Intent(this, ConversationListActivity.class);
        startActivity(intent);
    }

    private boolean passwordMatched(String name) {
        settings = getSharedPreferences(PreferenceConstants.PREF_NAME, MODE_PRIVATE);
        String password = settings.getString(PreferenceConstants.PREF_PASSWORD, PreferenceConstants.DEFAULT_PASSWORD);

        return name.equals(password);
    }
}