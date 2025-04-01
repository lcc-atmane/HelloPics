/*
 * Copyright (C) 2024 HelloPics
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0
 * International License. You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at:
 *     https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * You are free to share and adapt this work, provided that you give appropriate credit,
 * indicate if changes were made, and distribute your contributions under the same license.
 * However, you may not use this work for commercial purposes.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.phantomhive.exil.hellopics.R;
public class SeekbarValueSettings {
    int Value;

    public  void OnValueManuelEdit(TextView textView, Context context, SeekBar seekBar){
        ShowEditText(textView,context,seekBar);
    }

    private void ShowEditText(TextView textView, Context context, SeekBar seekBar){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.seekbaredittext, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(textView.getText());
        builder.setView(popupView);

        // Find buttons in the custom layout
        EditText button1 = popupView.findViewById(R.id.edit);
        button1.setHint(textView.getText());
        button1.setGravity(Gravity.CENTER);
        button1.setInputType(InputType.TYPE_CLASS_NUMBER);
        button1.setText(String.valueOf(seekBar.getProgress()));
        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {
            String v = button1.getText().toString().trim();
            seekBar.setProgress(Integer.parseInt(v));
            Value = Integer.parseInt(v);
            dialog.cancel();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.setCancelable(true);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public int getValue() {
        return Value;
    }
}
