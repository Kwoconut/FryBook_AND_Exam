package com.example.frybl.Utility.LiveData;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.frybl.Model.Ingredient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class IngredientLiveData extends LiveData<List<Ingredient>> implements EventListener<QuerySnapshot> {

    private CollectionReference collectionReference;
    private ListenerRegistration listenerRegistration;
    private List<Ingredient> tempList;

    public IngredientLiveData(CollectionReference collectionReference)
    {
        this.collectionReference = collectionReference;
        tempList = new ArrayList<>();
    }

    @Override
    protected void onActive() {
        super.onActive();
        listenerRegistration = collectionReference.addSnapshotListener(this);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        listenerRegistration.remove();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
        for (DocumentSnapshot snapshot: documents)
        {
            Ingredient ingredient = new Ingredient((String) snapshot.get(Ingredient.FIRESTOREKEY_INGREDIENT_NAME), (String) snapshot.get(Ingredient.FIRESTOREKEY_INGREDIENT_QUANTITY), false);
            ingredient.setRecipeId((String) snapshot.get(Ingredient.FIRESTOREKEY_INGREDIENT_PARENTRECIPE));
            tempList.add(ingredient);
        }
        setValue(tempList);
    }
}
