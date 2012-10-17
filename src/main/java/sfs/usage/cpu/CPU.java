package sfs.usage.cpu;

import java.util.Map;

import sfs.stat.cpu.CPUStat;

public interface CPU {

	public CPUStat[] getCPUUsage();
	public CPUStat[] getFormattedCPUUsage();
	public Map<String,String> getCPUUsageAsMap();
}
