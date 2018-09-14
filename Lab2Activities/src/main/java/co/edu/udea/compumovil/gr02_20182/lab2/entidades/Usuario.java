package co.edu.udea.compumovil.gr02_20182.lab2.entidades;

public class Usuario {

    private byte [] photo;
    private String name;
    private String eamil;
    private  String password;

    public Usuario(String name, String eamil, String password, byte[] photo) {
        this.photo = photo;
        this.name = name;
        this.eamil = eamil;
        this.password = password;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEamil() {
        return eamil;
    }

    public void setEamil(String eamil) {
        this.eamil = eamil;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}



