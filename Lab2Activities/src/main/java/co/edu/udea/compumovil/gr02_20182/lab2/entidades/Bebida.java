package co.edu.udea.compumovil.gr02_20182.lab2.entidades;

import java.io.Serializable;

/*
* Implementamos serializable
* para poder transtorpar el objeto
* */
public class Bebida {

    private int id;
    private String name;
    private double price;
    private String Ingredients;
    private byte [] photo;

    public Bebida(int id, String name, double price, String ingredients, byte[] photo) {
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

    public void setPrice(double price) {
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
