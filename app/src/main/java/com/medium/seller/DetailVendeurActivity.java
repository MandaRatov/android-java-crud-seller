package com.medium.seller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.medium.seller.model.Vendeur;
import com.medium.seller.viewmodel.VendeurViewModel;

public class DetailVendeurActivity extends AppCompatActivity {

    private int idvend;
    private String nom, datenais, photo;
    private VendeurViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vendeur);

        // ====== Configuration du Toolbar ======
        Toolbar toolbar = findViewById(R.id.toolbarDetail);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Force la couleur blanche de la flèche
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(android.graphics.Color.parseColor("#1C1C1E"));
        }
        // ====== FIN Toolbar ======

        ImageView imgPhoto = findViewById(R.id.imgDetailPhoto);
        TextView txtNom = findViewById(R.id.txtDetailNom);
        TextView txtDatenais = findViewById(R.id.txtDetailDatenais);
        Button btnModifier = findViewById(R.id.btnModifier);
        Button btnSupprimer = findViewById(R.id.btnSupprimer);

        Intent i = getIntent();
        idvend = i.getIntExtra("idvend", -1);
        nom = i.getStringExtra("nom");
        datenais = i.getStringExtra("datenais");
        photo = i.getStringExtra("photo");

        txtNom.setText(nom);
        txtDatenais.setText(datenais);
        if (photo != null && !photo.isEmpty()) {
            imgPhoto.setImageURI(Uri.parse(photo));
        }

        viewModel = new ViewModelProvider(this).get(VendeurViewModel.class);

        btnModifier.setOnClickListener(v -> {
            Intent editIntent = new Intent(this, AddEditVendeurActivity.class);
            editIntent.putExtra("idvend", idvend);
            editIntent.putExtra("nom", nom);
            editIntent.putExtra("datenais", datenais);
            editIntent.putExtra("photo", photo);
            startActivity(editIntent);
            finish();
        });

        btnSupprimer.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Supprimer " + nom + " ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        Vendeur vendeur = new Vendeur(nom, datenais, photo);
                        vendeur.setIdvend(idvend);
                        viewModel.delete(vendeur);
                        finish();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}