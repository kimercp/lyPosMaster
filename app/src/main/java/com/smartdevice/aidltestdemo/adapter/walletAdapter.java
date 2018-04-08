package com.smartdevice.aidltestdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartdevice.aidltestdemo.R;
import com.smartdevice.aidltestdemo.models.WalletItem;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by omar on 10/02/2018.
 */

public class walletAdapter extends ArrayAdapter<WalletItem> {

    private int resourceId = 0;
    private LayoutInflater inflater;
    View view;
    WalletItem item;
    public static Boolean isScrolling=false;


   static Boolean scrolling;
    TextView name, price;
    ImageView image;

    public walletAdapter(Context context, int resourceId, List<WalletItem> articles) {
        super(context, 0, articles);
        this.resourceId = resourceId;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {





        view = inflater.inflate(resourceId, parent, false);

        try {
            name = (TextView)view.findViewById(R.id.name);
            price = (TextView)view.findViewById(R.id.price);
            image = (ImageView)view.findViewById(R.id.image);

        } catch( ClassCastException e ) {
            throw e;
        }

       item = getItem(position);


        name.setText(item.getName());
        price.setText("£ "+item.getPrice());




       if(!isScrolling)

     {
      Picasso.with(getContext()).load(item.getIconUrl()).fit().centerCrop().into(image);
     }

        return view;
    }


}
