/*
 * Created on 12.3.2003
 */
package se.idega.idegaweb.commune.childcare.business;

import java.util.Comparator;

import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;

import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * @author laddi
 */
public class ChildCareQueueComparator implements Comparator {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		ChildCareQueue queue1 = (ChildCareQueue) arg0;
		ChildCareQueue queue2 = (ChildCareQueue) arg1;
		User user1 = queue1.getChild();
		User user2 = queue2.getChild();
		Age age1 = new Age(user1.getDateOfBirth());
		Age age2 = new Age(user2.getDateOfBirth());

		if (age1.isOlder(age2))
			return -1;
		else if (age2.isOlder(age1))
			return 1;
		return 0;
	}

}
