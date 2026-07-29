package com.medium.seller;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import com.medium.seller.model.Vendeur;
import com.medium.seller.viewmodel.VendeurViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditVendeurActivity extends AppCompatActivity {

    private EditText edtNom, edtDatenais;
    private ImageView imgPhoto;
    private String photoUri = "";
    private VendeurViewModel viewModel;
    private int idvend = -1;

    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;
    private static final int CAMERA_PERMISSION_CODE = 200;

    // Stocke temporairement l'URI de la photo en cours de capture,
    // car ACTION_IMAGE_CAPTURE ne renvoie rien dans onActivityResult :
    // il écrit directement dans le fichier qu'on lui a donné à l'avance
    private Uri photoCaptureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vendeur);

        Toolbar toolbar = findViewById(R.id.toolbarAddEdit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(
                    getIntent().hasExtra("idvend") ? "Modifier le vendeur" : "Ajouter un vendeur"
            );
        }
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(android.graphics.Color.parseColor("#1C1C1E"));
        }

        edtNom = findViewById(R.id.edtNom);
        edtDatenais = findViewById(R.id.edtDatenais);
        imgPhoto = findViewById(R.id.imgPhotoPreview);
        TextView btnChoosePhoto = findViewById(R.id.btnChoosePhoto);
        Button btnSave = findViewById(R.id.btnSave);

        viewModel = new ViewModelProvider(this).get(VendeurViewModel.class);

        edtDatenais.setOnClickListener(v -> showDatePicker());

        // Au clic : on propose un choix entre Galerie et Caméra
        btnChoosePhoto.setOnClickListener(v -> showPhotoSourceDialog());

        Intent i = getIntent();
        if (i.hasExtra("idvend")) {
            idvend = i.getIntExtra("idvend", -1);
            edtNom.setText(i.getStringExtra("nom"));
            edtDatenais.setText(i.getStringExtra("datenais"));
            photoUri = i.getStringExtra("photo");
            if (photoUri != null && !photoUri.isEmpty()) {
                imgPhoto.setImageURI(Uri.parse(photoUri));
            }
        }

        btnSave.setOnClickListener(v -> saveVendeur());
    }

    // ====== NOUVEAU : boîte de dialogue de choix Galerie / Caméra ======
    private void showPhotoSourceDialog() {
        String[] options = {"Prendre une photo", "Choisir depuis la galerie"};
        new AlertDialog.Builder(this)
                .setTitle("Photo du vendeur")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        checkCameraPermissionAndOpen();
                    } else {
                        openGallery();
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    // ====== NOUVEAU : vérifie la permission caméra avant d'ouvrir l'appli photo ======
    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission pas encore accordée : on la demande à l'utilisateur.
            // Le résultat arrive dans onRequestPermissionsResult ci-dessous.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ====== NOUVEAU : ouvre l'appli caméra ======
    private void openCamera() {
        try {
            // 1. On crée un fichier vide, à l'emplacement défini dans file_paths.xml,
            //    où la caméra va écrire la photo prise
            File photoFile = createImageFile();

            // 2. On transforme ce chemin de fichier en URI sécurisée via FileProvider
            photoCaptureUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    photoFile
            );

            // 3. On lance l'appli caméra en lui donnant cette URI comme
            //    destination d'écriture (EXTRA_OUTPUT)
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoCaptureUri);
            startActivityForResult(takePictureIntent, TAKE_PHOTO);

        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de la création du fichier photo", Toast.LENGTH_SHORT).show();
        }
    }

    // Crée un fichier avec un nom unique basé sur l'horodatage,
    // dans le dossier Pictures de l'app (défini dans file_paths.xml)
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "VENDEUR_" + timeStamp;
        File storageDir = getExternalFilesDir("Pictures");
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = String.format("%02d/%02d/%04d", day, month + 1, year);
            edtDatenais.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Galerie : l'URI de l'image choisie vient dans data.getData()
            Uri uri = data.getData();
            photoUri = uri.toString();
            imgPhoto.setImageURI(uri);

        } else if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            // Caméra : l'image a déjà été écrite dans photoCaptureUri
            // (on l'a fourni nous-mêmes via EXTRA_OUTPUT), pas besoin de data.getData()
            photoUri = photoCaptureUri.toString();
            imgPhoto.setImageURI(photoCaptureUri);
        }
    }

    private void saveVendeur() {
        String nom = edtNom.getText().toString().trim();
        String datenais = edtDatenais.getText().toString().trim();

        if (nom.isEmpty()) {
            edtNom.setError("Le nom est obligatoire");
            return;
        }

        Vendeur vendeur = new Vendeur(nom, datenais, photoUri);
        if (idvend != -1) {
            vendeur.setIdvend(idvend);
            viewModel.update(vendeur);
        } else {
            viewModel.insert(vendeur);
        }
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}