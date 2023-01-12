package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Nickname extends AppCompatActivity {
    Button Btn_NicknameSetting;
    EditText Et_NicknameSetting;
    TextView Tv_email, Tv_overlap;
    View view;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Btn_NicknameSetting = findViewById(R.id.btn_nicknameSetting);
        Et_NicknameSetting = findViewById(R.id.et_nicknameSetting);
        Tv_email = findViewById(R.id.tv_email);
        Tv_overlap = findViewById(R.id.tv_nickname_overlap);

        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");
        String name = intent.getExtras().getString("name");
        String id = intent.getExtras().getString("id");
        Tv_email.setText(String.valueOf(email));
        String nickname = Et_NicknameSetting.getText().toString();
        String overlap = "";
        ArrayList<String> myChal_list = new ArrayList<>();


        Btn_NicknameSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Et_NicknameSetting.length() == 0){
                    Toast.makeText(Nickname.this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else {


                    db.collection("users")
                            .whereEqualTo("nickname", Et_NicknameSetting.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().size() <= 0) {

                                            Toast.makeText(Nickname.this, "DolFin에 오신 것을 환영합니다.", Toast.LENGTH_SHORT).show();

                                            Map<String, Object> user = new HashMap<>();
                                            user.put("id", id);
                                            user.put("user_name", name);
                                            user.put("gmail", email);
                                            user.put("nickname", Et_NicknameSetting.getText().toString());
                                            user.put("user_point", 0);
                                            user.put("small_dolphin", 0);
                                            user.put("large_dolphin", 0);
                                            user.put("user_week_point", 0);
                                            user.put("user_rank", 0);
                                            user.put("badge_book", false);
                                            user.put("badge_exercise", false);
                                            user.put("badge_health", false);
                                            user.put("badge_hobby", false);
                                            user.put("badge_money", false);
                                            user.put("badge_study", false);
                                            user.put("my_chal", myChal_list);

                                            db.collection("users").add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d(TAG, "Document Snapshot successfully written" + documentReference.getId());
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });
                                            Intent intent1 = new Intent(Nickname.this, MainActivity.class);
                                            startActivity(intent1);

                                        }
                                        else {
                                            Toast.makeText(Nickname.this, "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            });



                }
            }
        });

    }

    private long presstime = 0;
    private final long finishtimeed = 1000;

    @Override
    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(Nickname.this, gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(Nickname.this);


        if (0 <= intervalTime && finishtimeed >= intervalTime) {
            finishAffinity();
        } else {
            presstime = tempTime;
            Toast.makeText(Nickname.this, "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }




    }
}
