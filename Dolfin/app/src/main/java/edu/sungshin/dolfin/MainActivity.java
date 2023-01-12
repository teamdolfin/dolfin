package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.sungshin.dolfin.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity implements onBackPressedListener {

    private final long finishtimeed = 1000; // (추가)
    private long presstime = 0; // (추가)

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //4가지 하단버튼 - Fragment UI
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHome fragHome;
    private FragRank fragRank;
    private FragDolfin fragDolfin;
    private FragMyPage fragMyPage;

    GoogleSignInClient mGoogleSignInClient;

    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.frag_home);

        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(MainActivity.this);

        db.collection("users")
                .whereEqualTo("gmail", gsa.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().size() <= 0){
                                Toast.makeText(MainActivity.this, "로그인 후 닉네임을 생성해주세요.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                            }
                            else {
                                System.out.println("welcome");
                            }
                        }
                    }
                });


        //setContentView(R.layout.activity_main);
        //뷰 바인딩
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setItemIconTintList(null); // 아이콘이 테마색으로 변경되지 않게함 - Tint초기화
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        setFrag(0);
                        break;
                    case R.id.action_rank:
                        setFrag(1);
                        break;
                    case R.id.action_dolfin:
                        setFrag(2);
                        break;
                    case R.id.action_setting:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });
        fragHome = new FragHome();
        fragRank = new FragRank();
        fragDolfin = new FragDolfin();
        fragMyPage = new FragMyPage();
        setFrag(0); // 첫 화면을 Home으로 지정

    }

    //  프래그먼트 교체가 일어나는 실행문이다.
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, fragHome);
                ft.commit(); //저장
                break;
            case 1:
                ft.replace(R.id.main_frame, fragRank);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, fragDolfin);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, fragMyPage);
                ft.commit();
                break;
        }
    }

    // (추가) 뒤로가기 기능 - 2번 누르면 앱 종료
    /*@Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;

        if (0 <= intervalTime && finishtimeed >= intervalTime)
        {
            finish();
        }
        else
        {
            presstime = tempTime;
            Toast.makeText(getApplicationContext(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }*/

    // (추가) 뒤로가기 기능 관련 추가
    @Override
    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;

        if (0 <= intervalTime && finishtimeed >= intervalTime)
        {
            finishAffinity();
        }
        else
        {
            presstime = tempTime;
            Toast.makeText(MainActivity.this, "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }


        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof onBackPressedListener) {
                ((onBackPressedListener) fragment).onBackPressed();
                return;
            }

        }

    }

}