package sfs.usage.cpu;

import sfs.stat.cpu.CPUStat;

public interface CPU {

	public CPUStat[] getCPUUsage();
	public CPUStat[] getFormattedCPUUsage();
}
