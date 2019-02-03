package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableLayout;

import com.bradleypmartinsandbox.chat_a_box_tutorial.dummy.DummyContent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.tkeunebr.gravatar.Gravatar;

public class MainActivity extends AppCompatActivity
        implements ChatMessageFragment.OnFragmentInteractionListener,
        HistoryFragment.OnListFragmentInteractionListener,
        MembersFragment.OnListFragmentInteractionListener, RewardedVideoAdListener {

    final String TAG = "FirebaseTest";

    FirebaseApp app;
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;

    FirebaseAuth.AuthStateListener authStateListener;
    String displayName;

    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    TabLayout mTabLayout;

    AdView mAdView;
    AdRequest mBannerAdRequest;
    InterstitialAd mInterstitialAd;
    RewardedVideoAd mRewardAd;

    int mAdvertCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initViewPager();
        initAdverts();
        initDatabaseChat();
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
                    initGravatars();
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
                initGravatars();
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

        int tabSelectedColor = ResourcesCompat.getColor( getResources(), R.color.colorPrimaryDark, null );
        int tabNotSelectedColor = ResourcesCompat.getColor( getResources(), R.color.colorAccent, null );
        mTabLayout.setTabTextColors( tabSelectedColor, tabNotSelectedColor );

        int tabColors = ResourcesCompat.getColor( getResources(), R.color.colorPrimary, null);
        mTabLayout.setBackground(new ColorDrawable(tabColors));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mAdvertCounter++;

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

                if (i == 0)
                    imm.showSoftInput(getCurrentFocus(), 0);
                else
                    imm.hideSoftInputFromWindow(mAdView.getWindowToken(), 0);

                if (mAdvertCounter >= 10) {
                    if (mRewardAd.isLoaded())
                        mRewardAd.show();

                    mAdvertCounter = 0;
                } else if (mAdvertCounter == 5) {
                    if (mInterstitialAd.isLoaded())
                        mInterstitialAd.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initAdverts() {
        MobileAds.initialize(this, "ca-app-pub-1369670350323074~6791506389");

        mAdView = findViewById(R.id.adView);
        mBannerAdRequest = new AdRequest.Builder().build();
        mAdView.loadAd(mBannerAdRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        mRewardAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardAd.setRewardedVideoAdListener(this);
        mRewardAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    public void onFragmentInteraction(Uri uri) {
        Log.i(TAG, "Chat Fragment");
    }

    public void onHistoryListFragmentInteraction(ChatMessage item) {
        Log.i(TAG, "History Fragment");
    }

    public void onMembersListFragmentInteraction(String item) {
        Log.i(TAG, "Members Fragment");
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        mRewardAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public void initDatabaseChat() {
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mDatabase.getReference("chatMessages");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HistoryFragment historyFragment = (HistoryFragment)fragmentAdapter.getItem(1);
                MembersFragment membersFragment = (MembersFragment)fragmentAdapter.getItem(2);


                // TODO: optimize this pattern for sorting chat history
                historyFragment.clearChatMessages();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    ChatMessage chat = child.getValue(ChatMessage.class);
                    historyFragment.routeChatMessage(chat);
                    membersFragment.routeChatMessage(chat.chatSender);
                    Log.i(TAG + " Child : ", chat.toString());
                }

                historyFragment.sortChatMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(listener);
    }

    public void initGravatars() {
        String myEmail = auth.getCurrentUser().getEmail();
        String gravatarURL = Gravatar.init().with(myEmail).size(100).build();
        try {
            DatabaseReference ref = mDatabase.getReference("userGravatars").child(displayName);
            ref.setValue(gravatarURL);
        } catch (Exception e) {
            Log.e(TAG, "Problem with populating userGravitars hash. Error: " + e);
        }
    }
}
