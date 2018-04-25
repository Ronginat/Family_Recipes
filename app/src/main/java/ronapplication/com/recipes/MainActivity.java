package ronapplication.com.recipes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import ronapplication.com.recipes.Adapter.SearchAdapter;
import ronapplication.com.recipes.Provider.InternalSQLDatabase.MySQLDatabase;

public class MainActivity extends Activity{

    final String TAG = "main";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter adapter;

    FloatingActionButton floatingMoreButton;
    FloatingActionButton floatingPlusButton;

    MaterialSearchBar materialSearchBar;
    List<String> suggestList = new ArrayList<>();

    MySQLDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "start onCreate");

        //init View
        bindUI();
        initRecycler();
        /*
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        */

        //init DB
        database = new MySQLDatabase(this);

        //Setup search bar
        initMaterialSearch();

        //init adapter default set all result
        adapter = new SearchAdapter(this, database.getRecipes());
        recyclerView.setAdapter(adapter);
        Log.e(TAG, "end onCreate");
    }

    private void bindUI() {
        recyclerView = findViewById(R.id.main_recycler_search);
        materialSearchBar = findViewById(R.id.main_search_bar);
        floatingMoreButton = findViewById(R.id.main_more_ActionButton);
        floatingPlusButton = findViewById(R.id.main_add_recipe_button);
    }

    private void initMaterialSearch(){
        materialSearchBar.setHint("Search");
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
                    floatingPlusButton.setVisibility(View.VISIBLE);
                }
                else
                    floatingPlusButton.setVisibility(View.GONE);
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
