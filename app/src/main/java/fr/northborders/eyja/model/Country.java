package fr.northborders.eyja.model;

import android.content.Context;

/**
 * Created by thibaultguegan on 24/01/15.
 */
public class Country {
    public String name;
    public String description;
    public String imageName;


    public int getImageResourceId(Context context)
    {
        try {
            return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
