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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FragHmMoney extends Fragment implements onBackPressedListener {

    private View view;
    ListView listview;
    ArrayAdapter<String> adapter_1;
    ArrayList<String> mylist;
    chal_listview adapter;
    Button flt, flt_latest, flt_lowercase, flt_recommend, flt_popular, FilterBtn;

    SearchView searchText;

    Button btnCreate;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<chal_listview.ListViewItem> arraylist = new ArrayList<chal_listview.ListViewItem>();

    ArrayList<chal_listview.ListViewItem> itemList = new ArrayList<chal_listview.ListViewItem>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_hm_category,container,false);


        //메뉴 활성화
        setHasOptionsMenu(true);

        flt = view.findViewById(R.id.FilterBtn);
        flt_latest = view.findViewById(R.id.FilterBtn_latest);
        flt_lowercase = view.findViewById(R.id.FilterBtn_lowerCase);
        flt_recommend = view.findViewById(R.id.FilterBtn_recommend);
        flt_popular = view.findViewById(R.id.FilterBtn_popular);

        FilterBtn = view.findViewById(R.id.FilterBtn);
        searchText = view.findViewById(R.id.searchText);
        listview = view.findViewById(R.id.chal_listview);
        /*

        // Adapter 생성
        adapter = new chal_listview(itemList);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.chal_listview);
        listview.setAdapter(adapter);

        adapter.addItem("Chal_1", 1, 10, "2022-11-02", "2022-11-22", 3);
        adapter.addItem("Chal_4", 3, 5, "2022-10-03", "2022-11-03", 4);
        adapter.addItem("Chal_5", 6, 8, "2022-03-09", "2022-12-09", 7);
        adapter.addItem("Chal_3", 2, 30, "2022-01-07", "2022-10-27", 5);
        adapter.addItem("Chal_2", 10, 10, "2022-12-01", "2023-11-30", 1);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view, int position,long id) {
                if(position==0){
                    Fragment fragment = new FragHmCategoryChal();
                    FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                    fm.replace(R.id.main_frame ,fragment).commit();
                }
            }
        });

         */
        adapter = new chal_listview(itemList);
        db.collection("challenges").whereEqualTo("category","금융")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        itemList.clear();

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                System.out.println("======================="+document.getData());
                                chal_listview.ListViewItem challenges = new chal_listview.ListViewItem();
                                chal_listview.ListViewItem item = new chal_listview.ListViewItem();
                                item.setName(document.get("chal_name").toString());
                                item.setmNum(Math.toIntExact((Long) document.get("member_num")));
                                item.setmMax(Math.toIntExact((Long) document.get("member_max")));
                                item.setStr(document.get("start_date").toString());
                                item.setEnd(document.get("end_date").toString());
                                item.setCnt(Math.toIntExact((Long) document.get("count_week")));

                                itemList.add(item);
                                System.out.println(challenges.getName() + challenges.getEnd());
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.print("실패...............................................................");
                        }
                    }
                });
/*
        adapter.addItem("Chal_1", 1, 10, "2022-11-02", "2022-11-22", 3);
        adapter.addItem("d_5", 6, 8, "2022-03-09", "2022-12-09", 7);
        adapter.addItem("Chal_hobby", 2, 30, "2022-01-07", "2022-10-27", 5);
        adapter.addItem("Chal_exercise", 10, 10, "2022-12-01", "2023-11-30", 1);*/
        //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1) ;

        mylist = adapter.getNameList();
//        mylist = new ArrayList<>();
//        mylist.add("Chal_1");
//        mylist.add("Chal_money");
//        mylist.add("d_5");
//        mylist.add("Chal_hobby");
//        mylist.add("Chal_exercise");
        adapter_1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mylist);
        listview.setAdapter(adapter);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setIconified(false);
            }
        });

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (mylist.contains(query)) {
                    adapter_1.getFilter().filter(query);
                    listview.setAdapter(adapter_1);
                } else {
                    // Search query not found in List View
                    Toast.makeText(getActivity(), "해당 챌린지는 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_1.getFilter().filter(newText);
                listview.setAdapter(adapter_1);
                return false;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view, int position,long id) {
                for(int i = 0; i < itemList.size(); i++) {
                    if (position == i) {
                        String chal_name = itemList.get(i).getName();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment fragment = new FragHmCategoryChal();

                        Bundle bundle = new Bundle();
                        bundle.putString("chal_name_check", chal_name);

                        fragment.setArguments(bundle);
                        ft.replace(R.id.main_frame, fragment);
                        ft.commit();

                    }
                }
            }
        });
        //최신순 필터 버튼 누를 때
        flt_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged() ;
            }
        });

        //사전순 필터 버튼 누를 때
        flt_lowercase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<chal_listview.ListViewItem> textAsc = new Comparator<chal_listview.ListViewItem>() {
                    @Override
                    public int compare(chal_listview.ListViewItem item1, chal_listview.ListViewItem item2) {
                        return item1.getName().compareTo(item2.getName());
                    }
                };
                Collections.sort(itemList, textAsc) ;
                adapter.notifyDataSetChanged() ;
            }
        });

        //추천순 필터 버튼 누를 때
        flt_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //추천순 -db 이후 작업 필요
            }
        });

        //인기순 필터 버튼 누를 때
        flt_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<chal_listview.ListViewItem> noDesc = new Comparator<chal_listview.ListViewItem>() {
                    @Override
                    public int compare(chal_listview.ListViewItem item1, chal_listview.ListViewItem item2) {
                        return (item2.getmNum() - item1.getmNum());
                    }
                };
                Collections.sort(itemList, noDesc) ;
                adapter.notifyDataSetChanged() ;
            }
        });

        // 달력
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constrainBuilder =  new CalendarConstraints.Builder();
        constrainBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constrainBuilder.build());
        MaterialDatePicker materialDatePicker = builder.build();
        FilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        //term.setText(materialDatePicker.getHeaderText());
                    }
                });
                //Fragment fragment = new Calendar2();
                //FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                //fm.replace(R.id.main_frame ,fragment).commit();
            }
        });

        btnCreate = (Button) view.findViewById(R.id.btn_create);
        //btnChal1 = (Button) view.findViewById(R.id.btnChal1);
        //각 버튼 클릭시 이동화면
        // 1) 방 만들기 클릭
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragHmCategoryAdd();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
            }
        });
//        // 2) 챌린지1 클릭
//        btnChal1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Fragment fragment = new FragHmExerciseChal();
//                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
//                fm.replace(R.id.main_frame ,fragment).commit();
//            }
//        });
        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Money");
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

    // (추가) 뒤로가기 기능 - FragHome으로 이동
    @Override
    public void onBackPressed() {

        Fragment fragment = new FragHome();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_frame ,fragment).commit();
    }
}