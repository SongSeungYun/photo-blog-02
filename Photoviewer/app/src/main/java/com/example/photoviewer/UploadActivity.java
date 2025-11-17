package com.example.photoviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_IMAGE = 101;

    EditText inputTitle, inputText; // author 필드는 다음 단계에서 필요하면 추가
    ImageView previewImage;
    Button btnSelect, btnPost;

    Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        inputTitle = findViewById(R.id.inputTitle);
        inputText  = findViewById(R.id.inputText);
        previewImage = findViewById(R.id.previewImage);
        btnSelect = findViewById(R.id.btnSelectImage);
        btnPost   = findViewById(R.id.btnPost);

        // 복원 (회전 등)
        if (savedInstanceState != null) {
            String savedUri = savedInstanceState.getString("selectedImageUri");
            if (savedUri != null) {
                selectedImageUri = Uri.parse(savedUri);
                previewImage.setImageURI(selectedImageUri);
            }
        }

        // 기본 미리보기 (fallback)
        if (selectedImageUri == null) {
            try {
                previewImage.setImageBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.test_upload)
                );
            } catch (Exception ignore) {
                // drawable 없으면 그냥 회색 배경 유지
            }
        }

        // ✅ 이미지 선택
        btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_SELECT_IMAGE);
        });

        // 업로드 버튼은 다음 단계에서 구현 (서버 전송)
        btnPost.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(this, "이미지를 선택해 주세요 (없으면 기본 이미지로 업로드됩니다)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "이미지 선택 완료!", Toast.LENGTH_SHORT).show();
            }
            // 다음 단계에서: 서버 업로드 로직 붙일 예정
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedImageUri != null) {
            outState.putString("selectedImageUri", selectedImageUri.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                previewImage.setImageURI(selectedImageUri);
                Log.d("UPLOAD", "Selected URI: " + selectedImageUri);
            } else {
                // 선택 취소 또는 없음 → fallback 유지
                Toast.makeText(this, "이미지 선택이 취소되었습니다. 기본 이미지를 사용합니다.", Toast.LENGTH_SHORT).show();
                try {
                    previewImage.setImageBitmap(
                            BitmapFactory.decodeResource(getResources(), R.drawable.test_upload)
                    );
                } catch (Exception ignore) {}
            }
        }
    }
}
