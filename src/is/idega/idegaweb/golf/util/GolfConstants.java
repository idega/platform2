/*
 * $Id: GolfConstants.java,v 1.2 2004/10/28 12:03:53 eiki Exp $
 * Created on Sep 17, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.util;

import com.idega.user.data.GroupTypeConstants;


/**
 * 
 *  Last modified: $Date: 2004/10/28 12:03:53 $ by $Author: eiki $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public interface GolfConstants {
	
	public static final String UNION_TYPE_UNION = "golf_union";
	public static final String GROUP_TYPE_UNION = GroupTypeConstants.GROUP_TYPE_LEAGUE;
	public static final String UNION_TYPE_CLUB = "golf_club";
	public static final String GROUP_TYPE_CLUB = GroupTypeConstants.GROUP_TYPE_CLUB;
	public static final String UNION_TYPE_EXTRA_CLUB = "extra_club";
	
	public static final String UNION_TYPE_NONE = "none";
	
	public static final String META_PREFIX = "golf_";
	public static final String SUB_CLUBS_META_DATA_KEY = META_PREFIX+"sub_clubs";
	public static final String MAIN_CLUB_META_DATA_KEY = META_PREFIX+"main_club";
	public static final String HANDICAP_META_DATA_KEY = META_PREFIX+"handicap";

}
