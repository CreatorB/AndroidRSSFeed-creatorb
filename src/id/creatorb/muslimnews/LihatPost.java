package id.creatorb.muslimnews;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import id.creatorb.androidrssfeed.R;

public class LihatPost extends Activity {
	
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.postview);
		Bundle bundle = this.getIntent().getExtras();
		String postContent = bundle.getString("content");
		
		webView = (WebView)this.findViewById(R.id.webview);
		webView.loadData(postContent, "text/html; charset=utf-8","utf-8");
	}
}
