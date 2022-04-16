package in.aryomtech.syno;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import in.aryomtech.syno.Adapter.postAdapter;
import in.aryomtech.syno.Model.post_data;
import me.ibrahimsn.lib.SmoothBottomBar;
import soup.neumorphism.NeumorphCardView;
import www.sanju.motiontoast.MotionToast;


public class Profile extends Fragment {

    View view;
    ImageView edit,tick,share_profile;
    private Context contextNullSafe;
    FirebaseAuth auth;
    FirebaseUser user;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    DatabaseReference user_ref;
    String email="",youtube="",linkedIn="",instagram="",github="";
    ImageView profile_image;
    String str_interest="";
    RecyclerView recyclerView;
    TextView txt_name,br_sem,text_interest,pr_bio,txt_bio,pr_skill,txt_skill,pr_project,txt_project;
    NeumorphCardView card_youtube,card_linkedin,card_email,card_insta,card_github;
    ArrayList interests;
    ArrayList<post_data> list=new ArrayList<>();
    DatabaseReference post_ref;
    String uid_of_user,addtostack;
    LottieAnimationView star;
    TextView edit_text;
    postAdapter postAdapter;
    ImageView delete_profile;
    SmoothBottomBar smoothBottomBar;
    boolean isadmin=false;
    boolean syno_admin=false;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (contextNullSafe == null) getContextNullSafety();

