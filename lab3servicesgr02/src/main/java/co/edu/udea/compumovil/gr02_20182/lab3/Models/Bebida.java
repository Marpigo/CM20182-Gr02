package co.edu.udea.compumovil.gr02_20182.lab3.Models;

public class Bebida {
    private int id;
    private String name;
    private int preci;
    private String Ingredients;
   // private byte [] photo;
   private String photo;
    private String photoUrl;

    public Bebida() {

    }

    public Bebida(int id, String name, int preci, String ingredients, String photo, String photoUrl) {
        this.id = id;
        this.name = name;
        this.preci = preci;
        Ingredients = ingredients;
        this.photo = photo;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPreci() {
        return preci;
    }

    public void setPreci(int preci) {
        this.preci = preci;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
