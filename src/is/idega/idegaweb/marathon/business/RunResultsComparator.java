/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Run;

import java.util.Comparator;


/**
 * @author laddi
 */
public class RunResultsComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		Run r0 = (Run) arg0;
		Run r1 = (Run) arg1;
		
		return r0.getRunTime() - r1.getRunTime();
	}

}
