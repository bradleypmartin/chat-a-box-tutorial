package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bradleypmartinsandbox.chat_a_box_tutorial.MembersFragment.OnListFragmentInteractionListener;
import com.bradleypmartinsandbox.chat_a_box_tutorial.dummy.DummyContent.DummyItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder> {

    String TAG = "FirebaseTestMembers";

    private final ArrayList<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    HashMap<String, String> mUserGravatars;
    FirebaseDatabase mDatabase;

    public MembersRecyclerViewAdapter(ArrayList<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        initGravatars();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_members, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mChatUserView.setText(holder.mItem);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMembersListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mChatUserView;
        public final ImageView mChatUserIconView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mChatUserView = view.findViewById(R.id.chatUser);
            mChatUserIconView = view.findViewById(R.id.chatUserIcon);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public void initGravatars() {
        mDatabase = FirebaseDatabase.getInstance();
        mUserGravatars = new HashMap<String, String>();

        DatabaseReference ref = mDatabase.getReference("userGravatars");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUserGravatars.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String childKey = child.getKey();
                    String childValue = (String) child.getValue();

                    Log.i(TAG, "Child : key [" + childKey + "] value : [" + childValue + "].");
                    mUserGravatars.put (childKey, childValue);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(listener);
    }
}
