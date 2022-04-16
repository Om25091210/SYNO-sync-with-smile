package in.aryomtech.syno.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.aryomtech.syno.Home;
import in.aryomtech.syno.Model.post_data;
import in.aryomtech.syno.R;
import in.aryomtech.syno.login;
import in.aryomtech.syno.view_post;
import www.sanju.motiontoast.MotionToast;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {

    Context context;
    ArrayList<post_data> list;
    DatabaseReference post_ref;

    public postAdapter(Context context, ArrayList<post_data> list) {
        this.list=list;
        this.context=context;
        post_ref= FirebaseDatabase.getInstance().getReference().child("post");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(position<list.size()) {

           /* Glide.with(context).asBitmap()
                    .load(list.get(position).getImage_link())
                    .thumbnail(0.1f)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.ic_colorfull)
                    .into(holder.post_image);*/
            try {
                Uri uri = Uri.parse(list.get(position).getImage_link());
                holder.draweeView.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.title.setText(list.get(position).getTitle());
            holder.content.setText(list.get(position).getBody());
            holder.seen.setText(withSuffix(Long.parseLong(list.get(position).getSeen())));
            holder.post_date.setText(list.get(position).getDate());

            holder.layout.setOnClickListener(v -> {
                post_ref.child(list.get(position).getPushkey() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            redirect(position);
                        } else {
                            list.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyDataSetChanged();
                            MotionToast.Companion.darkColorToast((Activity) context,
                                    "Post deleted",
                                    "The user has removed the post.",
                                    MotionToast.TOAST_DELETE,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(context, R.font.quicksand_bold));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            });
        }
    }

    private void redirect(int position) {

        view_post view_post=new view_post();
        Bundle args=new Bundle();
        args.putString("title_sending",list.get(position).getTitle());
        args.putString("body_sending",list.get(position).getBody());
        args.putString("category_sending",list.get(position).getCategory());
        args.putString("city_sending",list.get(position).getCity());
        args.putString("date_sending",list.get(position).getDate());
        args.putString("link_sending",list.get(position).getLink());
        args.putString("image_sending",list.get(position).getImage_link());
        args.putString("uid_sending",list.get(position).getUid());
        args.putString("pushkey_sending",list.get(position).getPushkey());
        args.putString("seen_sending",list.get(position).getSeen());
        view_post.setArguments(args);

        ((FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.drawer,view_post)
                .addToBackStack(null)
                .commit();
    }
    /*@Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.post_image);
    }*/
    @SuppressLint("DefaultLocale")
    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView post_image;
        LinearLayout layout;
        TextView title,content,post_date,seen;
        SimpleDraweeView draweeView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_image=itemView.findViewById(R.id.postimage);
            title=itemView.findViewById(R.id.textView5);
            content=itemView.findViewById(R.id.textView6);
            post_date=itemView.findViewById(R.id.textView8);
            seen=itemView.findViewById(R.id.textView7);
            layout=itemView.findViewById(R.id.layout);
            draweeView= itemView.findViewById(R.id.my_image_view);

        }
    }
}
