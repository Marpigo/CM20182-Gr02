package co.edu.edua.compumovil.gr02_20182.lab4.Models;

public class Bebida {

    private String id;
    private String name;
    private String price;
    private String ingredients;
    private String imagen;

    public Bebida() {

    }


    public Bebida(String id, String name, String price, String ingredients, String imagen) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return name + " $ " + price;
    }
}
