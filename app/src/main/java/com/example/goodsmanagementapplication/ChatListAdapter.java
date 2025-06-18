package com.example.goodsmanagementapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context mContext;
    private List<Friend> mChatList;
    private OnItemClickListener mListener; // Interface xử lý sự kiện click

    // Interface cho sự kiện click item
    public interface OnItemClickListener {
        void onItemClick(Friend friend);
    }

    // Constructor
    public ChatListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mChatList = new ArrayList<>(); // Khởi tạo danh sách để tránh lỗi null
    }

    // Gán dữ liệu mới cho Adapter
    public void setData(List<Friend> list) {
        mChatList = list;
        notifyDataSetChanged();
    }

    // Thiết lập sự kiện click
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chatlist, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        Friend friend = mChatList.get(position);
        if (friend == null) return;

        holder.tvName.setText(friend.getName());
        holder.tvLastMessage.setText(friend.getLastMessage());
        holder.tvTimestamp.setText(friend.getTimestamp());

        // Load ảnh đại diện bằng Glide
        Glide.with(mContext)
                .load(friend.getAvatarUrl())
                .placeholder(R.drawable.ic_user) // Ảnh mặc định nếu không có ảnh
                .into(holder.imgAvatar);

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(friend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatList != null ? mChatList.size() : 0;
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvLastMessage, tvTimestamp;
        private ImageView imgAvatar;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
        }
    }
}
