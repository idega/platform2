/*
 * $Id: DefaultPlacementHelper.java,v 1.5 2004/10/14 10:22:53 thomas Exp $
 * Created on 5.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.util.Date;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.idegaweb.IWResourceMessage;

/**
 * 
 *  Last modified: $Date: 2004/10/14 10:22:53 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.5 $
 */
public class DefaultPlacementHelper implements PlacementHelper{

    private ChildCareApplication application;
    private ChildCareContract contract;
    private SchoolClassMember member;
    /**
     * 
     */
    public DefaultPlacementHelper() {
        
    }
    
    public ChildCareApplication getApplication() {
        return application;
    }
    public ChildCareContract getContract() {
        return contract;
    }
    public Integer getCurrentCareTimeHours() {
        if(contract!=null)
            return new Integer(contract.getCareTime());
        return null;
    }
    public Integer getCurrentClassID() {
        if(member!=null)
            return new Integer(member.getSchoolClassId());
        return null;
    }
    public Integer getCurrentSchoolTypeID() {
        if(member!=null)
            return new Integer(member.getSchoolTypeId());
        return null;
    }
    public Integer getCurrentEmploymentID() {
        if(contract!=null)
            return new Integer(contract.getEmploymentTypeId());
        return null;
    }
    public Integer getCurrentProviderID() {
        if(contract!=null)
            return new Integer(application.getProviderId());
        return null;
    }
    public Date getEarliestPlacementDate() {
        if(contract!=null && contract.getTerminatedDate()!=null)
            return contract.getTerminatedDate();
        else
            return application.getFromDate();
    }
    public Date getLatestPlacementDate() {
        return null;
    }
    public Integer getMaximumCareTimeHours() {
        return new Integer(99);
    }
    public boolean hasEarliestPlacementDate() {
        return true;
    }
    public boolean hasLatestPlacementDate() {
        return false;
    }
    public void setApplication(ChildCareApplication app) {
        this.application = app;
    }
    public void setContract(ChildCareContract contract) {
        this.contract = contract;
        if(contract!=null)
            this.member = contract.getSchoolClassMember();
    }

    /* (non-Javadoc)
     * @see se.idega.idegaweb.commune.childcare.business.PlacementHelper#getEarliestPlacementMessage()
     */
    public IWResourceMessage getEarliestPlacementMessage() {
        return new IWResourceMessage("child_care.date_too_early", "Chosen date is too early");
    }

    /* (non-Javadoc)
     * @see se.idega.idegaweb.commune.childcare.business.PlacementHelper#getLatestPlacementMessage()
     */
    public IWResourceMessage getLatestPlacementMessage() {
        return new IWResourceMessage("child_care.date_too_late","Chosen date is too late");
    }
    
    
}
