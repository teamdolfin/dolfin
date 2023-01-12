package edu.sungshin.dolfin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import edu.sungshin.dolfin.databinding.FragDolfinBinding;

public class FragDolfin extends Fragment implements  onBackPressedListener {

    // (추가) 뒤로가기 관련 변수 추가
    private final long finishtimeed = 1000;
    private long presstime = 0;

    private View view;
    private ListView list;

    private FragDolfinBinding mBinding;
    //private FragmentManager fm;
    //private FragmentTransaction ft;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> chal_list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragDolfinBinding.inflate(inflater, container, false);
        //뷰 바인딩
        mBinding = FragDolfinBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();

        //메뉴 활성화
        setHasOptionsMenu(true);

        db.collection("challenges")
                .whereArrayContains("member_email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        chal_list.clear();
                        System.out.println("******************" + "들어갔니" + "*******************");
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                //DocumentReference docRef = db.collection("challenges").document(document.getId());
                                System.out.println("=================" + document.get("chal_name"));
                                //chal_list = ((ArrayList<String>)documentt.get("my_chal"));
                                chal_list.add((String)document.get("chal_name"));
                                System.out.println("=================" + chal_list);
                            }
                        }else{
                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        list = view.findViewById(R.id.list1);
        List<String> data1 = new ArrayList<>();

        list.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line,data1));
        System.out.println("***************************" + data1 + "**************************");

        data1.addAll(chal_list);



        //각 버튼 클릭시 이동화면
        // 1) 3km 걷기 챌린지
        mBinding.list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view, int position,long id) {
                for(int i = 0; i < chal_list.size(); i++) {
                    if (position == i) {
                        String chal_name = chal_list.get(i);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment fragment = new FragDfChal();

                        Bundle bundle = new Bundle();
                        bundle.putString("chal_name_check", chal_name);

                        fragment.setArguments(bundle);
                        ft.replace(R.id.main_frame, fragment);
                        ft.commit();

                    }
                }
            }
        });


        //진행 중인 리스트 뷰


        //완료된 리스튜 뷰
        list = view.findViewById(R.id.list2);
        List<String> data2 = new ArrayList<>();
        list.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line,data2));

        data2.add("ㄱ 챌린지");
        data2.add("ㄴ 챌린지");
        data2.add("ㄷ 챌린지");
        data2.add("ㄹ 챌린지");
        data2.add("ㅁ 챌린지");
        data2.add("ㅂ 챌린지");
        data2.add("ㅅ 챌린지");
        data2.add("ㅇ 챌린지");
        data2.add("ㅈ 챌린지");


        //완료한 챌린지, 끝난 챌린지 탭 호스트
        TabHost tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();

        //tab1
        TabHost.TabSpec spec = tabHost.newTabSpec("진행 중인 챌린지");
        spec.setContent(R.id.tab1);
        spec.setIndicator("진행 중인 챌린지");
        tabHost.addTab(spec);

        //tab2
        spec = tabHost.newTabSpec("완료한 챌린지");
        spec.setContent(R.id.tab2);
        spec.setIndicator("완료한 챌린지");
        tabHost.addTab(spec);

        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("나의 챌린지");
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