package com.example.goodsmanagementapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<CV> mListCV;

    public ViewPagerAdapter(@NonNull HomeFragment fragmentActivity, List<CV> list) {
        super(fragmentActivity);
        this.mListCV = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        CV cv = mListCV.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cv_infor", cv);
        CVFragment cvFragment = new CVFragment();
        cvFragment.setArguments(bundle);
        return cvFragment;
    }

    @Override
    public int getItemCount() {
        return mListCV.size();
    }

    @Override
    public long getItemId(int position) {
        // Trả về một ID duy nhất cho mỗi CV dựa trên email (hoặc ID từ Firestore nếu có)
        return mListCV.get(position).getEmail().hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        for (CV cv : mListCV) {
            if (cv.getEmail().hashCode() == itemId) {
                return true;
            }
        }
        return false;
    }
}
