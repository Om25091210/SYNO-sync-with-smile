package in.aryomtech.syno.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
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

import in.aryomtech.syno.Model.user_data;
import in.aryomtech.syno.Profile;
import in.aryomtech.syno.R;
import in.aryomtech.syno.login;
import www.sanju.motiontoast.MotionToast;

public class syncAdapter extends RecyclerView.Adapter<syncAdapter.ViewHolder> {

    Context context;
    ArrayList<user_data> list ;
    boolean verify;
    public syncAdapter(Context contextNullSafety, ArrayList<user_data> list,boolean verify) {
        this.context=contextNullSafety;
        this.list=list;
        this.verify=verify;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(list.get(position).getProfile_emoji()!=null) {
            if (Objects.equals(list.get(position).getProfile_emoji(), "em_1"))
                holder.profile_img.setImageResource(R.drawable.em_1);
            else if (Objects.equals(list.get(position).getProfile_emoji(), "em_2"))
                holder.profile_img.setImageResource(R.drawable.em_2);
            else if (Objects.equals(list.get(position).getProfile_emoji(), "em_3"))
                holder.profile_img.setImageResource(R.drawable.em_3);
            else if (Objects.equals(list.get(position).getProfile_emoji(), "em_4"))
                holder.profile_img.setImageResource(R.drawable.em_4);
            else if (Objects.equals(list.get(position).getProfile_emoji(), "em_5"))
                holder.profile_img.setImageResource(R.drawable.em_5);
            else if (Objects.equals(list.get(position).getProfile_emoji(), "em_6"))
                holder.profile_img.setImageResource(R.drawable.em_6);
            else if (Objects.equals(list.get(position).getProfile_emoji(), "em_7"))
                holder.profile_img.setImageResource(R.drawable.em_7);
            else if (Objects.equals(list.get(position).getProfile_emoji(), "em_8"))
                holder.profile_img.setImageResource(R.drawable.em_8);
        }
        holder.name.setText(list.get(position).getName());

        if(list.get(position).getBranch()!=null && list.get(position).getSemester()!=null) {
            String str_br_name = list.get(position).getSemester() + " " + list.get(position).getBranch();
            holder.br_sem.setText(str_br_name);
        }
        else{
            holder.br_sem.setText("");
        }
        if(list.get(position).getRoll_no()!=null) {
            if(list.get(position).getRoll_no().equals("Faculty")) {
                holder.tick.setVisibility(View.VISIBLE);
                holder.br_sem.setText(list.get(position).getRoll_no());
            }
            else if(list.get(position).getRoll_no().equals("Alumni")) {
                holder.tick.setVisibility(View.VISIBLE);
                holder.br_sem.setText(list.get(position).getRoll_no());
            }
            else if(list.get(position).getRoll_no().equals("Club")){
                holder.tick.setVisibility(View.VISIBLE);
                holder.br_sem.setText(list.get(position).getRoll_no());
            }
            else
                holder.tick.setVisibility(View.GONE);
        }

        holder.layout.setOnClickListener(v->{
            if (verify) {
                Profile profile = new Profile();
                Bundle args = new Bundle();
                args.putString("sending_user_from_sync","addstack");
                args.putString("uid_sending_profile", list.get(position).getUid());
                profile.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.container, profile)
                        .addToBackStack(null)
                        .commit();
            }
            else{
                MotionToast.Companion.darkColorToast((Activity) context,
                        "You're not authorized.",
                        "Please add your roll number in your profile",
                        MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context,R.font.quicksand_bold));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_img;
        TextView name,br_sem;
        ImageView tick;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img=itemView.findViewById(R.id.postimage);
            name=itemView.findViewById(R.id.textView5);
            br_sem=itemView.findViewById(R.id.textView6);
            tick=itemView.findViewById(R.id.imageView7);
            layout=itemView.findViewById(R.id.layout);
        }
    }
}
