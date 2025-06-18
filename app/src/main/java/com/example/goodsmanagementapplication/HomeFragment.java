package com.example.goodsmanagementapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ViewPager2 viewPager;
    private List<CV> mListCV;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageButton btnSearch;
    private ActivityResultLauncher<Intent> searchLauncher;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnSearch = requireActivity().findViewById(R.id.btn_search);
        btnSearch.setVisibility(View.VISIBLE);
        viewPager = view.findViewById(R.id.viewPager_home);

        // Khởi tạo danh sách rỗng và adapter
        mListCV = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(HomeFragment.this, mListCV);
        viewPager.setAdapter(viewPagerAdapter);

        // Gọi hàm lấy dữ liệu từ Firestore
        //loadDataFromFirestore();

        // Đăng ký callback để nhận kết quả từ SearchActivity
        searchLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                ArrayList<String> selectedClasses = data.getStringArrayListExtra("search_result");
                                if (selectedClasses != null) {
                                    Log.d(TAG, "Danh sách lớp đã chọn: " + selectedClasses.toString());
                                    filter_data(selectedClasses);
                                }
                            }
                        }
                    }
                });

        // Xử lý sự kiện khi nhấn vào nút tìm kiếm
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SearchActivity.class);
                searchLauncher.launch(intent);
            }
        });
        return view;
    }


    private void filter_data(ArrayList<String> selectedClasses) {
        CollectionReference CVsCollection = FirebaseFirestore.getInstance().collection("CVs");

        CVsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<CV> filteredList = new ArrayList<>();

                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    CV cv = document.toObject(CV.class);
                    if (cv != null) {
                        List<String> tags = (List<String>) document.get("tags"); // Lấy danh sách tags của CV

                        if (tags != null && tags.containsAll(selectedClasses)) {
                            // Chỉ thêm vào danh sách nếu CV chứa tất cả các tag trong selectedClasses
                            filteredList.add(cv);
                            Log.d(TAG, "Đã thêm " + cv.getEmail());
                        }
                    }
                }

                // Cập nhật danh sách hiển thị
                mListCV.clear();
                mListCV.addAll(filteredList);
                viewPagerAdapter.notifyDataSetChanged();

                Log.d(TAG, "Lọc xong, số CV phù hợp: " + filteredList.size());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Lỗi khi lọc dữ liệu!", e);
            }
        });
    }




    private void loadDataFromFirestore() {
        CollectionReference CVsCollection = FirebaseFirestore.getInstance().collection("CVs");
        CVsCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        mListCV.clear(); // Xóa danh sách cũ để tránh trùng dữ liệu

                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                CV cv = document.toObject(CV.class);
                                if (cv != null) {
                                    mListCV.add(cv);
                                    Log.d(TAG, "Lấy dữ liệu thành công: " + cv.getFullName());
                                }
                            }
                            Log.d(TAG, "Tổng số CV sau khi lấy từ Firestore: " + mListCV.size());
                            viewPagerAdapter.notifyDataSetChanged(); // Cập nhật Adapter
                        } else {
                            Log.d(TAG, "Không có dữ liệu!");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Lỗi khi lấy dữ liệu!", e);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDataFromFirestore();
        Log.d(TAG, "Load data from firestore success!");
    }
}
