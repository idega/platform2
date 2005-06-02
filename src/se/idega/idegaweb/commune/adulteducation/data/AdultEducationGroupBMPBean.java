/*
 * $Id: AdultEducationGroupBMPBean.java,v 1.1 2005/06/02 11:33:15 laddi Exp $
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
import com.idega.block.school.data.SchoolClassBMPBean;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * Last modified: $Date: 2005/06/02 11:33:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class AdultEducationGroupBMPBean extends SchoolClassBMPBean  implements AdultEducationGroup{

	public Collection ejbFindBySchoolAndSeasonAndStudyPathGroup(School school, SchoolSeason season, SchoolStudyPathGroup group) throws FinderException {
		Table table = new Table(this);
		Table course = new Table(AdultEducationCourse.class);
		Table path = new Table(SchoolStudyPath.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(table, SCHOOL, MatchCriteria.EQUALS, school));
		if (season != null) {
			query.addCriteria(new MatchCriteria(table, SEASON, MatchCriteria.EQUALS, season));
		}
		if (group != null) {
			query.addJoin(table, COLUMN_CODE, course, "course_code");
			try {
				query.addJoin(course, path);
			}
			catch (IDORelationshipException ire) {
				throw new FinderException(ire.getMessage());
			}
			query.addCriteria(new MatchCriteria(path, "STUDY_PATH_GROUP_ID", MatchCriteria.EQUALS, group));
		}
		query.addCriteria(new OR(new MatchCriteria(table, VALID, MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(VALID), true)));
		query.addOrder(table, NAME, true);
		
		return idoFindPKsByQuery(query);
	}
}