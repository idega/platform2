/*
 * $Id: HandicapServiceBean.java,v 1.1 2005/05/19 07:32:43 laddi Exp $
 * Created on May 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.business;

import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/05/19 07:32:43 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class HandicapServiceBean extends IBOServiceBean  implements HandicapService{

	public void updateAllHandicaps() {
		IWTimestamp stamp = new IWTimestamp();
		stamp.addMonths(-1);
		
		updateAllHandicaps(stamp);
	}
	
	public void updateAllHandicaps(IWTimestamp fromDate) {
		try {
			Collection members = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findAll();
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				Member member = (Member) iter.next();
				UpdateHandicap.update(member, null);
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}
}