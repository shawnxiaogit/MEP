package rcp.com.src.brithUtil;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class imageUtil {
	/***
	 * 
	 * ���ݲ�ͬ��·������bitmap;
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap getBitmapTodifferencePath(String path, Context context) {

		// ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ

		if (path.length() < 7) {
			return null;
		}
		String str = path.substring(0, 7);
		// ������һ���ڲ���ַcontent��ͷ
		if ("content".equals(str)) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				//�����ص��ڴ浱�С���
				options.inJustDecodeBounds = true;
			
				BitmapFactory.decodeStream(context.getContentResolver()
						.openInputStream(Uri.parse(path)), null, options);

				int height = options.outHeight;

				if (options.outWidth > 100) {
					// ���ݿ��������ű���
					options.inSampleSize = options.outWidth / 100 + 1 + 1;
					options.outWidth = 100;

					// �������ź�ĸ߶�
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
			// �ⲿ��ַ

			BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 8;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			int height = options.outHeight;

			if (options.outWidth > 100) {
				// ���ݿ��������ű���
				options.inSampleSize = options.outWidth / 100 + 1 + 1;
				options.outWidth = 100;

				// �������ź�ĸ߶�
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
