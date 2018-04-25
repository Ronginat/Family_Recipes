package ronapplication.com.recipes.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ronapplication.com.recipes.Constants;
import ronapplication.com.recipes.FullscreenActivity;
import ronapplication.com.recipes.Model.Recipe;
import ronapplication.com.recipes.R;

class SearchViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{

    private Context context;
    public TextView name, description, kind;//, category2;
    protected String fileName, recipeName;

    public SearchViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        itemView.setOnClickListener(this);
        name = itemView.findViewById(R.id.item_name);
        description = itemView.findViewById(R.id.item_description);
        kind = itemView.findViewById(R.id.item_food_kind);
        //category2 = itemView.findViewById(R.id.item_category2);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, FullscreenActivity.class);
        intent.putExtra(Constants.EXTRA_RECIPE_NAME, recipeName);
        intent.putExtra(Constants.EXTRA_FILE_NAME, fileName);
        context.startActivity(intent);
    }
}


public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Context context;
    private List<Recipe> recipes;

    public SearchAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_item, parent, false);
        return new SearchViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.name.setText(recipes.get(position).getName());
        holder.description.setText(recipes.get(position).getDescription());
        holder.kind.setText(recipes.get(position).getKind());
        holder.fileName = recipes.get(position).getFileName();
        holder.recipeName = recipes.get(position).getName();
        //holder.category2.setText(recipes.get(position).getCategory2());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
