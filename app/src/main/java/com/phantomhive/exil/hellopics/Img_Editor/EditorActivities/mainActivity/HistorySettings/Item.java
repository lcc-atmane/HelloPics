package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.HistorySettings;

import android.graphics.Bitmap;

public class Item {
        private Bitmap imageResId;
        private String text;

        public Item(Bitmap imageResId, String text) {
            this.imageResId = imageResId;
            this.text = text;
        }

        public Bitmap getImageResId() {
            return imageResId;
        }

        public String getText() {
            return text;
        }

}
