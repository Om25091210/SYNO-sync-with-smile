package in.aryomtech.syno;

import static com.av.smoothviewpager.utils.Smoolider_Utils.autoplay_viewpager;
import static com.av.smoothviewpager.utils.Smoolider_Utils.stop_autoplay_ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.av.smoothviewpager.Smoolider.SmoothViewpager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import in.aryomtech.syno.Adapter.postAdapter;
import in.aryomtech.syno.Model.post_data;
import in.aryomtech.syno.data.ModelSmoolider;
import in.aryomtech.syno.data.SmooliderAdapter;
import me.ibrahimsn.lib.SmoothBottomBar;


public class home_content extends Fragment {

    List<String> titles=new ArrayList<>();
    List<String> position=new ArrayList<>();
    private ShimmerFrameLayout shimmer_slider;
    int counter=0;
    private boolean is_autoplay = false;
    DatabaseReference post_ref;
    ProgressBar progressBar;
    boolean is_admin=false;
    private SmoothViewpager viewPager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String deep_link_value,deep_link_uid_value,deep_link_name,deep_link_uid_value_profile;
    private List<ModelSmoolider> feedItemList;
    private Context contextNullSafe;
    RecyclerView recyclerView;
    DatabaseReference users_ref;
    ArrayList<post_data> list=new ArrayList<>();
    postAdapter postAdapter;
    FirebaseAuth auth;
    FirebaseUser user;
    SmoothBottomBar smoothBottomBar;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home_content, container, false);
        if (contextNullSafe == null) getContextNullSafety();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        users_ref=FirebaseDatabase.getInstance().getReference().child("users");
        admin_check();
        try {
            deep_link_value = getArguments().getString("deep_link_value");//deep link value
            deep_link_uid_value = getArguments().getString("deep_link_uid_value");//deep link uid value
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            deep_link_name=getArguments().getString("deep_link_name");
            deep_link_uid_value_profile=getArguments().getString("deep_link_uid_value_profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fresco.initialize(
                getContextNullSafety(),
                ImagePipelineConfig.newBuilder(getContextNullSafety())
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());

        if(deep_link_name!=null){
            DatabaseReference users_ref=FirebaseDatabase.getInstance().getReference().child("users");
            users_ref.child(deep_link_uid_value_profile).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Profile profile = new Profile();
                        Bundle args = new Bundle();
                        args.putString("uid_sending_profile", deep_link_uid_value_profile);
                        profile.setArguments(args);
                        ((FragmentActivity) getContext()).getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                .add(R.id.container, profile)
                                .addToBackStack(null)
                                .commit();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        if(deep_link_value!=null){
            DatabaseReference post_ref=FirebaseDatabase.getInstance().getReference().child("post");
            post_ref.child(deep_link_value).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           String title=snapshot.child("title").getValue(String.class);
                           String category=snapshot.child("category").getValue(String.class);
                           String city=snapshot.child("city").getValue(String.class);
                           String link=snapshot.child("link").getValue(String.class);
                           String seen=snapshot.child("seen").getValue(String.class);
                           String date=snapshot.child("date").getValue(String.class);
                           String image_link=snapshot.child("image_link").getValue(String.class);
                           String body=snapshot.child("body").getValue(String.class);

                           view_post fluid_overview=new view_post();
                           Bundle args=new Bundle();
                           args.putString("pushkey_sending",deep_link_value);
                           args.putString("uid_sending",deep_link_uid_value);
                           args.putString("title_sending",title);
                           args.putString("body_sending",body);
                           args.putString("category_sending",category);
                           args.putString("city_sending",city);
                           args.putString("date_sending",date);
                           args.putString("link_sending",link);
                           args.putString("image_sending",image_link);
                           args.putString("seen_sending",seen);

                           fluid_overview.setArguments(args);

                           ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                                   .beginTransaction()
                                   .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                                   .add(R.id.drawer,fluid_overview)
                                   .addToBackStack(null)
                                   .commit();
                       }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        //wait

        smoothBottomBar=getActivity().findViewById(R.id.bottomBar);
        smoothBottomBar.setItemActiveIndex(0);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getContextNullSafety(), R.color.white));
        window.setNavigationBarColor(ContextCompat.getColor(getContextNullSafety(),R.color.white));

        viewPager = view.findViewById(R.id.smoolider);

        shimmer_slider = view.findViewById(R.id.shimmer_slider);
        progressBar=view.findViewById(R.id.progressBar2);
        recyclerView=view.findViewById(R.id.rv_home);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);

        post_ref= FirebaseDatabase.getInstance().getReference().child("post");

        mSwipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.green, R.color.orange, R.color.purple_300);
        mSwipeRefreshLayout.setOnRefreshListener(this::get_data);
        get_data();
        generate_items();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/myTopic3")
                .addOnCompleteListener(task -> {
                    String msg = "Done";
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                    }
                    Log.d("topic_log", msg);
                });
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
        return view;
    }
    private void generate_items(){
        feedItemList = new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("slider");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keys: snapshot.getChildren()){
                    counter++;
                    position.add(""+counter+" / "+snapshot.getChildrenCount());
                    titles.add(snapshot.child(Objects.requireNonNull(keys.getKey())).child("head_text").getValue(String.class));
                    ModelSmoolider gift = new ModelSmoolider();
                    gift.setImage_url(snapshot.child(Objects.requireNonNull(keys.getKey())).child("image_url").getValue(String.class));
                    gift.setHead_text(snapshot.child(Objects.requireNonNull(keys.getKey())).child("head_text").getValue(String.class));
                    gift.setDes_text(snapshot.child(Objects.requireNonNull(keys.getKey())).child("des_text").getValue(String.class));
                    gift.setDeep_link(snapshot.child(Objects.requireNonNull(keys.getKey())).child("deep_link").getValue(String.class));
                    feedItemList.add(gift);
                }
                Collections.reverse(titles);
                Collections.reverse(feedItemList);
                shimmer_slider.hideShimmer();
                shimmer_slider.setVisibility(View.GONE);
                //slider
                viewPager.setAdapter(new SmooliderAdapter(feedItemList,getContextNullSafety()));
                manage_autoplay();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void get_data() {
        post_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    mSwipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(post_data.class));
                    }
                    Collections.reverse(list);
                    postAdapter = new postAdapter(getContextNullSafety(), list);
                    postAdapter.notifyDataSetChanged();
                    if (recyclerView != null)
                        recyclerView.setAdapter(postAdapter);
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                   progressBar.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void admin_check(){
        users_ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                is_admin= snapshot.child("admin").exists();
                if(is_admin){
                    getContextNullSafety().getSharedPreferences("admin_or_not",Context.MODE_PRIVATE).edit()
                            .putBoolean("isadmin_or_not101",true).apply();
                }
                else{
                    getContextNullSafety().getSharedPreferences("admin_or_not",Context.MODE_PRIVATE).edit()
                            .putBoolean("isadmin_or_not101",false).apply();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void manage_autoplay(){
        if(is_autoplay){
            is_autoplay = false;
            stop_autoplay_ViewPager();
        } else {
            is_autoplay = true;
            autoplay_viewpager(viewPager,feedItemList.size());
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }
    /**CALL THIS IF YOU NEED CONTEXT*/
    public Context getContextNullSafety() {
        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }
}