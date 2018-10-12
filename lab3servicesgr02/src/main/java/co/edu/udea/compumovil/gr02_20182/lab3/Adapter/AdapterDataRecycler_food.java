package co.edu.udea.compumovil.gr02_20182.lab3.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab3.Models.Comida;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.VolleySingleton;
import co.edu.udea.compumovil.gr02_20182.lab3.R;

public class AdapterDataRecycler_food extends
        RecyclerView.Adapter<AdapterDataRecycler_food.ViewHolderDatos>
        implements View.OnClickListener{

    List<Comida> comidaList;
    Context context;

    private View.OnClickListener listener;
    public AdapterDataRecycler_food(List<Comida> comidaList,  Context context) {
        this.comidaList = comidaList;
        this.context=context;
    }


    @Override
    public AdapterDataRecycler_food.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list,null,false);

        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        /*evento click en las comida*/
        view.setOnClickListener(this);
        return new AdapterDataRecycler_food.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdapterDataRecycler_food.ViewHolderDatos holder, int position) {
        holder.name.setText(comidaList.get(position).getName());
        holder.price.setText(comidaList.get(position).getPreci()+"");

        if (comidaList.get(position).getPhotoUrl()!=null){
            //
            openPhotoUrl(comidaList.get(position).getPhotoUrl(),holder);
        }else{
            holder.photo.setImageResource(R.drawable.food);
        }
    }

    private void openPhotoUrl(String photoUrl, final ViewHolderDatos holder) {

        String ipserver = context.getString(R.string.s_ip_000webhost);
        String urlImagen = photoUrl;
        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.photo.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,context.getString(R.string.s_web_not_photo),Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);

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
}
