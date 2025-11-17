package com.example.photoviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<PostItem> postList;
    private SharedPreferences prefs;

    public ImageAdapter(Context context, List<PostItem> postList) {
        this.context = context;
        this.postList = postList;
        this.prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        loadFavorites(); // 앱 실행 시 저장된 즐겨찾기 상태 불러오기
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false); // ✅ item_image.xml 사용
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        PostItem post = postList.get(position);

        holder.textTitle.setText(post.getTitle());
        holder.textDate.setText(post.getCreatedDate());

        // ✅ Glide로 이미지 로드
        Glide.with(context)
                .load(post.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        // ✅ 별 아이콘 상태 표시 (즐겨찾기 여부에 따라 이미지 변경)
        holder.btnFavorite.setImageResource(
                post.isFavorite() ? R.drawable.ic_star_filled : R.drawable.ic_star_border
        );

        // ✅ 별 버튼 클릭 이벤트
        holder.btnFavorite.setOnClickListener(v -> {
            boolean newState = !post.isFavorite();
            post.setFavorite(newState);

            // UI 즉시 갱신
            holder.btnFavorite.setImageResource(
                    newState ? R.drawable.ic_star_filled : R.drawable.ic_star_border
            );

            // SharedPreferences에 상태 저장
            prefs.edit().putBoolean(post.getTitle(), newState).apply();
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // ✅ SharedPreferences에서 즐겨찾기 상태 불러오기
    private void loadFavorites() {
        for (PostItem post : postList) {
            boolean isFav = prefs.getBoolean(post.getTitle(), false);
            post.setFavorite(isFav);
        }
    }

    // ✅ 리스트 갱신용 (정렬/필터용)
    public void updateList(List<PostItem> newList) {
        this.postList = newList;
        loadFavorites(); // 갱신 시에도 즐겨찾기 반영
        notifyDataSetChanged();
    }

    // ViewHolder 정의
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton btnFavorite;
        TextView textTitle, textDate;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPost);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }
}
