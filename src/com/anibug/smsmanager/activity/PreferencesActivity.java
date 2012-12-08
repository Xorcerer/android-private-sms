package com.anibug.smsmanager.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.anibug.smsmanager.R;
import com.anibug.smsmanager.utils.PreferenceConstants;

public class PreferencesActivity extends Activity {

    private CheckBox passwordRequiredCheckBox;
    private EditText passwordEdit;
    private EditText vibrationDurationEdit;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        SharedPreferences settings = getSharedPreferences(PreferenceConstants.PREF_NAME, MODE_PRIVATE);
        editor = settings.edit();

        passwordRequiredCheckBox = (CheckBox) findViewById(R.id.password_required);
        passwordEdit = (EditText) findViewById(R.id.password_input);
        vibrationDurationEdit = (EditText) findViewById(R.id.vibration_duration_input);


        boolean passwordRequired = settings.getBoolean(PreferenceConstants.PREF_PASSWORD_REQUIRED, true);
        String password = settings.getString(PreferenceConstants.PREF_PASSWORD, PreferenceConstants.DEFAULT_PASSWORD);
        int vibrationDuration = settings.getInt(PreferenceConstants.PREF_VIBRATION_DURATION,
                PreferenceConstants.DEFAULT_VIBRATION_DURATION);

        passwordRequiredCheckBox.setChecked(passwordRequired);
        passwordEdit.setEnabled(passwordRequired);
        passwordEdit.setText(password);

        vibrationDurationEdit.setText(String.valueOf(vibrationDuration));
    }

    private void save() {
        editor.putString(PreferenceConstants.PREF_PASSWORD, passwordEdit.getText().toString());

        int duration = 0;
        try {
            duration = Integer.parseInt(vibrationDurationEdit.getText().toString());
        }
        catch (NumberFormatException e)
        {
            Log.d("pref", e.getMessage());
        }
        editor.putInt(PreferenceConstants.PREF_VIBRATION_DURATION, duration);
        editor.commit();
    }

    @Override
    protected void onPause() {
        save();
        super.onPause();
    }

    public void onPasswordRequiredClicked(View v) {
        boolean checked = passwordRequiredCheckBox.isChecked();
        editor.putBoolean(PreferenceConstants.PREF_PASSWORD_REQUIRED, checked);
        passwordEdit.setEnabled(checked);
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }
}
