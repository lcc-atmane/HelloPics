/*
 * Copyright (C) 2025 HelloPics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
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
