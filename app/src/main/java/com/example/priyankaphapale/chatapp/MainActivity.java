package com.example.priyankaphapale.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.example.priyankaphapale.chatapp.model.ChatMessageItem;
import com.example.priyankaphapale.chatapp.model.ChatMsgAdapter;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    EditText mEditText;
    Button sendBtn;
    ChatMsgAdapter mAdapter;
    String username="JohnSmith";
    ChildEventListener mChildEventListener;
    DatabaseReference myRef;

    final int RC_SIGN_IN=12;
    final int RC_PHOTO_PICKER=13;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        mFirebaseAuth=FirebaseAuth.getInstance();

        mFirebaseStorage=FirebaseStorage.getInstance();



        List<ChatMessageItem> msgs_Mdl=new ArrayList<>();
        mAdapter=new ChatMsgAdapter(this,R.layout.chat_msg_item,msgs_Mdl);

        mListView=(ListView)findViewById(R.id.chat_list);
        sendBtn=(Button) findViewById(R.id.send_msg);
        mEditText=(EditText) findViewById(R.id.msg_box);
        mListView.setAdapter(mAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText.getText()!=null){

                    myRef.push().setValue(new ChatMessageItem(mEditText.getText().toString(),
                            username, getCurrentTime(),null));


                    mEditText.setText("");
                }
            }
        });


        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessageItem messageItem=dataSnapshot.getValue(ChatMessageItem.class);
                mAdapter.add(messageItem);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        };


        myRef.addChildEventListener(mChildEventListener);


        mStorageReference=mFirebaseStorage.getReference().child("images");


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    username=firebaseAuth.getCurrentUser().getDisplayName();
                    Toast.makeText(MainActivity.this, "You are already signed in",
                            Toast.LENGTH_LONG).show();
                }else{
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()

                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        } ;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

    }

    public String getCurrentTime(){
        SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }

    public void sendPicture(View view) {

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,"Complete action using"),
                RC_PHOTO_PICKER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(">> ", "Before check");
        if(requestCode==RC_PHOTO_PICKER){
            Log.i(">> ", "After check");
            Uri uri=data.getData();
            StorageReference storageReference= mStorageReference
                    .child(uri.getLastPathSegment());
            Log.i(">> ", "Before Listnr");
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                   @Override
                                                                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                       Log.i(">> ", "In Success Listnr");
                                                                       myRef.push().setValue(new ChatMessageItem(null,
                                                                               username, getCurrentTime(),taskSnapshot
                                                                               .getDownloadUrl().toString()));




                                                                   }
                                                               }
            );
        }
    }
}
