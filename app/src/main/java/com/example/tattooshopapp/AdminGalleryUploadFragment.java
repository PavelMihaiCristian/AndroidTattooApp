package com.example.tattooshopapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class AdminGalleryUploadFragment extends Fragment {

    private Button chooseImageButton;
    private Button uploadImageButton;
    private EditText imageDescriptionEditText;
    private ImageView imageToUploadView;

    private ProgressDialog progressDialog;

    private Uri filePath;

    private int Image_Request_Code = 3;
    private int DEFAULT_MSG_LENGTH_LIMIT = 100;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference galleryListReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference galleryStorageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_gallery_upload_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        galleryListReference = firebaseDatabase.getReference().child("galleryList");
        galleryStorageReference = firebaseStorage.getReference().child("tattooGallery");

        chooseImageButton = view.findViewById(R.id.getImageFromGallery);
        uploadImageButton = view.findViewById(R.id.uploadImage);
        imageDescriptionEditText = view.findViewById(R.id.imageDescription);
        imageToUploadView = view.findViewById(R.id.imageToUpload);

        progressDialog = new ProgressDialog(getActivity());

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImageFileToFirebaseStorage();
            }
        });

        imageDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 5) {
                    uploadImageButton.setEnabled(true);
                } else {
                    uploadImageButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        imageDescriptionEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Uri uriPhoto = data.getData();
            Glide.with(getActivity())
                    .load(uriPhoto)
                    .into(imageToUploadView);
            Toast.makeText(getActivity(), "Write description for the image", Toast.LENGTH_LONG);
        }
    }

    private void UploadImageFileToFirebaseStorage() {
        if (filePath != null) {
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            StorageReference photeRef = galleryStorageReference.child(filePath.getLastPathSegment());
            photeRef.putFile(filePath).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDescription = imageDescriptionEditText.getText().toString().trim();
                            GalleryImage image = new GalleryImage(imageDescription, uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            galleryListReference.push().setValue(image);
                            imageDescriptionEditText.setText("");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setTitle("Image is Uploading...");
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }
}
