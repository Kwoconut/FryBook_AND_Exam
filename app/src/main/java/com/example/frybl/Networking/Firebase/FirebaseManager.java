package com.example.frybl.Networking.Firebase;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Model.Recipe;
import com.example.frybl.Model.RecipeMinimal;
import com.example.frybl.Model.Upload;
import com.example.frybl.Utility.LiveData.IngredientLiveData;
import com.example.frybl.Utility.LiveData.InstructionLiveData;
import com.example.frybl.Utility.LiveData.UploadLiveData;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class FirebaseManager {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<Boolean> isErrorLogin;

    @Inject
    public FirebaseManager(FirebaseFirestore firebaseFirestore, StorageReference storageReference, FirebaseAuth firebaseAuth)
    {
        this.firebaseFirestore = firebaseFirestore;
        this.storageReference = storageReference;
        this.firebaseAuth = firebaseAuth;
        this.isLoading = new MutableLiveData<>();
        this.isErrorLogin = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        this.user.setValue(firebaseAuth.getCurrentUser());

    }

    public LiveData<FirebaseUser> getCurrentUser()
    {
        return user;
    }

    public LiveData<Boolean> isErrorLogin()
    {
        return isErrorLogin;
    }

    public LiveData<Boolean> getUploadIsLoading()
    {
        return isLoading;
    }

    public void uploadNewRecipe(final Recipe recipe, Uri uri, String extension) {
        final Recipe uploadRecipe = recipe;
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + extension);
        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference newDocument = firebaseFirestore.collection("Uploads").document();
                        WriteBatch batch = firebaseFirestore.batch();
                        Map<String,Object> upload = new HashMap<>();
                        upload.put(Recipe.FIRESTOREKEY_RECIPE_ID,newDocument.getId());
                        upload.put(Upload.FIRESTOREKEY_UPLOAD_AUTHOR,firebaseAuth.getCurrentUser().getDisplayName());
                        upload.put(Recipe.FIRESTOREKEY_RECIPE_NAME,uploadRecipe.getName());
                        upload.put(Recipe.FIRESTOREKEY_RECIPE_CATEGORY,uploadRecipe.getCategory());
                        upload.put(Recipe.FIRESTOREKEY_RECIPE_DESCRIPTION,uploadRecipe.getDescription());
                        upload.put(Recipe.FIRESTOREKEY_RECIPE_COOKTIME,uploadRecipe.getCookTime());
                        upload.put(Recipe.FIRESTOREKEY_RECIPE_PREPARATIONTIME,uploadRecipe.getPreparationTime());
                        upload.put(Upload.FIRESTOREKEY_UPLOAD_RATING,0);
                        upload.put(Recipe.FIRESTOREKEY_RECIPE_IMAGE,uri.toString());
                        upload.put(Upload.FIRESTOREKEY_UPLOAD_NR_OF_RATING,0);
                        batch.set(newDocument,upload);

                        for (int i = 0 ; i < uploadRecipe.getIngredients().size();i++)
                        {
                            uploadRecipe.getIngredients().get(i).setRecipeId(newDocument.getId());
                            DocumentReference newIngredientsDocument = firebaseFirestore.collection("Uploads").document(newDocument.getId()).collection("Ingredients").document();
                            batch.set(newIngredientsDocument,uploadRecipe.getIngredients().get(i));
                        }


                        for (int i = 0 ; i < uploadRecipe.getInstructions().size();i++)
                        {
                            uploadRecipe.getInstructions().get(i).setRecipeId(newDocument.getId());
                            DocumentReference newInstructionsDocument = firebaseFirestore.collection("Uploads").document(newDocument.getId()).collection("Instructions").document();
                            batch.set(newInstructionsDocument,uploadRecipe.getInstructions().get(i));
                        }

                        batch.commit();
                        isLoading.setValue(false);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                isLoading.setValue(true);
            }
        });
    }

    public void removeUpload(final String recipeId) {
        firebaseFirestore.collection("Uploads").document(recipeId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseFirestore.collection("Uploads").document(recipeId).collection("Ingredients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments())
                        {
                            snapshot.getReference().delete();
                        }
                    }
                });

                firebaseFirestore.collection("Uploads").document(recipeId).collection("Instructions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments())
                        {
                            snapshot.getReference().delete();
                        }
                    }
                });
            }
        });
    }

    public FirestoreRecyclerOptions<Upload> getFirestoreFeedOptions()
    {
        return new FirestoreRecyclerOptions.Builder<Upload>().setQuery(firebaseFirestore.collection("Uploads"), new SnapshotParser<Upload>() {
            @NonNull
            @Override
            public Upload parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                Recipe recipe = new Recipe((String)snapshot.get(Recipe.FIRESTOREKEY_RECIPE_NAME),(String)snapshot.get(Recipe.FIRESTOREKEY_RECIPE_DESCRIPTION),(String)snapshot.get(Recipe.FIRESTOREKEY_RECIPE_CATEGORY),snapshot.getLong(Recipe.FIRESTOREKEY_RECIPE_PREPARATIONTIME).intValue(),snapshot.getLong(Recipe.FIRESTOREKEY_RECIPE_COOKTIME).intValue());
                recipe.setImageUri((String)snapshot.get(Recipe.FIRESTOREKEY_RECIPE_IMAGE));
                recipe.setRecipeId((String)snapshot.get(Recipe.FIRESTOREKEY_RECIPE_ID));
                Upload upload = new Upload((String)snapshot.get(Upload.FIRESTOREKEY_UPLOAD_AUTHOR),recipe,snapshot.getDouble(Upload.FIRESTOREKEY_UPLOAD_RATING).floatValue());
                return upload;
            }
        }).build();
    }

    public FirestoreRecyclerOptions<RecipeMinimal> getFirestoreUserRecipeOptions()
    {
        return new FirestoreRecyclerOptions.Builder<RecipeMinimal>().setQuery(firebaseFirestore.collection("Uploads").whereEqualTo(Upload.FIRESTOREKEY_UPLOAD_AUTHOR,firebaseAuth.getCurrentUser().getDisplayName()), new SnapshotParser<RecipeMinimal>() {
            @NonNull
            @Override
            public RecipeMinimal parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                RecipeMinimal recipe = new RecipeMinimal((String) snapshot.get(Recipe.FIRESTOREKEY_RECIPE_ID),(String) snapshot.get(Recipe.FIRESTOREKEY_RECIPE_NAME));
                return recipe;
            }
        }).build();
    }

    public LiveData<List<Ingredient>> getIngredientListLiveData(String value)
    {
        return new IngredientLiveData(firebaseFirestore.collection("Uploads").document(value).collection("Ingredients"));
    }

    public LiveData<List<Instruction>> getInstructionListLiveData(String value)
    {
        return new InstructionLiveData(firebaseFirestore.collection("Uploads").document(value).collection("Instructions"));
    }

    public LiveData<Upload> getUploadLiveData(String recipeId) {
        DocumentReference documentReference = firebaseFirestore.collection("Uploads").document(recipeId);
        return new UploadLiveData(documentReference);
    }

    public void rateRecipe(final float rating, String recipeId) {
        final DocumentReference documentReference = firebaseFirestore.collection("Uploads").document(recipeId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists())
                    {
                        float uploadRating = documentSnapshot.getDouble(Upload.FIRESTOREKEY_UPLOAD_RATING).floatValue();
                        int numberOfRatings = documentSnapshot.getLong(Upload.FIRESTOREKEY_UPLOAD_NR_OF_RATING).intValue();
                        float sum = uploadRating * numberOfRatings;
                        sum += rating;
                        numberOfRatings ++;
                        Map<String,Object> ratingUpdate = new HashMap<>();
                        ratingUpdate.put(Upload.FIRESTOREKEY_UPLOAD_RATING,sum/numberOfRatings);
                        ratingUpdate.put(Upload.FIRESTOREKEY_UPLOAD_NR_OF_RATING,numberOfRatings);
                        documentReference.update(ratingUpdate);
                    }
                }
            }
        });
    }

    public void registerUser(final String name, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    firebaseAuth.getCurrentUser().updateProfile(profileChangeRequest);
                    user.setValue(firebaseAuth.getCurrentUser());
                } else {
                }
            }
        });
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    user.setValue(firebaseAuth.getCurrentUser());
                    isErrorLogin.setValue(false);
                }
                else
                {
                    user.setValue(null);
                    isErrorLogin.setValue(true);
                }
            }
        });
    }

    public void logout() {
        firebaseAuth.signOut();
    }

}
