package com.example.goodsmanagementapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ChatViewHolder> {
    private List<ChatItem> chatList;
    private Context mContext;
    private FirebaseUser currentUser;
    public ChatItemAdapter(Context context) {
        this.mContext = context;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    public void setData(List<ChatItem> list) {
        this.chatList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItem chatItem = chatList.get(position);
        holder.txtUsername.setText(chatItem.getEmail().equals(currentUser.getEmail())?"Bạn":chatItem.getName());
        holder.txtMessage.setText(chatItem.getLastMessage());
        holder.txtTimestamp.setText(chatItem.getTimestamp());
        Glide.with(mContext)
                .load(chatItem.getAvatarUrl())
                .placeholder(R.drawable.ic_user) // Ảnh mặc định nếu không có ảnh
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtUsername, txtMessage,txtTimestamp;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar_itemchat);
            txtUsername = itemView.findViewById(R.id.txt_username_itemchat);
            txtMessage = itemView.findViewById(R.id.txt_message_itemchat);
            txtTimestamp = itemView.findViewById(R.id.tv_timestamp_itemchat);
        }
    }
}

