package co.edu.udea.compumovil.gr02_20182.lab2.entidades;

public class Comida {

    private int id;
    private String name;
    private String schedule;
    private String type;
    private String time;
    private int price;
    private String ingredients;
    private byte [] photo;

    public Comida(int id, String name, String schedule, String type, String time, int price, String ingredients, byte[] photo) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.type = type;
        this.time = time;
        this.price = price;
        this.ingredients = ingredients;
        this.photo = photo;
    }

    public Comida() {

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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