        try {
            addtostack=getArguments().getString("sending_user_from_sync");
            uid_of_user = getArguments().getString("uid_sending_profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        user_ref= FirebaseDatabase.getInstance().getReference().child("users");
        post_ref= FirebaseDatabase.getInstance().getReference().child("post");

        profile_image=view.findViewById(R.id.profile_image);
        txt_name=view.findViewById(R.id.textView14);
        br_sem=view.findViewById(R.id.textView15);
        card_youtube=view.findViewById(R.id.card_youtube);
        card_linkedin=view.findViewById(R.id.card_linkedin);
        card_email=view.findViewById(R.id.card_email);
        card_insta=view.findViewById(R.id.card_insta);
        card_github=view.findViewById(R.id.card_github);
        text_interest=view.findViewById(R.id.textView21);
        pr_bio=view.findViewById(R.id.pr_bio);
        txt_bio=view.findViewById(R.id.bio);
        pr_skill=view.findViewById(R.id.pr_skill);
        txt_skill=view.findViewById(R.id.textView24);
        pr_project=view.findViewById(R.id.pr_project);
        txt_project=view.findViewById(R.id.projects);
        edit_text=view.findViewById(R.id.textView34);
        tick=view.findViewById(R.id.imageView7);
        star=view.findViewById(R.id.ad_star);
        share_profile=view.findViewById(R.id.share_profile);
        delete_profile=view.findViewById(R.id.imageRemoveImage);

        tick.setVisibility(View.GONE);
        br_sem.setVisibility(View.GONE);
        card_youtube.setVisibility(View.GONE);
        card_linkedin.setVisibility(View.GONE);
        card_insta.setVisibility(View.GONE);
        card_github.setVisibility(View.GONE);
        pr_bio.setVisibility(View.GONE);
        txt_bio.setVisibility(View.GONE);
        pr_skill.setVisibility(View.GONE);
        txt_skill.setVisibility(View.GONE);
        pr_project.setVisibility(View.GONE);
        txt_project.setVisibility(View.GONE);

        isadmin=getContextNullSafety().getSharedPreferences("admin_or_not",Context.MODE_PRIVATE)
                .getBoolean("isadmin_or_not101",false);

        if(uid_of_user==null) {
            uid_of_user = user.getUid();//check for uid bundle if yes then don't do this and vice-versa.
        }

        smoothBottomBar=getActivity().findViewById(R.id.bottomBar);
        smoothBottomBar.setItemActiveIndex(2);

        delete_profile.setOnClickListener(v->delete_by_admin());

        recyclerView=view.findViewById(R.id.rv_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(layoutManager);

        edit=view.findViewById(R.id.imageView6);
        if(uid_of_user.equals(user.getUid())) {
            edit.setVisibility(View.VISIBLE);
            edit_text.setVisibility(View.VISIBLE);
        }
        else {
            edit.setVisibility(View.GONE);
            edit_text.setVisibility(View.GONE);
        }

        is_syno_verified();

        edit.setOnClickListener(v->{
            Edit_profile edit_profile=new Edit_profile();
            Bundle args=new Bundle();
            args.putString("uid_sending_profile",uid_of_user);
            edit_profile.setArguments(args);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.drawer,edit_profile)
                    .addToBackStack(null)
                    .commit();
        });

        get_user_data();

        mSwipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.green, R.color.orange, R.color.purple_300);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if(interests!=null)
                interests.clear();
            str_interest="";
            get_data();
            get_user_data();
        });
        new Handler(Looper.myLooper()).postDelayed(this::get_data,500);
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(addtostack!=null){
                    smoothBottomBar.setItemActiveIndex(1);
                    FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    if(fm.getBackStackEntryCount()>0) {
                        fm.popBackStack();
                    }
                    ft.commit();
                }
                else {
                    if (((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer) != null) {
                        ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                                .beginTransaction().
                                remove(Objects.requireNonNull(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer))).commit();
                    }
                    ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new home_content())
                            .commit();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        share_profile.setOnClickListener(v->{
            String[] parts = txt_name.getText().toString().split("\\s+");
            String firstname = ""+parts[0];
            String profile_deep_link="https://syno.android/mosioco/"+uid_of_user+"/profile"+"/"+firstname;

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, profile_deep_link);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        });

        return view;
    }

    private void is_syno_verified() {
        user_ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                syno_admin= snapshot.child("syno_admin").exists();
                if(syno_admin)
                    delete_profile.setVisibility(View.VISIBLE);
                else
                    delete_profile.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    private void delete_by_admin() {
        dialog = new Dialog(getContextNullSafety());
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
                        if(Objects.equals(snapshot.child(Objects.requireNonNull(ds.getKey())).child("uid").getValue(String.class), uid_of_user)){
                            post_ref.child(ds.getKey()).removeValue();
                        }
                    }
                    check_for_views();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        });
    }
    private void check_for_views() {
        dialog.dismiss();
        user_ref.child(uid_of_user).removeValue();
        MotionToast.Companion.darkColorToast(getActivity(),
                "Deleted Successfully.",
                "Account Removed.",
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContextNullSafety(),R.font.quicksand_bold));
    }

    private void get_data() {
        post_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    mSwipeRefreshLayout.setRefreshing(false);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if(Objects.equals(snapshot.child(Objects.requireNonNull(ds.getKey())).child("uid").getValue(String.class), uid_of_user)) {
                            list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(post_data.class));
                        }
                    }
                    Collections.reverse(list);
                    postAdapter = new postAdapter(getContextNullSafety(), list);
                    postAdapter.notifyDataSetChanged();
                    if (recyclerView != null)
                        recyclerView.setAdapter(postAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void get_user_data() {
        user_ref.child(uid_of_user+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (snapshot.exists()) {
                    String profile_img = snapshot.child("profile_emoji").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String semester = snapshot.child("semester").getValue(String.class);
                    String branch = snapshot.child("branch").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    youtube = snapshot.child("youtube").getValue(String.class);
                    linkedIn = snapshot.child("linkedin").getValue(String.class);
                    instagram = snapshot.child("instagram").getValue(String.class);
                    github = snapshot.child("github").getValue(String.class);
                    String bio = snapshot.child("bio").getValue(String.class);
                    interests = (ArrayList) snapshot.child("interests").getValue();
                    String skills = snapshot.child("skills").getValue(String.class);
                    String projects = snapshot.child("projects").getValue(String.class);
                    String roll_noo = snapshot.child("roll_no").getValue(String.class);
                    boolean admin = snapshot.child("admin").exists();

                    if (profile_img != null) {
                        if (Objects.equals(profile_img, "em_1"))
                            profile_image.setImageResource(R.drawable.em_1);
                        else if (Objects.equals(profile_img, "em_2"))
                            profile_image.setImageResource(R.drawable.em_2);
                        else if (Objects.equals(profile_img, "em_3"))
                            profile_image.setImageResource(R.drawable.em_3);
                        else if (Objects.equals(profile_img, "em_4"))
                            profile_image.setImageResource(R.drawable.em_4);
                        else if (Objects.equals(profile_img, "em_5"))
                            profile_image.setImageResource(R.drawable.em_5);
                        else if (Objects.equals(profile_img, "em_6"))
                            profile_image.setImageResource(R.drawable.em_6);
                        else if (Objects.equals(profile_img, "em_7"))
                            profile_image.setImageResource(R.drawable.em_7);
                        else if (Objects.equals(profile_img, "em_8"))
                            profile_image.setImageResource(R.drawable.em_8);
                    }

                    txt_name.setText(name);
                    if (branch != null) {
                        String str_br_name = semester + " " + branch;
                        br_sem.setVisibility(View.VISIBLE);
                        br_sem.setText(str_br_name);
                    }

                    if (admin) {
                        star.setVisibility(View.VISIBLE);
                    } else {
                        star.setVisibility(View.GONE);
                    }

                    if (!email.equals(""))
                        card_email.setVisibility(View.VISIBLE);
                    else
                        card_email.setVisibility(View.GONE);

                    if (youtube != null) {
                        if (!youtube.equals(""))
                            card_youtube.setVisibility(View.VISIBLE);
                        else
                            card_youtube.setVisibility(View.GONE);
                    }

                    if (linkedIn != null) {
                        if (!linkedIn.equals(""))
                            card_linkedin.setVisibility(View.VISIBLE);
                        else
                            card_linkedin.setVisibility(View.GONE);
                    }

                    if (instagram != null) {
                        if (!instagram.equals(""))
                            card_insta.setVisibility(View.VISIBLE);
                        else
                            card_insta.setVisibility(View.GONE);
                    }

                    if (github != null) {
                        if (!github.equals(""))
                            card_github.setVisibility(View.VISIBLE);
                        else
                            card_github.setVisibility(View.GONE);
                    }

                    if (bio != null) {
                        if (!Objects.equals(bio, "")) {
                            pr_bio.setVisibility(View.VISIBLE);
                            txt_bio.setVisibility(View.VISIBLE);
                            txt_bio.setText(bio);
                        } else {
                            pr_bio.setVisibility(View.GONE);
                            txt_bio.setVisibility(View.GONE);
                        }
                    }
                    if (interests != null) {
                        for (int i = 0; i < interests.size(); i++) {
                            str_interest = str_interest + "\u2022 " + interests.get(i) + "\n";
                        }
                        text_interest.setText(str_interest);
                    }

                    if (skills != null) {
                        if (!Objects.equals(skills, "")) {
                            pr_skill.setVisibility(View.VISIBLE);
                            txt_skill.setVisibility(View.VISIBLE);
                            txt_skill.setText(skills);
                        } else {
                            pr_skill.setVisibility(View.GONE);
                            txt_skill.setVisibility(View.GONE);
                        }
                    }

                    if (projects != null) {
                        if (!Objects.equals(projects, "")) {
                            pr_project.setVisibility(View.VISIBLE);
                            txt_project.setVisibility(View.VISIBLE);
                            txt_project.setText(projects);
                        } else {
                            pr_project.setVisibility(View.GONE);
                            txt_project.setVisibility(View.GONE);
                        }
                    }

                    if (roll_noo != null) {
                        if (roll_noo.equals("Faculty")) {
                            tick.setVisibility(View.VISIBLE);
                            String fac = "Faculty";
                            br_sem.setText(fac);
                        } else if (roll_noo.equals("Alumni")) {
                            tick.setVisibility(View.VISIBLE);
                            String fac = "Alumni";
                            br_sem.setText(fac);
                        } else
                            tick.setVisibility(View.GONE);
                    }

                }
                else{
                    Toast.makeText(getContextNullSafety(), "Account doesn't exist.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        card_github.setOnClickListener(v->{
            try {
                String url = "https://github.com/"+github;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                Toast.makeText(getContextNullSafety(), "Invalid link added by user", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        card_linkedin.setOnClickListener(v->{
            try {
                String url = linkedIn;
                Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                startActivity(linkedInAppIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Invalid link", Toast.LENGTH_SHORT).show();
            }
        });

        card_youtube.setOnClickListener(v->{
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(youtube));
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Invalid link", Toast.LENGTH_SHORT).show();
            }
        });

        card_email.setOnClickListener(v->{
            /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

            /* Send it off to the Activity-Chooser */
            getContextNullSafety().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        });
        card_insta.setOnClickListener(v->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+instagram;
            String path = "https://instagram.com/"+instagram;
            String nomPackageInfo ="com.instagram.android";
            try {
                requireContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });
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