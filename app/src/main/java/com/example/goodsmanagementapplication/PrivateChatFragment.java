package com.example.goodsmanagementapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PrivateChatFragment extends Fragment {

    private static final String TAG = "PrivateChatFragment";
    private Friend friend;
    private RecyclerView rcvMessage;
    private FirebaseUser currentUser;
    private List<ChatItem> mListMessage;
    private ChatItemAdapter adapter;
    private EditText edt_message;
    private ImageButton btn_send;
    private CV currentUserCV;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_private_chat, container, false);

        // Nhận dữ liệu Friend từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("friend")) {
            friend = (Friend) bundle.getSerializable("friend");
            Log.d(TAG, "onCreateView: Received Friend data - Name: " + friend.getName());
        } else {
            Log.e(TAG, "onCreateView: No Friend data received!");
        }

        TextView tv_chat_header = view.findViewById(R.id.tv_chat_header);
        tv_chat_header.setText("Chat với: " + (friend != null ? friend.getName() : ""));

        edt_message = view.findViewById(R.id.edt_message);
        btn_send = view.findViewById(R.id.btn_send);
        rcvMessage = view.findViewById(R.id.recyclerViewMessages);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mListMessage = new ArrayList<>();
        adapter = new ChatItemAdapter(requireContext());
        adapter.setData(mListMessage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvMessage.setLayoutManager(linearLayoutManager);
        rcvMessage.setAdapter(adapter);

        // Lắng nghe realtime tin nhắn
        loadMessageFromFirestore();

        btn_send.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String formattedTime = sdf.format(Timestamp.now().toDate());
        // Lấy dữ liệu của currentUser
        db.collection("CVs").document(currentUser.getEmail())
                .get().addOnSuccessListener(documentSnapshot -> {
                    currentUserCV = documentSnapshot.toObject(CV.class);
                    String contentMessage = edt_message.getText().toString().trim();
                    if (currentUserCV == null || contentMessage.isEmpty()) return;

                    ChatItem message = new ChatItem(
                            currentUserCV.getImageUrl(),
                            currentUserCV.getEmail(),
                            currentUserCV.getFullName(),
                            contentMessage,
                            formattedTime
                    );

                    // Gửi vào cả 2 bên (người gửi và người nhận)
                    db.collection("CVs").document(currentUser.getEmail())
                            .collection("friends").document(friend.getEmail())
                            .collection("chat_history")
                            .add(message);

                    db.collection("CVs").document(friend.getEmail())
                            .collection("friends").document(currentUserCV.getEmail())
                            .collection("chat_history")
                            .add(message);

                    edt_message.setText("");
                });
    }

    private void loadMessageFromFirestore() {
        db.collection("CVs").document(currentUser.getEmail())
                .collection("friends").document(friend.getEmail())
                .collection("chat_history")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    List<ChatItem> list = new ArrayList<>();
                    if (snapshots != null) {
                        for (DocumentSnapshot docRef : snapshots.getDocuments()) {
                            ChatItem message = docRef.toObject(ChatItem.class);
                            list.add(message);
                        }
                    }
                    mListMessage.clear();
                    mListMessage.addAll(list);
                    adapter.notifyDataSetChanged();
                    if (mListMessage.size() > 0) {
                        rcvMessage.scrollToPosition(mListMessage.size() - 1); // Cuộn xuống cuối
                    }
                    Log.d(TAG, "Realtime: Đã lấy số tin nhắn : " + mListMessage.size());
                });
    }
}
