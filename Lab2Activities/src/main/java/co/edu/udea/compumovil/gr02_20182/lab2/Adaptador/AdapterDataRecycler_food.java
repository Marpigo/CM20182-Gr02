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
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Comida;

public class AdapterDataRecycler_food extends RecyclerView.Adapter<AdapterDataRecycler_food.ViewHolderDatos> {

    List<Comida> comidaList;


    public AdapterDataRecycler_food(List<Comida> comidaList) {
        this.comidaList = comidaList;
    }


    @Override
    public AdapterDataRecycler_food.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list,null,false);
        return new AdapterDataRecycler_food.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdapterDataRecycler_food.ViewHolderDatos holder, int position) {
        holder.name.setText(comidaList.get(position).getName());
        holder.price.setText(comidaList.get(position).getPrice()+"");
        byte[] data = comidaList.get(position).getPhoto();
        Bitmap image = toBitmap(data);
        holder.photo.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return comidaList.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView photo;


        public ViewHolderDatos(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtNama_item_fodd);
            price = (TextView) itemView.findViewById(R.id.txtPrice_item_food);
            photo = (ImageView) itemView.findViewById(R.id.imgPhoto_item_food);
        }
    }

    public static Bitmap toBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}