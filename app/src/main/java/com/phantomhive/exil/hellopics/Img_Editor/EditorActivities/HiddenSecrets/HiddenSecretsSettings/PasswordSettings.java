package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsSettings;

import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import android.content.Context;
import android.util.Log;

import java.security.KeyStore;


public class PasswordSettings {
    private static final String PREFS_NAME = "secure_prefs";
    private static final String KEY_PASSWORD = "user_password";
    private static final String MASTER_KEY_ALIAS = "master_key";

    private Context context;
    private MasterKey masterKey;
    private SharedPreferences encryptedPrefs;

    public PasswordSettings(Context context) {
        this.context = context;
        initializeEncryption();
    }

    private void initializeEncryption() {
        try {
            // Create master key with specific alias for better key persistence
            masterKey = new MasterKey.Builder(context, MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .setRequestStrongBoxBacked(true) // For better security on supported devices
                    .build();

            encryptedPrefs = EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e("PasswordSettings", "Error initializing encryption", e);
            // Reset everything if encryption fails
            clearPassword();
        }
    }

    public void savePassword(String password) {
        try {
            if (encryptedPrefs == null) {
                initializeEncryption();
            }
            encryptedPrefs.edit().putString(KEY_PASSWORD, password).apply();
        } catch (Exception e) {
            Log.e("PasswordSettings", "Error saving password", e);
            clearPassword();
            // Retry initialization and save once
            try {
                initializeEncryption();
                encryptedPrefs.edit().putString(KEY_PASSWORD, password).apply();
            } catch (Exception retryError) {
                Log.e("PasswordSettings", "Error on retry save", retryError);
            }
        }
    }

    public String getPassword() {
        try {
            if (encryptedPrefs == null) {
                initializeEncryption();
            }
            return encryptedPrefs.getString(KEY_PASSWORD, null);
        } catch (Exception e) {
            Log.e("PasswordSettings", "Error getting password", e);
            clearPassword();
            return null;
        }
    }

    public void clearPassword() {
        try {
            // Clear both regular and encrypted prefs to ensure complete cleanup
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Delete the master key if it exists
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            keyStore.deleteEntry(MASTER_KEY_ALIAS);

            // Reinitialize encryption
            initializeEncryption();
        } catch (Exception e) {
            Log.e("PasswordSettings", "Error clearing password", e);
        }
    }
}