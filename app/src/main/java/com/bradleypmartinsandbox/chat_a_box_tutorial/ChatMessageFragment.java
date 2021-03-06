package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatMessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatMessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String TAG = "FirebaseTestChat";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button mSendButton;
    EditText mChatMessageEdit;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    String mDisplayName;

    public ChatMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatMessageFragment newInstance(String param1, String param2) {
        ChatMessageFragment fragment = new ChatMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDisplayName = user.getDisplayName();

        Log.i(TAG, "Current Message user : Email: [" + user.getEmail() +
        "] DisplayName : [" + user.getDisplayName() + "].");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_message, container, false);

        mSendButton = view.findViewById(R.id.chatSendButton);
        mChatMessageEdit = view.findViewById(R.id.chatEditText);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Send Button Clicked");

                ChatMessage chat = new ChatMessage();

                String chatSender = mDisplayName;
                String chatMessage = mChatMessageEdit.getText().toString();

                android.text.format.DateFormat df = new android.text.format.DateFormat();
                String chatSendTime = df.format("dd:MM:yyyy HH:mm:ss", new java.util.Date()).toString();

                chat.chatSender = chatSender;
                chat.chatSendTime = chatSendTime;
                chat.chatText = chatMessage;

                String nodeKey = chat.chatSendTime + " " + chat.chatSender;

                DatabaseReference ref = mDatabase.getReference("chatMessages").child(nodeKey);
                ref.setValue(chat);

                mChatMessageEdit.setText("");
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
