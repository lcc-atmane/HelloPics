package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets;

import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsSettings.PasswordSettings;
import com.phantomhive.exil.hellopics.R;

public class HiddenSecretsPasswordEnteringActivity extends AppCompatActivity {

    ImageButton GoBack;
    EditText hiddenSecretsPasswordeditText;
    PasswordSettings passwordSettings;
    TextView PasswordShowing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_secrets_password_entring);
        EdgeToEdgeFixing(R.id.activity_hidden_secrets_password_entringL,this);

        GoBack = findViewById(R.id.GoBackFromPasswordEntering);
        hiddenSecretsPasswordeditText = findViewById(R.id.editTextTextHiddenSecretsPasswordEntering);
        PasswordShowing = findViewById(R.id.PasswordEnteringShowing);

        passwordSettings = new PasswordSettings(this);

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();

            }
        });

        hiddenSecretsPasswordeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PasswordShowing.setText("Your Password Is: \n"+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        hiddenSecretsPasswordeditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    try {
                        if (v.getText().toString().equals(passwordSettings.getPassword())){
                            Intent PasswordEntering = new Intent(HiddenSecretsPasswordEnteringActivity.this, HiddenSecretsImagesListActivity.class);
                            startActivity(PasswordEntering);
                            finish();
                        }else {
                            Toast.makeText(HiddenSecretsPasswordEnteringActivity.this, "The Password is Wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                    return true;
                }
                return false;
            }
        });
    }
}