package sfs.stat.memory;

public class MemoryStat {

	private double totalMemory;
	private double currentMemory;
	public static final String UNIT = "kB";

	public MemoryStat() {

	}

	public MemoryStat(double currentMemory) {
		this.currentMemory = currentMemory;
	}

	public MemoryStat(double totalMemory, double currentMemory) {
		this.totalMemory = totalMemory;
		this.currentMemory = currentMemory;
	}

	public void setTotalMemory(double totalMemory) {
		this.totalMemory = totalMemory;
	}

	public double getTotalMemory() {
		return totalMemory;
	}

	public void setCurrentMemory(double currentMemory) {
		this.currentMemory = currentMemory;
	}

	public double getCurrentMemory() {
		return currentMemory;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof MemoryStat ) {

			MemoryStat stat = (MemoryStat) obj;
			return ( totalMemory == stat.getTotalMemory() ) && currentMemory == stat.getCurrentMemory();
		}

		return false;
	}

	@Override
	public String toString() {
		return "{total=" + totalMemory + " current memory=" + currentMemory + "}";
	}
}
