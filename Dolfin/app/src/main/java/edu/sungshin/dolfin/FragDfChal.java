package edu.sungshin.dolfin;


import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
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


import java.util.ArrayList;



import edu.sungshin.dolfin.databinding.FragDfChalBinding;

public class FragDfChal extends Fragment implements  onBackPressedListener{
    private View view;
    private FragDfChalBinding mBinding;
    //Uri path = Uri.parse("content://media/external/images/media/953");

    ListView listview;
    ArrayAdapter<String> adapter_10;
    ArrayList<String> mylist;
    feed_listview adapter10;
    String chal_name;
    TextView intro_view;


    ArrayList<feed_listview.ListViewItem> arraylist = new ArrayList<feed_listview.ListViewItem>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GoogleSignInClient mGoogleSignInClient;


    //img = view.findViewById(R.id.img);

    ArrayList<feed_listview.ListViewItem> itemList10 = new ArrayList<feed_listview.ListViewItem>();

    //


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_df_chal, container, false);
        mBinding = FragDfChalBinding.inflate(inflater, container, false);
        //
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();

        Bundle bundle = getArguments();

        if(bundle != null){
            chal_name = bundle.getString("chal_name_check");
        }
        //



        //뷰 바인딩
        mBinding = FragDfChalBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();

        //메뉴 활성화
        setHasOptionsMenu(true);


        //피드
        listview = view.findViewById(R.id.feed_listview);

        adapter10 = new feed_listview(itemList10);

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        itemList10.clear();

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                System.out.println("======================="+document.getData());
                                feed_listview.ListViewItem posts = new feed_listview.ListViewItem();
                                feed_listview.ListViewItem item = new feed_listview.ListViewItem();
                                item.setmyachieve(document.get("percentage").toString());
                                item.setfeedtitle(document.get("title").toString());
                                System.out.println("************************"+document.get("feedname"));
                                item.setfeedname(document.get("feedname").toString());
                                item.setfeeddate(document.get("feeddate").toString());
                                Uri path = Uri.parse(document.get("file").toString());
                                item.setimage(path);
                                item.setfeedcontents(document.get("contents").toString());

//                                if(path.toString().contains(".mp4")){
//                                    item.setVideoView3(path);
//
//                                }
//                                else{
//                                    item.setimage(path);
//                                }


                                itemList10.add(item);
                                System.out.println(posts.getfeedtitle() + posts.getfeedname());
                                adapter10.notifyDataSetChanged();

                            }

                        }
                        else{

                            Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.print("실패...............................................................");
                        }
                    }

                });
        //siisssss
//        adapter10.addItem("10.0",  "쓰레기 줍기 인증합니다", "투투", "2022-11-19",
//                ContextCompat.getDrawable(getActivity(),R.drawable.img_chal_ex1),"쓰레기 줍기 인증글 입니다. 모두 파이팅^^","삭제");
//        adapter10.addItem("3.0", "쓰레기 줍기 인증합니다아", "체리", "2022-11-19",
//                ContextCompat.getDrawable(getActivity(),R.drawable.img_chal_ex2),"쓰레기 줍기 인증글 입니다. 오늘도 힘내세요","삭제");
//        adapter10.addItem("3.0", "쓰레기 줍기 인증합니다!!", "사과", "2022-11-19",
//                ContextCompat.getDrawable(getActivity(),R.drawable.img_chal_ex3),"쓰레기 줍기 인증글 입니다.","삭제");
//        adapter10.addItem("5.0", "쓰레기 줍기 인증합니다~", "자두", "2022-11-19",
//                ContextCompat.getDrawable(getActivity(),R.drawable.img_chal_ex4),"쓰레기 줍기 인증글 입니다. 야호","삭제");
//        adapter10.addItem("7.0", "쓰레기 줍기 인증합니다!~", "투투", "2022-11-19",
//                ContextCompat.getDrawable(getActivity(),R.drawable.img_chal_ex5),"쓰레기 줍기 인증글 입니다!!","삭제");

        adapter_10 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mylist);
        listview.setAdapter(adapter10);






        //각 버튼 클릭시 이동화면

        // 1) 탈퇴하기
        mBinding.btnChalOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("챌린지를 탈퇴하시겠습니까? \n※ 탈퇴 시 작성하신 글은 자동으로 삭제되지 않고 남겨집니다. ※");
                builder.setCancelable(true);
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle bundle = getArguments();

                        if(bundle != null){
                            chal_name = bundle.getString("chal_name_check");
                        }

                        db.collection("user")
                                .whereEqualTo("gmail", email)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                DocumentReference docRef = db.collection("user").document(document.getId());
                                                docRef.update("my_chal", FieldValue.arrayRemove((String)chal_name));
                                            }
                                        }else{
                                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        db.collection("challenge")
                                .whereEqualTo("chal_name", chal_name)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                DocumentReference docRef = db.collection("chal_name").document(document.getId());
                                                docRef.update("member_email", FieldValue.arrayRemove(email));
                                            }
                                        }else{
                                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });



                        Fragment fragment = new FragDolfin();
                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                        fm.replace(R.id.main_frame ,fragment).commit();
                        Toast.makeText(getContext(), "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


//        mBinding.btnChalOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("챌린지를 탈퇴하시겠습니까? \n※ 탈퇴 시 작성하신 글은 자동으로 삭제되지 않고 남겨집니다. ※");
//                builder.setCancelable(true);
//                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Fragment fragment = new FragDolfin();
//                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
//                        fm.replace(R.id.main_frame ,fragment).commit();
//                        Toast.makeText(getContext(), "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });

        // 2) 글 작성하기

        mBinding.btnChalWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ChalWriteActivity();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame, fragment).commit();

            }
        });

        // 3) 챌린지 달력

        mBinding.btnChalCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragCalender = new ChalCalendar();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame, fragCalender).commit();
            }
        });
        /*
        mBinding.btnChalWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inChalWrite = new Intent(getActivity(), ChalWriteActivity.class);
                startActivity(inChalWrite);
            }
        });*/

        //액션바
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(chal_name);
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){
            chal_name = bundle.getString("chal_name_check");
        }

        intro_view = view.findViewById(R.id.introview);


        db.collection("challenges")
                .whereEqualTo("chal_name", chal_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                DocumentReference docRef = db.collection("challenges").document(document.getId());
                                intro_view.setText((String)document.get("chal_intro"));

                            }
                        }else{
                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        view.findViewById(R.id.btnChalintro).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (view.findViewById(R.id.introview).getVisibility()==View.GONE)
                    view.findViewById(R.id.introview).setVisibility(View.VISIBLE);
                else
                    view.findViewById(R.id.introview).setVisibility(View.GONE);
            }
        });
    }


    //소개 메뉴 팝업
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view,savedInstanceState);
//        view.findViewById(R.id.btnChalintro).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                if (view.findViewById(R.id.introview).getVisibility()==View.GONE)
//                    view.findViewById(R.id.introview).setVisibility(View.VISIBLE);
//                else
//                    view.findViewById(R.id.introview).setVisibility(View.GONE);
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//        getActivity().getSupportFragmentManager().popBackStack();
        Fragment fragment = new FragDolfin();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_frame ,fragment).commit();
    }


}
