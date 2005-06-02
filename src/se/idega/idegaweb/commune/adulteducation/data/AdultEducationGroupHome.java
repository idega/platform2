/*
 * $Id: AdultEducationGroupHome.java,v 1.1 2005/06/02 11:33:15 laddi Exp $
 * Created on Jun 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPathGroup;


/**
 * Last modified: $Date: 2005/06/02 11:33:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface AdultEducationGroupHome extends SchoolClassHome {

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationGroupBMPBean#ejbFindBySchoolAndSeasonAndStudyPathGroup
	 */
	public Collection findBySchoolAndSeasonAndStudyPathGroup(School school, SchoolSeason season,
			SchoolStudyPathGroup group) throws FinderException;
}
