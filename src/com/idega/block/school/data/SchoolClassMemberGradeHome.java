/*
 * $Id: SchoolClassMemberGradeHome.java,v 1.1 2005/06/20 12:55:48 laddi Exp $
 * Created on Jun 20, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/06/20 12:55:48 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolClassMemberGradeHome extends IDOHome {

	public SchoolClassMemberGrade create() throws javax.ejb.CreateException;

	public SchoolClassMemberGrade findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#ejbFindAllByStudent
	 */
	public Collection findAllByStudent(SchoolClassMember student) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#ejbFindByStudent
	 */
	public SchoolClassMemberGrade findByStudent(SchoolClassMember student) throws FinderException;
}
