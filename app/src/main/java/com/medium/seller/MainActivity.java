package com.medium.seller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.medium.seller.adapter.VendeurAdapter;
import com.medium.seller.model.Vendeur;
import com.medium.seller.viewmodel.VendeurViewModel;

public class MainActivity extends AppCompatActivity implements VendeurAdapter.OnItemActionListener {

    private VendeurViewModel viewModel;
    private VendeurAdapter adapter;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VendeurAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(VendeurViewModel.class);
        viewModel.getAllVendeurs().observe(this, adapter::setVendeurList);

        findViewById(R.id.btnAdd).setOnClickListener(v ->
                startActivityForResult(new Intent(this, AddEditVendeurActivity.class), REQUEST_ADD));

        EditText edtSearch = findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    viewModel.getAllVendeurs().observe(MainActivity.this, adapter::setVendeurList);
                } else {
                    viewModel.search(s.toString()).observe(MainActivity.this, adapter::setVendeurList);
                }
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

    }

    @Override
    public void onEdit(Vendeur vendeur) {
        Intent intent = new Intent(this, AddEditVendeurActivity.class);
        intent.putExtra("idvend", vendeur.getIdvend());
        intent.putExtra("nom", vendeur.getNom());
        intent.putExtra("datenais", vendeur.getDatenais());
        intent.putExtra("photo", vendeur.getPhoto());
        startActivityForResult(intent, REQUEST_EDIT);
    }

    @Override
    public void onDelete(Vendeur vendeur) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Supprimer " + vendeur.getNom() + " ?")
                .setPositiveButton("Oui", (dialog, which) -> viewModel.delete(vendeur))
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    public void onView(Vendeur vendeur) {
        // On passe toutes les infos du vendeur via l'Intent :
        // DetailVendeurActivity n'a pas accès à la base de données directement,
        // donc on lui donne ce dont elle a besoin pour AFFICHER (pas modifier).
        Intent intent = new Intent(this, DetailVendeurActivity.class);
        intent.putExtra("idvend", vendeur.getIdvend());
        intent.putExtra("nom", vendeur.getNom());
        intent.putExtra("datenais", vendeur.getDatenais());
        intent.putExtra("photo", vendeur.getPhoto());
        startActivity(intent);
    }
}