package edu.sungshin.dolfin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FragRank extends Fragment implements  onBackPressedListener{

    // (추가) 뒤로가기 관련 변수 추가
    private final long finishtimeed = 1000;
    private long presstime = 0;

    ListView listview ;
    Rank_listview adapter1;
    Rank_listview adapter2;
    private View view;
    GoogleSignInClient mGoogleSignInClient;

    ArrayList<Rank_listview.ListViewItem> itemList1 = new ArrayList<Rank_listview.ListViewItem>() ;
    ArrayList<Rank_listview.ListViewItem> itemList2 = new ArrayList<Rank_listview.ListViewItem>() ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_rank, container, false);

        //메뉴 활성화
        setHasOptionsMenu(true);

        //개인별 랭킹, 팀별 랭킹 탭 호스트
        TabHost tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();

        //tab1
        TabHost.TabSpec spec = tabHost.newTabSpec("개인별 랭킹");
        spec.setContent(R.id.tab3);
        spec.setIndicator("개인별 랭킹");
        tabHost.addTab(spec);

        //tab2
        spec = tabHost.newTabSpec("팀별 랭킹");
        spec.setContent(R.id.tab4);
        spec.setIndicator("팀별 랭킹");
        tabHost.addTab(spec);

        //Adapter 생성
        adapter1 = new Rank_listview(itemList1) ;
        adapter2 = new Rank_listview(itemList2) ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listview11);
        listview.setAdapter(adapter1);

        adapter1.addItem(4, "Choi");
        adapter1.addItem(18,"P18ark");
        adapter1.addItem(19, "Lee19");
        adapter1.addItem(3, "Lee");
        adapter1.addItem(21, "Choi21");
        adapter1.addItem(5, "Pong");
        adapter1.addItem(17, "Ki17m");
        adapter1.addItem(8,"P8ark");
        adapter1.addItem(14, "L14ee");
        adapter1.addItem(2,"Park");
        adapter1.addItem(22, "K22im");
        adapter1.addItem(6, "Ch6oi");
        adapter1.addItem(10, "P10ong");
        adapter1.addItem(23,"23Park");
        adapter1.addItem(20, "Po20ng");
        adapter1.addItem(9, "L9ee");
        adapter1.addItem(15, "Pon15g");
        adapter1.addItem(12, "Ki12");
        adapter1.addItem(13,"Park13");
        adapter1.addItem(1, "Kim");
        adapter1.addItem(24, "Lee24");
        adapter1.addItem(11, "Choi11");
        adapter1.addItem(7, "Kim7");
        adapter1.addItem(16, "Ch16oi");
        adapter1.addItem(25, "Po25ng");

        listview = (ListView) view.findViewById(R.id.listview12);
        listview.setAdapter(adapter2);

        adapter2.addItem(4, "Lee team");
        adapter2.addItem(18,"P18ark team");
        adapter2.addItem(19, "Lee19 team");



        Comparator<Rank_listview.ListViewItem> noAsc = new Comparator<Rank_listview.ListViewItem>() {
            @Override
            public int compare(Rank_listview.ListViewItem item1, Rank_listview.ListViewItem item2) {
                return (item1.getNo() - item2.getNo()) ;
            }
        } ;

        Collections.sort(itemList1, noAsc) ;
        adapter1.notifyDataSetChanged() ;

        Collections.sort(itemList2, noAsc) ;
        adapter2.notifyDataSetChanged() ;

        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Rank");
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