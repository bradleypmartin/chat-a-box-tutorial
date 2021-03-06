package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bradleypmartinsandbox.chat_a_box_tutorial.MembersFragment.OnListFragmentInteractionListener;
import com.bradleypmartinsandbox.chat_a_box_tutorial.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MembersRecyclerViewAdapter(ArrayList<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
}
