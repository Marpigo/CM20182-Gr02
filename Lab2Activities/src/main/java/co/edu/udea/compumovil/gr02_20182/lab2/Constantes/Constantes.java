package co.edu.udea.compumovil.gr02_20182.lab2.Constantes;

public class Constantes {

    /*
    * Atributos de la tabla Usuarios, Representadas por las constantes
    *
    * */

    public static final String TABLA_USUARIO = "usuario";
    public static final String CAMPO_PHOTO = "photo";
    public static final String CAMPO_NAME = "name";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_PASSWORD = "password";


    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "+  TABLA_USUARIO + " ("+CAMPO_NAME+" TEXT, "+CAMPO_EMAIL+" TEXT, "+CAMPO_PASSWORD+" TEXT, "+CAMPO_PHOTO+" BLOB)";


}
