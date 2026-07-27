package com.medium.seller.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;


//@Entity dit à room : cette classe = une table sqlite
// tableName precise le nom exacte de la table (sinon Room prend le nom de la classe)
@Entity(tableName="vendeur")
public class Vendeur {

    //@PrimaryKey = clé primaire. autoGenerate = true -> sqlite incremente
    //automatiquement idvend , on n'a jamais besoin de le fournier nous meme
    @PrimaryKey(autoGenerate = true)
    private int idvend;


    @NonNull //impossible d'inserer un vendeur sans nom
    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "datenais")
    private String datenais;// format "dd/mm/yyyy"

    @ColumnInfo(name = "photo")
    private String photo;//chemin /URI de l'image

    //constructeur sans idvend: c'est room qui s'en occupe
    public Vendeur (@NonNull String nom, String datenais, String photo) {
        this.nom = nom;
        this.datenais = datenais;
        this.photo = photo;
    }

    //Getters/ Setters : Room en a besoin pour lire/ecrire le champs
    public int getIdvend(){ return idvend;}
    public void setIdvend(int idvend){this.idvend = idvend;}

    @NonNull
    public String getNom(){return nom;}
    public void setNom(@NonNull String nom){this.nom = nom;}

    public String getDatenais(){return datenais;}
    public void setDatenais(String datenais) {this.datenais = datenais;}

    public String getPhoto(){return photo;}
    public void setPhoto(String photo){this.photo = photo;}


}
