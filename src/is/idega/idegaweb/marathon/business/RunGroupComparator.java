/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import java.util.Comparator;

import com.idega.util.Counter;


/**
 * @author laddi
 */
public class RunGroupComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		RunGroup r0 = (RunGroup) arg0;
		RunGroup r1 = (RunGroup) arg1;
		
		Counter counter1 = r0.getCounter();
		Counter counter2 = r1.getCounter();
		
		return counter1.compareTo(counter2);
	}

}
