package com.example.fbu_voterxv.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.MainActivity;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText profileAge;
    private EditText profileParty;
    private EditText profileStreet;
    private EditText profileCity;
    private EditText profileState;
    private EditText profileZipcode;
    private ImageView profilePic;
    private User user;
    private Button saveButton;
    private File photoFile;
    public String photoFileName = "fbu-voterxv_profile_photo.jpg";

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = Parcels.unwrap(getArguments().getParcelable("user"));

        profileAge = getActivity().findViewById(R.id.profileAge);
        profileParty = getActivity().findViewById(R.id.profileParty);
        profileStreet = getActivity().findViewById(R.id.profileStreet);
        profileCity = getActivity().findViewById(R.id.profileCity);
        profileState = getActivity().findViewById(R.id.profileState);
        profileZipcode = getActivity().findViewById(R.id.profileZipcode);
        saveButton = getActivity().findViewById(R.id.saveButton);
        profilePic = getActivity().findViewById(R.id.profilePic);

        populateFields();

        //update profile when button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileStreet.getText().toString().isEmpty() || profileCity.getText().toString().isEmpty() || profileState.getText().toString().isEmpty() || profileZipcode.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Must fill in address", Toast.LENGTH_SHORT).show();
                }
                else{
                    saveChanges();
                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

    }

    //save changes
    public void saveChanges(){
        ParseUser parseUser = ParseUser.getCurrentUser();

        int age = Integer.parseInt(profileAge.getText().toString());
        String party = profileParty.getText().toString();
        String street = profileStreet.getText().toString();
        String city = profileCity.getText().toString();
        String state = profileState.getText().toString();
        String zipcode = profileZipcode.getText().toString();

        //update parseUser
        parseUser.put("age", age);
        parseUser.put("party", party);
        parseUser.put("street", street);
        parseUser.put("city", city);
        parseUser.put("state", state);
        parseUser.put("zipcode", zipcode);
        try{
            parseUser.put("image", new ParseFile(photoFile));
        }
        catch (NullPointerException e){
            Log.i(TAG, "no new picture taken");
        }
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved profile", Toast.LENGTH_SHORT).show();

                    ((MainActivity) getActivity()).populateUser();
                    populateFields();
                }
            }
        });
    }

    //populate fields
    public void populateFields(){
        profileAge.setText(Integer.toString(user.getAge()));
        profileParty.setText(user.getParty());
        profileStreet.setText(user.getStreet());
        profileCity.setText(user.getCity());
        profileState.setText(user.getState());
        profileZipcode.setText(user.getZipcode());
        ParseFile image = user.getImage();
        if (image != null){
            Glide.with(getContext()).load(image.getUrl()).placeholder(android.R.drawable.ic_menu_camera).into(profilePic);
        }
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.example.fbu_voterxv", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                profilePic.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}