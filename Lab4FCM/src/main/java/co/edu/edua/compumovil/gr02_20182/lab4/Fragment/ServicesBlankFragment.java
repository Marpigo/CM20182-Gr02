package co.edu.edua.compumovil.gr02_20182.lab4.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.edua.compumovil.gr02_20182.lab4.R;


public class ServicesBlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_services_blank, container, false);

    }

}
