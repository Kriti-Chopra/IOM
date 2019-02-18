package kriti.somevalue.com.iom;


import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Profile extends Fragment {


    private String AADHAR_RECEIVED=MainActivity.AADHAR_NUMBER;
    private DatabaseReference databaseReference;
    private DocumentReference documentReference;
    TextView txtName,txtAddress,txtDOB,txtBiometric,txtEmail,txtFather,txtPhone;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        txtName=view.findViewById(R.id.txtName);
        txtAddress=view.findViewById(R.id.txtAddress);
        txtDOB=view.findViewById(R.id.txtDOB);
        txtBiometric=view.findViewById(R.id.txtBiometric);
        txtEmail=view.findViewById(R.id.txtEmail);
        txtFather=view.findViewById(R.id.txtFather);
        txtPhone=view.findViewById(R.id.txtPhoneNumber);


//        documentReference= FirebaseFirestore.getInstance().collection("CustomerDB").document(AADHAR_RECEIVED);
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        txtName.setText(document.getString("Name"));
//                        txtAddress.setText(document.getString("Address"));
//                        txtBiometric.setText(document.getString("Biometric"));
//                        txtDOB.setText(document.getString("D.O.B."));
//                        txtEmail.setText(document.getString("Email ID"));
//                        txtFather.setText(document.getString("Father's Name"));
//                        txtPhone.setText(document.getString("Mobile"));
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "User does not exist.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            try {
                documentReference = FirebaseFirestore.getInstance().collection("CustomerDB").document(AADHAR_RECEIVED);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                txtName.setText(document.getString("Name"));
                                txtAddress.setText(document.getString("Address"));
                                txtBiometric.setText(document.getString("Biometric"));
                                txtDOB.setText(document.getString("DOB"));
                                txtEmail.setText(document.getString("Email ID"));
                                txtFather.setText(document.getString("Father's Name"));
                                txtPhone.setText(document.getString("Mobile"));
                            } else {
                                Toast.makeText(getActivity(), "User does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("ERROR TAG", e.toString());
            }
        }
    }
}
