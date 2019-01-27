package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.bradleypmartinsandbox.chat_a_box_tutorial.dummy.DummyContent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements ChatMessageFragment.OnFragmentInteractionListener,
        HistoryFragment.OnListFragmentInteractionListener,
        MembersFragment.OnListFragmentInteractionListener {

    final String TAG = "FirebaseTest";

    FirebaseApp app;
    FirebaseAuth auth;

    FirebaseAuth.AuthStateListener authStateListener;
    String displayName;

    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    TabLayout mTabLayout;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initViewPager();
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initFirebase() {

        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    Log.i(TAG, "Auth state update : valid current user logged on : email [" +
                            user.getEmail() + "] display name [" +
                            user.getDisplayName() + "]");
                    displayName = user.getDisplayName();
                } else {
                    Log.i(TAG, "Auth state update : no valid current user logged on.");
                    displayName = "No valid user";

                    auth.removeAuthStateListener(authStateListener);
                    Intent signIn = new Intent(getApplicationContext(), SignIn.class);
                    startActivityForResult(signIn, 101);
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG,"Activity returned");

        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                displayName = data.getStringExtra("displayName");
                Log.i(TAG, "Intent returned display name : [" + displayName + "].");
                auth.addAuthStateListener(authStateListener);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            Log.i(TAG, "Logout option selected.");
            auth.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        fragmentAdapter = new FragmentAdapter( getSupportFragmentManager() );
        viewPager.setAdapter(fragmentAdapter);
        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(viewPager);
    }

    public void onFragmentInteraction(Uri uri) {
        Log.i(TAG, "Chat Fragment");
    }

    public void onHistoryListFragmentInteraction(DummyContent.DummyItem item) {
        Log.i(TAG, "History Fragment");
    }

    public void onMembersListFragmentInteraction(DummyContent.DummyItem item) {
        Log.i(TAG, "Members Fragment");
    }
}
