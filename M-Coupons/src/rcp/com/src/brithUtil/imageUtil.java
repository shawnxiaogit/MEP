package rcp.com.src.brithUtil;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class imageUtil {
	/***
	 * 
	 * 根据不同的路径建立bitmap;
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap getBitmapTodifferencePath(String path, Context context) {

		// 图片宽高都为原来的二分之一，即图片为原来的四分之一

		if (path.length() < 7) {
			return null;
		}
		String str = path.substring(0, 7);
		// 代表了一个内部地址content打头
		if ("content".equals(str)) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				//不加载到内存当中、、
				options.inJustDecodeBounds = true;
			
				BitmapFactory.decodeStream(context.getContentResolver()
						.openInputStream(Uri.parse(path)), null, options);

				int height = options.outHeight;

				if (options.outWidth > 100) {
					// 根据宽设置缩放比例
					options.inSampleSize = options.outWidth / 100 + 1 + 1;
					options.outWidth = 100;

					// 计算缩放后的高度
					height = options.outHeight / options.inSampleSize;
					options.outHeight = height;
				}
				options.inJustDecodeBounds = false;
				options.inPurgeable = true;
				options.inInputShareable = true;
				

				return BitmapFactory.decodeStream(context.getContentResolver()
						.openInputStream(Uri.parse(path)), null, options);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// 外部地址

			BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 8;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			int height = options.outHeight;

			if (options.outWidth > 100) {
				// 根据宽设置缩放比例
				options.inSampleSize = options.outWidth / 100 + 1 + 1;
				options.outWidth = 100;

				// 计算缩放后的高度
				height = options.outHeight / options.inSampleSize;
				options.outHeight = height;
			}
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			return BitmapFactory.decodeFile(path, options);

			// public static Bitmap decodeFile(String filepath,final int
			// REQUIRED_SIZE){
			//

		}

		return null;
	}

}
