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

public class MainActivity extends AppCompatActivity implements VendeurAdapter.OnItemClickListener {

    private VendeurViewModel viewModel;
    private VendeurAdapter adapter;
    public static final int REQUEST_ADD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VendeurAdapter(this);
        recyclerView.setAdapter(adapter);

        android.widget.TextView txtCount = findViewById(R.id.txtCount);

        viewModel = new ViewModelProvider(this).get(VendeurViewModel.class);
        viewModel.getAllVendeurs().observe(this, vendeurs -> {
            adapter.setVendeurList(vendeurs);
            int count = vendeurs.size();
            txtCount.setText("Nombre de vendeurs enregistrés : " + count);
        });

        findViewById(R.id.btnAdd).setOnClickListener(v ->
                startActivityForResult(new Intent(this, AddEditVendeurActivity.class), REQUEST_ADD));

        findViewById(R.id.btnSort).setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(this, v);
            popup.getMenu().add("Trier par nom");
            popup.getMenu().add("Trier par âge");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Trier par nom")) {
                    adapter.sortByNom();
                } else {
                    adapter.sortByAge();
                }
                return true;
            });
            popup.show();
        });

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

    // ⚠️ onEdit() et onDelete() : retirés, ces actions vivent maintenant
    // uniquement dans DetailVendeurActivity (accessible via onView ci-dessous)

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