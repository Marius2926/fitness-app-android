package eu.unibuc.ro.network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import eu.unibuc.ro.R;

public class ProductAdapter extends ArrayAdapter<Product> {

    List<Product> alimentsList;
    Context context;
    LayoutInflater inflater;
    TextView tvName;
    TextView tvCaloriesNumber;
    TextView tvQuantity;


    public ProductAdapter(@NonNull Context context,List<Product> alimentsList, LayoutInflater inflater) {
        super(context, R.layout.product_lv_item,alimentsList);
        this.context=context;
        this.alimentsList=alimentsList;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = inflater.inflate(R.layout.product_lv_item,parent,false);
        Product product=alimentsList.get(position);
        if(product != null){

            tvName=view.findViewById(R.id.product_lv_item_name);
            tvName.setText(product.getName());

            tvQuantity=view.findViewById(R.id.product_lv_item_quantity);
            tvQuantity.setText(product.getQuantity());

            tvCaloriesNumber=view.findViewById(R.id.product_lv_item_calories);
            tvCaloriesNumber.setText(String.valueOf(product.getCaloriesNumber()));
        }

        return view;
    }
}
