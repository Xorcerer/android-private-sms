package com.anibug.smsmanager.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.anibug.smsmanager.R;
import com.anibug.smsmanager.Utils;

public class PreferencesActivity extends Activity {
    private SharedPreferences settings;

    private CheckBox passwordRequiredCheckBox;
    private EditText passwordEdit;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        settings = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
        editor = settings.edit();
        passwordRequiredCheckBox = (CheckBox) findViewById(R.id.password_required);
        passwordEdit = (EditText) findViewById(R.id.password_input);

        boolean passwordRequired = settings.getBoolean(Utils.PREF_PASSWORD_REQUIRED, true);
        String password = settings.getString(Utils.PREF_PASSWORD, Utils.DEFAULT_PASSWORD);

        passwordRequiredCheckBox.setChecked(passwordRequired);
        passwordEdit.setEnabled(passwordRequired);
        passwordEdit.setText(password);
    }

    private void save() {
        editor.putString(Utils.PREF_PASSWORD, passwordEdit.getText().toString());
        editor.commit();
    }

    @Override
    protected void onPause() {
        save();
        super.onPause();
    }

    public void onPasswordRequiredClicked(View v) {
        boolean checked = passwordRequiredCheckBox.isChecked();
        editor.putBoolean(Utils.PREF_PASSWORD_REQUIRED, checked);
        passwordEdit.setEnabled(checked);
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }
}
