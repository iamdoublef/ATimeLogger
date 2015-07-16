package doublef.tool.excel.entity;

import java.util.Date;

import doublef.tool.excel.utils.DateSupportUtils;

public class TimerEntity implements Comparable<TimerEntity> {

	private String name;
	
	private Date startTime;
	private Date endTime;
	private long useTime;
	private String note;

	public TimerEntity() {
		super();
	}
	
	public TimerEntity(String name, Date startTime, Date endTime, String note) {
		super();
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.useTime = DateSupportUtils.getUseTime(endTime, startTime);
		this.note = note;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "TimerEntity [name=" + name + ", startTime=" + DateSupportUtils.date2str(startTime) + ", endTime=" + DateSupportUtils.date2str(endTime) + ", useTime="
				+ DateSupportUtils.formatUseTime(useTime) + ", note=" + note + "]";
	}


	public int compareTo(TimerEntity o) {
		if (this.startTime.before( o.startTime)) {  
            return 1;  
        } else if (this.startTime.equals( o.startTime)) {  
            return 0;  
        } else {  
            return -1;  
        } 
	}
}
