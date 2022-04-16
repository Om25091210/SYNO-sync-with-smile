package in.aryomtech.syno;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosio.myapplication.views.DuoDrawerLayout;
import com.mosio.myapplication.views.DuoMenuView;
import com.mosio.myapplication.widgets.DuoDrawerToggle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import www.sanju.motiontoast.MotionToast;

public class Home extends AppCompatActivity implements DuoMenuView.OnMenuClickListener  {

    private menuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    LottieAnimationView create;
    private ArrayList<String> mTitles = new ArrayList<>();
    SmoothBottomBar bottomBar;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView txt_header;
    Uri deep_link_uri;
    DatabaseReference user_ref;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference post_ref;
    private AppUpdateManager mAppUpdateManager;
    private final int RC_APP_UPDATE = 999;
    private int inAppUpdateType;
    boolean is_admin=false;
    private com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask;
    private InstallStateUpdatedListener installStateUpdatedListener;
    DatabaseReference debug_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setStatusBarTransparent();
        user_ref= FirebaseDatabase.getInstance().getReference().child("users");
        post_ref=FirebaseDatabase.getInstance().getReference().child("post");

        // Creates instance of the manager.
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();
        //lambda operation used for below listener
        //For flexible update
        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        };
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        //For Immediate
        inAppUpdateType = AppUpdateType.IMMEDIATE; //1
        inAppUpdate();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        is_admin=getSharedPreferences("admin_or_not", Context.MODE_PRIVATE)
                .getBoolean("isadmin_or_not101",false);

        txt_header=findViewById(R.id.txt_header);
        user_ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   String name=snapshot.child("name").getValue(String.class);
                   if(name!=null) {
                       String[] parts = name.split("\\s+");
                       String firstname = "Welcome " + parts[0];
                       txt_header.setText(firstname);
                   }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        debug_ref= FirebaseDatabase.getInstance().getReference().child("debugging");
        debug_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Objects.equals(snapshot.getValue(String.class), "yes") && !is_admin){
                    Intent intent=new Intent(Home.this,Maintainence.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        deep_link_uri = getIntent().getData();//deep link value

        // Show main fragment in container
        goToFragment(new home_content());
        mMenuAdapter.setViewSelected(0);
        setTitle(mTitles.get(0));

        create=findViewById(R.id.create);
        create.setOnClickListener(v->{
            if (Home.this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                Home.this.getSupportFragmentManager()
                        .beginTransaction().
                        remove(Objects.requireNonNull(Home.this.getSupportFragmentManager().findFragmentById(R.id.container))).commit();
            }
            Home.this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.drawer, new post(), "post")
                    .commit();
        });
        //smooth tab bar
        bottomBar=findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(0);
        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            if (i == 0) {
                bottomBar.setItemActiveIndex(0);
                if (getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new home_content())
                        .commit();
            } else if (i == 1) {
                bottomBar.setItemActiveIndex(1);
                if (Home.this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                    Home.this.getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(Home.this.getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                }
                Home.this.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Sync(), "list_announcement")
                        .commit();
            } else if (i == 2) {
                bottomBar.setItemActiveIndex(2);
                if (Home.this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                    Home.this.getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(Home.this.getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                }
                Home.this.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Profile(), "list_announcement")
                        .commit();
            }
            return false;
        });

        findViewById(R.id.card_fb).setOnClickListener(s-> {
            String facebookUrl ="https://www.facebook.com/profile.php?id=100076654766286";
            Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            facebookAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(facebookAppIntent);
        });
        findViewById(R.id.card_twitter).setOnClickListener(s->{
            String url = "https://twitter.com/syno_Gecb?t=7c06cvzEPdgEj9CgRFxFnw&s=09";
            Intent twitterAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            twitterAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(twitterAppIntent);
        });

        findViewById(R.id.card_linkedin).setOnClickListener(s->{
            String url = "https://www.linkedin.com/in/syno-gec-1323b322a";
            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(linkedInAppIntent);
        });
        findViewById(R.id.card_whatsapp).setOnClickListener(s->{
            String url = "https://api.whatsapp.com/send?phone=" +"+91"+ "9301982112";
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        findViewById(R.id.card_insta).setOnClickListener(s->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"__syno___";
            String path = "https://instagram.com/"+"__syno___";
            String nomPackageInfo ="com.instagram.android";
            try {
                getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });

    }
    private void setStatusBarTransparent() {
        Window window = Home.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
    private void handleMenu() {
        mMenuAdapter = new menuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }
    private void goToFragment(Fragment fragment) {
        if(deep_link_uri!=null){
            if (deep_link_uri.toString().equals("https://syno.android")){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.container, fragment,"mainFrag").commit();
            }
            else if(deep_link_uri.toString().equals("http://syno.android")){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.container, fragment,"mainFrag").commit();
            }
            else if(deep_link_uri.toString().equals("syno.android")){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.container, fragment,"mainFrag").commit();
            }
            else{
                // if the uri is not null then we are getting the
                // path segments and storing it in list.
                List<String> parameters = deep_link_uri.getPathSegments();
                // after that we are extracting string from that parameters.
                if(parameters!=null) {
                    if(parameters.size()>1) {
                        String check_profile=parameters.get(parameters.size()-2);
                        if(check_profile.trim().equals("profile")){

                            String name=parameters.get(parameters.size()-1);
                            String uid=parameters.get(parameters.size()-3);
                            //sending values to home_content frag for opening profile...
                            Bundle args = new Bundle();
                            args.putString("deep_link_name", name);
                            args.putString("deep_link_uid_value_profile", uid);
                            fragment.setArguments(args);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.add(R.id.container, fragment, "mainFrag").commit();

                        }
                        else {
                            String param = parameters.get(parameters.size() - 1);
                            String uid = parameters.get(parameters.size() - 2);
                            // on below line we are setting
                            // that string to our text view
                            // which we got as params.
                            Log.e("deep_link_value", param + "");
                            Log.e("deep_link_value_uid", uid + "");
                            Bundle args = new Bundle();
                            args.putString("deep_link_value", param);
                            args.putString("deep_link_uid_value", uid);
                            fragment.setArguments(args);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.add(R.id.container, fragment, "mainFrag").commit();
                        }
                    }
                    else{
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.add(R.id.container, fragment,"mainFrag").commit();
                    }
                }
            }
        }
        else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, fragment,"mainFrag").commit();
        }
    }
    @Override
    public void onFooterClicked() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Home.this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Home.this, task -> MotionToast.Companion.darkColorToast(Home.this,
                        "Logout Successfull",
                        "Sign out Successfull!",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(Home.this, R.font.quicksand_bold)));
        auth.signOut();
        startActivity(new Intent(Home.this , Splash.class));
        finish();
    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title


        // Set the right options selected
        mMenuAdapter.setViewSelected(position);

        // Navigate to the right fragment
        if(position==1) {
            Intent web=new Intent(Home.this, d_Website.class);
            web.putExtra("link_send","https://pages.flycricket.io/syno/privacy.html");
            startActivity(web);
            mMenuAdapter.setViewSelected(0);
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        else if(position==2) {
            Home.this.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.drawer,new about_syno())
                    .addToBackStack(null)
                    .commit();
            mMenuAdapter.setViewSelected(0);
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        else if(position==3) {
            String title ="*SYNO - sync with smile*"+"\n\n"+"Download this app to stay synced"; //Text to be shared
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title+"\n\n"+"This is a playstore link to download.. " + "https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
            mMenuAdapter.setViewSelected(0);
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        else if(position==4) {
            Home.this.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.drawer,new about_dev())
                    .addToBackStack(null)
                    .commit();
            mMenuAdapter.setViewSelected(0);
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        else if(position==5){
            Intent web=new Intent(Home.this, d_Website.class);
            web.putExtra("link_send","https://pages.flycricket.io/syno/terms.html");
            startActivity(web);
            mMenuAdapter.setViewSelected(0);
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        else if(position==6){
            Dialog dialog = new Dialog(Home.this);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_for_sure);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView cancel=dialog.findViewById(R.id.textView96);
            TextView yes=dialog.findViewById(R.id.textView95);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            cancel.setOnClickListener(vi-> dialog.dismiss());
            yes.setOnClickListener(vi-> {
                post_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            if(Objects.equals(snapshot.child(Objects.requireNonNull(ds.getKey())).child("uid").getValue(String.class), user.getUid())){
                                post_ref.child(ds.getKey()).removeValue();
                            }
                        }
                        check_for_views();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            });
            mMenuAdapter.setViewSelected(0);
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        else {
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        // Close the drawer

    }

   private void check_for_views() {
        user_ref.child(user.getUid()).removeValue();
        MotionToast.Companion.darkColorToast(Home.this,
                "Deleted Successfully.",
                "Account Removed.",
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(Home.this,R.font.quicksand_bold));
        onFooterClicked();
    }

    private void inAppUpdate() {

        try {
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(inAppUpdateType)) {
                    // Request the update.

                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                inAppUpdateType,
                                // The current activity making the update request.
                                Home.this,
                                // Include a request code to later monitor this update request.
                                RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException ignored) {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            //when user clicks update button
            if (resultCode == RESULT_OK) {
                Toast.makeText(Home.this, "App download starts...", Toast.LENGTH_LONG).show();
            } else if (resultCode != RESULT_CANCELED) {
                //if you want to request the update again just call checkUpdate()
                Toast.makeText(Home.this, "App download canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                Toast.makeText(Home.this, "App download failed.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void popupSnackbarForCompleteUpdate() {
        try {
            Snackbar snackbar =
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            "An update has just been downloaded.\nRestart to update",
                            Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("INSTALL", view -> {
                if (mAppUpdateManager != null){
                    mAppUpdateManager.completeUpdate();
                }
            });
            snackbar.setActionTextColor(ResourcesCompat.getColor(getResources(),R.color.green_A400,null));
            snackbar.show();

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        try {
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() ==
                        UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                inAppUpdateType,
                                this,
                                RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });


            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                //For flexible update
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }
    @Override
    protected void onDestroy() {
        mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onDestroy();
    }
    private class ViewHolder {
        private final DuoDrawerLayout mDuoDrawerLayout;
        private final DuoMenuView mDuoMenuView;
        private final ImageView mToolbar;

        ViewHolder() {
            mDuoDrawerLayout =findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = findViewById(R.id.toolbar);
        }
    }


}