package org.kotemaru.android.camera2sample;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.util.Size;

public class Camera2Util {
	public static String getCameraId(CameraManager cameraManager, int facing) throws CameraAccessException {
		for (String cameraId : cameraManager.getCameraIdList()) {
			CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
			if (characteristics.get(CameraCharacteristics.LENS_FACING) == facing) {
				return cameraId;
			}
		}
		return null;
	}

	public static ImageReader getMaxSizeImageReader(StreamConfigurationMap map, int imageFormat) throws CameraAccessException {
		Size[] sizes = map.getOutputSizes(imageFormat);
		Size maxSize = sizes[0];
		for (Size size:sizes) {
			if (size.getWidth() > maxSize.getWidth()) {
				maxSize = size;
			}
		}
		ImageReader imageReader = ImageReader.newInstance(
				//maxSize.getWidth(), maxSize.getHeight(), // for landscape.
				maxSize.getHeight(), maxSize.getWidth(), // for portrait.
				imageFormat, /*maxImages*/1);
		return imageReader;
	}

	public static Size getBestPreviewSize(StreamConfigurationMap map, ImageReader imageSize) throws CameraAccessException {
		//float imageAspect = (float) imageSize.getWidth() / imageSize.getHeight(); // for landscape.
		float imageAspect = (float) imageSize.getHeight() / imageSize.getWidth(); // for portrait
		float minDiff = 1000000000000F;
		Size[] previewSizes = map.getOutputSizes(SurfaceTexture.class);
		Size previewSize = previewSizes[0];
		for (Size size : previewSizes) {
			float previewAspect = (float) size.getWidth() / size.getHeight();
			float diff = Math.abs(imageAspect - previewAspect);
			if (diff < minDiff) {
				previewSize = size;
				minDiff = diff;
			}
			if (diff == 0.0F) break;
		}
		return previewSize;
	}
}
