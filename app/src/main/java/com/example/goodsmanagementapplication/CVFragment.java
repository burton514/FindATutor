package com.example.goodsmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.integrity.i;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CVFragment extends Fragment {
    private static final String TAG = "CVFragment";
    private Button btnChat;
    private CV cv;
    private CV currentUserCV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Fragment CVFragment được tạo");

        View view = inflater.inflate(R.layout.fragment_cv, container, false);
        btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewFriendIntoChatList(cv);
                startChatActivity();
            }
        });

        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey("cv_infor")) {
            Log.w(TAG, "onCreateView: Không tìm thấy dữ liệu CV trong Bundle");
            return view;
        }

        cv =  bundle.getSerializable("cv_infor",CV.class);

        if (cv == null) {
            Log.e(TAG, "onCreateView: CV nhận được là null");
            return view;
        }
        loadCurrentUserCV();

        Log.d(TAG, "onCreateView: Đang hiển thị thông tin của CV: " + cv.getFullName());

        // Ánh xạ view
        TextView txtFullName = view.findViewById(R.id.txtFullName);
        TextView txtDateOfBirth = view.findViewById(R.id.txtDateOfBirth);
        TextView txtHomeTown = view.findViewById(R.id.txtHomeTown);
        TextView txtCurrentAddress = view.findViewById(R.id.txtCurrentAddress);
        TextView txtWorkExperience = view.findViewById(R.id.txtWorkExperienceValue);
        TextView txtAchievements = view.findViewById(R.id.txtAchievementsValue);
        TextView txtSelfIntroduction = view.findViewById(R.id.txtSelfIntroductionValue);
        TextView txtTags = view.findViewById(R.id.txtTagList);

        // Gán dữ liệu từ CV vào UI
        txtFullName.setText(cv.getFullName() != null ? cv.getFullName() : "N/A");
        txtDateOfBirth.setText(cv.getDateOfBirth() != null ? cv.getDateOfBirth() : "N/A");
        txtHomeTown.setText(cv.getHomeTown() != null ? cv.getHomeTown() : "N/A");
        txtCurrentAddress.setText(cv.getCurrentAddress() != null ? cv.getCurrentAddress() : "N/A");
        txtWorkExperience.setText(cv.getWorkExperience() != null ? cv.getWorkExperience() : "N/A");
        txtAchievements.setText(cv.getAchievements() != null ? cv.getAchievements() : "N/A");
        txtSelfIntroduction.setText(cv.getSelfIntroduction() != null ? cv.getSelfIntroduction() : "N/A");

        Log.d(TAG, "onCreateView: Hiển thị thông tin thành công");

        // Hiển thị danh sách tags
        List<String> tags = cv.getTags();
        if (tags != null && !tags.isEmpty()) {
            String tagsString = android.text.TextUtils.join(", ", tags);
            txtTags.setText(tagsString);
            Log.d(TAG, "onCreateView: Tags: " + tagsString);
        } else {
            txtTags.setText("(Chưa có tags)");
            Log.w(TAG, "onCreateView: CV này chưa có tags");
        }

        return view;
    }


    /**
     * Lấy CV của current user và lưu vào biến currentUserCV
     */
    private void loadCurrentUserCV() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserEmail = user.getEmail().trim();
            FirebaseFirestore.getInstance()
                    .collection("CVs")
                    .document(currentUserEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            currentUserCV = documentSnapshot.toObject(CV.class);
                            if (currentUserCV != null) {
                                Log.d(TAG, "Lấy CV thành công: " + currentUserCV.getFullName());
                            } else {
                                Log.e(TAG, "CV hiện tại là null");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Lỗi khi lấy CV", e);
                        }
                    });
        } else {
            Log.e(TAG, "Không tìm thấy user đang đăng nhập");
        }
    }

    private void addNewFriendIntoChatList(CV cv) {
        if (currentUserCV == null) {
            Log.e(TAG, "currentUserCV chưa được tải!");
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String currentUserEmail = currentUserCV.getEmail();
        DocumentReference userDocRef = firestore.collection("CVs").document(currentUserEmail);
        CollectionReference friendsCollectionRef = userDocRef.collection("friends");

        friendsCollectionRef.document(cv.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "Bạn bè đã tồn tại: " + cv.getEmail());
                            sendMessageToChatHistory(userDocRef, cv.getEmail(), "Hello!");
                            sendMessageToChatHistory(firestore.collection("CVs").document(cv.getEmail()), currentUserEmail, "Hello!");
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                            String formattedTime = sdf.format(Timestamp.now().toDate());

                            Friend newFriend = new Friend(cv.getFullName(), cv.getEmail(), "",
                                    formattedTime, cv.getImageUrl());

                            friendsCollectionRef.document(cv.getEmail()).set(newFriend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Đã thêm bạn mới: " + newFriend.getEmail());
                                            addForOther(cv.getEmail());
                                            sendMessageToChatHistory(userDocRef, cv.getEmail(), "Hi");
                                            sendMessageToChatHistory(firestore.collection("CVs").document(cv.getEmail()), currentUserEmail, "Hi");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Lỗi khi thêm bạn bè", e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Lỗi khi kiểm tra bạn bè", e);
                    }
                });
    }

    private void addForOther(String friendEmail) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference friendDocRef = firestore.collection("CVs").document(friendEmail);
        CollectionReference friendsCollectionRef = friendDocRef.collection("friends");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String formattedTime = sdf.format(Timestamp.now().toDate());

        if (currentUserCV != null) {
            Friend currentUser = new Friend(currentUserCV.getFullName(), currentUserCV.getEmail(), "",
                    formattedTime, currentUserCV.getImageUrl());

            friendsCollectionRef.document(currentUserCV.getEmail()).set(currentUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Đã đồng bộ bạn bè cho: " + friendEmail);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Lỗi khi đồng bộ bạn bè", e);
                        }
                    });
        }
    }

    private void sendMessageToChatHistory(DocumentReference userDocRef, String friendEmail, String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String formattedTime = sdf.format(Timestamp.now().toDate());

        CollectionReference chatHistoryRef = userDocRef
                .collection("friends")
                .document(friendEmail)
                .collection("chat_history");

        ChatItem chatMessage = new ChatItem(
                null,
                currentUserCV.getEmail(),
                currentUserCV.getFullName(),
                message,
                formattedTime
        );

        chatHistoryRef.add(chatMessage);
    }

    private void startChatActivity() {
        Intent intent = new Intent(requireActivity(), ChatActivity.class);
        startActivity(intent);
    }
}
