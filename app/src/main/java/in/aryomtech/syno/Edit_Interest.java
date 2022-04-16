package in.aryomtech.syno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Edit_Interest extends AppCompatActivity {

    ImageView back;
    RelativeLayout science,technology,fiction,politics,movies,art,food,anime,sports,news,stocks,crypto;
    TextView save;
    List<String> choices;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference users_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_interest);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        users_ref= FirebaseDatabase.getInstance().getReference().child("users");
        choices=new ArrayList<>();
        Window window = Edit_Interest.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Edit_Interest.this, R.color.white));

        back=findViewById(R.id.imageView4);
        back.setOnClickListener(v->onBackPressed());

        save=findViewById(R.id.textView3);

        science=findViewById(R.id.science);
        technology=findViewById(R.id.technology);
        fiction=findViewById(R.id.fiction);
        politics=findViewById(R.id.politics);
        movies=findViewById(R.id.movies);
        art=findViewById(R.id.art);
        food=findViewById(R.id.food);
        anime=findViewById(R.id.anime);
        sports=findViewById(R.id.sports);
        news=findViewById(R.id.news);
        stocks=findViewById(R.id.stocks);
        crypto=findViewById(R.id.crypto);

        save.setOnClickListener(v->save());
        science.setOnClickListener(v->{
            if (!choices.contains("Science")) {
                choices.add("Science");
                science.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Science");
                science.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        technology.setOnClickListener(v->{
            if (!choices.contains("Technology")) {
                choices.add("Technology");
                technology.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Technology");
                technology.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        fiction.setOnClickListener(v->{
            if (!choices.contains("Fiction")) {
                choices.add("Fiction");
                fiction.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Fiction");
                fiction.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        politics.setOnClickListener(v->{
            if (!choices.contains("Politics")) {
                choices.add("Politics");
                politics.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Politics");
                politics.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        movies.setOnClickListener(v->{
            if (!choices.contains("Movies")) {
                choices.add("Movies");
                movies.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Movies");
                movies.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        art.setOnClickListener(v->{
            if (!choices.contains("Art")) {
                choices.add("Art");
                art.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Art");
                art.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        food.setOnClickListener(v->{
            if (!choices.contains("Food")) {
                choices.add("Food");
                food.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Food");
                food.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        anime.setOnClickListener(v->{
            if (!choices.contains("Anime")) {
                choices.add("Anime");
                anime.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Anime");
                anime.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        sports.setOnClickListener(v->{
            if (!choices.contains("Sports")) {
                choices.add("Sports");
                sports.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Sports");
                sports.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        news.setOnClickListener(v->{
            if (!choices.contains("News")) {
                choices.add("News");
                news.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("News");
                news.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        stocks.setOnClickListener(v->{
            if (!choices.contains("Stocks")) {
                choices.add("Stocks");
                stocks.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Stocks");
                stocks.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

        crypto.setOnClickListener(v->{
            if (!choices.contains("Crypto")) {
                choices.add("Crypto");
                crypto.setBackgroundResource(R.drawable.stroke_active_bg);
            }
            else {
                choices.remove("Crypto");
                crypto.setBackgroundResource(R.drawable.stroke_bg);
            }
        });

    }
    private void save() {
        if(choices.size()!=0){
            users_ref.child(user.getUid()).child("interests").removeValue();
            for (int element = 0; element < choices.size(); element++) {
                users_ref.child(user.getUid()).child("interests").child(element + "").setValue(choices.get(element) + "");
            }
            finish();
            Toast.makeText(Edit_Interest.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Edit_Interest.this, "Please Select At Least One Interest", Toast.LENGTH_SHORT).show();
        }
    }
}