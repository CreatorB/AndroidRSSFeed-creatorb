package id.creatorb.refresh;

import java.text.SimpleDateFormat;
import java.util.Date;

import id.creatorb.androidrssfeed.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RefresherListView extends ListView implements OnScrollListener {

	private final int HEADER_HEIGHT = 60;
	private final int HEADER_TOP = 10;
	private final int STATE_PULL_TO_REFRESH = 0;
	private final int STATE_RELEASE_TO_UPDATE = 1;
	private int currentState;
	private ImageView arrowImage;
	private ProgressBar progressBar;
	private TextView headerTextView;
	private TextView lastUpdateDateTextView;
	private LinearLayout headerRelativeLayout;
	private RotateAnimation rotateAnimation;
	private RotateAnimation reverseRotateAnimation;
	private Refresher refreshDelegate;

	private RelativeLayout footerLayout;
	private ProgressBar footerProgressBar;
	private boolean isLoadingMore;
	
	private boolean isLoading;
	//private boolean isDragging;
	private float startY;
	private float deltaY;
	

	public RefresherListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public RefresherListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		Log.d("debug", "debug");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//isDragging = true;
			startY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isLoading) {
				deltaY = ev.getY() - startY;

				Log.d("debug", String.valueOf(deltaY));

				headerRelativeLayout.setPadding(
						headerRelativeLayout.getPaddingLeft(), -1
								* HEADER_HEIGHT + (int) deltaY, 0,
						headerRelativeLayout.getPaddingBottom());
				
				if(headerRelativeLayout.getPaddingTop() >= HEADER_HEIGHT && currentState == STATE_PULL_TO_REFRESH) {
					//change state
					currentState = STATE_RELEASE_TO_UPDATE;
					arrowImage.clearAnimation();
					arrowImage.startAnimation(rotateAnimation);
					headerTextView.setText(R.string.release_to_refresh);
				} else if (headerRelativeLayout.getPaddingTop() < HEADER_HEIGHT && currentState == STATE_RELEASE_TO_UPDATE) {
					currentState = STATE_PULL_TO_REFRESH;
					arrowImage.clearAnimation();
					arrowImage.startAnimation(reverseRotateAnimation);
					headerTextView.setText(R.string.pull_to_refresh);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			//isDragging = false;
			
			if (!isLoading) {
				if (headerRelativeLayout.getPaddingTop() < HEADER_HEIGHT) {
					// come back
					headerRelativeLayout.setPadding(
							headerRelativeLayout.getPaddingLeft(), -1
									* HEADER_HEIGHT, 0,
							headerRelativeLayout.getPaddingBottom());
				} else {
					// come to HEADER_HEIGHT and start the trigger
					headerRelativeLayout.setPadding(
							headerRelativeLayout.getPaddingLeft(), HEADER_TOP, 0,
							headerRelativeLayout.getPaddingBottom());
					headerTextView.setText(R.string.loading);
					progressBar.setVisibility(View.VISIBLE);
					arrowImage.clearAnimation();
					arrowImage.setVisibility(View.GONE);
					
					//START LOADING
					isLoading = true;
					if (refreshDelegate != null) {
						refreshDelegate.startFresh();
					}
				}
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void init(Context context) {
		headerRelativeLayout = (LinearLayout) inflate(context,
				R.layout.refresh_header_view, null);
		arrowImage = (ImageView) headerRelativeLayout
				.findViewById(R.id.head_arrowImageView);
		progressBar = (ProgressBar) headerRelativeLayout
				.findViewById(R.id.head_progressBar);
		headerTextView = (TextView) headerRelativeLayout
				.findViewById(R.id.head_tipsTextView);
		headerTextView.setText(R.string.pull_to_refresh);
		lastUpdateDateTextView = (TextView) headerRelativeLayout
				.findViewById(R.id.head_lastUpdatedDateTextView);
		lastUpdateDateTextView.setText("");
		headerRelativeLayout.setPadding(headerRelativeLayout.getPaddingLeft(),
				-1 * HEADER_HEIGHT, 0, headerRelativeLayout.getPaddingBottom());
		this.addHeaderView(headerRelativeLayout, null, false);

		footerLayout = (RelativeLayout) inflate(context, R.layout.refresh_footer_view, null);
		footerProgressBar = (ProgressBar)footerLayout.findViewById(R.id.footer_progressBar);
		footerLayout.setOnClickListener(loadMoreClickListener);
		this.addFooterView(footerLayout, null, false);
		isLoadingMore = false;
		
		//isDragging = false;
		currentState = STATE_PULL_TO_REFRESH;
		this.setOnScrollListener(this);

		rotateAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setDuration(250);
		rotateAnimation.setFillAfter(true);

		reverseRotateAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseRotateAnimation.setInterpolator(new LinearInterpolator());
		reverseRotateAnimation.setDuration(1);
		reverseRotateAnimation.setFillAfter(true);
	}
	
	private OnClickListener loadMoreClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!isLoadingMore) {
				isLoadingMore = true;
				footerProgressBar.setVisibility(View.VISIBLE);
				refreshDelegate.startLoadMore();
			}
		}
	};

	public void onRefreshComplete() {
		progressBar.setVisibility(View.GONE);
		arrowImage.setVisibility(View.VISIBLE);
		arrowImage.startAnimation(reverseRotateAnimation);
		
		headerRelativeLayout.setPadding(headerRelativeLayout.getPaddingLeft(),
				-1 * HEADER_HEIGHT, 0, headerRelativeLayout.getPaddingBottom());
		
		SimpleDateFormat format =new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
		String date=format.format(new Date());
		lastUpdateDateTextView.setText("Last Updated: " + date);
				
		isLoading = false;
		//isDragging = false;
	}
	
	public void onRefreshStart() {
		headerRelativeLayout.setPadding(headerRelativeLayout.getPaddingLeft(),
				HEADER_TOP, 0, headerRelativeLayout.getPaddingBottom());
		
		progressBar.setVisibility(View.VISIBLE);
		arrowImage.setVisibility(View.GONE);
		isLoading = true;
		
		if (refreshDelegate != null) {
			refreshDelegate.startFresh();
		}
	}
	
	public void setOnRefresh(Refresher d){
		refreshDelegate = d;
	}
	
	public void onLoadingMoreComplete() {
		footerProgressBar.setVisibility(View.GONE);
		isLoadingMore = false;
	}
	
}
