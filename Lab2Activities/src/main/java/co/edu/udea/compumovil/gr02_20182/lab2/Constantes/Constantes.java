package co.edu.udea.compumovil.gr02_20182.lab2.Constantes;

public class Constantes {

    /*
     * Atributos de la tabla Usuarios, Representadas por las constantes
     *
     * */


    public static final String TABLA_USUARIO="usuario";
    public static final String CAMPO_PHOTO="photo";
    public static final String CAMPO_NAME="name";
    public static final String CAMPO_EMAIL="email";
    public static final String CAMPO_PASSWORD="password";
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " +
            ""+TABLA_USUARIO+ " (" + CAMPO_NAME +" "+
            "TEXT, " + CAMPO_EMAIL+" "+
            "TEXT, " + CAMPO_PASSWORD+" "+
            "TEXT, " + CAMPO_PHOTO+ " BLOB)";



    public static final String TABLA_BEBIDA="bebida";
    public static final String CAMPO_ID_B="id";
    public static final String CAMPO_NAME_B="name";
    public static final String CAMPO_PRECIO_B="preci";
    public static final String CAMPO_INGREDIENTES_B="ingredient";
    public static final String CAMPO_PHOTO_B="photo";

    public static final String CREATE_DRINK_TABLE = "CREATE TABLE IF NOT EXISTS " +
            ""+TABLA_BEBIDA+ " (" + CAMPO_ID_B +" "+
            "TEXT," + CAMPO_NAME_B+" "+
            "TEXT, " + CAMPO_PRECIO_B+" "+
            "TEXT, " + CAMPO_INGREDIENTES_B+" "+
            "TEXT, " + CAMPO_PHOTO_B+ " BLOB)";




    public static final String TABLA_COMIDA="comida";
    public static final String CAMPO_ID_C="id";
    public static final String CAMPO_NAME_C="name";
    public static final String CAMPO_HORARIO_C="schedule";
    public static final String CAMPO_TIPO_C="type";
    public static final String CAMPO_TIME_C="time";
    public static final String CAMPO_PRECIO_C="preci";
    public static final String CAMPO_INGREDIENTES_C="ingredient";
    public static final String CAMPO_PHOTO_C="photo";

    public static final String CREATE_FOOD_TABLE = "CREATE TABLE IF NOT EXISTS " +
            ""+TABLA_COMIDA+ " (" + CAMPO_ID_C +" "+
            "TEXT," + CAMPO_NAME_C+" "+
            "TEXT, " + CAMPO_HORARIO_C+" "+
            "TEXT, " + CAMPO_TIPO_C+" "+
            "TEXT, " + CAMPO_TIME_C+" "+
            "TEXT, " + CAMPO_PRECIO_C+" "+
            "TEXT, " + CAMPO_INGREDIENTES_C+" "+
            "TEXT, " + CAMPO_PHOTO_C+ " BLOB)";




}
