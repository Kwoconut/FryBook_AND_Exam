package com.example.frybl.Utility.DaggerComponents;

import com.example.frybl.Networking.Firebase.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    @Provides
    @Singleton
    static FirebaseFirestore provideFirebaseFirestore()
    {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    static StorageReference provideFirebaseStorageUploads()
    {
        return FirebaseStorage.getInstance().getReference("uploads");
    }

    @Provides
    @Singleton
    static FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    static FirebaseManager provideFirebaseManager(FirebaseFirestore firebaseFirestore, StorageReference firebaseStorage, FirebaseAuth firebaseAuth)
    {
        return new FirebaseManager(firebaseFirestore,firebaseStorage,firebaseAuth);
    }

}
