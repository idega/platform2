/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Run;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import com.idega.util.Counter;
import com.idega.util.datastructures.MultivaluedHashMap;


/**
 * @author laddi
 */
public class RunGroupMap extends MultivaluedHashMap {

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		Collection values = getCollection(key);
		boolean containsOne = false;
		if (values == null) {
			values = new TreeSet(new RunResultsComparator());
			containsOne = true;
		}
		values.add(value);
		
		Counter counter = new Counter();
		if (containsOne) {
			int index = 0;
			Iterator iter = values.iterator();
			while (iter.hasNext()) {
				Run element = (Run) iter.next();
				if (index < 3) {
					counter.addSeconds(element.getRunTime());
				}
				index++;
			}
		}
		else {
			counter.addSeconds(((Run) value).getRunTime());
		}
		
		((RunGroup) key).setCounter(counter);
		
		return super.put(key, values);
	}
}