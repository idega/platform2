/*
 * $Id: MusicSchoolReportHomeImpl.java,v 1.2 2005/04/06 09:29:06 laddi Exp $
 * Created on 6.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolReportHomeImpl
 * </p>
 *  Last modified: $Date: 2005/04/06 09:29:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MusicSchoolReportHomeImpl extends IBOHomeImpl implements MusicSchoolReportHome {

	protected Class getBeanInterfaceClass() {
		return MusicSchoolReport.class;
	}

	public MusicSchoolReport create() throws javax.ejb.CreateException {
		return (MusicSchoolReport) super.createIBO();
	}
}
