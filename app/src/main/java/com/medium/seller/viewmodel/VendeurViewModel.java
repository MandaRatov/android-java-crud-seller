package com.medium.seller.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.medium.seller.model.Vendeur;
import com.medium.seller.repository.VendeurRepository;
import java.util.List;

public class VendeurViewModel extends AndroidViewModel {

    private final VendeurRepository repository;
    private final LiveData<List<Vendeur>> allVendeurs;

    public VendeurViewModel(Application application) {
        super(application);
        repository = new VendeurRepository(application);
        allVendeurs = repository.getAllVendeurs();
    }

    public LiveData<List<Vendeur>> getAllVendeurs() { return allVendeurs; }
    public LiveData<List<Vendeur>> search(String keyword) { return repository.search(keyword); }

    public void insert(Vendeur v) { repository.insert(v); }
    public void update(Vendeur v) { repository.update(v); }
    public void delete(Vendeur v) { repository.delete(v); }
}