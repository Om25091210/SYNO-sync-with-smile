package in.aryomtech.syno.data;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import in.aryomtech.syno.R;
import in.aryomtech.syno.view_post;

public class SmooliderAdapter extends PagerAdapter {

    private final Context mContext;
    private final List<ModelSmoolider> feedItemList;

    public SmooliderAdapter(List<ModelSmoolider> feedItemList, Context mContext) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        final ModelSmoolider slider_data = feedItemList.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.structure_gift, container, false);

        //RoundedImageView img_slider = view.findViewById(R.id.img_slider);

        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.img_slider);
        TextView textView_des = view.findViewById(R.id.txt_details);

        new Boom(textView_des);//optional

        textView_des.setText(slider_data.getDes_text());
        /*try {
            Glide.with(mContext).asBitmap().load(slider_data.getImage_url()).placeholder(R.drawable.ic_image_holder).into(img_slider);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Uri uri = Uri.parse(slider_data.getImage_url());
        draweeView.setImageURI(uri);

        draweeView.setOnClickListener(v -> {
            //Slider action
            if(!slider_data.getDeep_link().equals("no")){
                Uri deep_link_uri=Uri.parse(slider_data.getDeep_link());
                List<String> parameters = deep_link_uri.getPathSegments();
                String deep_link_value = parameters.get(parameters.size() - 1);
                String deep_link_uid_value = parameters.get(parameters.size() - 2);
                if(deep_link_value!=null){
                    DatabaseReference post_ref= FirebaseDatabase.getInstance().getReference().child("post");
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

                                ((FragmentActivity) mContext).getSupportFragmentManager()
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
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return feedItemList.size();
    }

    @Override
    public void destroyItem (ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
