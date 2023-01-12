package edu.sungshin.dolfin;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
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
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FragHmCategoryAdd extends Fragment implements onBackPressedListener{

    int y=0, m=0, d=0, h=0, mi=0;

    private View view;

    EditText name,intro,num;
    TextView category,term,cnt;
    Button button1,button2,button3,button4;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String start_date, end_date;
    GoogleSignInClient mGoogleSignInClient;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_hm_category_add,container,false);
        //메뉴 활성화
        setHasOptionsMenu(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
        String email = gsa.getEmail();


        category = (TextView) view.findViewById(R.id.category);
        name = (EditText) view.findViewById(R.id.name);
        cnt = (TextView) view.findViewById(R.id.cnt);
        term = (TextView) view.findViewById(R.id.term);
        intro = (EditText) view.findViewById(R.id.intro);
        num = (EditText) view.findViewById(R.id.num);
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
        button4 = (Button) view.findViewById(R.id.button4);


        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("챌린지 만들기");
        actionBar.setDisplayHomeAsUpEnabled(false);

        // 날짜 관련 변수 추가
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yy년 MM월 dd일", Locale.KOREA );
        Date currentTime = new Date();
        String oTime = mSimpleDateFormat.format ( currentTime ); //현재시간 (String)


        // 추가) 완료 챌린지 카운트(end_date 기준으로 구분)
        db.collection("challenges").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        DocumentReference docRef = db.collection("challenges").document(document.getId());
                        String a = document.get("end_date").toString();
                        int compare = oTime.compareTo( a ); // 날짜비교
                        if (compare>0){
                            //Log.d(TAG, "&&&&&&&&&&&&완료된 챌린지&&&&&&&&&&&&&", task.getException());
                            docRef.update("finished", true);
                            Log.d(TAG, "%%%%%%%%%%%%%%%%"+a, task.getException());
                        }

                    }
                }else{
                    Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        // 카테고리 버튼 이벤트
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] versionArray = new String[]{"운동", "건강", "독서", "금융", "취미", "공부"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("챌린지 카테고리를 선택해주세요.");
                dlg.setIcon(R.drawable.img_menu_dolfin);
                dlg.setSingleChoiceItems(versionArray, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                category.setText(versionArray[which]);
                            }
                        });
                dlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        });

        // (추가) 횟수 버튼 이벤트
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] versionArray = new String[]{"주 1회", "주 2회", "주 3회", "주 4회", "주 5회", "주 6회","주 7회"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("챌린지를 진행할 횟수를 선택해주세요.");
                dlg.setIcon(R.drawable.img_menu_dolfin);
                dlg.setSingleChoiceItems(versionArray, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cnt.setText(versionArray[which]);
                            }
                        });
                dlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        });

        // (추가) 달력 버튼 이벤트
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constrainBuilder =  new CalendarConstraints.Builder();
        constrainBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constrainBuilder.build());



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitleText("Date Picker");

                //미리 날짜 선택
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");

                //확인 버튼
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy년 MM월 dd일");
                        Date date1 = new Date();
                        Date date2 = new Date();
                        date1.setTime(selection.first);
                        date2.setTime(selection.second);
                        start_date=simpleDateFormat.format(date1);
                        end_date=simpleDateFormat.format(date2);
                        term.setTextSize(12);
                        term.setText(start_date+"~"+end_date);
                    }
                });
            }
        });


        // 만들기 버튼 이벤트
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryS = category.getText().toString();
                String nameS = name.getText().toString();
                String cntS = cnt.getText().toString();
                String termS = term.getText().toString();
                String start_dateS=start_date;
                String end_dateS=end_date;
                String introS = intro.getText().toString();
                String numS = num.getText().toString();
                int categoryI = categoryS.length();
                int nameI = nameS.length();
                int cntL = cntS.length();
                int termI = termS.length();
                int introI = introS.length();
                int numL = numS.length();
                ArrayList<String> member_list = new ArrayList<>();


                if((categoryI==0)||(nameI==0)||(cntL==0)||(termI==0)||(introI==0)||(numL==0)){
                    //Toast.makeText(getApplicationContext(),"모두 입력해주세요",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"모두 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    //데베용 선언
                    int numI = Integer.parseInt(numS);
                    char cntN = cntS.charAt(2);
                    int cntI = Character.getNumericValue(cntN);

                    if (numS.equals("0")) {
                        Toast.makeText(getActivity(), "챌린지 인원은 1명 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                        dlg.setTitle("챌린지 방을 만드시겠습니까?");
                        dlg.setIcon(R.drawable.img_menu_dolfin);
                        dlg.setMessage("카테고리 : " + categoryS + "\n이름 : " + nameS
                                + "\n횟수 : " + cntS + "\n기간 : " + termS
                                + "\n소개글 : " + introS + "\n인원 수 : " + numS);
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // (추가)
                                member_list.add(gsa.getEmail());
                                Map<String, Object> challenges = new HashMap<>();
                                challenges.put("category", categoryS);
                                challenges.put("chal_name", nameS);
                                challenges.put("count_week", cntI);
                                challenges.put("start_date", start_dateS);
                                challenges.put("end_date", end_dateS);
                                challenges.put("chal_intro", introS);
                                challenges.put("member_max",numI);
                                challenges.put("member_num", 1);
                                challenges.put("finished", false);
                                challenges.put("member_email", member_list);

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
                                                        docRef.update("my_chal", FieldValue.arrayUnion(nameS));

                                                    }
                                                }else{
                                                    Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                                                    //Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                                db.collection("challenges").add(challenges)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "Document Snapshot successfully written"+ documentReference.getId());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                                Toast.makeText(getActivity(), "방만들기 완료", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                }
            }
        });

        return view;
    }
    // (추가) 뒤로 가기 기능
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("방 만들기를 종료하시겠습니까?");
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
                Fragment fragment = new FragHmExercise();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
                //getActivity().finish();
                //finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




}


