package co.edu.udea.compumovil.gr02_20182.lab2.entidades;

public class Comida {

    private int id;
    private byte photo;
    private String name;
    private double price;
    private String ingredients;

    public Comida(int id, byte photo, String name, double price, String ingredients) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getPhoto() {
        return photo;
    }

    public void setPhoto(byte photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
