package in.aryomtech.syno;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import java.util.Objects;

import in.aryomtech.syno.Adapter.syncAdapter;
import in.aryomtech.syno.Model.user_data;
import me.ibrahimsn.lib.SmoothBottomBar;


public class Sync extends Fragment {

    View view;
    private Context contextNullSafe;
    ImageView image;
    RecyclerView recyclerView;
    EditText search;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<user_data> list;
    ArrayList<user_data> mylist;
    TextView msg;
    syncAdapter syncAdapter;
    SmoothBottomBar smoothBottomBar;
    DatabaseReference user_ref;
    boolean verify=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sync, container, false);
        if (contextNullSafe == null) getContextNullSafety();
//Hide the keyboard
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        user_ref= FirebaseDatabase.getInstance().getReference().child("users");
        isverified();

        search=view.findViewById(R.id.search);
        list=new ArrayList<>();
        mylist=new ArrayList<>();
        image=view.findViewById(R.id.imageView3);
        msg=view.findViewById(R.id.textView27);
        recyclerView=view.findViewById(R.id.rv_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);

        smoothBottomBar=getActivity().findViewById(R.id.bottomBar);
        smoothBottomBar.setItemActiveIndex(1);

        get_users();
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer) != null) {
                    ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer))).commit();
                }
                ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,new home_content())
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s+"");
            }
        });


        return view;
    }
    private void isverified() {
        user_ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                verify= snapshot.child("roll_no").exists();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void get_users() {
        list.clear();
        mylist.clear();
        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    list.add(snapshot.child(ds.getKey()).getValue(user_data.class));
                }
                image.setVisibility(View.GONE);
                msg.setVisibility(View.GONE);
                syncAdapter=new syncAdapter(getContextNullSafety(),list,verify);
                syncAdapter.notifyDataSetChanged();
                if(recyclerView!=null)
                    recyclerView.setAdapter(syncAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void search(String s) {
        mylist.clear();
        for(user_data object: list){
            try {
                if (object.getName().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }else if (object.getBranch().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }else if (object.getSemester().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        syncAdapter syncAdapter=new syncAdapter(getContextNullSafety(),mylist,verify);
        syncAdapter.notifyDataSetChanged();
        if(recyclerView!=null)
            recyclerView.setAdapter(syncAdapter);
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