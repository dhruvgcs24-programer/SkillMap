package com.example.mad_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Utility methods for image handling across the app.
 *
 * WHY THIS EXISTS:
 * Both HomeActivity and EditProfileActivity contained the same try/catch block
 * for decoding a Base64 string into a Bitmap. This class eliminates that duplication.
 *
 * HOW TO USE:
 *   ImageUtils.loadBase64Image(base64String, myImageView);
 *   String encoded = ImageUtils.encodeToBase64(bitmap);
 *
 * HOW TO EXTEND:
 *   Add new static image helpers here (e.g., Glide loading, circular crop, etc.)
 *   so all image logic stays in one place.
 */
public final class ImageUtils {

    private ImageUtils() {} // Utility class — no instantiation

    /**
     * Decodes a Base64-encoded image string and loads it into an ImageView.
     * Silently does nothing if the string is null/empty or decoding fails.
     *
     * @param base64String Base64-encoded JPEG/PNG image string (from Firebase DB)
     * @param imageView    Target ImageView to load the image into
     */
    public static void loadBase64Image(String base64String, ImageView imageView) {
        if (base64String == null || base64String.isEmpty()) return;
        try {
            byte[] decoded = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                imageView.clearColorFilter();
            }
        } catch (Exception ignored) {}
    }

    /**
     * Compresses and encodes a Bitmap to a Base64 string suitable for
     * storing in Firebase Realtime Database.
     *
     * The image is scaled down to 150px wide and compressed at 50% quality
     * to keep the stored size small.
     *
     * @param bitmap Source bitmap (e.g., picked from gallery)
     * @return Base64-encoded JPEG string
     */
    public static String encodeToBase64(Bitmap bitmap) {
        int previewWidth  = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
