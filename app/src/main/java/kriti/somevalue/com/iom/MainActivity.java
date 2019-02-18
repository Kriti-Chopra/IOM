package kriti.somevalue.com.iom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText edtAadhar,edtOtp;
    private TextInputLayout edtAadharLL,edtOtpLL;
    private Button btnGenerateOtp,btnGenerateNewOtp,btnLogin;
    private LinearLayout generateNew;
    private ProgressBar progressBar;
    private TextView txtServiceProvider;
    public static String AADHAR_NUMBER;

    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private static final String KEY_MOBILE="Mobile";
    private String phoneNumber;
    private String verificationId;
    private static final String InternationalCode = "+91";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        edtAadhar=(TextInputEditText) findViewById(R.id.edtAadhar);
        edtAadharLL=(TextInputLayout) findViewById(R.id.edtAadharLL);
        edtOtp=(TextInputEditText) findViewById(R.id.edtOtp);
        edtOtpLL=(TextInputLayout) findViewById(R.id.edtOtpLL);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnGenerateOtp=(Button) findViewById(R.id.btnGenerateOtp);
        btnGenerateNewOtp=(Button) findViewById(R.id.btnGenerateNewOtp);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        generateNew=(LinearLayout) findViewById(R.id.generateNew);
        txtServiceProvider=(TextView) findViewById(R.id.txtServiceProvider);

        btnGenerateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidityOfAadhar()) {
                    String result=checkUserOnFirebase();
                    if(result!=null) sendOtp(result);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=edtOtp.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){
                    edtOtpLL.setError("Enter Code");
                    edtOtp.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });

        txtServiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToShopkeeper=new Intent(MainActivity.this,Shopkeeper.class);
                startActivity(intentToShopkeeper);
            }
        });

    }

    private void sendOtp(String result){
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        String ref_phone=InternationalCode.concat(result);
        sendVerificationCode(ref_phone);
    }

    private void verifyCode(String code){
        //checking if the entered code matches with what was sent
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
        Toast.makeText(this, credential.getSmsCode(), Toast.LENGTH_SHORT).show();
        signInWithPhoneCredential(credential);
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        Toast.makeText(this, credential.getSmsCode(), Toast.LENGTH_SHORT).show();
        mAuth.signInWithCredential(credential).
                addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(MainActivity.this,Customer.class);
                            //close all previous activities in the stack
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS, this,mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //get code auto
            String code= phoneAuthCredential.getSmsCode();
            if (code!=null){
                edtOtp.setText(code);
                //verifyCode(code);
                //Toast.makeText(MainActivity.this, "WORKING FINE"+phoneAuthCredential.getSmsCode(), Toast.LENGTH_SHORT).show();
                signInWithPhoneCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //the code that is send is "s" this is to be matched with what the user has entered
            super.onCodeSent(s, forceResendingToken);

            btnGenerateOtp.setVisibility(View.GONE);
            edtOtpLL.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            generateNew.setVisibility(View.VISIBLE);

            AADHAR_NUMBER=edtAadhar.getText().toString();
            verificationId=s;
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };


    private boolean checkValidityOfAadhar() {
        if(edtAadhar.getText().toString().isEmpty() || edtAadhar.getText().toString().length()!=12){
            edtAadharLL.setError("Please enter valid Aadhar number");
            return false;
        }
        else{
            return true;
        }

    }

    private String checkUserOnFirebase(){
        documentReference= firestore.collection("CustomerDB").document(edtAadhar.getText().toString());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       // Toast.makeText(MainActivity.this, document.getString(KEY_MOBILE), Toast.LENGTH_SHORT).show();
                        phoneNumber= document.getString(KEY_MOBILE);
                        //Toast.makeText(MainActivity.this, phoneNumber, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "User does not exist.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return phoneNumber;
    }
}
