package co.edu.udea.compumovil.gr02_20182.lab2.Adaptador;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.R;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;

public class AdapterDataRecycler_drink extends RecyclerView.Adapter<AdapterDataRecycler_drink.ViewHolderDatos>
        implements View.OnClickListener{



    List<Bebida> bebidaList;
    private View.OnClickListener listener;

    public AdapterDataRecycler_drink(List<Bebida> bebidaList) {
        this.bebidaList = bebidaList;
    }

    /*Este metodo nos enlaza el adaptador con item_list.xml*/
    @Override
    public AdapterDataRecycler_drink.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drink_list,null,false);


        /*evento click en las comida*/
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    /*establece la comunicacion entre el adaptador y el ViewHolderDatos*/
    @Override
    public void onBindViewHolder(AdapterDataRecycler_drink.ViewHolderDatos holder, int position) {
        holder.name.setText(bebidaList.get(position).getName());
        holder.price.setText(bebidaList.get(position).getPrice()+"");
        byte[] data = bebidaList.get(position).getPhoto();
        Bitmap image = toBitmap(data);
        holder.photo.setImageBitmap(image);
    }


    @Override
    public int getItemCount() {
        return bebidaList.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView photo;


        public ViewHolderDatos(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtName_item_drink);
            price = (TextView) itemView.findViewById(R.id.txtPrice_item_drink);
            photo = (ImageView) itemView.findViewById(R.id.imgPhoto_item_drink);
        }
    }

    public static Bitmap toBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }



    /*Encargado de escuchar el evento onclik*/
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    /*Implementacion metodo basico de seleccion onclik*/
    @Override
    public void onClick(View view) {
        if (listener != null)
        {
            listener.onClick(view);
        }
    }
}
