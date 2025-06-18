package com.example.goodsmanagementapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {
    private EditText edtFullName, edtDateOfBirth, edtHomeTown, edtCurrentAddress, edtWorkExp, edtArchievement, edtSelfIntroduction;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private ImageButton btnSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        btnSearch = requireActivity().findViewById(R.id.btn_search);
        btnSearch.setVisibility(View.GONE);
        edtFullName = view.findViewById(R.id.edtFullName);
        edtDateOfBirth = view.findViewById(R.id.edtDateOfBirth);
        edtHomeTown = view.findViewById(R.id.edtHomeTown);
        edtCurrentAddress = view.findViewById(R.id.edtCurrentAddress);
        edtWorkExp = view.findViewById(R.id.edtWorkExperience);
        edtArchievement = view.findViewById(R.id.edtAchievements);
        edtSelfIntroduction = view.findViewById(R.id.edtSelfIntroduction);

        checkBoxes.add(view.findViewById(R.id.tag_lop1));
        checkBoxes.add(view.findViewById(R.id.tag_lop2));
        checkBoxes.add(view.findViewById(R.id.tag_lop3));
        checkBoxes.add(view.findViewById(R.id.tag_lop4));
        checkBoxes.add(view.findViewById(R.id.tag_lop5));
        checkBoxes.add(view.findViewById(R.id.tag_lop6));
        checkBoxes.add(view.findViewById(R.id.tag_lop7));
        checkBoxes.add(view.findViewById(R.id.tag_lop8));
        checkBoxes.add(view.findViewById(R.id.tag_lop9));
        checkBoxes.add(view.findViewById(R.id.tag_lop10));
        checkBoxes.add(view.findViewById(R.id.tag_lop11));
        checkBoxes.add(view.findViewById(R.id.tag_lop12));
        checkBoxes.add(view.findViewById(R.id.tag_toan));
        checkBoxes.add(view.findViewById(R.id.tag_van));
        checkBoxes.add(view.findViewById(R.id.tag_tienganh));

        Button btnUpdateProfile = view.findViewById(R.id.btnUpDateProfile);
        btnUpdateProfile.setOnClickListener(v -> {
            if (currentUser != null) {
                addOrUpdateCV(currentUser.getEmail());
            }
        });

        Button btnDeleteProfile = view.findViewById(R.id.btnDeleteProfile);
        btnDeleteProfile.setOnClickListener(v -> {
            if (currentUser != null) {
                deleteUserProfile(currentUser.getEmail());
            }
        });



        view.setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });

        // Tự động lấy dữ liệu nếu đã có
        if (currentUser != null) {
            loadUserProfile(currentUser.getEmail());
        }

        return view;
    }

    // Xóa hồ sơ của user khỏi Firestore
    private void deleteUserProfile(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cvCollection = db.collection("CVs");
        cvCollection.whereEqualTo("email", userEmail).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Xóa từng document tìm được (trường hợp 1 email nhiều bản ghi)
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            cvCollection.document(doc.getId()).delete();
                        }
                        clearProfileFields();
                        showToast("Đã gỡ hồ sơ thành công!");
                    } else {
                        showToast("Không tìm thấy hồ sơ để gỡ!");
                    }
                })
                .addOnFailureListener(e -> showToast("Lỗi khi xóa hồ sơ!"));
    }

    // Xóa trắng các trường nhập liệu trên giao diện
    private void clearProfileFields() {
        edtFullName.setText("");
        edtDateOfBirth.setText("");
        edtHomeTown.setText("");
        edtCurrentAddress.setText("");
        edtWorkExp.setText("");
        edtArchievement.setText("");
        edtSelfIntroduction.setText("");
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(false);
        }
    }


    // Lấy dữ liệu hồ sơ user và hiển thị lên giao diện
    private void loadUserProfile(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cvCollection = db.collection("CVs");

        cvCollection.whereEqualTo("email", userEmail).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                        edtFullName.setText(doc.getString("fullName"));
                        edtDateOfBirth.setText(doc.getString("dateOfBirth"));
                        edtHomeTown.setText(doc.getString("homeTown"));
                        edtCurrentAddress.setText(doc.getString("currentAddress"));
                        edtWorkExp.setText(doc.getString("workExperience"));
                        edtArchievement.setText(doc.getString("achievements"));
                        edtSelfIntroduction.setText(doc.getString("selfIntroduction"));

                        // Set các tag/checkbox theo dữ liệu
                        List<String> tags = (List<String>) doc.get("tags");
                        if (tags != null) {
                            for (CheckBox checkBox : checkBoxes) {
                                checkBox.setChecked(tags.contains(checkBox.getText().toString()));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> showToast("Lỗi khi lấy thông tin hồ sơ!"));
    }

    // Thêm hoặc cập nhật thông tin CV
    private void addOrUpdateCV(final String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference cvCollection = db.collection("CVs");

        List<String> selectedTags = new ArrayList<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                selectedTags.add(checkBox.getText().toString());
            }
        }

        CV userCV = new CV(
                null,
                edtFullName.getText().toString(),
                userEmail,
                edtDateOfBirth.getText().toString(),
                edtCurrentAddress.getText().toString(),
                edtHomeTown.getText().toString(),
                edtWorkExp.getText().toString(),
                edtArchievement.getText().toString(),
                edtSelfIntroduction.getText().toString(),
                selectedTags
        );

        cvCollection.whereEqualTo("email", userEmail).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference cvRef = cvCollection.document(documentSnapshot.getId());
                        cvRef.set(userCV)
                                .addOnSuccessListener(aVoid -> showToast("Cập nhật thành công!"))
                                .addOnFailureListener(e -> showToast("Lỗi khi cập nhật"));
                    } else {
                        DocumentReference cvRef = cvCollection.document(userEmail);
                        cvRef.set(userCV)
                                .addOnSuccessListener(aVoid -> showToast("Cập nhật thành công!"))
                                .addOnFailureListener(e -> showToast("Cập nhật thất bại!"));
                    }
                })
                .addOnFailureListener(e -> showToast("Lỗi khi tìm kiếm CV"));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
