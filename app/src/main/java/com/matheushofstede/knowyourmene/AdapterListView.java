

package com.matheushofstede.knowyourmene;

import java.util.List;

import com.matheushofstede.knowyourmene.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import android.content.SharedPreferences;
import android.widget.BaseAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.matheushofstede.knowyourmene.Post;

public class AdapterListView extends BaseAdapter {
	private List<Post> list;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
    private String item;
    public static final String PREFS_NAME = "com.matheushofstede.knowyourmene_preferences";
    private String grid_columns;

    //String grid_columns;




    public AdapterListView(Context context, List<Post> list, ImageLoader mImageLoader, String grid_columns){
		this.list = list;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mImageLoader = mImageLoader;
        this.grid_columns = grid_columns;
	}




	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return (Post) list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        //MainActivity main = new MainActivity();
        //String grid_columnsA = main.grid_columns;

		if(convertView == null){



            ;
            Log.i("TAG",grid_columns);

            if (grid_columns.equals("2")){
                //grid_columns = "R.layout.item_list2";
                convertView = inflater.inflate(R.layout.item_list2, null);

            }
            if (grid_columns.equals("3")){
                //grid_columns = "R.layout.item_list";
                convertView = inflater.inflate(R.layout.item_list, null);

            }
            if (grid_columns.equals("4")){
                //grid_columns = "R.layout.item_list";
                convertView = inflater.inflate(R.layout.item_list4, null);

            }







			holder = new ViewHolder();
			convertView.setTag(holder);
			
			holder.ivPost = (ImageView) convertView.findViewById(R.id.imageView);
			//holder.tvPost = (TextView) convertView.findViewById(R.id.textView1);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//holder.tvPost.setText(list.get(position).getTitle());
		
		// IMAGE LOADER
		mImageLoader.displayImage(list.get(position).getLink(),
			holder.ivPost,
			null,
			new ImageLoadingListener(){

				@Override
				public void onLoadingCancelled(String uri, View view) {
					Log.i("Script", "onLoadingCancelled()");
				}
	
				@Override
				public void onLoadingComplete(String uri, View view, Bitmap bmp) {
					Log.i("Script", "onLoadingComplete()");
				}
	
				@Override
				public void onLoadingFailed(String uri, View view, FailReason fail) {
					Log.i("Script", "onLoadingFailed("+fail+")");
				}
	
				@Override
				public void onLoadingStarted(String uri, View view) {
					Log.i("Script", "onLoadingStarted()");
				}
				
			}, new ImageLoadingProgressListener(){
				@Override
				public void onProgressUpdate(String uri, View view, int current, int total) {
					Log.i("Script", "onProgressUpdate("+uri+" : "+total+" : "+current+")");
				}
			});
		
		return convertView;
	}

	
	private class ViewHolder{
		public ImageView ivPost;
		//public TextView tvPost;
	}
}