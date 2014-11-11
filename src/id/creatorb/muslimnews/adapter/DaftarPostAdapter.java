package id.creatorb.muslimnews.adapter;

import id.creatorb.muslimnews.post.PostData;

import java.util.ArrayList;

import id.creatorb.androidrssfeed.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DaftarPostAdapter extends ArrayAdapter<PostData> {
	private LayoutInflater inflater;
	private ArrayList<PostData> datas;

	public DaftarPostAdapter(Context context, int textViewResourceId,
			ArrayList<PostData> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		inflater = ((Activity) context).getLayoutInflater();
		datas = objects;
	}

	static class ViewHolder {
		TextView postTitleView;
		TextView postDateView;
		ImageView postThumbView;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_artikel, null);

			viewHolder = new ViewHolder();
			viewHolder.postThumbView = (ImageView) convertView
					.findViewById(R.id.postThumb);
			viewHolder.postTitleView = (TextView) convertView
					.findViewById(R.id.postTitleLabel);
			viewHolder.postDateView = (TextView) convertView
					.findViewById(R.id.postDateLabel);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (datas.get(position).postThumbUrl == null) {
			viewHolder.postThumbView
					.setImageResource(R.drawable.muslim_logo_creatorb);
		}

		viewHolder.postTitleView.setText(datas.get(position).postTitle);
		viewHolder.postDateView.setText(datas.get(position).postDate);

		return convertView;
	}
}
