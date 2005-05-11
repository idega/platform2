/*
 * $Id: AdultEducationChoiceReasonHome.java,v 1.2 2005/05/11 13:14:12 laddi Exp $
 * Created on May 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/05/11 13:14:12 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface AdultEducationChoiceReasonHome extends IDOHome {

	public AdultEducationChoiceReason create() throws javax.ejb.CreateException;

	public AdultEducationChoiceReason findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonBMPBean#ejbFindByName
	 */
	public AdultEducationChoiceReason findByName(String name) throws FinderException;
}
