package com.example.frybl.Utility.LiveData;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.frybl.Model.Instruction;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class InstructionLiveData extends LiveData<List<Instruction>> implements EventListener<QuerySnapshot> {

    private CollectionReference collectionReference;
    private ListenerRegistration listenerRegistration;
    private ArrayList<Instruction> tempList;

    public InstructionLiveData(CollectionReference collectionReference)
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
        for (DocumentSnapshot snapshots : documents)
        {
            Instruction instruction = new Instruction((String) snapshots.get(Instruction.FIRESTOREKEY_INSTRUCTION_DESCRIPTION),false);
            instruction.setRecipeId((String) snapshots.get(Instruction.FIRESTOREKEY_INSTRUCTION_RECIPEID));
            tempList.add(instruction);
        }
        setValue(tempList);
    }
}
