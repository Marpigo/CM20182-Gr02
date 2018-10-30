package co.edu.edua.compumovil.gr02_20182.lab4.Models;

public class Bebida {

    private int id;
    private String name;
    private int price;
    private String Ingredients;
    private byte [] photo;

    public Bebida(int id, String name, int price, String ingredients, byte[] photo) {
        this.id = id;
        this.name = name;
        this.price = price;
        Ingredients = ingredients;
        this.photo = photo;
    }

    public Bebida() {

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

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}
