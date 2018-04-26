package ronapplication.com.recipes.Provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Executor;

import ronapplication.com.recipes.Constants;
import ronapplication.com.recipes.MainActivity;
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
        /*
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AnonymousSignIn", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "signed in anonymously", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AnonymousSignIn", "signInAnonymously:failure", task.getException());
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                })
                */
    }
}
