package id.creatorb.muslimnews.splashscreen;


import id.creatorb.androidrssfeed.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class Splash extends Activity {

	protected boolean _active = true;
    protected int _splashTime = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		StartAnimations();//Menjalankan Method Start Animasi
		
		Thread splashThread = new Thread() {
			//Timer Splash
			public void run() {
				try{
					int waited = 0;
					 while(_active && (waited < _splashTime)) {
	                        sleep(700);
	                        if(_active) {
	                            waited += 700;
	                        }
	                    }
	                } catch(InterruptedException e) {
	                    // do nothing
	                } finally {
	                    finish();
	                    Intent newIntent=new Intent(Splash.this, id.creatorb.muslimnews.MainActivity.class);//pindah Activity Main
	            		startActivityForResult(newIntent,0);
	                }
	            }
	        };
	        splashThread.start();
	    }
	 
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        if (event.getAction() == MotionEvent.ACTION_DOWN) {
	            _active = false;
	        }
	        return true;
	    }
	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}


	//Disini Deklarasi Animasinya(StartAnimation)
	private void StartAnimations() {
		// TODO Auto-generated method stub
		//Animasi untuk Frame Layout mengunakan alpha.xml
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        FrameLayout l=(FrameLayout) findViewById(R.id.FrameLayout1);
        l.clearAnimation();
        l.startAnimation(anim);
        
      //Animasi untuk ProgressBar1 mengunakan alpha.xml
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim1.reset();
        ProgressBar l1=(ProgressBar) findViewById(R.id.progressBar1);
        l1.clearAnimation();
        l1.startAnimation(anim);
	}
	   

}