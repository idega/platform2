/*
 * $Id: MeetingFeeFormulaHomeImpl.java,v 1.2 2004/12/06 21:30:34 laddi Exp $
 * Created on 5.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.data;


import javax.ejb.FinderException;

import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2004/12/06 21:30:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MeetingFeeFormulaHomeImpl extends IDOFactory implements MeetingFeeFormulaHome {

	protected Class getEntityInterfaceClass() {
		return MeetingFeeFormula.class;
	}

	public MeetingFeeFormula create() throws javax.ejb.CreateException {
		return (MeetingFeeFormula) super.createIDO();
	}

	public MeetingFeeFormula findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (MeetingFeeFormula) super.findByPrimaryKeyIDO(pk);
	}

	public MeetingFeeFormula findLatestFormula() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MeetingFeeFormulaBMPBean) entity).ejbFindLatestFormula();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
