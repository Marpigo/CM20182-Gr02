package co.edu.udea.compumovil.gr02_20182.lab3.Models;

public class Comida {
    private int id;
    private String name;
    private String schedule;
    private String type;
    private String time;
    private int preci;
    private String ingredient;
   private byte [] photo;
    //private String photo;
    private String photoUrl;

    public Comida() {
    }


    public Comida(int id, String name, String schedule, String type, String time, int preci, String ingredient, byte[] photo, String photoUrl) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.type = type;
        this.time = time;
        this.preci = preci;
        this.ingredient = ingredient;
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

    public int getPreci() {
        return preci;
    }

    public void setPreci(int preci) {
        this.preci = preci;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
