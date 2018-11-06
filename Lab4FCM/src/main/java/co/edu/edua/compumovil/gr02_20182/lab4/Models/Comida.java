package co.edu.edua.compumovil.gr02_20182.lab4.Models;

public class Comida {
    private String id;
    private String name;
    private String schedule;
    private String type;
    private String time;
    private String preci;
    private String ingredient;
   private String imagen;

    public Comida() {

    }

    public Comida(String id, String name, String schedule, String type, String time, String preci, String ingredient, String imagen) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.type = type;
        this.time = time;
        this.preci = preci;
        this.ingredient = ingredient;
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

    public String getPreci() {
        return preci;
    }

    public void setPreci(String preci) {
        this.preci = preci;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return name + " $ " + preci;
    }
}
