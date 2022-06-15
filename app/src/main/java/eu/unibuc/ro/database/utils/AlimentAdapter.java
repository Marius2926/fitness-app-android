package eu.unibuc.ro.database.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import eu.unibuc.ro.R;
import eu.unibuc.ro.network.Product;

public class AlimentAdapter extends RecyclerView.Adapter<AlimentAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View view, Product item);
    }

    private ArrayList<Product> products;
    private final OnItemClickListener listener;

    public AlimentAdapter(ArrayList<Product> productArrayList, OnItemClickListener listener) {
        this.products = productArrayList;
        this.listener = listener;
    }

    public void filterList(ArrayList<Product> filterllist) {
        products = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlimentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView productNameTv;
        private final TextView productQuantityTv;
        private final TextView productCaloriesTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.aliments_list_item_product);
            productQuantityTv = itemView.findViewById(R.id.aliments_list_item_quantity);
            productCaloriesTv = itemView.findViewById(R.id.aliments_list_item_calories);
        }

        public void bind(final Product product, final OnItemClickListener listener) {
            productNameTv.setText(product.getName());
            productQuantityTv.setText(product.getQuantity());
            productCaloriesTv.setText(String.valueOf(product.getCaloriesNumber()));
            itemView.setOnClickListener(v -> listener.onItemClick(v, product));
        }
    }
}
