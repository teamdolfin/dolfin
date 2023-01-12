package edu.sungshin.dolfin;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Dialog;
import android.app.Instrumentation;
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
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

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
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri imageUri;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yy년 MM월 dd일");



    private static final int MY_PERMISSION_STORAGE = 1111;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_df_chal_write,container,false);
        //메뉴 활성화
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
        //System.out.println("**********************"+email);


        // 파일 업로드 필수 지정
        btnWriteUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((editChalWrite.length()==0)||(edittitle.length()==0)||((imageView.getVisibility()==imageView.INVISIBLE)&&(videoView.getVisibility()==videoView.INVISIBLE))||(rate==null)){
                    Toast.makeText(getActivity(),"값을 모두 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadToFirebase(imageUri);

                    Map<String, Object> post = new HashMap<>();
                    post.put("title",edittitle.getText().toString());
                    post.put("contents", editChalWrite.getText().toString());
                    post.put("percentage", rate);
                    post.put("file",imageUri.toString());
                    post.put("feeddate", currentdate);

                    post.put("feedname",email);
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
//
//
//                                        }
//                                    }else{
//                                        Toast.makeText(getActivity(),"불러오기오류",Toast.LENGTH_SHORT).show();
//                                        Log.d(TAG, "Error getting documents: ", task.getException());
//                                    }
//                                }
//                            });







                    Fragment fragment = new FragDfChal();
                    FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                    fm.replace(R.id.main_frame ,fragment).commit();
                    //Toast.makeText(getActivity().getApplicationContext(), "Fragment전환됨", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 파일 업로드 버튼 기능
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

        // 글자 수 제한
        editChalWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = editChalWrite.getText().toString();
                countLetter.setText(input.length()+" / 200자");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 달성률 체크
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

                //리스너 설정 - OnRatingBarChangeListener
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

        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("글 작성하기");
        actionBar.setDisplayHomeAsUpEnabled(false);

        return view;
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_df_chal_write,fragment);
        fragmentTransaction.commit();
    }

    // 권한 설정(1)
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("알림")
                        .setMessage("저장소 권한이 필요합니다. 설정에서 저장소 권한을 허가해주세요.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
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

    // 권한 설정(2)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(getActivity(), "해당 권한을 활성화 하셔야 합니다. \n현재 페이지에서 나갔다가 다시 들어와주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    // 뒤로 가기 기능
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("글 작성하기를 종료하시겠습니까?");
        builder.setCancelable(true);
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
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

    // 파일 보여주는 기능
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                imageUri = data.getData();
                if (resultCode == RESULT_OK) {
                    imageView.setVisibility(android.view.View.VISIBLE);
                    videoView.setVisibility(android.view.View.INVISIBLE);
                    imageView.setImageURI(imageUri);
                }
                break;
            case 2:
                imageUri = data.getData();
                if (resultCode == RESULT_OK) {
                    videoView.setVisibility(android.view.View.VISIBLE);
                    imageView.setVisibility(android.view.View.INVISIBLE);
                    videoView.setVideoURI(imageUri);
                    videoView.start();
                }
                break;
        }

    }

    //firestore에 이미지 저장
    private void uploadToFirebase(Uri uri) {

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 모델에 담기
                        Model model = new Model(uri.toString());
                        //키로 아이디 생성
                        String modelId = root.push().getKey();
                        //데이터 넣기
                        root.child(modelId).setValue(model);
                        //프로그래스바 숨김
                        progressBar.setVisibility(View.INVISIBLE);
                        //Toast.makeText(getActivity(), "업로드 성공", Toast.LENGTH_SHORT).show();
//                        imageView.setImageResource(R.drawable.ic_add_photo);
                    }
                });
            }
        }).addOnProgressListener(new com.google.firebase.storage.OnProgressListener<TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //프로그래스바 숨김
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //파일타입 가져오기

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}