package com.voxeldev.models;

public class TheoristNote {
    public String title;
    public String text;
    public String imgUrl;
    
    public TheoristNote(String title, String text, boolean isImage){
        this.title = title;

        if (isImage) {
            this.imgUrl = text;
            return;
        }

        this.text = text;
    }
}
