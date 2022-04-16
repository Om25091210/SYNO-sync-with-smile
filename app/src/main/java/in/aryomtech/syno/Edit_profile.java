package in.aryomtech.syno;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import soup.neumorphism.NeumorphCardView;
import www.sanju.motiontoast.MotionToast;


public class Edit_profile extends Fragment {

    View view;
    ImageView back;
    LottieAnimationView save;
    String profile_img;
    FirebaseAuth auth;
    FirebaseUser user;
    Dialog dialog;
    boolean isfac_al=false;
    String str_fac="";
    String semester;
    TextView pr_roll;
    DatabaseReference roll_ref;
    ArrayList<String> roll_list=new ArrayList<>();
    CircleImageView profile_image;
    NeumorphCardView em_1,em_2,em_3,em_4,em_5,em_6,em_7,em_8;
    private Context contextNullSafe;
    DatabaseReference user_ref;
    String uid_of_user;
    TextView edit_interest;
    String branch;
    TextView one, two,three,four,five,six,seven,eight;
    TextView cse,it,ett,ee,mining,mech,civil;
    EditText name,bio,youtube_link,linkedIn_link,email,instagram,skills,projects,roll,github_link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        if (contextNullSafe == null) getContextNullSafety();

        //binding views
        name=view.findViewById(R.id.editTextTextMultiLine);
        bio=view.findViewById(R.id.editTextTextMultiLine4);
        youtube_link=view.findViewById(R.id.editTextTextMultiLine5);
        linkedIn_link=view.findViewById(R.id.editTextTextMultiLine9);
        email=view.findViewById(R.id.editTextTextMultiLine10);
        instagram=view.findViewById(R.id.editTextTextMultiLine11);
        skills=view.findViewById(R.id.ed_skill);
        projects=view.findViewById(R.id.ed_project);
        roll=view.findViewById(R.id.roll);
        pr_roll=view.findViewById(R.id.pr_roll);
        edit_interest=view.findViewById(R.id.edit_interest);
        github_link=view.findViewById(R.id.editTextTextMultiLine1155);

        em_1=view.findViewById(R.id.em_1);
        em_2=view.findViewById(R.id.em_2);
        em_3=view.findViewById(R.id.em_3);
        em_4=view.findViewById(R.id.em_4);
        em_5=view.findViewById(R.id.em_5);
        em_6=view.findViewById(R.id.em_6);
        em_7=view.findViewById(R.id.em_7);
        em_8=view.findViewById(R.id.em_8);

        cse=view.findViewById(R.id.cse);
        it=view.findViewById(R.id.it);
        ett=view.findViewById(R.id.ett);
        ee=view.findViewById(R.id.elec);
        mining=view.findViewById(R.id.mining);
        mech=view.findViewById(R.id.mech);
        civil=view.findViewById(R.id.civil);

        one=view.findViewById(R.id.one);
        two=view.findViewById(R.id.two);
        three=view.findViewById(R.id.three);
        four=view.findViewById(R.id.four);
        five=view.findViewById(R.id.five);
        six=view.findViewById(R.id.six);
        seven=view.findViewById(R.id.seven);
        eight=view.findViewById(R.id.eight);




