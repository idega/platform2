/*
 * $Id: AdultEducationChoiceReasonHomeImpl.java,v 1.2 2005/05/11 13:14:12 laddi Exp $
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
import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/05/11 13:14:12 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class AdultEducationChoiceReasonHomeImpl extends IDOFactory implements AdultEducationChoiceReasonHome {

	protected Class getEntityInterfaceClass() {
		return AdultEducationChoiceReason.class;
	}

	public AdultEducationChoiceReason create() throws javax.ejb.CreateException {
		return (AdultEducationChoiceReason) super.createIDO();
	}

	public AdultEducationChoiceReason findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (AdultEducationChoiceReason) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationChoiceReasonBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public AdultEducationChoiceReason findByName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AdultEducationChoiceReasonBMPBean) entity).ejbFindByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
