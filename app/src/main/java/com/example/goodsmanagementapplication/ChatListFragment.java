package com.example.goodsmanagementapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {
    private static final String TAG = "ChatListFragment";
    private List<Friend> mChatList;
    private ChatListAdapter chatListAdapter;
    private FirebaseUser currentUser ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Fragment is being created");
        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);
        RecyclerView rcvChatList;
        rcvChatList = view.findViewById(R.id.recyclerViewChatList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvChatList.setLayoutManager(linearLayoutManager);
        chatListAdapter = new ChatListAdapter(requireContext());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        chatListAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Friend friend) {
                startPrivateChat(friend);
            }
        });
        mChatList = new ArrayList<>();
        chatListAdapter.setData(mChatList);
        rcvChatList.setAdapter(chatListAdapter);
        setData();

        return view;
    }

    private void startPrivateChat(Friend friend) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("friend",friend);
        PrivateChatFragment privateChatFragment = new PrivateChatFragment();
        privateChatFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.chatActivityContainer,privateChatFragment)
                .addToBackStack(null)
                .commit();
        Log.d(TAG, "Khởi động private chat");
    }


    private void setData() {
        List<Friend> listFriend = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CVs")
                .document(currentUser.getEmail())
                .collection("friends")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Friend friend =(Friend) document.toObject(Friend.class);
                            listFriend.add(friend);
                        }
                        mChatList.clear();
                        mChatList.addAll(listFriend);
                        chatListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "setData: Chat list updated, size: " + mChatList.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents.", e);
                    }
                });
    }
}
