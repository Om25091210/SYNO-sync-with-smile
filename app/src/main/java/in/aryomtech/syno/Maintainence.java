package in.aryomtech.syno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Maintainence extends AppCompatActivity {

    DatabaseReference debug_ref;
    boolean is_admin=false;
    ValueEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintainence);

        is_admin=getSharedPreferences("admin_or_not", Context.MODE_PRIVATE)
                .getBoolean("isadmin_or_not101",false);

        debug_ref= FirebaseDatabase.getInstance().getReference().child("debugging");
        listener=debug_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Objects.equals(snapshot.getValue(String.class), "no") || is_admin){
                    Intent intent=new Intent(Maintainence.this,Home.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        debug_ref.removeEventListener(listener);
    }
}