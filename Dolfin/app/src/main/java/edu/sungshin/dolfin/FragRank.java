package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class FragRank extends Fragment implements  onBackPressedListener{

    // (추가) 뒤로가기 관련 변수 추가
    private final long finishtimeed = 1000;
    private long presstime = 0;

    ListView listview ;
    Rank_listview adapter1;
    Rank_listview adapter2;
    ArrayList<String> mylist;
    ArrayAdapter<String> adapter_1;
    private View view;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Rank_listview.ListViewItem> itemList1 = new ArrayList<Rank_listview.ListViewItem>() ;
    ArrayList<Rank_listview.ListViewItem> itemList2 = new ArrayList<Rank_listview.ListViewItem>() ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_rank, container, false);

        //메뉴 활성화
        setHasOptionsMenu(true);

        listview = view.findViewById(R.id.listview11);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();

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

        //Rank 데이터 불러오는 코드
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        itemList1.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                if(task.getResult().size() <= 0){
                                }else{
                                    //System.out.println("======================="+document.get("chal_name") + "은 존재하지 않습니다.");
                                    Rank_listview.ListViewItem point = new Rank_listview.ListViewItem();
                                    Rank_listview.ListViewItem item = new Rank_listview.ListViewItem();
                                    int points = Math.toIntExact((Long) document.get("user_point"));
                                    item.setPoint(points);
                                    item.setText(document.get("nickname").toString());
                                    int[] point_line_up ={points};

                                    //Arrays.sort(point_line_up,Collections.reverseOrder());
                                    //System.out.println(point_line_up);
                                    //item.setRank(Arrays.stream(point_line_up).collect(Collectors.toList()).indexOf(point_line_up));
                                    //ArrayList<Integer>  p = new ArrayList<>(Arrays.asList(point_line_up));
                                    //for(int i=1;i< point_line_up.length;i++) {
                                    //item.setRank(i);
                                    //}

                                    //item.setRank();


                                    itemList1.add(item);
                                    Comparator<Rank_listview.ListViewItem> noAsc = new Comparator<Rank_listview.ListViewItem>() {
                                        @Override
                                        public int compare(Rank_listview.ListViewItem item1, Rank_listview.ListViewItem item2) {
                                            return (item2.getPoint() - item1.getPoint()) ;
                                        }
                                    } ;
                                    Collections.sort(itemList1, noAsc) ;
                                    adapter1.notifyDataSetChanged() ;
                                }
                            }
                        }
                        else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.print("실패...............................................................");
                        }
                    }
                });

        mylist = adapter1.getRankList();

        adapter_1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,mylist);
        listview.setAdapter(adapter1);

        listview = (ListView) view.findViewById(R.id.listview12);
        listview.setAdapter(adapter2);

        /*adapter2.addItem(4, "Lee team");
        adapter2.addItem(18,"P18ark team");
        adapter2.addItem(19, "Lee19 team");*/

        Comparator<Rank_listview.ListViewItem> noAsc = new Comparator<Rank_listview.ListViewItem>() {
            @Override
            public int compare(Rank_listview.ListViewItem item1, Rank_listview.ListViewItem item2) {
                return (item1.getPoint() - item2.getPoint()) ;
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