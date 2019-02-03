package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bradleypmartinsandbox.chat_a_box_tutorial.HistoryFragment.OnListFragmentInteractionListener;
import com.bradleypmartinsandbox.chat_a_box_tutorial.dummy.DummyContent.DummyItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private final List<ChatMessage> mValues;
    private final OnListFragmentInteractionListener mListener;
    FirebaseAuth mAuth;
    String mDisplayName = "Unknown";

    String TAG = "FirebaseTestHistory";

    public HistoryRecyclerViewAdapter(List<ChatMessage> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null)
            mDisplayName = user.getDisplayName();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mChatSender.setText( holder.mItem.getChatSender() );
        holder.mChatText.setText( holder.mItem.getChatText() );
        holder.mChatSendTime.setText( "(" + holder.mItem.getChatSendTime() + ")" );
        holder.mChatIcon.setImageResource(R.drawable.common_google_signin_btn_icon_light);

        try {
            if (holder.mItem.getChatSender().equals(mDisplayName))
                holder.mChatIcon.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
        } catch (Exception e) {
            Log.e(TAG, "Problem with chat message avatar population: " + e);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onHistoryListFragmentInteraction(holder.mItem);
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
        public final TextView mChatSender;
        public final TextView mChatText;
        public final TextView mChatSendTime;
        public final ImageView mChatIcon;
        public ChatMessage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mChatSender = view.findViewById(R.id.chatSender);
            mChatText = view.findViewById(R.id.chatMessageText);
            mChatSendTime = view.findViewById(R.id.chatSendTime);
            mChatIcon = view.findViewById(R.id.chatIcon);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
