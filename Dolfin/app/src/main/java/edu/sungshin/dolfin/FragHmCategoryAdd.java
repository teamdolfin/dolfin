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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
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
        //?????? ?????????
        setHasOptionsMenu(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses??? ?????????
                .build();

        // ????????? ?????? GoogleSignInOptions??? ????????? GoogleSignInClient ????????? ??????
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // ????????? ????????? ?????? ????????? ????????????.
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


        //?????????
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("????????? ?????????");
        actionBar.setDisplayHomeAsUpEnabled(false);

        //
        // ?????? ?????? ?????? ??????
//        Date currentDate; // ???????????? Date
//        //String oTime = ""; // ????????????
//        String compareVal = "N";
//        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yy??? MM??? dd???", Locale.KOREA );
//        Date currentTime = new Date();
//        String oTime = mSimpleDateFormat.format ( currentTime ); //???????????? (String)
        //currentDate =  mSimpleDateFormat.format( oTime );
        //String end_dates = db.collection("challenges").
        //DocumentReference d = db.collection("challenges").get().getResult();
//        Task<DocumentSnapshot> task = null;
//        assert task != null;
//        DocumentSnapshot dd = task.getResult();
//        String end_dated = dd.getData().get("end_date").toString();
        //String end_dates = db.collection("challenges").get().toString();

        // ??????) ?????? ????????? ?????????(end_date ???????????? ??????)
//        db.collection("challenges").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        DocumentReference docRef = db.collection("challenges").document(document.getId());
//                        String a = document.get("end_date").toString();
//                        int compare = oTime.compareTo( a ); // ????????????
//                        if (compare>0){
//                            //Log.d(TAG, "&&&&&&&&&&&&????????? ?????????&&&&&&&&&&&&&", task.getException());
//                            docRef.update("finished", true);
//                            Log.d(TAG, "%%%%%%%%%%%%%%%%"+a, task.getException());
//                        }
//                        //intro_view.setText((String)document.get("chal_intro"));
//                    }
//                }else{
//                    Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
//                    //Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//        db.collection("challenges")
//                .whereEqualTo("end_date",end_date)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot document : task.getResult()){
//                                int compare = oTime.compareTo( end_date); // ????????????
//                                if (compare>0){
//                                    DocumentReference docRef = db.collection("challenges").document(document.getId());
//                                    docRef.update("finished", true);
//                                    Log.d(TAG, "&&&&&&&&&&&&????????? ?????????&&&&&&&&&&&&&", task.getException());
//                                }
//
//                                //intro_view.setText((String)document.get("chal_intro"));
//
//                            }
//                        }else{
//                            Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
//                            //Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
        //

        // ???????????? ?????? ?????????
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] versionArray = new String[]{"??????", "??????", "??????", "??????", "??????", "??????"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("????????? ??????????????? ??????????????????.");
                dlg.setIcon(R.drawable.img_menu_dolfin);
                dlg.setSingleChoiceItems(versionArray, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                category.setText(versionArray[which]);
                            }
                        });
                dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        });

        // (??????) ?????? ?????? ?????????
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] versionArray = new String[]{"??? 1???", "??? 2???", "??? 3???", "??? 4???", "??? 5???", "??? 6???","??? 7???"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("???????????? ????????? ????????? ??????????????????.");
                dlg.setIcon(R.drawable.img_menu_dolfin);
                dlg.setSingleChoiceItems(versionArray, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cnt.setText(versionArray[which]);
                            }
                        });
                dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        });

        // (??????) ?????? ?????? ?????????
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constrainBuilder =  new CalendarConstraints.Builder();
        constrainBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constrainBuilder.build());



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitleText("Date Picker");

                //?????? ?????? ??????
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");

                //?????? ??????
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy??? MM??? dd???");
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


        // ????????? ?????? ?????????
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
                    //Toast.makeText(getApplicationContext(),"?????? ??????????????????",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"?????? ??????????????????",Toast.LENGTH_SHORT).show();
                }
                else {
                    //????????? ??????
                    int numI = Integer.parseInt(numS);
                    char cntN = cntS.charAt(2);
                    int cntI = Character.getNumericValue(cntN);

                    if (numS.equals("0")) {
                        Toast.makeText(getActivity(), "????????? ????????? 1??? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                        dlg.setTitle("????????? ?????? ??????????????????????");
                        dlg.setIcon(R.drawable.img_menu_dolfin);
                        dlg.setMessage("???????????? : " + categoryS + "\n?????? : " + nameS
                                + "\n?????? : " + cntS + "\n?????? : " + termS
                                + "\n????????? : " + introS + "\n?????? ??? : " + numS);
                        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // (??????)
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

//                                // ?????? ?????? ?????? ??????
//                                Date currentDate; // ???????????? Date
//
//                                //String oTime = ""; // ????????????
//
//                                String compareVal = "N";
//                                SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yy??? MM??? dd???", Locale.KOREA );
//
//                                Date currentTime = new Date();
//
//                                String oTime = mSimpleDateFormat.format ( currentTime ); //???????????? (String)
//
//                                //currentDate =  mSimpleDateFormat.format( oTime );
//
//
//                                int compare = oTime.compareTo( end_dateS ); // ????????????
//
//                                // ??????) ?????? ????????? ?????????(end_date ???????????? ??????)
//                                db.collection("challenges")
//                                        .whereEqualTo("end_date",end_dateS)
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                if(task.isSuccessful()){
//                                                    for(QueryDocumentSnapshot document : task.getResult()){
//                                                        if (compare>0){
//                                                            DocumentReference docRef = db.collection("challenges").document(document.getId());
//                                                            docRef.update("finished", true);
//                                                            Log.d(TAG, "&&&&&&&&&&&&????????? ?????????&&&&&&&&&&&&&", task.getException());
//                                                        }
//
//                                                        //intro_view.setText((String)document.get("chal_intro"));
//
//                                                    }
//                                                }else{
//                                                    Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
//                                                    //Log.d(TAG, "Error getting documents: ", task.getException());
//                                                }
//                                            }
//                                        });
                                //

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
                                                    Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dlg.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
    // (??????) ?????? ?????? ??????
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("??? ???????????? ?????????????????????????");
        builder.setCancelable(true);
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
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


