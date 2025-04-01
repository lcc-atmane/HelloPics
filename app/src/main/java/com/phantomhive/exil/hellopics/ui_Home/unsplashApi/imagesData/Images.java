package com.phantomhive.exil.hellopics.ui_Home.unsplashApi.imagesData;

public class Images {
    String small;
    String regular;

    public Images(String small, String regular) {
        this.small = small;
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }
}