        user_ref= FirebaseDatabase.getInstance().getReference().child("users");
        roll_ref= FirebaseDatabase.getInstance().getReference().child("ROLL_NO");

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        try {
            uid_of_user = getArguments().getString("uid_sending_profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(uid_of_user==null)
            uid_of_user=user.getUid();

        save=view.findViewById(R.id.save);
        save.setOnClickListener(v->{
            upload();
        });

        profile_image=view.findViewById(R.id.profile_image);

        edit_interest.setOnClickListener(v->{
            Intent intent=new Intent(getContextNullSafety(),Edit_Interest.class);
            startActivity(intent);
        });

        em_1.setOnClickListener(v->{
            profile_img="em_1";
            profile_image.setImageResource(R.drawable.em_1);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        });

        em_2.setOnClickListener(v->{
            profile_img="em_2";
            profile_image.setImageResource(R.drawable.em_2);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        });

        em_3.setOnClickListener(v->{
            profile_img="em_3";
            profile_image.setImageResource(R.drawable.em_3);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        });

        em_4.setOnClickListener(v->{
            profile_img="em_4";
            profile_image.setImageResource(R.drawable.em_4);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        });

        em_5.setOnClickListener(v->{
            profile_img="em_5";
            profile_image.setImageResource(R.drawable.em_5);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        });

        em_6.setOnClickListener(v->{
            profile_img="em_6";
            profile_image.setImageResource(R.drawable.em_6);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        });

        em_7.setOnClickListener(v->{
            profile_img="em_7";
            profile_image.setImageResource(R.drawable.em_7);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        });

        em_8.setOnClickListener(v->{
            profile_img="em_8";
            profile_image.setImageResource(R.drawable.em_8);
            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
        });

        back=view.findViewById(R.id.imageView4);
        back.setOnClickListener(v->back());

        ///

        cse.setOnClickListener(v->{
            branch="CSE";
            cse.setBackgroundResource(R.drawable.bg_branch_edit);

            it.setBackgroundResource(R.drawable.border_amount_bg);
            ee.setBackgroundResource(R.drawable.border_amount_bg);
            ett.setBackgroundResource(R.drawable.border_amount_bg);
            mining.setBackgroundResource(R.drawable.border_amount_bg);
            mech.setBackgroundResource(R.drawable.border_amount_bg);
            civil.setBackgroundResource(R.drawable.border_amount_bg);
        });

        it.setOnClickListener(v->{
            branch="IT";
            it.setBackgroundResource(R.drawable.bg_branch_edit);

            cse.setBackgroundResource(R.drawable.border_amount_bg);
            ee.setBackgroundResource(R.drawable.border_amount_bg);
            ett.setBackgroundResource(R.drawable.border_amount_bg);
            mining.setBackgroundResource(R.drawable.border_amount_bg);
            mech.setBackgroundResource(R.drawable.border_amount_bg);
            civil.setBackgroundResource(R.drawable.border_amount_bg);
        });

        ee.setOnClickListener(v->{
            branch="Electrical";
            ee.setBackgroundResource(R.drawable.bg_branch_edit);

            cse.setBackgroundResource(R.drawable.border_amount_bg);
            it.setBackgroundResource(R.drawable.border_amount_bg);
            ett.setBackgroundResource(R.drawable.border_amount_bg);
            mining.setBackgroundResource(R.drawable.border_amount_bg);
            mech.setBackgroundResource(R.drawable.border_amount_bg);
            civil.setBackgroundResource(R.drawable.border_amount_bg);
        });

        ett.setOnClickListener(v->{
            branch="ET&T";
            ett.setBackgroundResource(R.drawable.bg_branch_edit);

            cse.setBackgroundResource(R.drawable.border_amount_bg);
            it.setBackgroundResource(R.drawable.border_amount_bg);
            ee.setBackgroundResource(R.drawable.border_amount_bg);
            mining.setBackgroundResource(R.drawable.border_amount_bg);
            mech.setBackgroundResource(R.drawable.border_amount_bg);
            civil.setBackgroundResource(R.drawable.border_amount_bg);
        });
        mining.setOnClickListener(v->{
            branch="Mining";
            mining.setBackgroundResource(R.drawable.bg_branch_edit);

            cse.setBackgroundResource(R.drawable.border_amount_bg);
            it.setBackgroundResource(R.drawable.border_amount_bg);
            ee.setBackgroundResource(R.drawable.border_amount_bg);
            ett.setBackgroundResource(R.drawable.border_amount_bg);
            mech.setBackgroundResource(R.drawable.border_amount_bg);
            civil.setBackgroundResource(R.drawable.border_amount_bg);
        });

        mech.setOnClickListener(v->{
            branch="Mechanical";
            mech.setBackgroundResource(R.drawable.bg_branch_edit);

            cse.setBackgroundResource(R.drawable.border_amount_bg);
            it.setBackgroundResource(R.drawable.border_amount_bg);
            ee.setBackgroundResource(R.drawable.border_amount_bg);
            ett.setBackgroundResource(R.drawable.border_amount_bg);
            mining.setBackgroundResource(R.drawable.border_amount_bg);
            civil.setBackgroundResource(R.drawable.border_amount_bg);
        });

        civil.setOnClickListener(v->{
            branch="Civil";
            civil.setBackgroundResource(R.drawable.bg_branch_edit);

            cse.setBackgroundResource(R.drawable.border_amount_bg);
            it.setBackgroundResource(R.drawable.border_amount_bg);
            ee.setBackgroundResource(R.drawable.border_amount_bg);
            ett.setBackgroundResource(R.drawable.border_amount_bg);
            mining.setBackgroundResource(R.drawable.border_amount_bg);
            mech.setBackgroundResource(R.drawable.border_amount_bg);
        });
        ///

        ///

        one.setOnClickListener(v->{
            semester="1st";
            one.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            two.setBackgroundResource(R.drawable.bg_sem);
            three.setBackgroundResource(R.drawable.bg_sem);
            four.setBackgroundResource(R.drawable.bg_sem);
            five.setBackgroundResource(R.drawable.bg_sem);
            six.setBackgroundResource(R.drawable.bg_sem);
            seven.setBackgroundResource(R.drawable.bg_sem);
            eight.setBackgroundResource(R.drawable.bg_sem);
        });

        two.setOnClickListener(v->{
            semester="2nd";
            two.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            one.setBackgroundResource(R.drawable.bg_sem);
            three.setBackgroundResource(R.drawable.bg_sem);
            four.setBackgroundResource(R.drawable.bg_sem);
            five.setBackgroundResource(R.drawable.bg_sem);
            six.setBackgroundResource(R.drawable.bg_sem);
            seven.setBackgroundResource(R.drawable.bg_sem);
            eight.setBackgroundResource(R.drawable.bg_sem);
        });

        three.setOnClickListener(v->{
            semester="3rd";
            three.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            one.setBackgroundResource(R.drawable.bg_sem);
            two.setBackgroundResource(R.drawable.bg_sem);
            four.setBackgroundResource(R.drawable.bg_sem);
            five.setBackgroundResource(R.drawable.bg_sem);
            six.setBackgroundResource(R.drawable.bg_sem);
            seven.setBackgroundResource(R.drawable.bg_sem);
            eight.setBackgroundResource(R.drawable.bg_sem);
        });

        four.setOnClickListener(v->{
            semester="4th";
            four.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            one.setBackgroundResource(R.drawable.bg_sem);
            two.setBackgroundResource(R.drawable.bg_sem);
            three.setBackgroundResource(R.drawable.bg_sem);
            five.setBackgroundResource(R.drawable.bg_sem);
            six.setBackgroundResource(R.drawable.bg_sem);
            seven.setBackgroundResource(R.drawable.bg_sem);
            eight.setBackgroundResource(R.drawable.bg_sem);
        });

        five.setOnClickListener(v->{
            semester="5th";
            five.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            one.setBackgroundResource(R.drawable.bg_sem);
            two.setBackgroundResource(R.drawable.bg_sem);
            three.setBackgroundResource(R.drawable.bg_sem);
            four.setBackgroundResource(R.drawable.bg_sem);
            six.setBackgroundResource(R.drawable.bg_sem);
            seven.setBackgroundResource(R.drawable.bg_sem);
            eight.setBackgroundResource(R.drawable.bg_sem);
        });

        six.setOnClickListener(v->{
            semester="6th";
            six.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            one.setBackgroundResource(R.drawable.bg_sem);
            two.setBackgroundResource(R.drawable.bg_sem);
            three.setBackgroundResource(R.drawable.bg_sem);
            four.setBackgroundResource(R.drawable.bg_sem);
            five.setBackgroundResource(R.drawable.bg_sem);
            seven.setBackgroundResource(R.drawable.bg_sem);
            eight.setBackgroundResource(R.drawable.bg_sem);
        });

        seven.setOnClickListener(v->{
            semester="7th";
            seven.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            one.setBackgroundResource(R.drawable.bg_sem);
            two.setBackgroundResource(R.drawable.bg_sem);
            three.setBackgroundResource(R.drawable.bg_sem);
            four.setBackgroundResource(R.drawable.bg_sem);
            five.setBackgroundResource(R.drawable.bg_sem);
            six.setBackgroundResource(R.drawable.bg_sem);
            eight.setBackgroundResource(R.drawable.bg_sem);
        });

        eight.setOnClickListener(v->{
            semester="8th";
            eight.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

            one.setBackgroundResource(R.drawable.bg_sem);
            two.setBackgroundResource(R.drawable.bg_sem);
            three.setBackgroundResource(R.drawable.bg_sem);
            four.setBackgroundResource(R.drawable.bg_sem);
            five.setBackgroundResource(R.drawable.bg_sem);
            six.setBackgroundResource(R.drawable.bg_sem);
            seven.setBackgroundResource(R.drawable.bg_sem);
        });

        ///
        get_user_data();
        roll.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                    if(editable.length()==12){
                        if(editable.toString().substring(4,7).equals("020")){
                            check_roll_no("civil",editable.toString().trim());
                        }
                        else if(editable.toString().substring(4,7).equals("022")){
                            check_roll_no("cse",editable.toString().trim());
                        }
                        else if(editable.toString().substring(4,7).equals("024")){
                            check_roll_no("electrical",editable.toString().trim());
                        }
                        else if(editable.toString().substring(4,7).equals("028")){
                            check_roll_no("etnt",editable.toString().trim());
                        }
                        else if(editable.toString().substring(4,7).equals("033")){
                            check_roll_no("it",editable.toString().trim());
                        }
                        else if(editable.toString().substring(4,7).equals("037")){
                            check_roll_no("mechanical",editable.toString().trim());
                        }
                        else if(editable.toString().substring(4,7).equals("039")){
                            check_roll_no("mining",editable.toString().trim());
                        }
                        else if(editable.toString().trim().equals("gecb26102504")){
                            user_ref.child(user.getUid()).child("roll_no").setValue("1st year");
                            MotionToast.Companion.darkColorToast(getActivity(),
                                    "You are now authorized",
                                    "Verified Successfully.",
                                    MotionToast.TOAST_SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContext(),R.font.quicksand_bold));
                        }
                        else if(editable.toString().equals("faculty18921")){
                            isfac_al=true;
                            user_ref.child(user.getUid()).child("roll_no").setValue("Faculty");
                            MotionToast.Companion.darkColorToast(getActivity(),
                                    "You are now authorized",
                                    "Verified Successfully.",
                                    MotionToast.TOAST_SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContext(),R.font.quicksand_bold));
                        }
                        else if(editable.toString().equals("alumni180921")){
                            isfac_al=true;
                            user_ref.child(user.getUid()).child("roll_no").setValue("Alumni");
                            MotionToast.Companion.darkColorToast(getActivity(),
                                    "You are now authorized",
                                    "Verified Successfully.",
                                    MotionToast.TOAST_SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContext(),R.font.quicksand_bold));
                        }

                    }
                    else{
                        if(editable.length()==10){
                            if(editable.toString().equals("3073315038")){
                                user_ref.child(user.getUid()).child("roll_no").setValue("3073315038");
                                MotionToast.Companion.darkColorToast(getActivity(),
                                        "You are now authorized",
                                        "Verified Successfully.",
                                        MotionToast.TOAST_SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(getContext(),R.font.quicksand_bold));
                            }
                        }
                    }
            }
        });
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

    private void check_roll_no(String reference,String roll_num) {
        dialog = new Dialog(getContextNullSafety());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        roll_ref.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        roll_list.add(ds.getKey());
                    }
                    if(roll_list.contains(roll_num)){
                        user_ref.child(user.getUid()).child("roll_no").setValue(roll_num);
                        MotionToast.Companion.darkColorToast(getActivity(),
                                "You are now authorized",
                                "Verified Successfully.",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getContext(),R.font.quicksand_bold));
                    }
                    else{
                        roll.setError("Not found");
                        MotionToast.Companion.darkColorToast(getActivity(),
                                "Not Found",
                                "Failed to verify.",
                                MotionToast.TOAST_WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getContext(),R.font.quicksand_bold));
                    }
                    dialog.dismiss();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void upload() {
        if(!name.getText().toString().trim().equals("")){
            if( branch!=null || isfac_al){
                if(semester!=null || isfac_al){

                    if(!linkedIn_link.getText().toString().equals("")) {
                        if (!Patterns.WEB_URL.matcher(linkedIn_link.getText().toString()).matches()) {
                            linkedIn_link.setError("Invalid Link");
                            MotionToast.Companion.darkColorToast(getActivity(),
                                    "Invalid Link",
                                    "Please enter a valid link.",
                                    MotionToast.TOAST_WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContextNullSafety(), R.font.quicksand_bold));
                            linkedIn_link.setText("");
                        }
                    }
                    if(!youtube_link.getText().toString().equals("")) {
                        if (!Patterns.WEB_URL.matcher(youtube_link.getText().toString()).matches()) {
                            youtube_link.setError("Invalid Link");
                            MotionToast.Companion.darkColorToast(getActivity(),
                                    "Invalid Link",
                                    "Please enter a valid link.",
                                    MotionToast.TOAST_WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContextNullSafety(), R.font.quicksand_bold));
                            youtube_link.setText("");
                        }
                    }

                    //push to firebase
                    user_ref.child(user.getUid()).child("name").setValue(name.getText().toString().trim());
                    user_ref.child(user.getUid()).child("email").setValue(email.getText().toString().trim());
                    user_ref.child(user.getUid()).child("profile_emoji").setValue(profile_img);
                    user_ref.child(user.getUid()).child("branch").setValue(branch);
                    user_ref.child(user.getUid()).child("semester").setValue(semester);
                    user_ref.child(user.getUid()).child("bio").setValue(bio.getText().toString().trim());
                    user_ref.child(user.getUid()).child("youtube").setValue(youtube_link.getText().toString().trim());
                    user_ref.child(user.getUid()).child("linkedin").setValue(linkedIn_link.getText().toString().trim());
                    user_ref.child(user.getUid()).child("instagram").setValue(instagram.getText().toString().trim());
                    user_ref.child(user.getUid()).child("github").setValue(github_link.getText().toString().trim());
                    user_ref.child(user.getUid()).child("skills").setValue(skills.getText().toString().trim());
                    user_ref.child(user.getUid()).child("projects").setValue(projects.getText().toString().trim());
                    if(str_fac!=null)
                        user_ref.child(user.getUid()).child("faculty").setValue(str_fac);
                    else
                        user_ref.child(user.getUid()).child("faculty").setValue("");
                    MotionToast.Companion.darkColorToast(getActivity(),
                            "Updated Successfully!!",
                            "Hurray\uD83C\uDF89\uD83C\uDF89",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContextNullSafety(),R.font.helvetica_regular));
                    back();
                }
                else{
                   // semester.setError("Empty");
                    MotionToast.Companion.darkColorToast(getActivity(),
                            "Empty semester",
                            "Please add your semester",
                            MotionToast.TOAST_WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContextNullSafety(),R.font.quicksand_bold));
                }
            }
            else{
               // branch.setError("Empty");
                MotionToast.Companion.darkColorToast(getActivity(),
                        "Empty branch",
                        "Please add your branch",
                        MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getContextNullSafety(),R.font.quicksand_bold));
            }
        }
        else{
            name.setError("Empty");
            MotionToast.Companion.darkColorToast(getActivity(),
                    "Empty name",
                    "Please add your name",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getContextNullSafety(),R.font.quicksand_bold));
        }
    }
    private void get_user_data() {
        user_ref.child(uid_of_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str_profile_img = snapshot.child("profile_emoji").getValue(String.class);
                String str_name = snapshot.child("name").getValue(String.class);
                String str_semester = snapshot.child("semester").getValue(String.class);
                String str_branch = snapshot.child("branch").getValue(String.class);
                String str_email = snapshot.child("email").getValue(String.class);
                String youtube = snapshot.child("youtube").getValue(String.class);
                String linkedIn = snapshot.child("linkedin").getValue(String.class);
                String str_instagram = snapshot.child("instagram").getValue(String.class);
                String str_github = snapshot.child("github").getValue(String.class);
                String str_bio = snapshot.child("bio").getValue(String.class);
                String str_skills = snapshot.child("skills").getValue(String.class);
                String str_projects = snapshot.child("projects").getValue(String.class);
                String roll_noo=snapshot.child("roll_no").getValue(String.class);

                if (str_profile_img != null) {
                    switch (str_profile_img) {
                        case "em_1":
                            profile_img = "em_1";
                            profile_image.setImageResource(R.drawable.em_1);
                            em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                        case "em_2":
                            profile_img = "em_2";
                            profile_image.setImageResource(R.drawable.em_2);
                            em_2.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                        case "em_3":
                            profile_img = "em_3";
                            profile_image.setImageResource(R.drawable.em_3);
                            em_3.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                        case "em_4":
                            profile_img = "em_4";
                            profile_image.setImageResource(R.drawable.em_4);
                            em_4.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                        case "em_5":
                            profile_img = "em_5";
                            profile_image.setImageResource(R.drawable.em_5);
                            em_5.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                        case "em_6":
                            profile_img = "em_6";
                            profile_image.setImageResource(R.drawable.em_6);
                            em_6.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                        case "em_7":
                            profile_img = "em_7";
                            profile_image.setImageResource(R.drawable.em_7);
                            em_7.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                        case "em_8":
                            profile_img = "em_8";
                            profile_image.setImageResource(R.drawable.em_8);
                            em_8.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                            break;
                    }
                }
                else{
                    em_1.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF6F00")));
                }

                name.setText(str_name);
                email.setText(str_email);

                if(roll_noo!=null) {
                    String pr="Roll no  "+roll_noo;
                    pr_roll.setText(pr);
                    if(roll_noo.equals("Faculty")) {
                        isfac_al=true;
                    }
                    else isfac_al= roll_noo.equals("Alumni");
                }

                if (str_branch != null){
                    if(str_branch.equals("CSE")){
                        branch="CSE";
                        cse.setBackgroundResource(R.drawable.bg_branch_edit);

                        civil.setBackgroundResource(R.drawable.border_amount_bg);
                        it.setBackgroundResource(R.drawable.border_amount_bg);
                        ee.setBackgroundResource(R.drawable.border_amount_bg);
                        ett.setBackgroundResource(R.drawable.border_amount_bg);
                        mining.setBackgroundResource(R.drawable.border_amount_bg);
                        mech.setBackgroundResource(R.drawable.border_amount_bg);
                    }
                    else if(str_branch.equals("IT")){
                        branch="IT";
                        it.setBackgroundResource(R.drawable.bg_branch_edit);

                        civil.setBackgroundResource(R.drawable.border_amount_bg);
                        cse.setBackgroundResource(R.drawable.border_amount_bg);
                        ee.setBackgroundResource(R.drawable.border_amount_bg);
                        ett.setBackgroundResource(R.drawable.border_amount_bg);
                        mining.setBackgroundResource(R.drawable.border_amount_bg);
                        mech.setBackgroundResource(R.drawable.border_amount_bg);
                    }
                    else if(str_branch.equals("Electrical")){
                        branch="Electrical";
                        ee.setBackgroundResource(R.drawable.bg_branch_edit);

                        civil.setBackgroundResource(R.drawable.border_amount_bg);
                        cse.setBackgroundResource(R.drawable.border_amount_bg);
                        it.setBackgroundResource(R.drawable.border_amount_bg);
                        ett.setBackgroundResource(R.drawable.border_amount_bg);
                        mining.setBackgroundResource(R.drawable.border_amount_bg);
                        mech.setBackgroundResource(R.drawable.border_amount_bg);
                    }
                    else if(str_branch.equals("ET&T")){
                        branch="ET&T";
                        ett.setBackgroundResource(R.drawable.bg_branch_edit);

                        civil.setBackgroundResource(R.drawable.border_amount_bg);
                        cse.setBackgroundResource(R.drawable.border_amount_bg);
                        it.setBackgroundResource(R.drawable.border_amount_bg);
                        ee.setBackgroundResource(R.drawable.border_amount_bg);
                        mining.setBackgroundResource(R.drawable.border_amount_bg);
                        mech.setBackgroundResource(R.drawable.border_amount_bg);
                    }
                    else if(str_branch.equals("Mining")){
                        branch="Mining";
                        mining.setBackgroundResource(R.drawable.bg_branch_edit);

                        civil.setBackgroundResource(R.drawable.border_amount_bg);
                        cse.setBackgroundResource(R.drawable.border_amount_bg);
                        it.setBackgroundResource(R.drawable.border_amount_bg);
                        ee.setBackgroundResource(R.drawable.border_amount_bg);
                        ett.setBackgroundResource(R.drawable.border_amount_bg);
                        mech.setBackgroundResource(R.drawable.border_amount_bg);
                    }
                    else if(str_branch.equals("Mechanical")){
                        branch="Mechanical";
                        mech.setBackgroundResource(R.drawable.bg_branch_edit);

                        civil.setBackgroundResource(R.drawable.border_amount_bg);
                        cse.setBackgroundResource(R.drawable.border_amount_bg);
                        it.setBackgroundResource(R.drawable.border_amount_bg);
                        ee.setBackgroundResource(R.drawable.border_amount_bg);
                        ett.setBackgroundResource(R.drawable.border_amount_bg);
                        mining.setBackgroundResource(R.drawable.border_amount_bg);
                    }
                    else if(str_branch.equals("Civil")){
                        branch="Civil";
                        civil.setBackgroundResource(R.drawable.bg_branch_edit);

                        mech.setBackgroundResource(R.drawable.border_amount_bg);
                        cse.setBackgroundResource(R.drawable.border_amount_bg);
                        it.setBackgroundResource(R.drawable.border_amount_bg);
                        ee.setBackgroundResource(R.drawable.border_amount_bg);
                        ett.setBackgroundResource(R.drawable.border_amount_bg);
                        mining.setBackgroundResource(R.drawable.border_amount_bg);
                    }
                }


                if(str_semester!=null){
                    if(str_semester.equals("1st")){
                        semester="1st";
                        one.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        seven.setBackgroundResource(R.drawable.bg_sem);
                        two.setBackgroundResource(R.drawable.bg_sem);
                        three.setBackgroundResource(R.drawable.bg_sem);
                        four.setBackgroundResource(R.drawable.bg_sem);
                        five.setBackgroundResource(R.drawable.bg_sem);
                        six.setBackgroundResource(R.drawable.bg_sem);
                        eight.setBackgroundResource(R.drawable.bg_sem);
                    }
                    else if(str_semester.equals("2nd")){
                        semester="2nd";
                        two.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        seven.setBackgroundResource(R.drawable.bg_sem);
                        one.setBackgroundResource(R.drawable.bg_sem);
                        three.setBackgroundResource(R.drawable.bg_sem);
                        four.setBackgroundResource(R.drawable.bg_sem);
                        five.setBackgroundResource(R.drawable.bg_sem);
                        six.setBackgroundResource(R.drawable.bg_sem);
                        eight.setBackgroundResource(R.drawable.bg_sem);
                    }
                    else if(str_semester.equals("3rd")){
                        semester="3rd";
                        three.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        seven.setBackgroundResource(R.drawable.bg_sem);
                        one.setBackgroundResource(R.drawable.bg_sem);
                        two.setBackgroundResource(R.drawable.bg_sem);
                        four.setBackgroundResource(R.drawable.bg_sem);
                        five.setBackgroundResource(R.drawable.bg_sem);
                        six.setBackgroundResource(R.drawable.bg_sem);
                        eight.setBackgroundResource(R.drawable.bg_sem);
                    }
                    else if(str_semester.equals("4th")){
                        semester="4th";
                        four.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        seven.setBackgroundResource(R.drawable.bg_sem);
                        one.setBackgroundResource(R.drawable.bg_sem);
                        two.setBackgroundResource(R.drawable.bg_sem);
                        three.setBackgroundResource(R.drawable.bg_sem);
                        five.setBackgroundResource(R.drawable.bg_sem);
                        six.setBackgroundResource(R.drawable.bg_sem);
                        eight.setBackgroundResource(R.drawable.bg_sem);
                    }
                    else if(str_semester.equals("5th")){
                        semester="5th";
                        five.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        seven.setBackgroundResource(R.drawable.bg_sem);
                        one.setBackgroundResource(R.drawable.bg_sem);
                        two.setBackgroundResource(R.drawable.bg_sem);
                        three.setBackgroundResource(R.drawable.bg_sem);
                        four.setBackgroundResource(R.drawable.bg_sem);
                        six.setBackgroundResource(R.drawable.bg_sem);
                        eight.setBackgroundResource(R.drawable.bg_sem);
                    }
                    else if(str_semester.equals("6th")){
                        semester="6th";
                        six.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        seven.setBackgroundResource(R.drawable.bg_sem);
                        one.setBackgroundResource(R.drawable.bg_sem);
                        two.setBackgroundResource(R.drawable.bg_sem);
                        three.setBackgroundResource(R.drawable.bg_sem);
                        four.setBackgroundResource(R.drawable.bg_sem);
                        five.setBackgroundResource(R.drawable.bg_sem);
                        eight.setBackgroundResource(R.drawable.bg_sem);
                    }
                    else if(str_semester.equals("7th")){
                        semester="7th";
                        seven.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        six.setBackgroundResource(R.drawable.bg_sem);
                        one.setBackgroundResource(R.drawable.bg_sem);
                        two.setBackgroundResource(R.drawable.bg_sem);
                        three.setBackgroundResource(R.drawable.bg_sem);
                        four.setBackgroundResource(R.drawable.bg_sem);
                        five.setBackgroundResource(R.drawable.bg_sem);
                        eight.setBackgroundResource(R.drawable.bg_sem);
                    }
                    else if(str_semester.equals("8th")){
                        semester="8th";
                        eight.setBackgroundResource(R.drawable.bg_sem_edit_stroke);

                        six.setBackgroundResource(R.drawable.bg_sem);
                        one.setBackgroundResource(R.drawable.bg_sem);
                        two.setBackgroundResource(R.drawable.bg_sem);
                        three.setBackgroundResource(R.drawable.bg_sem);
                        four.setBackgroundResource(R.drawable.bg_sem);
                        five.setBackgroundResource(R.drawable.bg_sem);
                        seven.setBackgroundResource(R.drawable.bg_sem);
                    }
                }

                if (youtube != null)
                   youtube_link.setText(youtube);

                if (linkedIn != null)
                    linkedIn_link.setText(linkedIn);

                if (str_instagram != null)
                    instagram.setText(str_instagram);

                if(str_github != null)
                    github_link.setText(str_github);

                if (str_bio != null)
                    bio.setText(str_bio);

                if (str_skills != null)
                    skills.setText(str_skills);

                if (str_projects != null)
                    projects.setText(str_projects);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void back(){
        FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        ft.commit();
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