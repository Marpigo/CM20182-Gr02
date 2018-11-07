package co.edu.edua.compumovil.gr02_20182.lab4.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.edu.edua.compumovil.gr02_20182.lab4.Models.Bebida;
import co.edu.edua.compumovil.gr02_20182.lab4.R;


public class AdapterDataRecycler_drink extends RecyclerView.Adapter<AdapterDataRecycler_drink.ViewHolderDatos>
        implements View.OnClickListener, Filterable {



    List<Bebida> bebidaList;
    List<Bebida> bebidaListfull;
    private View.OnClickListener listener;
    Context contesto;

    public AdapterDataRecycler_drink(List<Bebida> bebidaList, Context context) {

        this.bebidaList = bebidaList;
        this.bebidaListfull = new ArrayList<>(bebidaList);
         this.contesto = context;

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

        String imag = bebidaList.get(position).getImagen();
        Glide.with(contesto).load(imag).into(holder.photo);

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


    //  implements Filterable
    @Override
    public Filter getFilter() {
        return drinkFilter;
    }


    private Filter drinkFilter = new Filter() {
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Bebida> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(bebidaListfull);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for (Bebida item : bebidaListfull) {
                    if (item.getName().toUpperCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bebidaList.clear();
            bebidaList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
