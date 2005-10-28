/*
 * $Id: AssessmentRoundHomeImpl.java,v 1.3 2005/10/28 11:02:20 palli Exp $
 * Created on Oct 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2005/10/28 11:02:20 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.3 $
 */
public class AssessmentRoundHomeImpl extends IDOFactory implements AssessmentRoundHome {

	protected Class getEntityInterfaceClass() {
		return AssessmentRound.class;
	}

	public AssessmentRound create() throws javax.ejb.CreateException {
		return (AssessmentRound) super.createIDO();
	}

	public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (AssessmentRound) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByClubAndDivision(Group club, Group div) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AssessmentRoundBMPBean) entity).ejbFindAllByClubAndDivision(club, div);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
