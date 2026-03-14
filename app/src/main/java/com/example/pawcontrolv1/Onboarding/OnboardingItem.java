package com.example.pawcontrolv1.Onboarding;

public class OnboardingItem {

    private final String titleNormal;   // e.g. "View "
    private final String titleOrange;   // e.g. "Live Map"
    private final String titleNormal2;  // e.g. " Locations" (can be empty)
    private final String subtitle;
    private final int imageResId;

    public OnboardingItem(String titleNormal, String titleOrange,
                          String titleNormal2, String subtitle, int imageResId) {
        this.titleNormal  = titleNormal;
        this.titleOrange  = titleOrange;
        this.titleNormal2 = titleNormal2;
        this.subtitle     = subtitle;
        this.imageResId   = imageResId;
    }

    public String getTitleNormal()  { return titleNormal; }
    public String getTitleOrange()  { return titleOrange; }
    public String getTitleNormal2() { return titleNormal2; }
    public String getSubtitle()     { return subtitle; }
    public int getImageResId()      { return imageResId; }
}