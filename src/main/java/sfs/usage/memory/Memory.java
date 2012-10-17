package sfs.usage.memory;

import java.util.Map;

import sfs.stat.memory.MemoryStat;

public interface Memory {

	public MemoryStat getMemoryUsage();
	public Map<String,Double> getMemoryUsageAsMap();
}
