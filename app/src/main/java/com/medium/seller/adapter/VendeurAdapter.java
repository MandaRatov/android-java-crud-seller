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
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEdit(Vendeur vendeur);
        void onDelete(Vendeur vendeur);
        void onView(Vendeur vendeur);
    }

    public VendeurAdapter(OnItemActionListener listener) {
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
        // Charger la photo (URI) si présente, sinon garder l'image par défaut
        if (v.getPhoto() != null && !v.getPhoto().isEmpty()) {
            holder.imgPhoto.setImageURI(android.net.Uri.parse(v.getPhoto()));
        }

        // itemView = la racine du layout item_vendeur.xml (le CardView entier).
        // On met CE listener en dernier, et les listeners des boutons AVANT/APRÈS
        // n'entrent pas en conflit : Android gère la vue la plus "profonde" cliquée
        // en priorité (bouton), sinon ça remonte au conteneur (carte entière).
        holder.itemView.setOnClickListener(view -> listener.onView(v));

        holder.btnEdit.setOnClickListener(view -> listener.onEdit(v));
        holder.btnDelete.setOnClickListener(view -> listener.onDelete(v));
    }

    @Override
    public int getItemCount() { return vendeurList.size(); }

    static class VendeurViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView imgPhoto;
        android.widget.TextView txtNom, txtDatenais;
        android.widget.ImageButton btnEdit, btnDelete;

        VendeurViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtNom = itemView.findViewById(R.id.txtNom);
            txtDatenais = itemView.findViewById(R.id.txtDatenais);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}