package com.example.caronrent;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignUp extends AppCompatActivity {
    static final String TAG = "SignUp";
    Spinner spinner;
    TextInputEditText txtname, txtmobile, txtemail, txtpass, txtconpass, txtdl;
    RadioGroup radiogender;
    RadioButton male, female, other;
    Button btnregister;
    String[] city = {"Surat", "Vadodara", "Ahmedabad", "Rajkot", "Bhavnagar"};

    private ImageButton uploadUserDl;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PIC_IMAGE_REQUEST = 1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //mine

        Toast.makeText(this, "You can register now", Toast.LENGTH_LONG).show();
        txtname = findViewById(R.id.txtname);
        txtmobile = findViewById(R.id.txtmobile);
        txtemail = findViewById(R.id.txtemail);
        txtpass = findViewById(R.id.txtpass);
        txtconpass = findViewById(R.id.txtconpass);
        txtdl = findViewById(R.id.txtDl);
        radiogender = findViewById(R.id.radioGroup);
        radiogender.clearCheck();
        btnregister = findViewById(R.id.btnregister);
        spinner = findViewById(R.id.spinner);


        uploadUserDl = findViewById(R.id.uploadUserDl);

        authProfile = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        firebaseUser = authProfile.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("DrivingLicence");

        Uri uri = firebaseUser.getPhotoUrl();

        Picasso.with(SignUp.this).load(uri).into(uploadUserDl);



        uploadUserDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            progressBar.setVisibility(View.VISIBLE);
            UploadPic();



                int selectedGenderid = radiogender.getCheckedRadioButtonId();
                male = findViewById(selectedGenderid);

                String txtName = txtname.getText().toString();
                String txtMobile = txtmobile.getText().toString();
                String txtEmail = txtemail.getText().toString();
                String txtPass = txtpass.getText().toString();
                String txtConPass = txtconpass.getText().toString();
                String txtDll = txtdl.getText().toString();
                String City = city[spinner.getSelectedItemPosition()];

                String txtGender;


                if (TextUtils.isEmpty(txtName)) {
                    Toast.makeText(SignUp.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    txtname.setError("Name is required");
                    txtname.requestFocus();
                } else if (TextUtils.isEmpty(txtMobile)) {
                    Toast.makeText(SignUp.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    txtmobile.setError("Mobile Number is required");
                    txtmobile.requestFocus();
                } else if (txtMobile.length() != 10) {
                    Toast.makeText(SignUp.this, "Please Re-Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    txtmobile.setError("Mobile no. should be 10 digits");
                    txtmobile.requestFocus();
                } else if (TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(SignUp.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    txtemail.setError("Email is required");
                    txtemail.requestFocus();
                }
                //else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
//                    Toast.makeText(SignUp.this, "Please Re-Enter Email", Toast.LENGTH_SHORT).show();
//                    txtemail.setError("Valid Email is required");
//                    txtemail.requestFocus();}
                else if (TextUtils.isEmpty(txtPass)) {
                    Toast.makeText(SignUp.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    txtpass.setError("Password is required");
                    txtpass.requestFocus();
                } else if (txtPass.length() < 8) {
                    Toast.makeText(SignUp.this, "Password should be at least 8 digits", Toast.LENGTH_SHORT).show();
                    txtpass.setError("Password should be 8 digits");
                    txtpass.requestFocus();
                } else if (TextUtils.isEmpty(txtConPass)) {
                    Toast.makeText(SignUp.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    txtconpass.setError("Password is required");
                    txtconpass.requestFocus();
                } else if (!txtPass.equals(txtConPass)) {
                    Toast.makeText(SignUp.this, "Please enter right password", Toast.LENGTH_SHORT).show();
                    txtpass.setError("Same password required");
                    txtpass.requestFocus();
                    txtpass.clearComposingText();
                    txtconpass.clearComposingText();
                } else if (radiogender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SignUp.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    male.setError("Gender is required");
                    male.requestFocus();
                } else if (TextUtils.isEmpty(txtDll)) {
                    Toast.makeText(SignUp.this, "Please Enter Driving license no.", Toast.LENGTH_SHORT).show();
                    txtconpass.setError("Driving license no. is required");
                    txtconpass.requestFocus();
                } else {
                    txtGender = male.getText().toString();
                    registeruser(txtName, txtMobile, txtEmail, txtPass, txtDll,City,txtGender);
                }
            }
        });




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_spinner_item, city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String value =parent.getItemAtPosition(position).toString();
//                Toast.makeText(SignUp.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void registeruser(String txtName, String txtMobile, String txtEmail, String txtPass, String txtDll,String City,String txtGender) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(uriImage != null) {
            auth.createUserWithEmailAndPassword(txtEmail, txtPass).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        //Toast.makeText(SignUp.this, "User created successfully", Toast.LENGTH_SHORT).show();


                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtName, txtMobile, txtEmail, txtDll, txtPass,City,txtGender);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
//                        reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        reference.child(txtMobile.toString()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    firebaseUser.sendEmailVerification();
                                    Toast.makeText(SignUp.this, "User created successfully.Please verify your email", Toast.LENGTH_SHORT).show();
//                                Intent intent=new Intent(SignUp.this, MainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();

                                } else {
                                    Toast.makeText(SignUp.this, "User registration failed.Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




//                    FirebaseUser firebaseUser = auth.getCurrentUser();
//                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtName, txtMobile, txtEmail, txtDll, txtPass,City,txtGender);
//                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
//                    reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//
//                                    firebaseUser.sendEmailVerification();
//                                    Toast.makeText(SignUp.this, "User created successfully.Please verify your email", Toast.LENGTH_SHORT).show();
////                                Intent intent=new Intent(SignUp.this, MainActivity.class);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(intent);
////                                finish();
//
//                            } else {
//                                Toast.makeText(SignUp.this, "User registration failed.Please try again", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            txtpass.setError("Your password is to weak.Kindly use a mix of alphabets,numbers and special characters");
                            txtpass.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            txtemail.setError("Your email is invalid or already in use.Kindly re-enter.");
                            txtemail.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e) {
                            txtemail.setError("User is already registered with email.Use another email");
                            txtemail.requestFocus();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else {
            Toast.makeText(SignUp.this, "File select Karo", Toast.LENGTH_SHORT).show();
        }

    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PIC_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PIC_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriImage = data.getData();
            uploadUserDl.setImageURI(uriImage);
        }

    }

    private void UploadPic(){
        if(uriImage != null)
        {
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid() + "." + getFileExtention(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authProfile.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
//                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Uploaded", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
