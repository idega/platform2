/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Run;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeSet;

import com.idega.util.Counter;
import com.idega.util.datastructures.MultivaluedHashMap;


/**
 * @author laddi
 */
public class RunGroupMap extends MultivaluedHashMap implements SortedMap {

	/* (non-Javadoc)
	 * @see java.util.SortedMap#comparator()
	 */
	public Comparator comparator() {
		return new RunGroupComparator();
	}
	/* (non-Javadoc)
	 * @see java.util.SortedMap#firstKey()
	 */
	public Object firstKey() {
		Collection keys = keySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			return iter.next();
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see java.util.SortedMap#headMap(java.lang.Object)
	 */
	public SortedMap headMap(Object arg0) {
		SortedMap map = new RunGroupMap();
		Collection keys = keySet();
		Iterator iter = keys.iterator();
		boolean addToMap = true;
		while (iter.hasNext()) {
			Object key = iter.next();
			map.put(key, getCollection(key));

			if (key.equals(arg0)) {
				addToMap = false;
			}
		}
		return map;
	}
	/* (non-Javadoc)
	 * @see java.util.SortedMap#lastKey()
	 */
	public Object lastKey() {
		Collection keys = keySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			if (!iter.hasNext()) {
				return iter.next();
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see java.util.SortedMap#subMap(java.lang.Object, java.lang.Object)
	 */
	public SortedMap subMap(Object arg0, Object arg1) {
		SortedMap map = new RunGroupMap();
		Collection keys = keySet();
		Iterator iter = keys.iterator();
		boolean addToMap = false;
		while (iter.hasNext()) {
			Object key = iter.next();
				
			if (key.equals(arg0)) {
				addToMap = true;
			}
			map.put(key, getCollection(key));
			if (key.equals(arg1)) {
				addToMap = false;
			}
		}
		return map;
	}
	/* (non-Javadoc)
	 * @see java.util.SortedMap#tailMap(java.lang.Object)
	 */
	public SortedMap tailMap(Object arg0) {
		SortedMap map = new RunGroupMap();
		Collection keys = keySet();
		Iterator iter = keys.iterator();
		boolean addToMap = false;
		while (iter.hasNext()) {
			Object key = iter.next();
				
			if (key.equals(arg0)) {
				addToMap = true;
			}
			map.put(key, getCollection(key));
		}
		return map;
	}
	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		Collection values = getCollection(key);
		if (values == null) {
			values = new TreeSet(new RunResultsComparator());
		}
		values.add(value);
		
		Counter counter = new Counter();
		
		int index = 0;
		Iterator iter = values.iterator();
		while (iter.hasNext()) {
			Run element = (Run) iter.next();
			if (index < 3) {
				counter.addSeconds(element.getRunTime());
			}
			index++;
		}
		
		((RunGroup) key).setCounter(counter);
		
		return super.put(key, values);
	}
}