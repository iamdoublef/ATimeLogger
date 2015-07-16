package doublef.tool.excel.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import doublef.tool.excel.utils.DateSupportUtils;

public class TimerMap {

	private String name;

	private Map<String, TimerMap> childrenMap;

	private Set<TimerEntity> timerEntries;

	private String parentName;

	private long useTime;

	private String percent;

	private boolean isEntity;

	private int level;

	public TimerMap() {
		super();
	}

	public TimerMap(String name, long useTime, String percent) {
		super();
		this.name = name;
		this.useTime = useTime;
		this.percent = percent;
	}

	public TimerMap(String name, Map<String, TimerMap> childrenMap, Set<TimerEntity> aTimeLogger, String parentName,
			long useTime, String percent, boolean isEntity, int level) {
		super();
		this.name = name;
		this.childrenMap = childrenMap;
		this.timerEntries = aTimeLogger;
		this.parentName = parentName;
		this.useTime = useTime;
		this.percent = percent;
		this.isEntity = isEntity;
		this.level = level;
	}

	public void addChildren(TimerMap tm) {
		if (null == childrenMap) {
			childrenMap = new HashMap<String, TimerMap>();
		}
		childrenMap.put(tm.getName(), tm);
	}

	public void removeChildren(TimerEntity atl) {
		if (null == childrenMap) {
			throw new NullPointerException("childrenMap is null");
		}
		childrenMap.remove(atl.getName());
	}

	public Map<String, TimerMap> getChildrenMap() {
		return childrenMap;
	}

	public void setChildrenMap(Map<String, TimerMap> childrenMap) {
		this.childrenMap = childrenMap;
	}

	public Set<TimerEntity> getaTimeLogger() {
		return timerEntries;
	}

	public void setaTimeLogger(Set<TimerEntity> aTimeLogger) {
		this.timerEntries = aTimeLogger;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public boolean isEntity() {
		return isEntity;
	}

	public void setEntity(boolean isEntity) {
		this.isEntity = isEntity;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TimerMap [name=" + name  + ", timerEntries=" + timerEntries
				+ ", parentName=" + parentName + ", useTime=" + DateSupportUtils.formatUseTime(useTime) + ", percent=" + percent + ", isEntity="
				+ isEntity + ", level=" + level + ", \nchildrenMap=" + childrenMap+ "]";
	}

}
