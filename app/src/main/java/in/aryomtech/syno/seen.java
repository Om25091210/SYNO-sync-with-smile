package in.aryomtech.syno;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import in.aryomtech.syno.Adapter.seenAdapter;
import in.aryomtech.syno.Adapter.syncAdapter;
import in.aryomtech.syno.Model.user_data;


public class seen extends Fragment {

    View view;
    ProgressBar progressBar;
    String pushkey;
    ArrayList<user_data> list;
    private Context contextNullSafe;
    DatabaseReference post_ref,users_ref;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView back;
    boolean verify=false;
    List<String> uids_list=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_seen, container, false);
        if (contextNullSafe == null) getContextNullSafety();
        try {
            pushkey = getArguments().getString("pushkey_of_post");
        } catch (Exception e) {
            e.printStackTrace();
        }

        back=view.findViewById(R.id.imageView4);
        back.setOnClickListener(v-> back());

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        post_ref= FirebaseDatabase.getInstance().getReference().child("post");
        users_ref=FirebaseDatabase.getInstance().getReference().child("users");

        list=new ArrayList<>();
        progressBar=view.findViewById(R.id.progressBar2);
        recyclerView=view.findViewById(R.id.rv_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);

        isverified();

        get_seen_users();
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                if(fm.getBackStackEntryCount()>0) {
                    fm.popBackStack();
                }
                ft.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        return view;
    }
    private void back(){
        FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        ft.commit();
    }
    private void get_seen_users() {
        uids_list.clear();
        list.clear();
        post_ref.child(pushkey).child("view").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(!uids_list.contains(ds.getKey()))
                        uids_list.add(ds.getKey());
                }
                get_details_of_user();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void get_details_of_user() {
        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i=0;i<uids_list.size();i++){
                    if(snapshot.child(uids_list.get(i)).exists())
                        list.add(snapshot.child(uids_list.get(i)).getValue(user_data.class));
                    else
                        post_ref.child(pushkey).child("view").child(uids_list.get(i)).removeValue();
                }
                progressBar.setVisibility(View.GONE);
                seenAdapter seenAdapter=new seenAdapter(getContextNullSafety(),list,verify);
                seenAdapter.notifyDataSetChanged();
                if(recyclerView!=null)
                    recyclerView.setAdapter(seenAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void isverified() {
        users_ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                verify= snapshot.child("roll_no").exists();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }
}