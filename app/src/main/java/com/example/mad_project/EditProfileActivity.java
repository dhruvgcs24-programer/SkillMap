package com.example.mad_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etPhone;
    private ShapeableImageView ivProfilePic;
    private TextView tvProfileName;
    private MaterialButton btnSave, btnLogout;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        // Initialize Views
        etFirstName = findViewById(R.id.etEditFirstName);
        etPhone = findViewById(R.id.etEditPhone);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvProfileName = findViewById(R.id.tvProfileName);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);
        ImageView btnBack = findViewById(R.id.btnBack);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Back button functionality
        btnBack.setOnClickListener(v -> finish());

        // Bottom Navigation Logic
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_roadmaps) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_progress) {
                startActivity(new Intent(this, Progress.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });

        // Load current data
        mDatabase.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String firstName = snapshot.child("firstName").getValue(String.class);
                etFirstName.setText(firstName);
                tvProfileName.setText(firstName != null ? firstName : "User Name");
                etPhone.setText(snapshot.child("phone").getValue(String.class));

                String base64String = snapshot.child("profileImageUrl").getValue(String.class);
                if (base64String != null && !base64String.isEmpty()) {
                    try {
                        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ivProfilePic.setImageBitmap(decodedByte);
                    } catch (Exception ignored) {}
                }
            }
        });

        // Image Picker logic
        ActivityResultLauncher<Intent> picker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            encodedImage = encodeImage(bitmap);
                            ivProfilePic.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        findViewById(R.id.btnChangePic).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            picker.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            String name = etFirstName.getText().toString().trim();
            String phone = etEditPhone(); // Helper call or just use etPhone

            Map<String, Object> map = new HashMap<>();
            map.put("firstName", name);
            map.put("phone", etPhone.getText().toString().trim());
            if (!encodedImage.isEmpty()) {
                map.put("profileImageUrl", encodedImage);
            }

            mDatabase.updateChildren(map).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                tvProfileName.setText(name);
            });
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String etEditPhone() {
        return etPhone.getText().toString().trim();
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}