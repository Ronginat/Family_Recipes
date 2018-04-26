package ronapplication.com.recipes.Provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import ronapplication.com.recipes.Model.Recipe;

public class Database extends SQLiteAssetHelper
{
    private static final String DB_NAME = "recipes.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //Function get all recipes
    public List<Recipe> getRecipes(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Id", "FileName", "Name", "Description", "Category1"};
        String tableName = "Recipes";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect,null,null,null,null,null);
        List<Recipe> result = new ArrayList<>();
        String[] columns = cursor.getColumnNames();
        if(cursor.moveToFirst()){
            do{
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndex(columns[0])));
                recipe.setFileName(cursor.getString(cursor.getColumnIndex(columns[1])));
                recipe.setName(cursor.getString(cursor.getColumnIndex(columns[2])));
                recipe.setDescription(cursor.getString(cursor.getColumnIndex(columns[3])));
                recipe.setKind(cursor.getString(cursor.getColumnIndex(columns[4])));

                result.add(recipe);
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    //Function get all recipes description
    public List<String> getDescriptions(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Description"};
        String tableName = "Recipes";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect,null,null,null,null,null);
        List<String> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(cursor.getColumnIndex("Description")));
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    //Function get all recipes names
    public List<String> getNames(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Name"};
        String tableName = "Recipes";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect,null,null,null,null,null);
        List<String> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(cursor.getColumnIndex("Name")));
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    //Function get recipe by description
    public List<Recipe> getRecipeByDescription(String desc){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Id", "FileName", "Name", "Description", "Category1"};
        String tableName = "Recipes";

        qb.setTables(tableName);
        //if you want to get the exact name
        //Cursor cursor = qb.query(db, sqlSelect,"Description = ?",new String[]{desc},null,null,null);

        //will be like query : Select * From Recipes Where Description LIKE %pattern%
        Cursor cursor = qb.query(db, sqlSelect,"Description LIKE ?",new String[]{"%"+desc+"%"},null,null,null);

        List<Recipe> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                recipe.setFileName(cursor.getString(cursor.getColumnIndex("FileName")));
                recipe.setName(cursor.getString(cursor.getColumnIndex("Name")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                recipe.setKind(cursor.getString(cursor.getColumnIndex("Category1")));

                result.add(recipe);
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    //Function get recipe by name
    public List<Recipe> getRecipeByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Id", "FileName", "Name", "Description", "Category1"};
        String tableName = "Recipes";

        qb.setTables(tableName);
        //if you want to get the exact name
        //Cursor cursor = qb.query(db, sqlSelect,"Description = ?",new String[]{desc},null,null,null);

        //will be like query : Select * From Recipes Where Description LIKE %pattern%
        Cursor cursor = qb.query(db, sqlSelect,"Name LIKE ?",new String[]{"%"+name+"%"},null,null,null);

        List<Recipe> result = new ArrayList<>();
        String[] columns = cursor.getColumnNames();
        if(cursor.moveToFirst()){
            do{
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndex(columns[0])));
                recipe.setFileName(cursor.getString(cursor.getColumnIndex(columns[1])));
                recipe.setName(cursor.getString(cursor.getColumnIndex(columns[2])));
                recipe.setDescription(cursor.getString(cursor.getColumnIndex(columns[3])));
                recipe.setKind(cursor.getString(cursor.getColumnIndex(columns[4])));

                result.add(recipe);
            }
            while(cursor.moveToNext());
        }
        return result;
    }
}
