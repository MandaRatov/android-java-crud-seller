package com.medium.seller.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.medium.seller.model.Vendeur;

import java.util.List;

@Dao
public interface VendeurDAO {

    @Insert
    void insert(Vendeur vendeur);

    @Update
    void update(Vendeur vendeur);

    @Delete
    void delete(Vendeur vendeur);

    @Query("Select * from vendeur order by nom asc")
    LiveData<List<Vendeur>> getAllVendeurs();

    @Query("Select * from vendeur where idvend = :id")
    Vendeur getVendeurById(int id);

    //Recherche par nom(partielle, sensible à la casse)
    @Query("Select * from vendeur where nom like '%' || :keyword || '%' order by nom asc")
    LiveData<List<Vendeur>> searchByNom(String keyword);


}