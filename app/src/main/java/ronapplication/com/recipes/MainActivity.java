package ronapplication.com.recipes;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import ronapplication.com.recipes.Adapter.SearchAdapter;
import ronapplication.com.recipes.Provider.InternalSQLDatabase.MySQLDatabase;

public class MainActivity extends AppCompatActivity {

    final String TAG = "main";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter adapter;

    FloatingActionButton floatingPlusButton;
    FloatingActionButton floatingFilterButton;

    MaterialSearchBar materialSearchBar;
    //SearchView searchView;
    List<String> suggestList = new ArrayList<>();

    MySQLDatabase database;
    Dialog filtersDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "start onCreate");

        filtersDialog = new Dialog(this);
        //init View
        bindUI();
        initRecycler();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        //init DB
        database = new MySQLDatabase(this);

        //Setup search bar
        initMaterialSearch();

        //init adapter default set all result
        adapter = new SearchAdapter(this, database.getRecipes());
        recyclerView.setAdapter(adapter);
        Log.e(TAG, "end onCreate");
    }

/*

            case R.id.action_clear_history:
                //Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("מחיקת היסטוריה");
                alertBuilder.setMessage("האם אתה בטוח שאתה רוצה למחוק את היסטוריית החיפושים?");
                alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "היסטוריה נמחקה", Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                });
                alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
                return true;

    }
*/

    private void bindUI() {
        recyclerView = findViewById(R.id.main_recycler_search);
        materialSearchBar = findViewById(R.id.main_search_bar);
        //floatingMoreButton = findViewById(R.id.main_more_ActionButton);
        //toolbar = findViewById(R.id.main_toolbar);
        floatingPlusButton = findViewById(R.id.main_add_recipe_button);
        floatingFilterButton = findViewById(R.id.main_filter_button);

        floatingPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveUserChoose()
                Toast.makeText(MainActivity.this, "add new recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //listens to floatingFilterButton onClick
    public void ShowPopup(View view){
        Log.e(TAG,"start ShowPopup");
        filtersDialog.setContentView(R.layout.layout_filters);
        Button closeButton = filtersDialog.findViewById(R.id.filters_finish_button);
        ListView FoodKindsListView = filtersDialog.findViewById(R.id.filters_food_kinds_listView);
        ListView holidaysListView = filtersDialog.findViewById(R.id.filters_holidays_listView);
        RadioButton radioButton1 = filtersDialog.findViewById(R.id.filters_kashrut1);
        RadioButton radioButton2 = filtersDialog.findViewById(R.id.filters_kashrut2);
        RadioButton radioButton3 = filtersDialog.findViewById(R.id.filters_kashrut3);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersDialog.dismiss();
            }
        });
        //filtersDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filtersDialog.show();
    }

    private void initMaterialSearch(){

        materialSearchBar.setSpeechMode(false);
        materialSearchBar.setHint(getString(R.string.materialbar_search_hint));
        materialSearchBar.setPadding(0,0,0,5);
        materialSearchBar.setMaxSuggestionCount(20);
        //materialSearchBar.setHint("Search");
        materialSearchBar.setCardViewElevation(10);
        loadSuggestList();
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for(String search: suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    //if close Search, just restore default
                    adapter = new SearchAdapter(getBaseContext(), database.getRecipes());
                    recyclerView.setAdapter(adapter);
                    //add fading out
                    //ask noam
                    floatingPlusButton.setVisibility(View.VISIBLE);
                    floatingFilterButton.setVisibility(View.VISIBLE);
                }
                else {
                    //add fading in
                    //ask noam
                    floatingPlusButton.setVisibility(View.GONE);
                    floatingFilterButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void initRecycler() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void startSearch(String text) {
        adapter = new SearchAdapter(this, database.getRecipeByName(text));
        recyclerView.setAdapter(adapter);
    }

    private void loadSuggestList() {
        suggestList = database.getNames();
        materialSearchBar.setLastSuggestions(suggestList);
    }
}
