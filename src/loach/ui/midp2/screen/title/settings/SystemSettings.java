/**
 * 
 */
package loach.ui.midp2.screen.title.settings;

import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.db.Persistent;

/**
 * LoachM - MatchSettings
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class SystemSettings implements Persistent {
	private static final String
		FRAME_SKIP_DB_LABEL = "system_frameskip",
		THREAD_SLEEP_DB_LABEL = "system_threadsleep"
	;
	
	private int frameskip = 0, threadSleep = 100;
	
	/**
	 * @param frameskip the frameSkip to set
	 */
	public void setFrameskip(final int frameskip) {
		this.frameskip = frameskip;
	}
	
	/**
	 * @return the frameskip
	 */
	public int getFrameskip() {
		return frameskip;
	}
	
	/**
	 * @return the threadSleep
	 */
	public int getThreadSleep() {
		return threadSleep;
	}
	
	/**
	 * @param threadSleep the threadSleep to set
	 */
	public void setThreadSleep(int threadSleep) {
		this.threadSleep = threadSleep;
	}
	
	/**
	 * @param loachDB
	 */
	public void push(final LoachDB loachDB) {
		loachDB.put(FRAME_SKIP_DB_LABEL, String.valueOf(frameskip));
		loachDB.put(THREAD_SLEEP_DB_LABEL, String.valueOf(threadSleep));
	}
	
	/**
	 * @param loachDB
	 */
	public void pull(final LoachDB loachDB) {
		String tmp = loachDB.get(FRAME_SKIP_DB_LABEL);
		if (tmp != null) frameskip = Integer.parseInt(tmp);
		
		tmp = loachDB.get(THREAD_SLEEP_DB_LABEL);
		if (tmp != null) threadSleep = Integer.parseInt(tmp);
	}
}