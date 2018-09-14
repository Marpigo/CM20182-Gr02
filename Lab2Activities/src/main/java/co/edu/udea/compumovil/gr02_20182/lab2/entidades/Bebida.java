package co.edu.udea.compumovil.gr02_20182.lab2.entidades;

public class Bebida {

    private int id;
    private byte photo;
    private String name;
    private String Schedule;
    private String type;
    private String time;
    private String price;
    private String Ingredients;

    public Bebida(int id, byte photo, String name, String schedule, String type, String time, String price, String ingredients) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        Schedule = schedule;
        this.type = type;
        this.time = time;
        this.price = price;
        Ingredients = ingredients;
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

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
