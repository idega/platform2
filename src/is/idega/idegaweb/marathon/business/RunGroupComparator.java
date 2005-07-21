/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Participant;

import java.util.Comparator;
import java.util.Iterator;

import com.idega.util.Counter;
import com.idega.util.datastructures.MultivaluedHashMap;


/**
 * @author laddi
 */
public class RunGroupComparator implements Comparator {

	private MultivaluedHashMap map;
	
	public RunGroupComparator(MultivaluedHashMap map) {
		this.map = map;
	}
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		RunGroup r0 = (RunGroup) arg0;
		RunGroup r1 = (RunGroup) arg1;
		
		Counter counter1 = initializeCounter(r0, r0.getCounter());
		Counter counter2 = initializeCounter(r1, r1.getCounter());
		
		return counter1.compareTo(counter2);
	}
	/**
	 * @param r1
	 * @param counter2
	 * @return
	 */
	private Counter initializeCounter(RunGroup r1, Counter counter2) {
		if (counter2 == null) {
			counter2 = new Counter();
			int index = 0;
			Iterator iter = map.getCollection(r1).iterator();
			while (iter.hasNext()) {
				Participant element = (Participant) iter.next();
				if (index < 3) {
					counter2.addSeconds(element.getRunTime());
				}
				else {
					//iter.remove();
				}
				index++;
			}
			r1.setCounter(counter2);
		}
		return counter2;
	}

}
