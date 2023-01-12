package edu.sungshin.dolfin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import edu.sungshin.dolfin.databinding.FragDolfinBinding;
import edu.sungshin.dolfin.databinding.FragHomeBinding;

public class FragHome extends Fragment implements  onBackPressedListener{

    // (추가) 뒤로가기 관련 변수 추가
    private final long finishtimeed = 1000;
    private long presstime = 0;

    private View view;
    private @NonNull FragHomeBinding mBinding;
    GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //view=inflater.inflate(R.layout.frag_home, container, false);
        mBinding = FragHomeBinding.inflate(inflater, container, false);
        //뷰 바인딩
        mBinding = FragHomeBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();

        //메뉴 활성화
        setHasOptionsMenu(true);

        //각 버튼 클릭시 이동화면
        // 1) 운동 카테고리 클릭
        //mBinding.btnHomeExercise.setOnClickListener(this::onClick);
        // 2,3,4,5,6) 카테고리 클릭
        //mBinding.btnHomeHealth.setOnClickListener(this::onClick);
        //mBinding.btnHomeBook.setOnClickListener(this::onClick);
        //mBinding.btnHomeMoney.setOnClickListener(this::onClick);
        //mBinding.btnHomeHobby.setOnClickListener(this::onClick);
        //mBinding.btnHomeStudy.setOnClickListener(this::onClick);
        mBinding.btnHomeExercise.setOnClickListener(this::onClick);
        mBinding.btnHomeHealth.setOnClickListener(this::onClick);
        mBinding.btnHomeBook.setOnClickListener(this::onClick);
        mBinding.btnHomeMoney.setOnClickListener(this::onClick);
        mBinding.btnHomeHobby.setOnClickListener(this::onClick);
        mBinding.btnHomeStudy.setOnClickListener(this::onClick);

        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Home");
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

    //추가
    //클릭시 이동 메소드
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_home_exercise:
                Fragment fragment1 = new FragHmExercise();
                FragmentTransaction fm1 = getActivity().getSupportFragmentManager().beginTransaction();
                fm1.replace(R.id.main_frame ,fragment1).commit();
                break;
            case R.id.btn_home_health:
                Fragment fragment2 = new FragHmHealth();
                FragmentTransaction fm2 = getActivity().getSupportFragmentManager().beginTransaction();
                fm2.replace(R.id.main_frame ,fragment2).commit();
                break;
            case R.id.btn_home_book:
                Fragment fragment3 = new FragHmBook();
                FragmentTransaction fm3 = getActivity().getSupportFragmentManager().beginTransaction();
                fm3.replace(R.id.main_frame ,fragment3).commit();
                break;
            case R.id.btn_home_money:
                Fragment fragment4 = new FragHmMoney();
                FragmentTransaction fm4 = getActivity().getSupportFragmentManager().beginTransaction();
                fm4.replace(R.id.main_frame ,fragment4).commit();
                break;
            case R.id.btn_home_hobby:
                Fragment fragment5 = new FragHmHobby();
                FragmentTransaction fm5 = getActivity().getSupportFragmentManager().beginTransaction();
                fm5.replace(R.id.main_frame ,fragment5).commit();
                break;
            case R.id.btn_home_study:
                Fragment fragment6 = new FragHmStudy();
                FragmentTransaction fm6 = getActivity().getSupportFragmentManager().beginTransaction();
                fm6.replace(R.id.main_frame ,fragment6).commit();
                break;
        }

    }
    //클릭시 이동 메소드
    //public void onClick(View view) {
        //Fragment fragment = new FragHmExercise();
        //FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        //fm.replace(R.id.main_frame ,fragment).commit();
    //}

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
        /*
        if(id==R.id.menuAlarm){
            Fragment fragment = new Notification();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.main_frame ,fragment).commit();
        }

         */

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
