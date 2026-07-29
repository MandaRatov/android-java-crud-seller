package com.medium.seller.adapter;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.medium.seller.R;
import com.medium.seller.model.Vendeur;
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
        holder.txtDatenais.setText(v.getDatenais());

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