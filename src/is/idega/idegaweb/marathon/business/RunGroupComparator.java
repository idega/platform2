/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Run;

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
		
		Counter counter1 = r0.getCounter();
		if (counter1 == null) {
			Counter counter = new Counter();
			int index = 0;
			Iterator iter = map.getCollection(r0).iterator();
			while (iter.hasNext()) {
				Run element = (Run) iter.next();
				if (index < 3) {
					counter.addSeconds(element.getRunTime());
				}
				index++;
			}
			r0.setCounter(counter);
		}

		Counter counter2 = r1.getCounter();
		if (counter2 == null) {
			Counter counter = new Counter();
			int index = 0;
			Iterator iter = map.getCollection(r1).iterator();
			while (iter.hasNext()) {
				Run element = (Run) iter.next();
				if (index < 3) {
					counter.addSeconds(element.getRunTime());
				}
				index++;
			}
			r1.setCounter(counter);
		}
		
		return counter1.compareTo(counter2);
	}

}
