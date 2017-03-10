package util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.siyann.studentcourseapp.R;

import java.util.List;

import widget.Photo;

/**
 * 让这个适配器继承自RecyclerView.Adapter，指定泛型为PhotoAdapter.ViewHolder
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private Context mContext;
    private List<Photo> mPhotolist;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView photoImage;
        TextView photoName;

        public ViewHolder(View view) {
        super(view);
        cardView= (CardView) view;
            photoImage= (ImageView) view.findViewById(R.id.photo_image);
            photoName= (TextView) view.findViewById(R.id.photo_name);
        }
    }

    public PhotoAdapter(List<Photo> photoList){
        mPhotolist=photoList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.photo_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo=mPhotolist.get(position);
        holder.photoName.setText(photo.getName());
        Glide.with(mContext).load(photo.getImageId()).into(holder.photoImage);
    }

    @Override
    public int getItemCount() {
        return mPhotolist.size();
    }

}
