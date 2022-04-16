package in.aryomtech.syno;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import soup.neumorphism.NeumorphCardView;


public class about_dev extends Fragment {

    ImageView back;
    View view;
    private Context contextNullSafe;
    NeumorphCardView arpi, nikhil, priyanshu, om;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_about_dev, container, false);
        if (contextNullSafe == null) getContextNullSafety();


        back=view.findViewById(R.id.imageView4);
        arpi=view.findViewById(R.id.neumorphCardView2);
        nikhil=view.findViewById(R.id.neumorphCardView3);
        priyanshu=view.findViewById(R.id.neumorphCardView4);
        om=view.findViewById(R.id.neumorphCardView5);

        arpi.setOnClickListener(v->{
            back();
            Profile profile = new Profile();
            Bundle args = new Bundle();
            args.putString("uid_sending_profile", "Zvuozf4k7oaE5B9L79SxZQigMSA2");
            profile.setArguments(args);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.container, profile)
                    .addToBackStack(null)
                    .commit();
        });
       nikhil.setOnClickListener(v->{
           back();
            Profile profile = new Profile();
            Bundle args = new Bundle();
            args.putString("uid_sending_profile", "uFvIsr5Ak8bUkWq32OfOaTvlLIj1");
            profile.setArguments(args);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.container, profile)
                    .addToBackStack(null)
                    .commit();
        });
       priyanshu.setOnClickListener(v->{
            back();
            Profile profile = new Profile();
            Bundle args = new Bundle();
            args.putString("uid_sending_profile", "Z14E8mL2Q8dht0ZvIR3Qf2MTofs2");
            profile.setArguments(args);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.container, profile)
                    .addToBackStack(null)
                    .commit();
        });
        om.setOnClickListener(v->{
            back();
            Profile profile = new Profile();
            Bundle args = new Bundle();
            args.putString("uid_sending_profile", "BUx8CDAJDrPVxHGbiRQLbtv57Y13");
            profile.setArguments(args);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.container, profile)
                    .addToBackStack(null)
                    .commit();
        });


        back.setOnClickListener(v-> back());

        view.findViewById(R.id.card_linkedin).setOnClickListener(s->{
            String url = "https://www.linkedin.com/in/arpiitah";
            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(linkedInAppIntent);
        });

        view.findViewById(R.id.card_insta).setOnClickListener(s->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"arpiitah";
            String path = "https://instagram.com/"+"arpiitah";
            String nomPackageInfo ="com.instagram.android";
            try {
                getContextNullSafety().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });

        view.findViewById(R.id.card_linkedin1).setOnClickListener(s->{
            String url = "https://www.linkedin.com/in/nikhil-verma2002";
            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(linkedInAppIntent);
        });

        view.findViewById(R.id.card_insta1).setOnClickListener(s->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"nikhil__vermaa_";
            String path = "https://instagram.com/"+"nikhil__vermaa_";
            String nomPackageInfo ="com.instagram.android";
            try {
                getContextNullSafety().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });

        view.findViewById(R.id.card_linkedin2).setOnClickListener(s->{
            String url = "https://www.linkedin.com/in/priyanshu-anand-5b9776217";
            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(linkedInAppIntent);
        });

        view.findViewById(R.id.card_insta2).setOnClickListener(s->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"priyanshu_2826";
            String path = "https://instagram.com/"+"priyanshu_2826";
            String nomPackageInfo ="com.instagram.android";
            try {
                getContextNullSafety().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });

        view.findViewById(R.id.card_linkedin3).setOnClickListener(s->{
            String url = "https://www.linkedin.com/in/om-yadav-3760921a7";
            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(linkedInAppIntent);
        });

        view.findViewById(R.id.card_insta3).setOnClickListener(s->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"i_m_om_kumar";
            String path = "https://instagram.com/"+"i_m_om_kumar";
            String nomPackageInfo ="com.instagram.android";
            try {
                getContextNullSafety().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
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

    private void back(){
        FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        ft.commit();
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