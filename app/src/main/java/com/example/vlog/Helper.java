package com.example.vlog;

import android.net.Uri;

import com.google.firebase.database.FirebaseDatabase;

public class Helper {

    String ImageUri,Title,Content;

    public Helper(String imageUri, String title, String content) {
        ImageUri = imageUri;
        Title = title;
        Content = content;
    }

    public Helper() {
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
