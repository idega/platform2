/*
 * Created on 11.8.2004
 */
package is.idega.idegaweb.marathon.business;

import java.util.Comparator;

import com.idega.user.data.Group;


/**
 * @author laddi
 */
public class RunDistanceComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		Group group1 = (Group) arg0;
		Group group2 = (Group) arg1;
		
		int distance1 = Integer.parseInt(group1.getName().substring(0, group1.getName().indexOf("_")));
		int distance2 = Integer.parseInt(group2.getName().substring(0, group2.getName().indexOf("_")));
		
		if (distance1 < distance2) {
			return -1;
		}
		else if (distance1 > distance2) {
			return 1;
		}
		return 0;
	}

}
