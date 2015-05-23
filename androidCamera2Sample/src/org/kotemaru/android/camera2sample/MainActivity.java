package org.kotemaru.android.camera2sample;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private AutoFitTextureView mTextureView;
	private ImageView mImageView;
	private Camera2StateMachine mCamera2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextureView = (AutoFitTextureView) findViewById(R.id.TextureView);
		mImageView = (ImageView) findViewById(R.id.ImageView);
		mCamera2 = new Camera2StateMachine();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCamera2.open(this, mTextureView);
	}
	@Override
	protected void onPause() {
		mCamera2.close();
		super.onPause();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mImageView.getVisibility() == View.VISIBLE) {
			mTextureView.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.INVISIBLE);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void onClickShutter(View view) {
		mCamera2.takePicture(new ImageReader.OnImageAvailableListener() {
			@Override
			public void onImageAvailable(ImageReader reader) {
				// 撮れた画像をImageViewに貼り付けて表示。
				final Image image = reader.acquireLatestImage();
				ByteBuffer buffer = image.getPlanes()[0].getBuffer();
				byte[] bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				image.close();

				mImageView.setImageBitmap(bitmap);
				mImageView.setVisibility(View.VISIBLE);
				mTextureView.setVisibility(View.INVISIBLE);
			}
		});
	}

}
