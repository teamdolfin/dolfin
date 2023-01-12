package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import edu.sungshin.dolfin.databinding.FragMypageBinding;

public class FragMyPage extends Fragment implements onBackPressedListener{

    // (추가) 뒤로가기 관련 변수 추가
    private final long finishtimeed = 1000;
    private long presstime = 0;

    private View view;
    private @NonNull
    FragMypageBinding mBinding;
    GoogleSignInClient mGoogleSignInClient;

    ToggleButton toggleAlarm;
    Button infoChange, makeAlarm;
    TextView nickname, gmail, user_point;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //view=inflater.inflate(R.layout.frag_mypage, container, false);
        mBinding = FragMypageBinding.inflate(inflater, container, false);
        //뷰 바인딩
        mBinding = FragMypageBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        //메뉴 활성화
        setHasOptionsMenu(true);

        toggleAlarm = (ToggleButton) view.findViewById(R.id.toggleAlarm);
        makeAlarm = (Button) view.findViewById(R.id.btnMakeAlarm);
        infoChange = (Button) view.findViewById(R.id.btn_infoChange);
        nickname = (TextView) view.findViewById(R.id.nickname);
        gmail = (TextView) view.findViewById(R.id.tv_my_gmail);
        user_point = (TextView) view.findViewById(R.id.tv_user_point);

        EditText et = new EditText(getContext());


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

                                nickname.setText(document.get("nickname").toString());
                                gmail.setText(document.get("gmail").toString());
                                user_point.setText(document.get("user_point").toString());

                            }
                        }else{
                            Toast.makeText(getActivity(),"불러오기오류",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        /*CollectionReference userRef = db.collection("users");
        Query query = userRef.whereEqualTo("users", "zzz");*/


        // 닉네임 수정 페이지 이동
        infoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final EditText et = new EditText(getContext());

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());

                alt_bld.setTitle("변경할 닉네임을 입력하세요")

                        .setIcon(R.drawable.img_menu_setting)

                        .setCancelable(false)

                        .setView(et)

                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                final String value = et.getText().toString().replaceAll("\\s", "");
                                int valueI = value.length();
                                if (valueI == 0) {
                                    Fragment fragment = new FragMyPage();
                                    FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                                    fm.replace(R.id.main_frame ,fragment).commit();

                                    Toast.makeText(getActivity(), "한글자 이상 입력해 주세요", Toast.LENGTH_SHORT).show();

                                } else {
                                db.collection("users")
                                        .whereEqualTo("nickname", value)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().size() <= 0) {
                                                        db.collection("users")
                                                                .whereEqualTo("gmail", email)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if(task.isSuccessful()){
                                                                            for(QueryDocumentSnapshot documentt : task.getResult()){

                                                                                DocumentReference docRef = db.collection("users").document(documentt.getId());
                                                                                docRef.update("nickname", value);
                                                                                nickname.setText(value);

                                                                                Fragment fragment = new FragMyPage();
                                                                                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                                                                                fm.replace(R.id.main_frame ,fragment).commit();

                                                                                Toast.makeText(getContext(), "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }else{
                                                                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        Toast.makeText(getActivity(), "닉네임이 중복되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }
                                        });
                            }

                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Fragment fragment = new FragMyPage();
                                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                                fm.replace(R.id.main_frame ,fragment).commit();
                                dialogInterface.dismiss();
                            }

                        });

            AlertDialog alert = alt_bld.create();
            alert.show();
        }
    });




        toggleAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    makeAlarm.setVisibility(View.VISIBLE);
                }
                else {
                    makeAlarm.setVisibility(View.GONE);
                }
            }
        });
        //각 버튼 클릭시 이동화면

        // 1) 알람설정 클릭
        makeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Notification();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
            }
        });

        // 2) 회원정보 클릭
        //mBinding.btnMyInfo.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View view) {

        //Fragment fragment = new FragMyInfo();
        //FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        //fm.replace(R.id.main_frame ,fragment).commit();
        //}
        //});

        // 3) 나의 달력 클릭
        mBinding.btnMyCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FragMyCalender();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
            }
        });
        // 4) 나의 뱃지 클릭
        mBinding.btnMyBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new edu.sungshin.dolfin.FragMyBadge();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
            }
        });

        // 5) 앱 탈퇴
    mBinding.dolfinExit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
            alt_bld.setTitle("정말 탈퇴하시겠습니까?")
                    .setIcon(R.drawable.img_menu_setting)
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            db.collection("posts").whereEqualTo("feedname", email)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(QueryDocumentSnapshot documentt : task.getResult()){
                                                    DocumentReference documentReference = db.collection("posts").document(documentt.getId());
                                                    documentReference.delete();

                                                }
                                            }
                                        }
                                    });

                       db.collection("users").whereEqualTo("gmail", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                               for (QueryDocumentSnapshot documentt : task.getResult()) {

                                   DocumentReference docRef = db.collection("users").document(documentt.getId());
                                   docRef.delete();

                                   GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                           .requestEmail() // email addresses도 요청함
                                           .build();
                                   mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

                            /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            firebaseUser.delete();*/
                                   FirebaseAuth.getInstance().signOut();

                                   mGoogleSignInClient.signOut();
                                   Intent intent = new Intent(getActivity(), Start.class);

                                   startActivity(intent);

                                   Toast.makeText(getContext(), "탈퇴하였습니다.", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                     }
                                 });

                            }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert = alt_bld.create();
            alert.show();
        }
    });

        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("MyPage");
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

    


    // (추가) 알림설정 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.

        if (id == R.id.menuLogout) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail() // email addresses도 요청함
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            Toast.makeText(getActivity(), "로그아웃 되었습니다.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), Start.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    // (추가) 뒤로가기 기능 - 2번 누르면 앱 종료
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;

        if (0 <= intervalTime && finishtimeed >= intervalTime)
        {
            getActivity().finish();
            //finish();
        }
        else
        {
            presstime = tempTime;
            Toast.makeText(getActivity(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }


}