package in.aryomtech.syno;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import in.aryomtech.syno.FCM.topic;
import in.aryomtech.syno.Model.post_data;
import www.sanju.motiontoast.MotionToast;

public class post extends Fragment {

    private AlertDialog dialogAddURL;
    private LinearLayout layoutWebURL;
    ImageView back;
    View view;
    FirebaseAuth auth;
    FirebaseUser user;
    private ImageView imageNote,imageRemoveImage;
    EditText body,title,category;
    private Context contextNullSafe;
    String selectedImagePath="";
    DatabaseReference post_ref;
    boolean verify=false;
    DatabaseReference user_ref;
    String name;
    ImageView link_remover,add_img;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    LottieAnimationView link,voice_text;
    ActivityResultLauncher<Intent> startActivityForImage;
    TextView textWebURL,post,city;
    Dialog dialog;
    int SELECT_PICTURE = 200;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_post, container, false);
        if (contextNullSafe == null) getContextNullSafety();
        layoutWebURL = view.findViewById(R.id.layoutWebURL);
        textWebURL = view.findViewById(R.id.textWebURL);
        link=view.findViewById(R.id.link);
        category=view.findViewById(R.id.editTextTextMultiLine13);
        post=view.findViewById(R.id.textView9);
        title=view.findViewById(R.id.inputNote);
        link_remover=view.findViewById(R.id.link_remover);
        voice_text=view.findViewById(R.id.voice_text);
        body=view.findViewById(R.id.body);
        back=view.findViewById(R.id.linearLayout3);
        add_img=view.findViewById(R.id.imageView9);
        imageNote = view.findViewById(R.id.imageNote);
        imageRemoveImage = view.findViewById(R.id.imageRemoveImage);
        city = view.findViewById(R.id.city);
        //Hide the keyboard
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        startActivityForImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Uri selectedImageUri = result.getData().getData();
                        addImageNote(selectedImageUri);
                    }
                }
        );

        user_ref= FirebaseDatabase.getInstance().getReference().child("users");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        add_img.setOnClickListener(v-> {
            //Ask for permission
            if (ContextCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            }
            else{
                selectImage();
            }
        });
        imageRemoveImage.setOnClickListener(v-> removeImage());

        link.setOnClickListener(v->{
            showAddURLDialog();
        });

        link_remover.setOnClickListener(v->{
            textWebURL.setText("");
            layoutWebURL.setVisibility(View.GONE);
        });

        back.setOnClickListener(v->{
            back();
        });

        post.setOnClickListener(v->{
            save_data();
        });

        isverified();

        ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        ArrayList<String> result_voice = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String voice_text_Str=body.getText().toString().trim()+" "+result_voice.get(0)+"";
                        body.setText(voice_text_Str);
                    }
                }
        );

        voice_text.setOnClickListener(v->{
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");

            try {
                startActivityForResult.launch(intent);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getContext(), "Sorry your device not supported", Toast.LENGTH_SHORT).show();
            }
        });

        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer) != null) {
                    ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer))).commit();
                }
                ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,new home_content())
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        return view;
    }

    private void save_data() {
        if (verify) {
            if (!title.getText().toString().trim().equals("")) {
                if (!category.getText().toString().trim().equals("")) {
                    if (!body.getText().toString().trim().equals("")) {
                        //push data in firebase
                        if (!selectedImagePath.equals("")) {
                            //if image is there...
                            dialog = new Dialog(getContextNullSafety());
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.loading);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.show();

                            post_ref = FirebaseDatabase.getInstance().getReference().child("post");
                            String pushkey = post_ref.push().getKey();


                            //for image storing
                            String imagepath = "Post/" + title.getText().toString().trim() + pushkey + ".png";

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagepath);
                            /*final String randomKey = UUID.randomUUID().toString();
                            BitmapDrawable drawable = (BitmapDrawable) imageNote.getDrawable();
                            Bitmap bitmap_up = drawable.getBitmap();
                            String path = MediaStore.Images.Media.insertImage(requireContext().getApplicationContext().getContentResolver(), bitmap_up, "" + randomKey, null);*/

                            try {
                                InputStream stream = new FileInputStream(new File(selectedImagePath));

                                storageReference.putStream(stream)
                                    .addOnSuccessListener(taskSnapshot ->
                                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                                    task -> {
                                                        String image_link = Objects.requireNonNull(task.getResult()).toString();
                                                        Calendar cal = Calendar.getInstance();
                                                        SimpleDateFormat simpleDateFormat_time = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                                                        post_data post_data=new post_data();
                                                        post_data.setTitle(title.getText().toString().trim());
                                                        post_data.setBody(body.getText().toString().trim());

                                                        String[] parts = category.getText().toString().trim().split("\\s+");
                                                        String category_one =parts[0];
                                                        post_data.setCategory(category_one);

                                                        post_data.setCity(city.getText().toString().trim());
                                                        post_data.setLink(textWebURL.getText().toString().trim());
                                                        post_data.setUid(user.getUid());
                                                        post_data.setPushkey(pushkey);
                                                        post_data.setSeen("0");
                                                        post_data.setDate(simpleDateFormat_time.format(cal.getTime()) + "");
                                                        post_data.setImage_link(image_link);

                                                        assert pushkey != null;
                                                        post_ref.child(pushkey).setValue(post_data);

                                                        topic topic=new topic();
                                                        topic.noti(name+" just made a post",title.getText().toString()+", Tap to open.");

                                                        dialog.dismiss();
                                                        MotionToast.Companion.darkColorToast(getActivity(),
                                                                "Posted Successfully!!",
                                                                "Hurray\uD83C\uDF89\uD83C\uDF89",
                                                                MotionToast.TOAST_SUCCESS,
                                                                MotionToast.GRAVITY_BOTTOM,
                                                                MotionToast.LONG_DURATION,
                                                                ResourcesCompat.getFont(getContextNullSafety(), R.font.helvetica_regular));
                                                        back();
                                                    }));
                             } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else {
                            post_ref = FirebaseDatabase.getInstance().getReference().child("post");
                            String pushkey = post_ref.push().getKey();

                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat_time = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                            post_data post_data=new post_data();
                            post_data.setTitle(title.getText().toString().trim());
                            post_data.setBody(body.getText().toString().trim());

                            String[] parts = category.getText().toString().trim().split("\\s+");
                            String category_one =parts[0];
                            post_data.setCategory(category_one);

                            post_data.setCity(city.getText().toString().trim());
                            post_data.setLink(textWebURL.getText().toString().trim());
                            post_data.setUid(user.getUid());
                            post_data.setPushkey(pushkey);
                            post_data.setSeen("0");
                            post_data.setDate(simpleDateFormat_time.format(cal.getTime()) + "");
                            post_data.setImage_link("");

                            assert pushkey != null;
                            post_ref.child(pushkey).setValue(post_data);

                            MotionToast.Companion.darkColorToast(getActivity(),
                                    "Posted Successfully!!",
                                    "Hurray\uD83C\uDF89\uD83C\uDF89",
                                    MotionToast.TOAST_SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContextNullSafety(), R.font.helvetica_regular));

                            topic topic=new topic();
                            topic.noti(name+" just made a post",title.getText().toString()+", Tap to open.");

                            back();
                        }
                    } else {
                        body.setError("Empty");
                        MotionToast.Companion.darkColorToast(getActivity(),
                                "Empty body",
                                "Please add Description",
                                MotionToast.TOAST_WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getContextNullSafety(), R.font.quicksand_bold));
                    }
                } else {
                    category.setError("Empty");
                    MotionToast.Companion.darkColorToast(getActivity(),
                            "Empty category",
                            "please add a category",
                            MotionToast.TOAST_WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContextNullSafety(), R.font.quicksand_bold));
                }
            } else {
                title.setError("Empty");
                MotionToast.Companion.darkColorToast(getActivity(),
                        "Empty Title",
                        "please add a title",
                        MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getContextNullSafety(), R.font.quicksand_bold));
            }
        }
        else{
            MotionToast.Companion.darkColorToast(getActivity(),
                    "You're not authorized.",
                    "Please add your roll no. in your profile to post",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getContext(),R.font.quicksand_bold));
        }
    }

    private void isverified() {
        user_ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                verify= snapshot.child("roll_no").exists();
                name=snapshot.child("name").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void back(){
        if(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer) != null) {
            ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                    .beginTransaction().
                    remove(Objects.requireNonNull(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer))).commit();
        }
        ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,new home_content())
                .commit();
    }

    void removeImage(){
        imageNote.setImageBitmap(null);
        imageNote.setVisibility(View.GONE);
        view.findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
        selectedImagePath = "";
    }

    private void showAddURLDialog(){
        if (dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View view0 = LayoutInflater.from(getContext()).inflate(R.layout.web_dialog, view.findViewById(R.id.layoutAddUrlContainer));

            builder.setView(view0);
            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final EditText inputURL = view0.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view0.findViewById(R.id.textAdd).setOnClickListener(v -> {
                if (inputURL.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Enter URL", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()){
                    Toast.makeText(getContext(), "Enter valid URL", Toast.LENGTH_SHORT).show();
                }
                else{
                    textWebURL.setText(inputURL.getText().toString().trim());
                    layoutWebURL.setVisibility(View.VISIBLE);
                    dialogAddURL.dismiss();
                }
            });

            view0.findViewById(R.id.textCancel).setOnClickListener(v -> dialogAddURL.dismiss());
        }

        dialogAddURL.show();

    }

    private void selectImage() {
        // If you have access to the external storage, do whatever you need
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForImage.launch(intent);
    }
    private void addImageNote(Uri imageUri) {

        imageNote.setVisibility(View.VISIBLE);
        selectedImagePath = compressImage(imageUri+"");
        imageNote.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
        view.findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }
            else{
                Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight+1;
        int actualWidth = options.outWidth+1;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            assert scaledBitmap != null;
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(getContextNullSafety().getExternalFilesDir(null).getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContextNullSafety().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}