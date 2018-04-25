package ronapplication.com.recipes.Provider;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import ronapplication.com.recipes.Constants;
import ronapplication.com.recipes.Provider.FireBaseStorage.MyFireBaseStorage;

public class MyFireBase {
    final String Tag = "MyFireBase";
    private DatabaseReference database;
    private MyFireBaseStorage mStorage;
    private FirebaseAuth mAuth;
    private boolean isSignIn = false;
    private Context context;
    public MyFireBase(Context context, ProgressBar progressBar) {
        this.context = context;
        mStorage = new MyFireBaseStorage(context, progressBar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            isSignIn = true;
        } else {
            signInAnonymously();
        }
        // Create a storage reference from our app
    }

    public void DownloadAndSetPhotoInView(String fileName, int directory, final ImageView imageView) {
        mStorage.DownloadAndSetPhotoInView(fileName, directory, imageView);
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
