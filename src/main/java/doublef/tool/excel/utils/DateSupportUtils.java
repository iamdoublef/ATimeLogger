package doublef.tool.excel.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateSupportUtils {
	
	public final static DateFormat chnFormat = new SimpleDateFormat("yyyy年MM月 dd HH:mm");
	public final static DateFormat yearFormat = new SimpleDateFormat("yyyy年");
	public final static DateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	public final static DateFormat mmFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	
	public static String date2str(Date date){
		return date==null ? null : mmFormat.format(date);
	}
	
	public static String getYearStr(Date date){
		return date==null ? null : yearFormat.format(date);
	}
	
	public static Date str2date(String str){
		try{
			return mmFormat.parse(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date GMT2Local(Date date){
		try{
			DateFormat tempFromat = (DateFormat) mmFormat.clone();
			tempFromat.setTimeZone(TimeZone.getTimeZone("GMT"));
			String sDate = tempFromat.format(date);
			tempFromat.setTimeZone(TimeZone.getDefault());
			return tempFromat.parse(sDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static long getUseTime(Date endTime, Date startTime){
		return endTime.getTime() - startTime.getTime();
	}
	
	public static String formatUseTime(long useTime){
		long hours = useTime/(1000*60*60);
		long minites = (useTime - hours*1000*60*60)/(1000*60);
		return hours + ":" + minites;
	}
	
	/**
	 * 今天的日期
	 * @return
	 */
	public static Date getCurrentDate(){
		return DateSupportUtils.getCurrentDateTime(0,0,0).getTime();
	}
	
	/**
	 * 当月一号
	 * @return
	 */
	public static Date getMonthOfFirstDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1); 
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 上个月的今天
	 * @return
	 */
	public static Date getLastMonthDate() {
		Calendar cal = DateSupportUtils.getCurrentDateTime(0, 0, 0);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
	
	/**
	 * 今天的几点几时几分
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Calendar getCurrentDateTime(Integer hour, Integer minute, Integer second){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour); 
		cal.set(Calendar.MINUTE, minute); 
		cal.set(Calendar.SECOND, second); 
		cal.set(Calendar.MILLISECOND,0);
		return cal;
	}
	
	/**
	 * 几天前/后的日期
	 * @param days
	 * @return
	 */
	public static Date dateAdd(Integer days){
		Calendar cal = DateSupportUtils.getCurrentDateTime(0, 0, 0);
		cal.roll(Calendar.DAY_OF_YEAR, days); 
		return cal.getTime();
	}
	
	/**
	 * 几天前/后的日期
	 * @param days
	 * @return
	 */
	public static Date dateAdd(Date date, Integer days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.roll(Calendar.DAY_OF_YEAR, days); 
		return cal.getTime();
	}
	
	/**
	 * 几天前/后的几点几时几分
	 * @param days
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date datetimeAdd(Integer days, Integer hour, Integer minute, Integer second){
		Calendar cal = DateSupportUtils.getCurrentDateTime(hour, minute, second);
		cal.roll(Calendar.DAY_OF_YEAR, days); 
		return cal.getTime();
	}
	
	/**
	 * 几秒前/后的日期
	 * @param days
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date secondAdd(Integer second){
		Calendar cal = DateSupportUtils.getCurrentDateTime(0, 0, 0);
		cal.roll(Calendar.SECOND, second); 
		return cal.getTime();
	}
	
	/**
	 * 几年后的时间
	 */
	public static Date yearAdd(Integer years ){
		
		//获取当前时间
		Date now=getCurrentDate();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(now);
		
		calendar.add(Calendar.YEAR, years);
	
		return calendar.getTime();
		
	}
	
	
	public static void main(String[] args) {
		System.out.println(new Date(0));
	}
	
	
}
