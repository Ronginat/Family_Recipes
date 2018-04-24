package ronapplication.com.recipes;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import ronapplication.com.recipes.Adapter.SearchAdapter;
import ronapplication.com.recipes.Database.Database;

import static android.view.View.inflate;

public class MainActivity extends Activity{

    final String TAG = "main";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter adapter;

    MaterialSearchBar materialSearchBar;
    List<String> suggestList = new ArrayList<>();

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "start onCreate");

        //init View
        recyclerView = findViewById(R.id.main_recycler_search);
        initRecycler();
        /*
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        */
        materialSearchBar = findViewById(R.id.main_search_bar);
        //init DB
        database = new Database(this);

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
