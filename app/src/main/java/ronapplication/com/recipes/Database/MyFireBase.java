package ronapplication.com.recipes.Database;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.Executor;

import ronapplication.com.recipes.Constants;

public class MyFireBase {
    final String Tag = "MyFireBase";
    private DatabaseReference database;
    private StorageReference storageRef;
    private String recipes_dir = Constants.FIREBASE_IMAGES_PATH + "recipes/";
    private String food_pictures_dir = Constants.FIREBASE_IMAGES_PATH + "food_pictures/";
    private String food_thumbnail_dir = "food_thumbnails/";
    private FirebaseAuth mAuth;
    private boolean isSignIn = false;
    private Uri requestedUri = null;
    public MyFireBase() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            isSignIn = true;
        } else {
            signInAnonymously();
        }
        // Create a storage reference from our app
    }

    public Uri DownloadPhoto(String fileName, int directory) {
        Log.e(Tag, "start downloadPhoto");
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
                    requestedUri = uri;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    int n = 0;
                    // Handle any errors
                }
            });
        }
        Log.e(Tag, "end downloadPhoto");
        return requestedUri;
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously();
                /*.addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        }) .addOnFailureListener(this, new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
                //Log.e("TAG", "signInAnonymously:FAILURE", exception);
            }
        });
    }
    */
    }
}
