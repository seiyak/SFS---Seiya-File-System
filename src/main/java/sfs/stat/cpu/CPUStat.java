package sfs.stat.cpu;

public class CPUStat {

	private double previousUser;
	private double previousNice;
	private double previousSystem;
	private double previousIdle;
	private double usage;

	public CPUStat() {

	}

	public CPUStat(double previousUser, double previousNice, double previousSystem, double previousIdle) {

		this.previousUser = previousUser;
		this.previousNice = previousNice;
		this.previousSystem = previousSystem;
		this.previousIdle = -previousIdle;
	}

	public double getPreviousUser() {
		return previousUser;
	}

	public void setPreviousUser(double previousUser) {
		this.previousUser = previousUser;
	}

	public double getPreviousNice() {
		return previousNice;
	}

	public void setPreviousNice(double previousNice) {
		this.previousNice = previousNice;
	}

	public double getPreviousSystem() {
		return previousSystem;
	}

	public void setPreviousSystem(double previousSystem) {
		this.previousSystem = previousSystem;
	}

	public double getPreviousIdle() {
		return previousIdle;
	}

	public void setPreviousIdle(double previousIdle) {
		this.previousIdle = previousIdle;
	}

	public double getUsage() {
		return usage;
	}

	public void setUsage(double usage) {
		this.usage = usage;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof CPUStat ) {

			CPUStat stat = (CPUStat) obj;
			return ( previousUser == stat.getPreviousUser() ) && ( previousNice == stat.getPreviousNice() )
					&& ( previousSystem == stat.getPreviousSystem() ) && ( previousIdle == stat.getPreviousIdle() )
					&& usage == stat.getUsage();

		}

		return false;
	}

	@Override
	public String toString() {
		return "{usage=" + usage + ", previous user=" + previousUser + " previous nice=" + previousNice
				+ " previous system=" + previousSystem + " previous idle=" + previousIdle + "}";
	}

}
