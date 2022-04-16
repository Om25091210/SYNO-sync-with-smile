package in.aryomtech.syno;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import www.sanju.motiontoast.MotionToast;


public class view_post extends Fragment {

    DatabaseReference post_ref,user_ref;
    ImageView back;
    ImageView share;
    View view;
    FirebaseAuth auth;
    FirebaseUser user;
    boolean isadmin=false;
    boolean verify=false;
    TextView txt_category,txt_seen,txt_title,name,txt_link,txt_date,txt_body,txt_loc,delete_img;
    ArrayList<String> list_of_uids=new ArrayList<>();
    private Context contextNullSafe;
    String title,category,city,link,seen,date,uid,pushkey,image_link,body;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_view_post, container, false);
        if (contextNullSafe == null) getContextNullSafety();
        setStatusBarTransparent();

        post_ref= FirebaseDatabase.getInstance().getReference().child("post");
        user_ref= FirebaseDatabase.getInstance().getReference().child("users");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        try {
            title = getArguments().getString("title_sending");
            category = getArguments().getString("category_sending");
            city = getArguments().getString("city_sending");
            link = getArguments().getString("link_sending");
            seen = getArguments().getString("seen_sending");
            date = getArguments().getString("date_sending");
            uid = getArguments().getString("uid_sending");
            pushkey = getArguments().getString("pushkey_sending");
            image_link = getArguments().getString("image_sending");
            body = getArguments().getString("body_sending");
        } catch (Exception e) {
            e.printStackTrace();
        }

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        isadmin=getContextNullSafety().getSharedPreferences("admin_or_not",Context.MODE_PRIVATE)
                .getBoolean("isadmin_or_not101",false);
        isverified();
        check_for_seen();
        delete_img=view.findViewById(R.id.delete);
        if(uid.equals(user.getUid()) || isadmin){
            delete_img.setVisibility(View.VISIBLE);
        }
        else{
            delete_img.setVisibility(View.GONE);
        }

        back=view.findViewById(R.id.imageView4);
        back.setOnClickListener(v-> back());

        txt_category=view.findViewById(R.id.textView10);
        if(title!=null){
            txt_category.setText(category);
         }

        Uri uri = Uri.parse(image_link);
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.imageNote);
        draweeView.setImageURI(uri);

        /*Glide.with(getContextNullSafety())
                .asBitmap()
                .load(image_link)
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        share_ready=false;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        share_ready=true;
                        bitmap=resource;
                        return false;
                    }
                })
                .placeholder(R.drawable.ic_colorfull)
                .into(img_image);*/

        draweeView.setOnClickListener(v->{
                View_photo view_photo = new View_photo();
                Bundle args = new Bundle();
                args.putString("image_sending_profile", image_link);
                view_photo.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.view_post_layout, view_photo)
                        .addToBackStack(null)
                        .commit();
        });

        share=view.findViewById(R.id.imageView5);
        share.setOnClickListener(v->{
            String str_title ="*"+title+"*"+"\n\n"+"Created by : "+name.getText().toString()+
                    "\n"+"Date : "+date+
                    "\n"+"Category : "+category+"\n\n"+"*View* :"+"https://syno.android/mosioco/"+uid+"/"+pushkey ; //Text to be shared
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, str_title+"\n\n"+"This is a playstore link to download.. " + "https://play.google.com/store/apps/details?id=" + getContextNullSafety().getPackageName());
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        });

        txt_seen=view.findViewById(R.id.textView11);
        txt_seen.setText(withSuffix(Long.parseLong(seen)));

        txt_seen.setOnClickListener(v->{
            if(uid.equals(user.getUid()) || isadmin) {
                seen seen = new seen();
                Bundle args = new Bundle();
                args.putString("pushkey_of_post", pushkey);
                seen.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.view_post_layout, seen)
                        .addToBackStack(null)
                        .commit();
            }
        });

        txt_title=view.findViewById(R.id.textView12);
        txt_title.setText(title);

        name=view.findViewById(R.id.textView14);
        get_name();

        name.setOnClickListener(v->{
            if(!name.getText().toString().equals("")) {
                if (verify) {
                    back();
                    Profile profile = new Profile();
                    Bundle args = new Bundle();
                    args.putString("uid_sending_profile", uid);
                    profile.setArguments(args);
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .add(R.id.container, profile)
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    MotionToast.Companion.darkColorToast(getActivity(),
                            "You're not authorized.",
                            "Please add your roll number in your profile",
                            MotionToast.TOAST_WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContextNullSafety(), R.font.quicksand_bold));
                }
            }
        });

        txt_link=view.findViewById(R.id.link);
        txt_link.setVisibility(View.GONE);
        if(link!=null) {
            if (!link.equals("")) {
                txt_link.setVisibility(View.VISIBLE);
                txt_link.setText(link);
            }
        }

        txt_date=view.findViewById(R.id.date);
        if(date!=null)
            txt_date.setText(date);

        txt_body=view.findViewById(R.id.textView17);
        if(body!=null)
            txt_body.setText(body);

        txt_loc=view.findViewById(R.id.textView18);
        if(city!=null) {
            if (!city.equals(""))
                txt_loc.setText(city);
            else
                txt_loc.setVisibility(View.GONE);
        }

        delete();

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
    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }
    private void delete() {
        delete_img.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_for_sure);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView cancel=dialog.findViewById(R.id.textView96);
            TextView yes=dialog.findViewById(R.id.textView95);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            cancel.setOnClickListener(vi-> dialog.dismiss());
            yes.setOnClickListener(vi-> {
                String imagepath = "Post/" + title + pushkey + ".png";
                MotionToast.Companion.darkColorToast(getActivity(),
                        "Deleted!!",
                        "Deleted successfully. Swipe down to refresh",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getActivity(), R.font.quicksand_bold));

                if (image_link != null) {
                    StorageReference storageReference =
                            FirebaseStorage.getInstance().getReference().child(imagepath);
                    storageReference.delete();
                }

                post_ref.child(pushkey).removeValue();
                dialog.dismiss();
                back();
            });

        });

    }

    private void get_name() {
        user_ref.child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name_str=snapshot.getValue(String.class);
                name.setText(name_str);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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
    private void check_for_seen() {
        post_ref.child(pushkey).child("view").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        list_of_uids.add(ds.getKey());
                    }
                    if(!list_of_uids.contains(user.getUid())){
                        add_by_one();
                    }
                }
                else{
                    post_ref.child(pushkey).child("view").child(user.getUid()).setValue("seen");
                    post_ref.child(pushkey).child("seen").setValue("1");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void add_by_one() {

        post_ref.child(pushkey).child("seen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   String seen_data=snapshot.getValue(String.class);
                   int seen_count=Integer.parseInt(seen_data)+1;
                   post_ref.child(pushkey).child("seen").setValue(seen_count+"");
                   post_ref.child(pushkey).child("view").child(user.getUid()).setValue("seen");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void setStatusBarTransparent() {
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
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