package ronapplication.com.recipes.Provider.FireBaseStorage;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import ronapplication.com.recipes.Constants;
import ronapplication.com.recipes.R;

public class MyFireBaseStorage {

    private final String Tag = "MyFireBaseStorage";
    private String recipes_dir = Constants.FIREBASE_IMAGES_PATH + "recipes/";
    private String food_pictures_dir = Constants.FIREBASE_IMAGES_PATH + "food_pictures/";
    private String food_thumbnail_dir = "food_thumbnails/";
    ProgressBar progressBar;
    private StorageReference storageRef;

    private Context context;

    public MyFireBaseStorage(Context context, ProgressBar progressBar){
        this.context = context;
        this.progressBar = progressBar;
    }

    public void DownloadAndSetPhotoInView(String fileName, int directory, final ImageView imageView) {
        Log.e(Tag, "start downloadPhoto");
        progressBar.setVisibility(View.VISIBLE);
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference storageRefTemp = null;
        switch (directory) {
            case Constants.STORAGE_IMAGES_RECIPES:
                storageRefTemp = storageRef.child(recipes_dir);
                break;
            case Constants.STORAGE_IMAGES_FOOD_PICTURES:
                storageRefTemp = storageRef.child(food_pictures_dir);
                break;
            case Constants.STORAGE_IMAGES_FOOD_THUMBNAILS:
                storageRefTemp = storageRef.child(food_thumbnail_dir);
                break;
        }
        if (storageRefTemp != null) {
            storageRefTemp.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if(uri != null)
                        Picasso.get().load(uri).into(imageView);
                    else
                        Picasso.get().load(R.drawable.image_not_found).into(imageView);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Picasso.get().load(R.drawable.image_not_found).into(imageView);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "can't retrieve the photo", Toast.LENGTH_SHORT).show();
                    ///imageView.setImageResource(R.drawable.image_not_found);
                }
            });
        }
        Log.e(Tag, "end downloadPhoto");
    }
}
