package com.example.mad_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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
    private MaterialButton btnSave, btnLogout;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        etFirstName = findViewById(R.id.etEditFirstName);
        etPhone = findViewById(R.id.etEditPhone);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Load current data
        mDatabase.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                etFirstName.setText(snapshot.child("firstName").getValue(String.class));
                etPhone.setText(snapshot.child("phone").getValue(String.class));

                String base64String = snapshot.child("profileImageUrl").getValue(String.class);
                if (base64String != null && !base64String.isEmpty()) {
                    byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivProfilePic.setImageBitmap(decodedByte);
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
                            encodedImage = encodeImage(bitmap); // Shrink and encode
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
            String phone = etPhone.getText().toString().trim();

            Map<String, Object> map = new HashMap<>();
            map.put("firstName", name);
            map.put("phone", phone);
            if (!encodedImage.isEmpty()) {
                map.put("profileImageUrl", encodedImage);
            }

            mDatabase.updateChildren(map).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            // Clear all activities so user can't "Back" into the app
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String encodeImage(Bitmap bitmap) {
        // Shrinking is required for Base64 to avoid database slowdowns
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}