package com.example.tattooshopapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private RecyclerView galleryRecyclerView;
    private GalleryAdaptor galleryAdaptor;
    private ArrayList<GalleryImage> galleryImages = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference galleryListReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference galleryStorageReference;

    private ChildEventListener galleryChildEventListener;
    private ValueEventListener galleryValueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gallery_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.hasFixedSize();
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        galleryAdaptor = new GalleryAdaptor(galleryImages);
        galleryRecyclerView.setAdapter(galleryAdaptor);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        galleryListReference = firebaseDatabase.getReference().child("galleryList");
        galleryStorageReference = firebaseStorage.getReference().child("tattooGallery");

        galleryChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GalleryImage galleryImage = dataSnapshot.getValue(GalleryImage.class);
                galleryImages.add(galleryImage);
                galleryAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        galleryValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GalleryImage image = dataSnapshot.getValue(GalleryImage.class);
                Log.d("TAG", "New Image Added: " + image.getGalleryImageDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Failed to read image", databaseError.toException());
            }
        };

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth != null) {
            galleryListReference.addChildEventListener(galleryChildEventListener);
            galleryListReference.addValueEventListener(galleryValueEventListener);
        } else {
            galleryListReference.removeEventListener(galleryChildEventListener);
            galleryListReference.removeEventListener(galleryValueEventListener);
        }
    }
}
