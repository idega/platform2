/*
 * $Id: PlacementHelper.java,v 1.2 2004/12/02 12:39:08 laddi Exp $
 * Created on 5.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.util.Date;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareContract;

import com.idega.idegaweb.IWResourceMessage;
import com.idega.util.TimePeriod;

/**
 * 
 *  Last modified: $Date: 2004/12/02 12:39:08 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public interface PlacementHelper {

    public void setApplication(ChildCareApplication app);
    public ChildCareApplication getApplication();
    public ChildCareContract getContract();
    public void setContract(ChildCareContract contract);
    public boolean hasEarliestPlacementDate();
    	public Date getEarliestPlacementDate();
    	public boolean hasLatestPlacementDate();
    	public Date getLatestPlacementDate();
    	public Integer getMaximumCareTimeHours();
    	public String getCurrentCareTimeHours();
    	public Integer getCurrentClassID();
    	public Integer getCurrentSchoolTypeID();
    	public Integer getCurrentProviderID();
    	public Integer getCurrentEmploymentID();
    	public IWResourceMessage getEarliestPlacementMessage();
    	public IWResourceMessage getLatestPlacementMessage();
    	
    	public boolean hasDeadlinePassed();
    	public TimePeriod getValidPeriod();
    	
    	public IWResourceMessage getMessageWhenDeadlinePassed();
    	
}