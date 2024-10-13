package org.example.model;

public class Image {

    private int id;
    private byte[] image;
    private String imageUrl;

    public Image() {
    }

    public Image(int id, byte[] image, String imageUrl) {
        this.id = id;
        this.image = image;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
