package zhangjingfeng.com.zproject.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class CameraSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	Context context;

	public CameraSurfaceView(Context context) {
		super(context);
		this.context = context;
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("deprecation")
	public void surfaceCreated(SurfaceHolder holder) {

		Log.d("Dennis", "surfaceCreated() is called");

		try {
			// Open the Camera in preview mode
			int CammeraIndex = FindFrontCamera();
			if (CammeraIndex == -1) {
				CammeraIndex = FindFrontCamera();
			}
			try {
				mCamera = Camera.open(CammeraIndex);
			} catch (Exception e) {
				Toast.makeText(context,"请在系统设置中打开本应用摄像头权限",
						Toast.LENGTH_LONG).show();
				return;
			}
			Camera.Parameters myParam = mCamera.getParameters();
			// //查询屏幕的宽和高
			// WindowManager wm =
			// (WindowManager)getSystemService(Context.WINDOW_SERVICE);
			// Display display = wm.getDefaultDisplay();
			// Log.i(tag,
			// "屏幕宽度："+display.getWidth()+" 屏幕高度:"+display.getHeight());

			myParam.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式

			// //查询camera支持的picturesize和previewsize
			// List<Size> pictureSizes = myParam.getSupportedPictureSizes();
			// List<Size> previewSizes = myParam.getSupportedPreviewSizes();
			// for(int i=0; i<pictureSizes.size(); i++){
			// Size size = pictureSizes.get(i);
			// Log.i(tag,
			// "initCamera:摄像头支持的pictureSizes: width = "+size.width+"height = "+size.height);
			// }
			// for(int i=0; i<previewSizes.size(); i++){
			// Size size = previewSizes.get(i);
			// Log.i(tag,
			// "initCamera:摄像头支持的previewSizes: width = "+size.width+"height = "+size.height);
			//
			// }

			setPreviewSize(myParam);
			// setPictureSize(myParam);
			// 设置大小和方向等参数
			int fitsize = 800;
			Size tempSize = null;
			for (Size size : myParam.getSupportedPictureSizes()) {
				if (size.width <= fitsize && size.width >= 300&&size.height<=800&&size.height>=400) {
					tempSize = size;
				}
			}
			if(tempSize==null){
				
				tempSize=myParam.getSupportedPictureSizes().get(myParam.getSupportedPictureSizes().size()/2);
			}
			myParam.setPictureSize(tempSize.width, tempSize.height);
			mCamera.setDisplayOrientation(90);
			mCamera.setParameters(myParam);
			mCamera.startPreview();
			mCamera.setPreviewDisplay(holder);

		} catch (IOException e) {
			Log.d("Dennis", "Error setting camera preview: " + e.getMessage());
		}
	}

	public static int[] getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return new int[] { dm.widthPixels, dm.heightPixels };

	}

	public void setPreviewSize(Camera.Parameters parametes) {
		List<Size> localSizes = parametes.getSupportedPreviewSizes();
		int screenHPixels = getScreenSize(context)[1];
		int screenWPixels = getScreenSize(context)[0];
		Camera.Size biggestSize = null;
		Camera.Size fitSize = null;// 优先选屏幕分辨率
		Camera.Size targetSize = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(大)的size
		Camera.Size targetSiz2 = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(小)的size
		if (localSizes != null) {
			int cameraSizeLength = localSizes.size();
			for (int n = 0; n < cameraSizeLength; n++) {
				Camera.Size size = localSizes.get(n);
				if (biggestSize == null
						|| (size.width >= biggestSize.width && size.height >= biggestSize.height)) {
					biggestSize = size;
				}

				if (size.width == screenHPixels && size.height == screenWPixels) {
					fitSize = size;
				} else if (size.width == screenHPixels
						|| size.height == screenWPixels) {
					if (targetSize == null) {
						targetSize = size;
					} else if (size.width < screenHPixels
							|| size.height < screenWPixels) {
						targetSiz2 = size;
					}
				}
			}

			if (fitSize == null) {
				fitSize = targetSize;
			}

			if (fitSize == null) {
				fitSize = targetSiz2;
			}

			if (fitSize == null) {
				fitSize = biggestSize;
			}
			parametes.setPreviewSize(fitSize.width, fitSize.height);
		}

	}

	/** 输出的照片为最高像素 */
	public static void setPictureSize(Camera.Parameters parametes) {
		List<Size> localSizes = parametes.getSupportedPictureSizes();
		Camera.Size biggestSize = null;
		Camera.Size fitSize = null;// 优先选预览界面的尺寸
		Camera.Size previewSize = parametes.getPreviewSize();
		float previewSizeScale = 0;
		if (previewSize != null) {
			previewSizeScale = previewSize.width / (float) previewSize.height;
		}

		if (localSizes != null) {
			int cameraSizeLength = localSizes.size();
			for (int n = 0; n < cameraSizeLength; n++) {
				Camera.Size size = localSizes.get(n);
				if (biggestSize == null) {
					biggestSize = size;
				} else if (size.width >= biggestSize.width
						&& size.height >= biggestSize.height) {
					biggestSize = size;
				}

				// 选出与预览界面等比的最高分辨率
				if (previewSizeScale > 0 && size.width >= previewSize.width
						&& size.height >= previewSize.height) {
					float sizeScale = size.width / (float) size.height;
					if (sizeScale == previewSizeScale) {
						if (fitSize == null) {
							fitSize = size;
						} else if (size.width >= fitSize.width
								&& size.height >= fitSize.height) {
							fitSize = size;
						}
					}
				}
			}

			// 如果没有选出fitSize, 那么最大的Size就是FitSize
			if (fitSize == null) {
				fitSize = biggestSize;
			}

			parametes.setPictureSize(fitSize.width, fitSize.height);
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private int FindFrontCamera() {
		int cameraCount = 0;
		CameraInfo info = new CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				return camIdx;
			}
		}
		return -1;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		Log.d("Dennis", "surfaceChanged() is called");

		try {
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d("Dennis", "Error starting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		Log.d("Dennis", "surfaceDestroyed() is called");
	}

	public void takePicture(PictureCallback imageCallback) {
		mCamera.takePicture(null, null, imageCallback);
	}
}