package com.phantomhive.exil.hellopics.EdgetoEdgeFix;

import android.app.Activity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EdgeToEdgeFix {
    public static void EdgeToEdgeFixing(int id, Activity context){
        ViewCompat.setOnApplyWindowInsetsListener(context.findViewById(id), (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout()
            );
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
