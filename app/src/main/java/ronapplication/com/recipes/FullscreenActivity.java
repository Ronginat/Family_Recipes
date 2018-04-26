package ronapplication.com.recipes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import ronapplication.com.recipes.Provider.MyFireBase;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private final String Tag = "FullScreen";
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    boolean isNavigationVisible = false;

    //MyAsyncTaskImageLoader asyncTaskImageLoader;

    TouchImageView touchImageView;
    View mContentView;
    ProgressBar progressBar;
    private final Handler mHideHandler = new Handler();
    private Runnable mHideRunnable;

    private String fileName, recipeName;
    private MyFireBase fireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tag, "start onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //initHideRunnable();
        bindUIAndListeners();
        getParametersFromIntent();
        //setTitle(recipeName);

        if(!isNetworkAvailable()) {
            touchImageView = findViewById(R.id.full_screen_image);
            touchImageView.setImageResource(R.drawable.image_not_found);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }

        else{
            fireBase = new MyFireBase(getBaseContext(), progressBar);
            fireBase.DownloadAndSetPhotoInView(fileName, Constants.STORAGE_IMAGES_RECIPES, touchImageView);
            //asyncTaskImageLoader = new MyAsyncTaskImageLoader(this, touchImageView);
            //asyncTaskImageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //loadImageToTouchView();
        }
        Log.d(Tag, "end onCreate");
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void bindUIAndListeners(){
        mContentView = findViewById(R.id.fullscreen_content);
        touchImageView = findViewById(R.id.full_screen_image);
        progressBar = findViewById(R.id.full_screen_progressBar);

        /*
        touchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNavigationVisible) {
                    //mHideHandler.postDelayed(mHideRunnable, 500);
                    isNavigationVisible = false;
                }
                else{
                    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    isNavigationVisible = true;
                }
            }
        });
        */
    }

    private void initHideRunnable() {
        mHideRunnable = new Runnable() {
            @Override
            public void run() {
                // Delayed removal of status and navigation bar
                mContentView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        };
    }

    private void getParametersFromIntent() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            recipeName =(String) b.get(Constants.EXTRA_RECIPE_NAME);
            fileName =(String) b.get(Constants.EXTRA_FILE_NAME);
        }
    }

    private boolean isNetworkAvailable() {
        //int countAttempts = 0;
        //String dialogTitle = "Checking connection";
        //ProgressDialog dialog = null;
        //dialog = ProgressDialog.show(this, dialogTitle, "failed attempt #" + countAttempts);
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadImageToTouchView() {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageRef;
        storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(Constants.FIREBASE_IMAGES_PATH+Constants.STORAGE_RECIPES+fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri != null)
                    /*
                    Glide.with(getBaseContext()).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            touchImageView.setImageBitmap(resource);
                        }
                    });
                    */
                    Picasso.get().load(uri).into(touchImageView);
                else
                    Picasso.get().load(R.drawable.image_not_found).into(touchImageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Picasso.get().load(R.drawable.image_not_found).into(touchImageView);
                Toast.makeText(getApplicationContext(), "can't retrieve the photo", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2500);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

}
