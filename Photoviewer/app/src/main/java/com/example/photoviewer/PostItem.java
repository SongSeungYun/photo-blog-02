package com.example.photoviewer;

public class PostItem {
    private String author;
    private String title;
    private String text;
    private String imageUrl;
    private String createdDate;   // ✅ 추가
    private boolean favorite;
    public PostItem(String author, String title, String text, String imageUrl, String createdDate) {
        this.author = author;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;  // ✅ 추가
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreatedDate() {  // ✅ 추가
        return createdDate;
    }
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
