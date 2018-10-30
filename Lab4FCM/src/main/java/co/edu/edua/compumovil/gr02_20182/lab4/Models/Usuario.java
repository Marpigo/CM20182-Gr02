package co.edu.edua.compumovil.gr02_20182.lab4.Models;

public class Usuario {
    private int id;
    private String name;
    private String email;
    private  String password;
    private byte [] photo;

    public Usuario() {

   }

    public Usuario(int id, String name, String email, String password, byte[] photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
