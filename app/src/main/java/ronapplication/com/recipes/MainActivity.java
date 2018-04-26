package ronapplication.com.recipes;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
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
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import ronapplication.com.recipes.Adapter.SearchAdapter;
import ronapplication.com.recipes.Provider.InternalSQLDatabase.MySQLDatabase;
import ronapplication.com.recipes.Provider.MySuggestionProvider;

public class MainActivity extends AppCompatActivity {

    final String TAG = "main";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter adapter;

    FloatingActionButton floatingPlusButton;
    Toolbar toolbar;

    MaterialSearchBar materialSearchBar;
    //SearchView searchView;
    List<String> suggestList = new ArrayList<>();

    MySQLDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "start onCreate");

        //init View
        bindUI();
        setSupportActionBar(toolbar);

        /*
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        */
        initRecycler();
        /*
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        */

        //init DB
        database = new MySQLDatabase(this);

        //Setup search bar
        //initMaterialSearch();

        //init adapter default set all result
        adapter = new SearchAdapter(this, database.getRecipes());
        recyclerView.setAdapter(adapter);
        Log.e(TAG, "end onCreate");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);
        ConstraintLayout rootView = (ConstraintLayout)searchItem.getActionView();
        materialSearchBar = rootView.findViewById(R.id.my_material_search_bar);

        materialSearchBar.setMaxSuggestionCount(20);
        materialSearchBar.setHint("Search");
        materialSearchBar.setCardViewElevation(10);

        /*
        // Associate searchable configuration with the SearchView
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "onQueryTextSubmit",Toast.LENGTH_SHORT).show();
                saveRecentQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, "onQueryTextChange",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Toast.makeText(MainActivity.this, "onSuggestionSelect",Toast.LENGTH_SHORT).show();
                //searchView.setQuery(searchView.getSuggestionsAdapter().getItem(position).toString(), false);
                //Called when a suggestion was selected by navigating to it
                return true;
                //true if the listener handles the event and wants to override the default behavior of possibly
                // rewriting the query based on the selected item, false otherwise
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Toast.makeText(MainActivity.this, "onSuggestionClick",Toast.LENGTH_SHORT).show();
                //Called when a suggestion was clicked
                return true;
            }
        });
        */

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                floatingPlusButton.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "collapse search", Toast.LENGTH_SHORT).show();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                floatingPlusButton.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "expand search", Toast.LENGTH_SHORT).show();
                return true;  // Return true to expand action view
            }
        });


        /*
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        materialSearchBar =
                (MaterialSearchBar) searchItem.getActionView();
        //initMaterialSearch();
        */
        // Define the listener


        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this,"newIntent", Toast.LENGTH_SHORT).show();
            // Do work using string
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_clear_history:
                //Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("מחיקת היסטוריה");
                alertBuilder.setMessage("האם אתה בטוח שאתה רוצה למחוק את היסטוריית החיפושים?");
                alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearRecentSuggestionsHistory();
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

            case R.id.action_search:
                //onSearchRequested();
                //Toast.makeText(this, "search requested", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }



    private void bindUI() {
        recyclerView = findViewById(R.id.main_recycler_search);
        //materialSearchBar = findViewById(R.id.main_search_bar);
        //floatingMoreButton = findViewById(R.id.main_more_ActionButton);
        toolbar = findViewById(R.id.main_toolbar);
        floatingPlusButton = findViewById(R.id.main_add_recipe_button);
    }

    private void initMaterialSearch(){
        /*
        android:id="@+id/main_search_bar"
                app:mt_speechMode="false"
                app:mt_hint="Search"
                android:layout_marginBottom="5dp"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"></com.mancj.materialsearchbar.MaterialSearchBar>

         */
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
                    //floatingPlusButton.setVisibility(View.VISIBLE);
                }
                //else
                    //floatingPlusButton.setVisibility(View.GONE);
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

    private void saveRecentQuery(String query){
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void clearRecentSuggestionsHistory(){
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
        suggestions.clearHistory();
    }
}
