/*
 * $Id: NoLeagueClubCollectionGroup.java,v 1.1 2005/05/31 11:45:34 eiki Exp $
 * Created on May 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business;

import javax.ejb.FinderException;


public class NoLeagueClubCollectionGroup extends FinderException {

	public NoLeagueClubCollectionGroup(String leagueName) {
		super("No club collection group found for league "+leagueName);
	}
}
