package com.ctyx.encrochat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

//import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth; // FOR USER AUTHENTICATION

    private  Toolbar mToolbar;


    private EditText eText;
    private ListView lView;

    private DatabaseReference databaseReference;

    private String stringMessage=null;
    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;


    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eText = findViewById(R.id.editText);
        lView = findViewById(R.id.listView);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("EncroChat");

        mViewPager =(ViewPager) findViewById(R.id.main_pager);
        mSectionPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionPagerAdapter);
        mTabLayout=(TabLayout) findViewById(R.id.main_tab);
        mTabLayout.setupWithViewPager(mViewPager);






        try{
            databaseReference = FirebaseDatabase.getInstance().getReference("Message");

            try {
                cipher = Cipher.getInstance("AES");
                decipher = Cipher.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }

            secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        stringMessage = dataSnapshot.getValue().toString();
                        stringMessage = stringMessage.substring(1, stringMessage.length() - 1);

                        String[] stringMessageArray = stringMessage.split(", ");
                        Arrays.sort(stringMessageArray);
                        String[] stringFinal = new String[stringMessageArray.length * 2];

                        try {
                            for (int i = 0; i < stringMessageArray.length; i++) {
                                String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                                stringFinal[2 * i] = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", Long.parseLong(stringKeyValue[0]));
                                stringFinal[2 * i + 1] = AESDecryptionMethod(stringKeyValue[1]);
                            }


                            lView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringFinal));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
   /********  Checking for user Authentication Otherwise sent to Login Page*****/
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null)
        {
           sendtostart();
        }

    }

    private void sendtostart(){
        Intent startIntent= new Intent(MainActivity.this,StartAct.class);
        startActivity(startIntent);
        finish();
    }


     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);


        return true;

     }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       super.onOptionsItemSelected(item);
       if (item.getItemId() == R.id.main_logout){
           FirebaseAuth.getInstance().signOut();
           sendtostart();}
        if (item.getItemId() == R.id.menu_setting){
            Intent settings_intent= new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settings_intent);
        }
        if (item.getItemId() == R.id.users_in_menu){
            Intent users_intent= new Intent(MainActivity.this,UsersActivity.class);
            startActivity(users_intent);

        }




       return true;
    }

    public void sendButton(View view){

        Date date = new Date();
        databaseReference.child(Long.toString(date.getTime())).setValue(AESEncryptionMethod(eText.getText().toString()));
        eText.setText("");

    }

    private String AESEncryptionMethod(String string){

        byte[] stringByte = string.getBytes();
        byte[] encryptedByte = new byte[stringByte.length];

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String returnString = null;

        try {
            returnString = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = string;

        byte[] decryption;

        try {
            decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }

}
