/*
 * $Id: KSIWS.java,v 1.1 2005/07/14 01:00:43 eiki Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.leagues.webservice;

/**
 * A webservice for the icelandic football association (KSI). The method it exposes performes a club member exchange on a player.
 * 
 *  Last modified: $Date: 2005/07/14 01:00:43 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.1 $
 */

public interface KSIWS {
	/**
	 * 
	 * @param personalIdOfPlayer
	 * @param clubNumberToRegisterTo
	 * @param dateOfActivation format is dd-mm-yyyy
	 * @return
	 */
	public String doClubMemberExchange(String personalIdOfPlayer,String clubNumberToRegisterTo,String dateOfActivation);
}
