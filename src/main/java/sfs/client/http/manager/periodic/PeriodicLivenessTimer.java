package sfs.client.http.manager.periodic;

import sfs.client.http.SingleplexHTTPClient;
import sfs.client.http.shortconversation.LivenessConversation;
import sfs.entry.HostEntry;

public class PeriodicLivenessTimer extends PeriodicTimer {

	private SingleplexHTTPClient task;

	public PeriodicLivenessTimer() {
		super();
	}

	public SingleplexHTTPClient getTask() {
		return task;
	}

	@Override
	public boolean addPeriodicTask(HostEntry hostEntry) {

		if ( task == null ) {
			task = new SingleplexHTTPClient( hostEntry, new LivenessConversation() );
		}

		return addPeriodicTask( task );
	}

	@Override
	public boolean removePeriodicTask() {
		return removePeriodicTask( task );
	}
}
