package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Arrays;

public class FragHmCategoryChal extends Fragment implements onBackPressedListener{
    private View view;
    TextView join_chal_Name;
    TextView join_week_Count;
    TextView join_Date;
    TextView join_Intro;
    ArrayList<String> chal_my_list = new ArrayList<>();
    Bundle bundle = new Bundle();
    String chal_name = "";
    GoogleSignInClient mGoogleSignInClient;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_hm_category_chal, container, false);
        join_chal_Name = view.findViewById(R.id.join_chal_name);
        join_week_Count = view.findViewById(R.id.join_week_count);
        join_Date = view.findViewById(R.id.join_date);
        join_Intro = view.findViewById(R.id.join_intro);

        ArrayList<chal_listview.ListViewItem> items = new ArrayList<chal_listview.ListViewItem>();

        Bundle bundle = getArguments();
        String chal_name = "";
        if(bundle != null){
            chal_name = bundle.getString("chal_name_check");

        }

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

        //각 버튼 클릭시 이동화면
        // 1) 가입버튼 클릭
        Button join = (Button) view.findViewById(R.id.btnChalJoin);
        join.setOnClickListener(this::onClick);
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
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.btnChalJoin:
                GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
                String email = gsa.getEmail();

                Bundle bundle = getArguments();


                if(bundle != null){
                    chal_name = bundle.getString("chal_name_check");
                }

                db.collection("challenges")
                        .whereEqualTo("chal_name", chal_name)
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
                                        docRef.update("my_chal", FieldValue.arrayUnion(chal_name));

                                    }
                                }else{
                                    Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                //데이터를 다이얼로그로 보냄
                Bundle args = new Bundle();
                args.putString("key", "value");
                //-----------------------------------//
                FragDialogChalJoin dialog = new FragDialogChalJoin();
                dialog.setArguments(args);
                dialog.show(getActivity().getSupportFragmentManager(),"tag");
                break;
               /* GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
                String email = gsa.getEmail();

                Bundle bundle = getArguments();

                //나의 챌린지 목록 가져오는 코드
                db.collection("users")
                        .whereEqualTo("gmail", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentt : task.getResult()){
                                        DocumentReference docRef = db.collection("users").document(documentt.getId());
                                        System.out.println("=**********************" + documentt.get("my_chal"));
                                        chal_my_list = ((ArrayList<String>)documentt.get("my_chal"));
                                        System.out.println("******************" + chal_my_list);
                                    }
                                }else{
                                    Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                //이미 가입한 챌린지 토스트......
                db.collection("challenges")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentt : task.getResult()){
                                        for(String i :chal_my_list)
                                            System.out.println("_----------------------------"+i);
                                        System.out.println("***********************"+documentt.get("chal_name"));


                                        if(Arrays.asList(chal_my_list).contains(documentt.get("chal_name"))) {
                                            Toast.makeText(getActivity(), "이미 가입한 챌린지 입니다.", Toast.LENGTH_SHORT).show();
                                            System.out.println("***완완완완완오나오ㅗ나오나ㅗㅇ농너ㅘ놔온**************");
                                        }
                                        else{
                                            System.out.println("***ㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜ");
                                            if(bundle != null){
                                                chal_name = bundle.getString("chal_name_check");
                                            }

                                            db.collection("challenges")
                                                    .whereEqualTo("chal_name", chal_name)
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
                                                                    chal_my_list = ((ArrayList<String>)documentt.get("my_chal"));
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
                                                                    docRef.update("my_chal", FieldValue.arrayUnion(chal_name));

                                                                }
                                                            }else{
                                                                Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                                                //Log.d(TAG, "Error getting documents: ", task.getException());
                                                            }
                                                        }
                                                    });

                                            //데이터를 다이얼로그로 보냄
                                            Bundle args = new Bundle();
                                            args.putString("key", "value");
                                            //-----------------------------------//
                                            FragDialogChalJoin dialog = new FragDialogChalJoin();
                                            dialog.setArguments(args);
                                            dialog.show(getActivity().getSupportFragmentManager(),"tag");
                                            break;
                                        }
                                    }
                                }else{
                                    Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

*/
        }
    }

    // (추가) 뒤로가기 기능 - FragHmExercise으로 이동
    @Override
    public void onBackPressed() {

        Fragment fragment = new FragHmExercise();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_frame ,fragment).commit();
    }

}