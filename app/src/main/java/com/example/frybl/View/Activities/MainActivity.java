package com.example.frybl.View.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.R;
import com.example.frybl.Utility.DaggerComponents.AppModule;
import com.example.frybl.Utility.DaggerComponents.DaggerRepositoryComponent;
import com.example.frybl.Utility.DaggerComponents.LocalDBModule;
import com.example.frybl.Utility.DaggerComponents.RepositoryComponent;
import com.example.frybl.Utility.DaggerComponents.RetrofitModule;
import com.example.frybl.View.Adapters.MainActivityAdapter;
import com.example.frybl.ViewModel.RecipeFeedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView feedView;
    MainActivityAdapter feedViewAdapter;
    FloatingActionButton floatingActionButton;
    RecipeFeedViewModel viewModel;
    static RepositoryComponent component;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_main);
        component = DaggerRepositoryComponent.builder().appModule(new AppModule(getApplication())).localDBModule(new LocalDBModule(getApplication())).retrofitModule(new RetrofitModule()).build();
        viewModel = new ViewModelProvider(this).get(RecipeFeedViewModel.class);
        component.inject(viewModel);


        if (viewModel.getCurrentUser().getValue() == null)
        {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        feedViewAdapter = new MainActivityAdapter(viewModel.getFirestoreFeedOptions());
        feedView = findViewById(R.id.recycler_view_feed);
        feedView.setHasFixedSize(true);
        feedView.setLayoutManager(new LinearLayoutManager(this));
        feedView.setAdapter(feedViewAdapter);
        feedViewAdapter.setOnItemClickListener(new MainActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getBaseContext(), UploadDetailsActivity.class);
                intent.putExtra("DOCUMENT_ID",id);
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        floatingActionButton = findViewById(R.id.floating_button_add_recipe);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        });

    }

    public static RepositoryComponent getComponent()
    {
        return component;
    }

    @Override
    protected void onStart() {
        super.onStart();
        feedViewAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
         feedViewAdapter.stopListening();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
            {
                break;
            }
            case R.id.nav_saved_recipes:
            {
                Intent intent = new Intent(MainActivity.this, SavedRecipesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_add_recipe:
            {
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_lang_settings:
            {
                showLanguageSettingsDialog();
                break;
            }
            case R.id.nav_logout:
            {
                viewModel.logout();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_user_recipe:
            {
                Intent intent = new Intent(MainActivity.this,UserUploadsActivity.class);
                startActivity(intent);
                break;
            }
            default: return false;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLanguageSettingsDialog() {
        final String[] items = {"English","Romana"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.choose_language));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 switch (which)
                 {
                     case 0: {
                         setLanguage("en");
                         recreate();
                         break;
                     }
                     case 1:{
                         setLanguage("ro");
                         recreate();
                         break;
                     }

                 }
                 dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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

}
