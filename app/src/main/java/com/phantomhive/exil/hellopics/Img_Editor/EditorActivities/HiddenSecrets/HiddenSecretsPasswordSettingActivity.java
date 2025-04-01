package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets;

import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsSettings.PasswordSettings;
import com.phantomhive.exil.hellopics.R;

public class HiddenSecretsPasswordSettingActivity extends AppCompatActivity {
    ImageButton GoBack;
    EditText hiddenSecretsPasswordeditText;
    PasswordSettings passwordSettings;
    TextView PasswordShowing;
    String Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_secrets_password_setting);
        EdgeToEdgeFixing(R.id.activity_hidden_secrets_password_settingL,this);

        GoBack = findViewById(R.id.GoBackFromPasswordSetting);
        hiddenSecretsPasswordeditText = findViewById(R.id.editTextTextHiddenSecretsPassword);
        PasswordShowing = findViewById(R.id.PasswordSettingShowing);

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

                    // Process the text
                    Password = v.getText().toString().trim();
                    if (Password.isEmpty()) {
                        Toast.makeText(HiddenSecretsPasswordSettingActivity.this, "Please choose a Password", Toast.LENGTH_SHORT).show();
                        return false;
                    }else if (Password.length()<5){
                        Toast.makeText(HiddenSecretsPasswordSettingActivity.this, "The Password need to contain more Than 5 characters", Toast.LENGTH_SHORT).show();
                        return false;
                    }else {
                        ShowConfirmationDialog();
                        return true;
                    }



                }
                return false;
            }
        });
    }

    public void ShowConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// Set the dialog title with a thick font style
        TextView title = new TextView(this);
        title.setText("Are You Sure");
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);

// Set the message under the title

        String TextMessage = "Your password is <font color='#A6A061'>" + Password + "</font> do you want to keep it";

        builder.setMessage(Html.fromHtml(TextMessage, Html.FROM_HTML_MODE_LEGACY));

// Set up OK and Cancel buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    passwordSettings.savePassword(Password);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                 getOnBackPressedDispatcher().onBackPressed();
/*
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent PasswordEntering = new Intent(HiddenSecretsPasswordSettingActivity.this, HomeActivity.class);
                startActivity(PasswordEntering);*/

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel action
                dialog.dismiss();
            }
        });

// Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}