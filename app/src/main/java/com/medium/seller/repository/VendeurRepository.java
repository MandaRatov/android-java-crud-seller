package com.medium.seller.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.medium.seller.dao.VendeurDAO;
import com.medium.seller.db.AppDatabase;
import com.medium.seller.model.Vendeur;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VendeurRepository {
    private final VendeurDAO vendeurDAO;
    private final ExecutorService executorService;

    public VendeurRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        vendeurDAO = db.vendeurDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Vendeur vendeur){
        executorService.execute(() -> vendeurDAO.insert(vendeur));
    }

    public void update(Vendeur vendeur){
        executorService.execute(() -> vendeurDAO.update(vendeur));
    }

    public void delete(Vendeur vendeur){
        executorService.execute(() -> vendeurDAO.delete(vendeur));
    }

    public LiveData<List<Vendeur>> getAllVendeurs(){
        return vendeurDAO.getAllVendeurs();
    }

    public LiveData<List<Vendeur>> search(String keyword){
        return vendeurDAO.searchByNom(keyword);
    }

}
