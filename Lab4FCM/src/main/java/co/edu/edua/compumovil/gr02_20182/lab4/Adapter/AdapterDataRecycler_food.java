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

import co.edu.edua.compumovil.gr02_20182.lab4.Models.Comida;
import co.edu.edua.compumovil.gr02_20182.lab4.R;

public class AdapterDataRecycler_food extends
    RecyclerView.Adapter<AdapterDataRecycler_food.ViewHolderDatos>
        implements View.OnClickListener, Filterable {

        List<Comida> comidaList;
        List<Comida> comidaListfull;
        Context contesto;

        private View.OnClickListener listener;



    public AdapterDataRecycler_food(List<Comida> comidaList, Context context) {

        this.comidaList = comidaList;
        this.comidaListfull = new ArrayList<>(comidaList);
        this.contesto = context;

        }


        @Override
        public AdapterDataRecycler_food.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list,null,false);


            /*evento click en las comida*/
            view.setOnClickListener(this);


            return new AdapterDataRecycler_food.ViewHolderDatos(view);

        }

        @Override
        public void onBindViewHolder(AdapterDataRecycler_food.ViewHolderDatos holder, int position) {
            holder.name.setText(comidaList.get(position).getName());
            holder.price.setText(comidaList.get(position).getPreci()+"");
            String imag = comidaList.get(position).getImagen();
            Glide.with(contesto).load(imag).into(holder.photo);

        }

        @Override
        public int getItemCount() {
            return comidaList.size();
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




    //  implements Filterable

    @Override
    public Filter getFilter() {
        return foodFilter;
    }



    private Filter foodFilter = new Filter() {
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Comida> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(comidaListfull);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for (Comida item : comidaListfull) {
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
            comidaList.clear();
            comidaList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}
