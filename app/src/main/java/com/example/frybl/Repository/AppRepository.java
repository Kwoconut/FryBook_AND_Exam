package com.example.frybl.Repository;
import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import com.example.frybl.LocalDB.AppDatabase;
import com.example.frybl.Model.Recipe;
import com.example.frybl.Model.Upload;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppRepository {

    private AppDatabase appDatabase;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    @Inject
    public AppRepository(AppDatabase appDatabase)
    {
        this.appDatabase = appDatabase;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }

    public void uploadNewRecipe(Recipe recipe, Uri uri, String extension) {
        final Recipe uploadRecipe = recipe;
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + extension);
        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadRecipe.setImageUri(uri.toString());
                        Upload upload = new Upload("Angel",uploadRecipe,5);
                        firebaseFirestore.collection("Uploads").document().set(upload);
                    }
                });
            }
        });
    }

    public FirestoreRecyclerOptions<Upload> getFirestoreOptions()
    {
        return new FirestoreRecyclerOptions.Builder<Upload>().setQuery(firebaseFirestore.collection("Uploads"),Upload.class).build();
    }
}
