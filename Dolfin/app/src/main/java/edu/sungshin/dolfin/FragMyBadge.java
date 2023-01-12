package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragMyBadge extends Fragment implements onBackPressedListener {
    private View view;
    ImageView badge_exercise, badge_health, badge_book, badge_money, badge_hobby, badge_study;

    boolean exercise, health, book, money, hobby, study;

    GoogleSignInClient mGoogleSignInClient;

    ToggleButton toggleAlarm;
    Button infoChange, makeAlarm;
    TextView nickname, gmail, user_point;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_my_badge, container, false);
        badge_exercise = view.findViewById(R.id.badge_exercise);
        badge_health = view.findViewById(R.id.badge_health);
        badge_book = view.findViewById(R.id.badge_book);
        badge_money = view.findViewById(R.id.badge_money);
        badge_hobby = view.findViewById(R.id.badge_hobby);
        badge_study = view.findViewById(R.id.badge_study);

//        exercise = health = book = study = false;
//        money = hobby = true;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();

        db.collection("users")
                .whereEqualTo("gmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){

                                exercise = document.getBoolean("badge_exercise");
                                health = document.getBoolean("badge_health");
                                book = document.getBoolean("badge_book");
                                money = document.getBoolean("badge_money");
                                hobby = document.getBoolean("badge_hobby");
                                study = document.getBoolean("badge_study");

                                ColorMatrix matrix = new ColorMatrix();
                                matrix.setSaturation(0);
                                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

                                if (exercise)
                                    badge_exercise.setColorFilter(null);
                                else
                                    badge_exercise.setColorFilter(filter);

                                if (health)
                                    badge_health.setColorFilter(null);
                                else
                                    badge_health.setColorFilter(filter);

                                if (book)
                                    badge_book.setColorFilter(null);
                                else
                                    badge_book.setColorFilter(filter);

                                if (money)
                                    badge_money.setColorFilter(null);
                                else
                                    badge_money.setColorFilter(filter);

                                if (hobby)
                                    badge_hobby.setColorFilter(null);
                                else
                                    badge_hobby.setColorFilter(filter);

                                if (study)
                                    badge_study.setColorFilter(null);
                                else
                                    badge_study.setColorFilter(filter);

                            }
                        }else{
                            Toast.makeText(getActivity(),"불러오기오류",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        //메뉴 활성화
        setHasOptionsMenu(true);

        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("나의뱃지");
        actionBar.setDisplayHomeAsUpEnabled(false);
        return view;
    }
    // 메뉴 활성화
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.

        if (id == R.id.menuLogout) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail() // email addresses도 요청함
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            Toast.makeText(getActivity(), "로그아웃 되었습니다.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), Start.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        /*
        if(id==R.id.menuAlarm){
            Fragment fragment = new Notification();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.main_frame ,fragment).commit();
        }

         */

        return super.onOptionsItemSelected(item);
    }

    // (추가) 뒤로가기 기능 - FragMypage으로 이동
    @Override
    public void onBackPressed() {

        Fragment fragment = new FragMyPage();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_frame ,fragment).commit();
    }
}