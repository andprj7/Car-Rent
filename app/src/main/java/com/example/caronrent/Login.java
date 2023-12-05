package com.example.caronrent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    Button btn;
    TextView txtact;
    TextInputEditText txtemail, txtpass;
    FirebaseAuth auth;

    //boolean passwordvisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn = findViewById(R.id.btn_login);
        txtact = findViewById(R.id.txt_regis);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Loc.class);
                startActivity(i);
            }
        });
        txtact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });

        txtemail = findViewById(R.id.txt_email_login);
        txtpass = findViewById(R.id.txt_login_pass);
//        txtpass.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                final int Right=2;
//                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
//                    if (motionEvent.getRawX()>=txtpass.getRight()-txtpass.getCompoundDrawables()[Right].getBounds().width()){
//                        int selection=txtpass.getSelectionEnd();
//                        if (passwordvisible){
//                            txtpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
//                            txtpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                            passwordvisible=false;
//                        }else {
//                            txtpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
//                            txtpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                            passwordvisible=true;
//                        }
//                        txtpass.setSelection(selection);
//                        return true;
//                    }
//                }
//                return true;
//            }
//        });
        auth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = txtemail.getText().toString();
                String txtPass = txtpass.getText().toString();

                if (TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(Login.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    txtemail.setError("Email is required");
                    txtemail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(Login.this, "Please Re-Enter Email", Toast.LENGTH_SHORT).show();
                    txtemail.setError("Valid Email is required");
                    txtemail.requestFocus();
                } else if (TextUtils.isEmpty(txtPass)) {
                    Toast.makeText(Login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    txtpass.setError("Password is required");
                    txtpass.requestFocus();
                } else {
                    loginuser(txtEmail, txtPass);
                }
            }
        });
    }

    private void loginuser(String txtEmail, String txtPass) {
        auth.signInWithEmailAndPassword(txtEmail, txtPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Intent intent=new Intent(Login.this, Loc.class);
//                    startActivity(intent);
//                    Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        Intent intent = new Intent(Login.this, Loc.class);
                        startActivity(intent);
                        Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();

                    } else {
                        firebaseUser.sendEmailVerification();
                        auth.signOut();
                        showAlertDialog();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        txtemail.setError("User does not exist or is no longer valid.Please register again");
                        txtemail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        txtemail.setError("Invalid credentials.Kindly check and re-enter");
                        txtemail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification");

        builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, Loc.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "You can login now!", Toast.LENGTH_SHORT).show();
        }
    }
}