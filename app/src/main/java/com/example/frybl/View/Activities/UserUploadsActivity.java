package com.example.frybl.View.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.R;
import com.example.frybl.View.Adapters.UserUploadsAdapter;
import com.example.frybl.ViewModel.UserUploadsViewModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Locale;

import static com.example.frybl.View.Activities.MainActivity.component;

public class UserUploadsActivity extends AppCompatActivity{

    UserUploadsViewModel viewModel;
    RecyclerView userUploadsRecyclerView;
    UserUploadsAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_user_uploads);

        viewModel = new ViewModelProvider(this).get(UserUploadsViewModel.class);
        component.inject(viewModel);
        adapter = new UserUploadsAdapter(viewModel.getFirestoreUserRecipeOptions());
        userUploadsRecyclerView = findViewById(R.id.user_uploads_recycler_view);
        userUploadsRecyclerView.setHasFixedSize(true);
        userUploadsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userUploadsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new UserUploadsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getBaseContext(), UploadDetailsActivity.class);
                intent.putExtra("DOCUMENT_ID",id);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(UserUploadsActivity.this).setTitle(getString(R.string.confirm_delete)).setMessage(getString(R.string.confirm_delete_message) + " " + adapter.getItem(viewHolder.getAdapterPosition()).getName() + "?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.removeUpload(adapter.getItem(viewHolder.getAdapterPosition()).getRecipeId());
                    }
                }).setNegativeButton(R.string.no, null).show();
            }
        }).attachToRecyclerView(userUploadsRecyclerView);
    }


    private void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("languageSettings", MODE_PRIVATE).edit();
        editor.putString("Language",language);
        editor.apply();
    }

    public void loadLanguage()
    {
        SharedPreferences preferences = getSharedPreferences("languageSettings", Activity.MODE_PRIVATE);
        String language = preferences.getString("Language","");
        setLanguage(language);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
