/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import java.util.Collection;
import java.util.TreeSet;

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
		if (values == null) {
			values = new TreeSet(new RunResultsComparator());
		}
		values.add(value);
		
		return super.put(key, values);
	}
}