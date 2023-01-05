/*
package edu.sungshin.dolfin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragDialogChalJoin extends DialogFragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> chal_my_list_ = new ArrayList<>();
    String chal_name = "";
    GoogleSignInClient mGoogleSignInClient;
    //private View view;
    private Fragment fragment;
//    View toastView;
//    TextView toastText;

    // Your own onCreate_Dialog_View method
    public View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_chal_join, container); // inflate here
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
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
                                chal_my_list_ = ((ArrayList<String>)documentt.get("my_chal"));
                                System.out.println("******************" + chal_my_list_);
                            }
                        }else{
                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //이미 있는 챌린지 클릭하면
        db.collection("challenges")
                .whereEqualTo("chal_name", chal_my_list_)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentt : task.getResult()){
                                Toast.makeText(getActivity(),"이미 가입한 챌린지 입니다",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


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



    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // setup dialog: buttons, title etc
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("가입하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        // (추가) 가입완료 후 챌린지 화면으로 넘어가기
                        Fragment fragment = new FragDfChal();
                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                        fm.replace(R.id.main_frame ,fragment).commit();
                    }
                })
                .setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();


    }


}*/
package edu.sungshin.dolfin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragDialogChalJoin extends DialogFragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> chal_my_list_ = new ArrayList<>();
    String chal_name = "";
    GoogleSignInClient mGoogleSignInClient;
    private Fragment fragment;

    public View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_chal_join, container); // inflate here
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* //mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(getActivity());
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
                                chal_my_list_ = ((ArrayList<String>)documentt.get("my_chal"));
                                System.out.println("******************" + chal_my_list_);
                            }
                        }else{
                            Toast.makeText(getActivity(),"오류",Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


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

*/

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // setup dialog: buttons, title etc
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("가입하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        // (추가) 가입완료 후 챌린지 화면으로 넘어가기
                        Fragment fragment = new FragDfChal();
                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                        fm.replace(R.id.main_frame ,fragment).commit();
                    }
                })
                .setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();


    }


}
