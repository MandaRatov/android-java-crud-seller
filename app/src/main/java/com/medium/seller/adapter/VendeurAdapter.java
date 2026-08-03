package com.medium.seller.adapter;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.medium.seller.R;
import com.medium.seller.model.Vendeur;
import com.medium.seller.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class VendeurAdapter extends RecyclerView.Adapter<VendeurAdapter.VendeurViewHolder> {

    private List<Vendeur> vendeurList = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onView(Vendeur vendeur);
    }

    public VendeurAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setVendeurList(List<Vendeur> list) {
        this.vendeurList = list;
        notifyDataSetChanged();
    }

    public void sortByNom() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            vendeurList.sort((v1, v2) -> v1.getNom().compareToIgnoreCase(v2.getNom()));
            notifyDataSetChanged();
        }
    }

    public void sortByAge() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            vendeurList.sort((v1, v2) -> {
                int age1 = DateUtils.calculateAge(v1.getDatenais());
                int age2 = DateUtils.calculateAge(v2.getDatenais());
                return Integer.compare(age1, age2);
            });
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public VendeurViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vendeur, parent, false);
        return new VendeurViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendeurViewHolder holder, int position) {
        Vendeur v = vendeurList.get(position);
        holder.txtNom.setText(v.getNom());
        
        int age = DateUtils.calculateAge(v.getDatenais());
        holder.txtDatenais.setText(age + " ans");

        if (v.getPhoto() != null && !v.getPhoto().isEmpty()) {
            holder.imgPhoto.setImageURI(android.net.Uri.parse(v.getPhoto()));
        }

        holder.itemView.setOnClickListener(view -> listener.onView(v));
    }

    @Override
    public int getItemCount() { return vendeurList.size(); }

    public static class VendeurViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView imgPhoto;
        android.widget.TextView txtNom, txtDatenais;

        VendeurViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtNom = itemView.findViewById(R.id.txtNom);
            txtDatenais = itemView.findViewById(R.id.txtDatenais);
        }
    }
}