package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnGoogle, btnGithub;

    private TextView tvSignUpPrompt, tvForgotPassword;

    private GoogleSignInClient googleSignInClient;

    private ActivityResultLauncher<Intent> googleLauncher;

    private final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnGithub = findViewById(R.id.btnGithub);

        tvSignUpPrompt = findViewById(R.id.tvSignUpPrompt);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // ---------------- AUTO LOGIN ----------------
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        // ---------------- EMAIL LOGIN ----------------
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Enter email");
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Enter password");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this,
                                    "Login Successful", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();

                        } else {

                            Toast.makeText(LoginActivity.this,
                                    "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                    });

        });

        // ---------------- FORGOT PASSWORD ----------------
        tvForgotPassword.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Enter your email first");
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(LoginActivity.this,
                                "Password reset email sent",
                                Toast.LENGTH_LONG).show();

                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(LoginActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    });

        });

        // ---------------- SIGN UP PAGE ----------------
        tvSignUpPrompt.setOnClickListener(v -> {

            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

        });

        // ---------------- GOOGLE CONFIG ----------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK) {

                        Task<GoogleSignInAccount> task =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        try {

                            GoogleSignInAccount account =
                                    task.getResult(ApiException.class);

                            firebaseAuthWithGoogle(account.getIdToken());

                        } catch (ApiException e) {

                            Log.e(TAG, "Google sign in failed", e);
                        }
                    }
                }
        );

        // GOOGLE BUTTON
        btnGoogle.setOnClickListener(v -> {

            googleSignInClient.signOut(); // forces account chooser

            Intent signInIntent = googleSignInClient.getSignInIntent();

            googleLauncher.launch(signInIntent);

        });

        // ---------------- GITHUB LOGIN ----------------
        btnGithub.setOnClickListener(v -> {

            OAuthProvider.Builder provider =
                    OAuthProvider.newBuilder("github.com");

            mAuth.startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(authResult -> {

                        Toast.makeText(LoginActivity.this,
                                "GitHub Login Success", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(LoginActivity.this,
                                "GitHub Login Failed", Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "GitHub Error", e);

                    });

        });

    }

    // GOOGLE FIREBASE AUTH
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(this,
                                "Google Login Success",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, HomeActivity.class));
                        finish();

                    } else {

                        Toast.makeText(this,
                                "Authentication Failed",
                                Toast.LENGTH_SHORT).show();
                    }

                });

    }
}