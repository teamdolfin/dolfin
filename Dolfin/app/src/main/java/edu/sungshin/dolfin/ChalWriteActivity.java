package edu.sungshin.dolfin;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChalWriteActivity extends Fragment implements onBackPressedListener{
    private View view;

    Button btnWriteUpload;
    Button btnWriteFile;
    VideoView videoView;
    ImageView imageView;
    EditText editChalWrite;
    EditText edittitle;
    TextView countLetter;
    private Dialog dialog;
    private RatingBar ratingBar;
    private String rate;
    Button btnSuccess,button2;
    private ProgressBar progressBar;
    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    //private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final FirebaseStorage reference = FirebaseStorage.getInstance();
    private StorageReference storageref = reference.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri imageUri;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yy??? MM??? dd???");
    String chal_name;



    private static final int MY_PERMISSION_STORAGE = 1111;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_df_chal_write,container,false);
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            // do your stuff
//        } else {
//            signInAnonymously();
//        }


        //?????? ?????????
        setHasOptionsMenu(true);
        checkPermission();
        btnWriteUpload = view.findViewById(R.id.btnWriteUpload);
        btnWriteFile = view.findViewById(R.id.btnWriteFile);
        imageView = view.findViewById(R.id.imageView);
        videoView = view.findViewById(R.id.videoView);
        editChalWrite = (EditText) view.findViewById(R.id.editChalWrite);
        countLetter = (TextView) view.findViewById(R.id.countLetter);
        edittitle = (EditText) view.findViewById(R.id.title_Write);
        btnSuccess = view.findViewById(R.id.btnSuccess);
        progressBar=view.findViewById(R.id.progress_View);
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        String currentdate = mFormat.format(mDate);
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();
        Bundle bundle = getArguments();
        if(bundle != null){
            chal_name = bundle.getString("chal_name_check");
        }


        // ?????? ????????? ?????? ??????
        btnWriteUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((editChalWrite.length()==0)||(edittitle.length()==0)||((imageView.getVisibility()==imageView.INVISIBLE)&&(videoView.getVisibility()==videoView.INVISIBLE))||(rate==null)){
                    Toast.makeText(getActivity(),"?????? ?????? ??????????????????",Toast.LENGTH_SHORT).show();
                }
                else{
                    //uploadToFirebase(imageUri);
                    uploadimg(); //????????????????????? ?????? ????????? ??????
                    Map<String, Object> post = new HashMap<>();
                    post.put("title",edittitle.getText().toString());
                    post.put("contents", editChalWrite.getText().toString());
                    post.put("percentage", rate);
                    post.put("file",imageUri.toString());
                    post.put("feeddate", currentdate);
                    post.put("feedname",email);
                    post.put("chal_name", chal_name);
                    //db??? post ?????? ?????????
                    db.collection("posts").add(post)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Document Snapshot successfully written"+ documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                    //??????????????? db??? user_point ????????????
                    db.collection("users").whereEqualTo("gmail", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            DocumentReference docRef = db.collection("users").document(document.getId());
                                            docRef.update("user_point", FieldValue.increment(1));

                                        }
                                    }
                                }
                            });

                    //progressBar = view.findViewById(R.id.progress_View);

//                    GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
//                    String email = gsa.getEmail();
//                    db.collection("users")
//                            .whereEqualTo("gmail", email)
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        for(QueryDocumentSnapshot document : task.getResult()){
//                                            Map<String, Object> post = new HashMap<>();
//                                            post.put("title",edittitle.getText().toString());
//                                            post.put("contents", editChalWrite.getText().toString());
//                                            post.put("percentage", rate);
//                                            post.put("file",imageUri.toString());
//                                            post.put("feeddate", currentdate);
//
//                                            post.put("feedname",document.get("nickname").toString());
//                    db.collection("posts").add(post)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Log.d(TAG, "Document Snapshot successfully written"+ documentReference.getId());
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error writing document", e);
//                                }
//                            });
//
//                                        }
//                                    }else{
//                                        Toast.makeText(getActivity(),"??????????????????",Toast.LENGTH_SHORT).show();
//                                        Log.d(TAG, "Error getting documents: ", task.getException());
//                                    }
//                                }
//                            });


                    Fragment fragment = new FragDfChal();
                    Bundle bundle = new Bundle();
                    bundle.putString("chal_name_check", chal_name);
                    fragment.setArguments(bundle);
                    FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                    fm.replace(R.id.main_frame ,fragment).commit();
                }
            }
        });

        // ?????? ????????? ?????? ??????
        btnWriteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(getActivity(),view);
                getActivity().getMenuInflater().inflate(R.menu.chal_write_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_menu1){
                            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            }
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, 1);
//                            activityResult.launch(intent);

                        }else if (menuItem.getItemId() == R.id.action_menu2){
                            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            }
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("video/*");
                            startActivityForResult(intent, 2);
