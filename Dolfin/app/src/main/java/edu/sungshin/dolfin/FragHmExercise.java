package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.sungshin.dolfin.databinding.FragHmCategoryBinding;
import edu.sungshin.dolfin.databinding.FragHomeBinding;



public class FragHmExercise extends Fragment implements  onBackPressedListener {
    private View view;
    private FragHmCategoryBinding mBinding;
    ListView listview;
    ArrayAdapter<String> adapter_1;
    ArrayList<String> mylist;
    chal_listview adapter;
    Button flt, flt_latest, flt_lowercase, flt_recommend, flt_popular, FilterBtn;
    Bundle bundle = new Bundle();
    SearchView searchText;
    ArrayList<String> chal_my_list = new ArrayList<>();
    ArrayList<chal_listview.ListViewItem> itemList = new ArrayList<chal_listview.ListViewItem>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GoogleSignInClient mGoogleSignInClient;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragHmCategoryBinding.inflate(inflater, container, false);
        //??? ?????????
        mBinding = FragHmCategoryBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses??? ?????????
                .build();
        // ????????? ?????? GoogleSignInOptions??? ????????? GoogleSignInClient ????????? ??????
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        // ????????? ????????? ?????? ????????? ????????????.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();
        //?????? ?????????
        setHasOptionsMenu(true);

        //????????? ???
        flt = view.findViewById(R.id.FilterBtn);
        flt_latest = view.findViewById(R.id.FilterBtn_latest);
        flt_lowercase = view.findViewById(R.id.FilterBtn_lowerCase);
        flt_recommend = view.findViewById(R.id.FilterBtn_recommend);
        flt_popular = view.findViewById(R.id.FilterBtn_popular);
        FilterBtn = view.findViewById(R.id.FilterBtn);
        searchText = view.findViewById(R.id.searchText);
        listview = view.findViewById(R.id.chal_listview);
        adapter = new chal_listview(itemList);

        ///////??????) ?????? ????????? ?????????(end_date ???????????? ??????) : ????????? true??? ??????
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yy??? MM??? dd???", Locale.KOREA );
        Date currentTime = new Date();
        String oTime = mSimpleDateFormat.format ( currentTime ); //???????????? (String)
        db.collection("challenges").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        DocumentReference docRef = db.collection("challenges").document(document.getId());
                        String a = document.get("end_date").toString();
                        int compare = oTime.compareTo( a ); // ????????????
                        if (compare>0){
                            //Log.d(TAG, "&&&&&&&&&&&&????????? ?????????&&&&&&&&&&&&&", task.getException());
                            docRef.update("finished", true);
                            Log.d(TAG, "%%%%%%%%%%%%%%%%"+a, task.getException());
                        }
                        //intro_view.setText((String)document.get("chal_intro"));
                    }
                }else{
                    Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        ///////

        //?????? ????????? ?????? ???????????? ??????
        db.collection("users")
                .whereEqualTo("gmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentt : task.getResult()){
                                DocumentReference docRef = db.collection("users").document(documentt.getId());
                                chal_my_list = ((ArrayList<String>)documentt.get("my_chal"));
                                System.out.println("******************" + chal_my_list);
                            }
                        }else{
                            Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //???????????? ???????????? ????????? ????????? ???????????? ??????
        db.collection("challenges").whereEqualTo("category", "??????")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        itemList.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                db.collection("challenges").whereArrayContains("member_email", email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    if(task.getResult().size() <= 0){


                                                    }else{
                                                        //System.out.println("======================="+document.get("chal_name") + "??? ???????????? ????????????.");
                                                        chal_listview.ListViewItem challenges = new chal_listview.ListViewItem();
                                                        chal_listview.ListViewItem item = new chal_listview.ListViewItem();
                                                        item.setName(document.get("chal_name").toString());
                                                        item.setmNum(Math.toIntExact((Long) document.get("member_num")));
                                                        item.setmMax(Math.toIntExact((Long) document.get("member_max")));
                                                        item.setStr(document.get("start_date").toString());
                                                        item.setEnd(document.get("end_date").toString());
                                                        item.setCnt(Math.toIntExact((Long) document.get("count_week")));

                                                        String name = document.get("chal_name").toString();
                                                        System.out.println(name);

                                                        ///////??????) finish challenge ????????? ??????????????? ???????????? ??????
                                                        String end = document.get("end_date").toString();
                                                        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yy??? MM??? dd???", Locale.KOREA );
                                                        Date currentTime = new Date();
                                                        String oTime = mSimpleDateFormat.format ( currentTime );
                                                        int compare = oTime.compareTo( end );
                                                        if (compare<0){
                                                            itemList.add(item);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                        else{
                                                            Log.d(TAG, "/////////////////////??????", task.getException());
                                                        }

                                                        //itemList.add(item);
                                                        //adapter.notifyDataSetChanged();

                                                        ///////

                                                    }
                                                }
                                            }
                                        });
                       /*         System.out.println("======================="+document.getData());
                                chal_listview.ListViewItem challenges = new chal_listview.ListViewItem();
                                chal_listview.ListViewItem item = new chal_listview.ListViewItem();
                                item.setName(document.get("chal_name").toString());
                                item.setmNum(Math.toIntExact((Long) document.get("member_num")));
                                item.setmMax(Math.toIntExact((Long) document.get("member_max")));
                                item.setStr(document.get("start_date").toString());
                                item.setEnd(document.get("end_date").toString());
                                item.setCnt(Math.toIntExact((Long) document.get("count_week")));

                                String name = document.get("chal_name").toString();
                                System.out.println(name);

                                for(int i=0; i< chal_my_list.size(); i++){
                                    System.out.println("!!!!!!!"+chal_my_list.get(i));
                                    if(chal_my_list.get(i).contains(name)){
                                        continue;
                                    }
                                    itemList.add(item);
                                    adapter.notifyDataSetChanged();
                                }*/


                            }
                        }
                        else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.print("??????...............................................................");
                        }
                    }

                });

        mylist = adapter.getNameList();
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
                    Toast.makeText(getActivity(), "?????? ???????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
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
        mBinding.chalListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        //????????? ?????? ?????? ?????? ???
        flt_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged() ;
            }
        });

        //????????? ?????? ?????? ?????? ???
        flt_lowercase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setAdapter(adapter);
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

        //????????? ?????? ?????? ?????? ???
        flt_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????? -db ?????? ?????? ??????
            }
        });

        //????????? ?????? ?????? ?????? ???
        flt_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setAdapter(adapter);
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




        // ??????
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
                    }
                });
            }
        });

        // ??? ????????? ?????? ???
        mBinding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragHmCategoryAdd();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
            }
        });


        //?????????
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Exercise");
        actionBar.setDisplayHomeAsUpEnabled(false);
        return view;
    }
    // ?????? ?????????
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch?????? ???????????? ?????? ??????.

        if (id == R.id.menuLogout) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail() // email addresses??? ?????????
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            Toast.makeText(getActivity(), "???????????? ???????????????.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), Start.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    // (??????) ???????????? ?????? - FragHome?????? ??????
    @Override
    public void onBackPressed() {

        Fragment fragment = new FragHome();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_frame ,fragment).commit();
    }


}