/*
 * Created on 12.3.2003
 */
package se.idega.idegaweb.commune.childcare.business;

import java.util.Comparator;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;

import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * @author laddi
 */
public class ChildCareApplicationComparator implements Comparator {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		ChildCareApplication application1 = (ChildCareApplication) arg0;
		ChildCareApplication application2 = (ChildCareApplication) arg1;
		User user1 = application1.getChild();
		User user2 = application2.getChild();
		Age age1 = new Age(user1.getDateOfBirth());
		Age age2 = new Age(user2.getDateOfBirth());

		if (age1.isOlder(age2))
			return -1;
		else if (age2.isOlder(age1))
			return 1;
		return 0;
	}

}
