package com.example.brown.ecard;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Brown on 6/5/2018.
 */

public class CardHolder extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_holder,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getFragmentManager().beginTransaction().replace(R.id.container,new CardFrontFragment()).commit();
    }

    public static class CardFrontFragment extends Fragment{

        ImageView logo;
        ImageButton flip;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.card_front,container,false);
            logo = (ImageView)view.findViewById(R.id.card);
            flip = (ImageButton)view.findViewById(R.id.imageButton);
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            final CardDisplay cardDisplay = new CardDisplay();
            flip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.card_flip_right_in,R.anim.card_flip_right_out)
                            .replace(R.id.container,cardDisplay).addToBackStack(null).commit();
                }
            });
        }
    }

   /* public static class CardBackFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.card_back,container,false);
        }
    } */
}
