package rcp.com.src.brithUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.widget.Toast;

public class otherUtil {

	/***
	 * 
	 * �õ���ɵ�֧
	 * 
	 * @return
	 */
	public static String[] getYera() {
		String str[] = new String[107];

		int index1 = 1942;
		// int num = year - 1900 + 36;
		for (int i = 0; i < str.length; i++) {
			str[i] = cyclicalm(index1 - 1900 + 36)+"��("+index1+")";
			index1++;
		}
		return str;
	}

	// ====== ���� ���յ�offset ���ظ�֧, 0=����
	public static String cyclicalm(int num) {
		final String[] Gan = new String[] { "��", "��", "��", "��", "��", "��", "��",
				"��", "��", "��" };
		final String[] Zhi = new String[] { "��", "��", "��", "î", "��", "��", "��",
				"δ", "��", "��", "��", "��" };
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/***
	 * ���������ջ��ж�����
	 * 
	 * @param year
	 * @param mouth
	 * @param date
	 * @return
	 */
	public static long getForMyBrithday(String year, String mouth, String date) {
		int month = Integer.parseInt(mouth);
		int date1 = Integer.parseInt(date);
		int year1 = Integer.parseInt(year);
		Calendar calendar = Calendar.getInstance(Locale.US);
		calendar.setTime(new Date());

		long now = calendar.getTimeInMillis();
		if (calendar.get(Calendar.MONTH) + 1 > month) {
			calendar.add(Calendar.YEAR, 1);
		}
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, date1);
		long nextBirthMillionSeconds = calendar.getTimeInMillis();
		long kk = now + nextBirthMillionSeconds;
		Date date2 = new Date();
		date2.setTime(kk);

		long julianDay = (nextBirthMillionSeconds - now)
				/ (1000 * 60 * 60 * 24);
		if (julianDay < 0) {
			if (isGregorianLeapYear(year1)) {
				return julianDay + 366;
			} else {
				return julianDay + 365;
			}
		}
		return julianDay;
	}

	/***
	 * ���ض������ľ�������
	 * 
	 * @param year
	 * @param mouth
	 * @param date
	 * @return
	 */
	public static String getForMyBrithdayToDate(long day) {

		Calendar calendar = Calendar.getInstance(Locale.US);
		calendar.setTime(new Date());

		long now = calendar.getTimeInMillis();

		long nextBirthMillionSeconds = day * 24 * 60 * 60 * 1000;
		long kk = now + nextBirthMillionSeconds;
		Date date2 = new Date();
		date2.setTime(kk);
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, date2.getYear() + 1900);
		ca.set(Calendar.MONTH, date2.getMonth() + 1 - 1);// Ҳ���������֣�0-11,һ��Ϊ0
		ca.set(Calendar.DAY_OF_MONTH, date2.getDay() + 1);
		lunner kkq = new lunner(ca);
		String str = (date2.getYear() + 1900) + "��" + (date2.getMonth() + 1)
				+ "��" + (date2.getDay() + 1) + "��("+kkq.toString()+")";
		return str;
	}

	/***
	 * �����������������ڼ�
	 * 
	 * @param year
	 * @param mouth
	 * @param date
	 * @return
	 */
	public static String getDateIsweekDay(String year, String mouth, String date) {
		int month = Integer.parseInt(mouth);
		int date1 = Integer.parseInt(date);
		int year1 = Integer.parseInt(year);
		Calendar calendar = Calendar.getInstance(Locale.US);
		calendar.set(Calendar.YEAR, year1);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, date1);
		String weekDay = new SimpleDateFormat("E").format(calendar.getTime());
		return weekDay;
	}

	// �ж��Ƿ�������
	public static boolean isGregorianLeapYear(int year) {
		boolean isLeap = false;
		if (year % 4 == 0)
			isLeap = true;
		if (year % 100 == 0)
			isLeap = false;
		if (year % 400 == 0)
			isLeap = true;
		return isLeap;
	}

	/***
	 * 
	 * ͨ����������������
	 * 
	 * @param mouth
	 * @param date
	 * @return
	 */
	public static String getconstellation(int mouth, int date) {
		String s = "ħ��ˮƿ˫��ĵ���ţ˫�Ӿ�зʨ�Ӵ�Ů�����Ы����ħ��";
		int arr[] = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		if (mouth <= 0 || date <= 0) {
			return "";
		}
		int index = 2 * mouth - (date < arr[mouth - 1] ? 2 : 0);
		String str = s.substring(index, 2 + index);
		return str + "��";
	}

	/***
	 * ���ݳ����������������Ф
	 * 
	 * @param year
	 * @return
	 */
	public static String getAnimals(int year) {
		String s = "����ţ������������Ｆ����";
		int index = (year % 12) - 3;

		if (index < 0) {
			return Character.toString(s.charAt(index + 12));
		} else {
			return Character.toString(s.charAt(index));
		}
	}

	/***
	 * 
	 * ���ؽ�������
	 * 
	 * @return
	 */
	public static int getCurYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

}
