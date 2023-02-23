package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.BarringInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

public class FragHmCategoryChal extends Fragment implements onBackPressedListener{
    private View view;
    TextView join_chal_Name;
    TextView join_week_Count;
    TextView join_Date;
    TextView join_Intro;
    Button btnchaljoin;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> chal_my_list = new ArrayList<>();
    Bundle bundle = new Bundle();
    String chal_name = "";
    String email="";
    ArrayList<chal_listview.ListViewItem> itemList = new ArrayList<chal_listview.ListViewItem>();
    ArrayList<String> mylist = new ArrayList<>();
    chal_listview adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_hm_category_chal, container, false);
        join_chal_Name = view.findViewById(R.id.join_chal_name);
        join_week_Count = view.findViewById(R.id.join_week_count);
        join_Date = view.findViewById(R.id.join_date);
        join_Intro = view.findViewById(R.id.join_intro);

        ArrayList<chal_listview.ListViewItem> items = new ArrayList<chal_listview.ListViewItem>();
        Button join = (Button) view.findViewById(R.id.btnChalJoin);
        Button move = (Button) view.findViewById(R.id.btnChalMove);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();

        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        email = gsa.getEmail();

        Bundle bundle = getArguments();

        if(bundle != null){
            chal_name = bundle.getString("chal_name_check");

        }
        ////-------------------------나의 챌린지 리스트//
        mylist.clear();
        db.collection("challenges")
                .whereArrayContains("member_email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                mylist.add(document.get("chal_name").toString());
                                System.out.println("=================" + mylist);
                            }
                            if(mylist.contains(chal_name)){
                                gone(join);
                                System.out.println("=======it contains " + chal_name);
                            } else{
                                gone(move);
                                System.out.println("=======it doesnt contain " + chal_name);
                            }
                        }else{
                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        db.collection("challenges").whereEqualTo("chal_name", chal_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                join_chal_Name.setText(document.get("chal_name").toString());
                                join_week_Count.setText("주 "+document.get("count_week").toString()+" 회");
                                join_Date.setText(document.get("start_date").toString()+" ~ "+document.get("end_date").toString());
                                join_Intro.setText(document.get("chal_intro").toString());
                            }
                        }else{
                            Toast.makeText(getActivity(),"불러오기오류",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //메뉴 활성화
        setHasOptionsMenu(true);

        String finalChal_name = chal_name;
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());

                alt_bld.setMessage("가입하시겠습니까?");
                alt_bld.setCancelable(true);
                alt_bld.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alt_bld.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
                        String email = gsa.getEmail();

                        Bundle bundle = getArguments();


                        db.collection("challenges")
                                .whereEqualTo("chal_name", finalChal_name)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot documentt : task.getResult()){
                                                DocumentReference docRef = db.collection("challenges").document(documentt.getId());
                                                System.out.println("=================" + documentt.getData());
                                                docRef.update("member_email", FieldValue.arrayUnion(email));
                                                docRef.update("member_num", (Long)documentt.get("member_num") + 1);

                                            }
                                        }else{
                                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        db.collection("users")
                                .whereEqualTo("gmail", email)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot documentt : task.getResult()){
                                                DocumentReference docRef = db.collection("users").document(documentt.getId());
                                                System.out.println("=================" + documentt.getData());
                                                docRef.update("my_chal", FieldValue.arrayUnion(finalChal_name));

                                            }
                                        }else{
                                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        Toast.makeText(getActivity(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        // (추가) 가입완료 후 챌린지 화면으로 넘어가기
                        Fragment fragment = new FragDfChal();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("chal_name_check", finalChal_name);
                        fragment.setArguments(bundle1);
                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                        fm.replace(R.id.main_frame,fragment).commit();
                    }
                });
                AlertDialog alertDialog = alt_bld.create();
                alertDialog.show();

            }
        });
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragDolfin();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame,fragment).commit();
            }
        });




        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("챌린지 가입 페이지");
        actionBar.setDisplayHomeAsUpEnabled(false);
        return view;
    }

    // 메뉴 활성화
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        return;
    }




    // (추가) 뒤로가기 기능 - FragHmExercise으로 이동
    @Override
    public void onBackPressed() {

        Fragment fragment = new FragHmExercise();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_frame ,fragment).commit();
    }

    public void gone(Button btn_name){
        btn_name.setVisibility((View.GONE));
    }

}