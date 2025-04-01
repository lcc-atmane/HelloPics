package com.phantomhive.exil.hellopics.Create_Art_Classes;

import android.content.Context;

public interface Image_Item_ClickListener {
    void OnChoosingTheMainPictureClick(String picturePath, Context PackageContext);
    void OnChoosingTheOneTimePictureClick(String picturePath, Context PackageContext);

}
