package casper;

public class CPUTimer {

	static final private long NOTSTARTED = -1;
	private long time = 0;
	private long start = NOTSTARTED;
	
/**
 * Returns the measured time.
 */
public long get() {
	return time;
}
/**
 * Resets the timer.
 */
public void reset () {
	time = 0;
	start = NOTSTARTED;
}
/**
 * Sets the timer.
 */
public void set (long value) {
	time = value;
}
/**
 * Starts the timer.
 */
public void start () {
	if (start == NOTSTARTED)
		start = System.nanoTime();
}
/**
 * Stops the timer.
 */
public void stop () {
	if (start != NOTSTARTED)
		time += System.nanoTime()-start;
	start = NOTSTARTED;
	}
}
