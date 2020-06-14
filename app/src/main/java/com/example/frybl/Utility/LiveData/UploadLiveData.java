package com.example.frybl.Utility.LiveData;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.frybl.Model.Recipe;
import com.example.frybl.Model.Upload;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class UploadLiveData extends LiveData<Upload> implements EventListener<DocumentSnapshot> {

    private DocumentReference documentReference;
    private ListenerRegistration listenerRegistration;

    public UploadLiveData(DocumentReference documentReference)
    {
        this.documentReference = documentReference;
    }

    @Override
    protected void onActive() {
        super.onActive();
        listenerRegistration =documentReference.addSnapshotListener(this);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        listenerRegistration.remove();
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (documentSnapshot != null && documentSnapshot.exists())
        {
            Recipe recipe = new Recipe((String)documentSnapshot.get(Recipe.FIRESTOREKEY_RECIPE_NAME),(String)documentSnapshot.get(Recipe.FIRESTOREKEY_RECIPE_DESCRIPTION),(String)documentSnapshot.get(Recipe.FIRESTOREKEY_RECIPE_CATEGORY),documentSnapshot.getLong(Recipe.FIRESTOREKEY_RECIPE_PREPARATIONTIME).intValue(),documentSnapshot.getLong(Recipe.FIRESTOREKEY_RECIPE_COOKTIME).intValue());
            recipe.setImageUri((String)documentSnapshot.get(Recipe.FIRESTOREKEY_RECIPE_IMAGE));
            recipe.setRecipeId((String)documentSnapshot.get(Recipe.FIRESTOREKEY_RECIPE_ID));
            Upload upload = new Upload((String) documentSnapshot.get(Upload.FIRESTOREKEY_UPLOAD_AUTHOR),recipe,documentSnapshot.getDouble(Upload.FIRESTOREKEY_UPLOAD_RATING).floatValue());
            setValue(upload);
        }

    }
}
