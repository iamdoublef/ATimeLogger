package doublef.tool.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import doublef.tool.excel.entity.TimerEntity;
import doublef.tool.excel.utils.DateSupportUtils;

/**
 * 时间处理说明：
 * JXL 默认按格林处理日期，需要把日期转换为当地时区处理。
 * @author Administrator
 *
 */
public class ExcelUtil {

	// 统计分类开始标志
	private static String statistics = "Statistics";
	// 统计开始至统计分类偏移行
	private static int statisticsOffset = 3;

	// 实体开始标志
	private static String timeEntriesFlag = "Time Entries";
	// 标志至实体偏移行
	private static int timeEntriesFlagOffset = 3;

	// 分类结束标志
	private static String endFlag = "Other";
	// 分类开始标志
	private static String startFlag = "Total";

	/**
	 * @param inPath
	 *            Excel的接口文档的路径
	 * @return 格式化的交易配置信息
	 */
	public static void loadExcel(String inPath, String outPath) {
		Sheet sheet = null;
		WritableSheet outSheet = null;
		InputStream is = null;
		Workbook wb = null;
		WritableWorkbook outwb = null;
		try {
			is = new FileInputStream(inPath);// 工作簿需要是标准的工作簿
			wb = Workbook.getWorkbook(is); // 得到工作薄，全部工作簿
			outwb = Workbook.createWorkbook(new File(outPath));
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				sheet = wb.getSheet(i);
				outSheet = outwb.createSheet(sheet.getName(), i);
				// 获取所有时间实体，
				Map<String, Set<TimerEntity>> timerMaps = getTimerEntityMap(sheet);
				wirteWordBook(sheet, outSheet, timerMaps);
//				wirteWordBook2Level(sheet, outSheet);
			}
			outwb.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				wb.close();
				is.close();
				outwb.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 获取时间实体
	 * 
	 * @param sheet
	 * @param token
	 *            开始行数
	 * @return
	 */
	private static Map<String, Set<TimerEntity>> getTimerEntityMap(Sheet sheet) {
		int rowCount = sheet.getRows(); // 得到excel的总行数
		// 获取节点项目开始
		int token = 0;
		for (; token < rowCount; token++) {
			String row1Value = getCellString(sheet, 0, token);
			if (timeEntriesFlag.equals(row1Value)) {
				token += timeEntriesFlagOffset;
				break;
			}
		}
		//
		Map<String, Set<TimerEntity>> temMap = new HashMap<String, Set<TimerEntity>>();
		for (; token < rowCount; token++) {
			String name = getCellString(sheet, 0, token);
			if (statistics.equals(name)||"".equals(name)) {
				break;
			} else {
				// 组装实体
				Date startTime = getCellDate(sheet, 1, token);
				Date endTime = getCellDate(sheet, 2, token);
				String note = getCellString(sheet, 4, token);
				TimerEntity te = new TimerEntity(name, startTime, endTime, note);
				Set<TimerEntity> temSet = temMap.get(name);
				if (null == temSet) {
					temSet = new TreeSet<TimerEntity>();
					temMap.put(name, temSet);
				}
				temSet.add(te);
				System.out.println(te);
			}
		}
		return temMap;
	}

	
	
	
	
	/**
	 * 写文件
	 * @param sheet
	 * @param outSheet
	 * @param timerMaps
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private static void wirteWordBook(Sheet sheet, WritableSheet outSheet, Map<String, Set<TimerEntity>> timerMaps)
			throws RowsExceededException, WriteException {
		int rowCount = sheet.getRows(); // 得到excel的总行数
		// 获取节点项目开始
		int index = 0;
		for (; index < rowCount; index++) {
			String name = getCellString(sheet, 0, index);
			if (statistics.equals(name)) {
				index += statisticsOffset;
				break;
			}
		}
		int outRow = 0;
		int outColumn = 0;
		for (; index < rowCount; index++) {
			// 类别处理
			String name = getCellString(sheet, 0, index);
			long useTime = countUseTime(getCellDate(sheet, 1, index));
			
			String percent = getCellString(sheet, 2, index);
			if (startFlag.equals(name)) {// total 处理
				continue;
			} else if (name.startsWith("1") || endFlag.equals(name)) {// 一级处理
				outColumn = 0;
			} else if (name.startsWith("2")) {// 二级处理
				outColumn = 3;
			} else if (name.startsWith("3")) {// 三级处理
				outColumn = 6;
			}
			jxl.write.Label nameLab = new jxl.write.Label(outColumn++, outRow, name);
			jxl.write.Label useTimeLab = new jxl.write.Label(outColumn++, outRow, DateSupportUtils.formatUseTime(useTime));
			jxl.write.Label percentLab = new jxl.write.Label(outColumn++, outRow, percent);
			outSheet.addCell(nameLab);
			outSheet.addCell(useTimeLab);
			outSheet.addCell(percentLab);
			System.out.println("name"+ name +";current column:" + outColumn + ";endUseTime"+ getCellString(sheet, 1, index)+";useTime"+ DateSupportUtils.formatUseTime(useTime));
			outRow++;
			// 实体处理
			if (timerMaps.containsKey(name)) {
				for (TimerEntity te : (Set<TimerEntity>) timerMaps.get(name)) {
					outColumn = 9;
					jxl.write.Label useTime1 = new jxl.write.Label(outColumn++, outRow, DateSupportUtils.formatUseTime(te.getUseTime()));
					jxl.write.Label note = new jxl.write.Label(outColumn++, outRow, te.getNote());
					jxl.write.Label startTime = new jxl.write.Label(outColumn++, outRow, DateSupportUtils.date2str(te
							.getStartTime()));
					jxl.write.Label endTime = new jxl.write.Label(outColumn++, outRow, DateSupportUtils.date2str(te
							.getEndTime()));
					System.out.println("----name"+ name +";current column:" + outColumn + ";useTime"+ DateSupportUtils.formatUseTime(te.getUseTime()));
					outSheet.addCell(useTime1);
					outSheet.addCell(startTime);
					outSheet.addCell(endTime);
					outSheet.addCell(note);
					outRow++;
				}
			}
		}
	}
	

	private static String getCellString(Sheet sheet, int i, int j) {
		String s = "";
		Cell cell = sheet.getCell(i, j);
		try {
			if(cell.getType() == CellType.DATE){
				Date date = getCellDate(sheet, i , j);
				s = DateSupportUtils.date2str(date);
			}else{
				s = cell.getContents().trim();
			}
			
		} catch (Exception e) {
			s = "";
		}
		return s;
	}
	
	/**
	 * jxl默认使用格林时间处理日期，需把该数据转化为当地时间。
	 * @param sheet
	 * @param i
	 * @param j
	 * @return
	 */
	private static Date getCellDate(Sheet sheet, int i, int j) {
		Date date = null;
		Cell cell = sheet.getCell(i, j);
		try {
			if(cell.getType() == CellType.DATE){
				DateCell dc = (DateCell)cell;
				date = 	DateSupportUtils.GMT2Local(dc.getDate());
			}else{
				String sDate = cell.getContents().trim();
				DateSupportUtils.str2date(sDate);
			}
			
		} catch (Exception e) {
		}
		return date;
	}
	
	
	/**
	 * 在读取使用时间区间时，jxl读取小于一天的时间为1899/12/30日开始，大于一天的时间从1900/01/01开始，
	 * 因此在此时做了个适应性处理
	 * @param sheet
	 * @param i
	 * @param j
	 * @return
	 */
	private static long countUseTime(Date endTimeDate) {
		String startDateStr = "1899/12/31 00:00:00";
		endTimeDate = useTimeDateCare(endTimeDate);
		Date startDate = DateSupportUtils.str2date(startDateStr);
		return endTimeDate.getTime() - startDate.getTime();
	}
	
	/**
	 * 在读取使用时间区间时，jxl读取小于一天的时间为1899/12/30日开始，大于一天的时间从1900/01/01开始，
	 * 因此在此时做了个适应性处理,如果是1899年开始的，增加一天，为31日开始
	 * @param sheet
	 * @param i
	 * @param j
	 * @return
	 */
	private static Date useTimeDateCare(Date endTimeDate) {
		if(DateSupportUtils.date2str(endTimeDate).contains("1899")){
			endTimeDate = DateSupportUtils.dateAdd(endTimeDate, 1);
		}
		return endTimeDate;
	}
	
	
	/**
	 * 写文件
	 * @param sheet
	 * @param outSheet
	 * @param timerMaps
	 * @throws RowsExceededException
	 * @throws WriteException
	 
	private static void wirteWordBook2Level(Sheet sheet, WritableSheet outSheet)
			throws RowsExceededException, WriteException {
		int rowCount = sheet.getRows(); // 得到excel的总行数
		// 获取节点项目开始
		int index = 0;
		for (; index < rowCount; index++) {
			String name = getCellString(sheet, 0, index);
			if (statistics.equals(name)) {
				index += statisticsOffset;
				break;
			}
		}
		int outRow = 0;
		int outColumn = 0;
		for (; index < rowCount; index++) {
			// 类别处理
			String name = getCellString(sheet, 0, index);
			long useTime = countUseTime(getCellDate(sheet, 1, index));
			String percent = getCellString(sheet, 2, index);
			if (startFlag.equals(name)) {// total 处理
				continue;
			} else if (name.startsWith("1") || endFlag.equals(name)) {// 一级处理
				outColumn = 0;
			} else if (name.startsWith("2")) {// 二级处理
				outColumn = 3;
			} else if (name.startsWith("3")) {// 三级处理
				outColumn = 6;
			}
			jxl.write.Label nameLab = new jxl.write.Label(outColumn++, outRow, name);
			jxl.write.Label useTimeLab = new jxl.write.Label(outColumn++, outRow, DateSupportUtils.formatUseTime(useTime));
			jxl.write.Label percentLab = new jxl.write.Label(outColumn++, outRow, percent);
			outSheet.addCell(nameLab);
			outSheet.addCell(useTimeLab);
			outSheet.addCell(percentLab);
			outRow++;
		}
	}
*/
	/**
	 * 获取分类
	 * 
	 * @param sheet
	 * @param token
	 *            开始行数
	 * @return
	 
	private static TimerMap getTimerMap(Sheet sheet, int token) {
		int rowCount = sheet.getRows(); // 得到excel的总行数
		// 获取节点项目开始
		for (; token < rowCount; token++) {
			String name = getCellString(sheet, 0, token);
			if (statistics.equals(name)) {
				token += statisticsOffset;
				break;
			}
		}
		// total 处理
		TimerMap totalMap = null;
		TimerMap curTimerMap = null;
		for (; token < rowCount; token++) {
			TimerMap timerMap = null;
			String name = getCellString(sheet, 0, token);
			long useTime = countUseTime(getCellDate(sheet, 1, token));
			String percent = getCellString(sheet, 2, token);
			String nextName = getCellString(sheet, 0, token + 1);
			timerMap = new TimerMap(name, useTime, percent);

			if (startFlag.equals(name)) {// total 处理
				totalMap = timerMap;
				curTimerMap = timerMap;
				continue;
			} else if (name.startsWith("1")) {// 一级处理
				curTimerMap.addChildren(timerMap);
				curTimerMap = timerMap;
				continue;
			} else if (endFlag.equals(nextName)) {// end
				curTimerMap.addChildren(timerMap);
				name = getCellString(sheet, 0, token);
				useTime = countUseTime(getCellDate(sheet, 1, token));
				percent = getCellString(sheet, 2, token);
				timerMap = new TimerMap(name, useTime, percent);// endTimerMap
				totalMap.addChildren(timerMap);
				break;
			} else {
				switch (Integer.parseInt((nextName.substring(0, 1))) - Integer.parseInt((name.substring(0, 1)))) {
				case 0:
					curTimerMap.addChildren(timerMap);
				default:
					totalMap.addChildren(timerMap);
					curTimerMap = timerMap;
				}
			}
		}
		System.out.println(totalMap);
		return null;
	}
*/

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExcelUtil.loadExcel("d:/input.xls", "d:/output.xls");
	}

}
