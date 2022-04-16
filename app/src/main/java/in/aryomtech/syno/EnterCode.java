package in.aryomtech.syno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import www.sanju.motiontoast.MotionToast;

public class EnterCode extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    Dialog dialog;
    EditText code;
    ArrayList<String> roll_list=new ArrayList<>();
    DatabaseReference roll_ref;
    DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        user_ref= FirebaseDatabase.getInstance().getReference().child("users");
        roll_ref= FirebaseDatabase.getInstance().getReference().child("ROLL_NO");

        Window window = EnterCode.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(EnterCode.this, R.color.white));

        code=findViewById(R.id.editTextTextMultiLine);
        code.addTextChangedListener(new TextWatcher() {
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
                    else if(editable.toString().toLowerCase().trim().equals("gecb26102504")){
                        //TODO: Give entry
                        user_ref.child(user.getUid()).child("roll_no").setValue("1st year");
                        MotionToast.Companion.darkColorToast(EnterCode.this,
                                "You are now authorized",
                                "Verified Successfully.",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(EnterCode.this,R.font.quicksand_bold));
                        open_Intent();
                    }
                    else if(editable.toString().toLowerCase().trim().equals("faculty18921")){
                        //TODO: Give entry
                        user_ref.child(user.getUid()).child("roll_no").setValue("Faculty");
                        MotionToast.Companion.darkColorToast(EnterCode.this,
                                "You are now authorized",
                                "Verified Successfully.",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(EnterCode.this,R.font.quicksand_bold));
                        open_Intent();
                    }
                    else if(editable.toString().toLowerCase().trim().equals("alumni180921")){
                        //TODO: Give entry
                        user_ref.child(user.getUid()).child("roll_no").setValue("Alumni");
                        MotionToast.Companion.darkColorToast(EnterCode.this,
                                "You are now authorized",
                                "Verified Successfully.",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(EnterCode.this,R.font.quicksand_bold));
                        open_Intent();
                    }

                }
                else{
                    if(editable.length()==10){
                        if(editable.toString().equals("3073315038")){
                            //TODO: Give entry
                            user_ref.child(user.getUid()).child("roll_no").setValue("3073315038");
                            MotionToast.Companion.darkColorToast(EnterCode.this,
                                    "You are now authorized",
                                    "Verified Successfully.",
                                    MotionToast.TOAST_SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(EnterCode.this,R.font.quicksand_bold));
                            open_Intent();
                        }
                    }
                }
            }
        });
    }

    private void open_Intent() {
        //saving the access result as true in the shared preferences...
        getSharedPreferences("Authorized_for_Access",MODE_PRIVATE).edit()
                .putBoolean("is_Authorized_to_access_the_app",true).apply();

        Intent intent=new Intent(EnterCode.this, interested_topic.class);
        startActivity(intent);
        finish();
    }

    private void check_roll_no(String reference,String roll_num) {
        dialog = new Dialog(EnterCode.this);
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
                        //TODO: Give entry
                        user_ref.child(user.getUid()).child("roll_no").setValue(roll_num);
                        MotionToast.Companion.darkColorToast(EnterCode.this,
                                "You are now authorized",
                                "Verified Successfully.",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(EnterCode.this,R.font.quicksand_bold));
                        open_Intent();
                    }
                    else{
                        code.setError("Not found");
                        MotionToast.Companion.darkColorToast(EnterCode.this,
                                "Not Found",
                                "Failed to verify.",
                                MotionToast.TOAST_WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(EnterCode.this,R.font.quicksand_bold));
                    }
                    dialog.dismiss();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user==null){
            Intent intent=new Intent(EnterCode.this,login.class);
            startActivity(intent);
        }
    }
}