//                            activityResult.launch(intent);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        // ?????? ??? ??????
        editChalWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = editChalWrite.getText().toString();
                countLetter.setText(input.length()+" / 200???");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // ????????? ??????
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(getActivity());

                dialog.setContentView(R.layout.rating_dialog);
                ratingBar = (RatingBar)dialog.findViewById(R.id.ratingBar);
                button2 = (Button)dialog.findViewById(R.id.button2);

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnSuccess.setText(rate + " / 10.0");
                        dialog.dismiss();
                    }
                });

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        float st = 10.0f/ratingBar.getNumStars();
                        rate = String.format("%.1f",(st * v) );
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        //?????????
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("??? ????????????");
        actionBar.setDisplayHomeAsUpEnabled(false);

        return view;
    }

//    public void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frag_df_chal_write,fragment);
//        fragmentTransaction.commit();
//    }

    // ?????? ??????(1)
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("??????")
                        .setMessage("????????? ????????? ???????????????. ???????????? ????????? ????????? ??????????????????.")
                        .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    }

    // ?????? ??????(2)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : ????????? ????????? 0, ????????? ????????? -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(getActivity(), "?????? ????????? ????????? ????????? ?????????. \n?????? ??????????????? ???????????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    // ?????? ?????? ??????
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("??? ??????????????? ?????????????????????????");
        builder.setCancelable(true);
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fragment fragment = new FragDfChal();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    ////
//    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode()==RESULT_OK&&result.getData()!=null){
//                        imageUri=result.getData().getData();
//                        imageView.setImageURI(imageUri);
//                    }
//                }
//            });

    ////

    // ?????? ???????????? ??????
    /// ??????) ??? ?????? ??? ?????? ????????? ??????????????? ???????????? ????????? ??????????????? ?????? ??????
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                try{
                    imageUri = data.getData();
                    if (resultCode == RESULT_OK) {
                        imageView.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.INVISIBLE);
                        imageView.setImageURI(imageUri);
                    }
                    break;
                }catch (NullPointerException e){
                    Log.d(TAG, "Error : ", e);
                }

            case 2:
                try {
                    imageUri = data.getData();
                    if (resultCode == RESULT_OK) {
                        videoView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                        videoView.setVideoURI(imageUri);
                        videoView.start();
                    }
                    break;
                }catch (NullPointerException e){
                    Log.d(TAG, "Error : ", e);
                }
        }
//            default:
//                throw new IllegalStateException("Unexpected value: " + requestCode);
    }
    ///

    //// ????????????????????? ?????? ??????????????? ?????? (?????? ??????)
    void uploadimg(){
        UploadTask uploadTask = null;
        //String timeStamp = mDate.format(new Date()); // ?????? ???????????? ?????? ?????? ???????????????
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();
        String imageFileName = "IMAGE_" + mFormat.format(mDate) +"_"+chal_name+"_"+email+ "_.png"; // ????????? ??????
        storageref  = reference.getReference().child("item").child(imageFileName); // ????????? ?????? ?????? ?????? (/item/imageFileName)
        uploadTask = storageref.putFile(imageUri); // ???????????? ????????? ???????????? ?????? ??????
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "----------------------------onSuccess: upload");
                downloadUri(); // ????????? ?????? ??? ???????????? ?????? Uri ????????????
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: upload");
            }
        });
    }
    ////

    ////
    void downloadUri(){
        storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@onSuccess: download");
                //Glide.with(requireView()).load(imageUri).into(imageView);
                //await FirebaseAuth.instance.signInAnonymously();
                System.out.println("^^^^^^^^^"+storageref.getDownloadUrl());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: download");
            }
        });

    }
    ////

//    private void signInAnonymously() {
//        mAuth.signInAnonymously().addOnSuccessListener(getActivity(), new  OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                // do your stuff
//            }
//        }).addOnFailureListener(getActivity(), new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
//                    }
//                });
//    }


    //firestore??? ????????? ??????
//    private void uploadToFirebase(Uri uri) {
//
//        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        //????????? ????????? ??????
//                        Model model = new Model(uri.toString());
//                        //?????? ????????? ??????
//                        String modelId = root.push().getKey();
//                        //????????? ??????
//                        root.child(modelId).setValue(model);
//                        //?????????????????? ??????
//                        progressBar.setVisibility(View.INVISIBLE);
//                        //Toast.makeText(getActivity(), "????????? ??????", Toast.LENGTH_SHORT).show();
////                        imageView.setImageResource(R.drawable.ic_add_photo);
//                    }
//                });
//            }
//        }).addOnProgressListener(new com.google.firebase.storage.OnProgressListener<TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                //?????????????????? ??????
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(getActivity(), "????????? ??????", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    //???????????? ????????????
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}