/*
 * $Id: ChildCareContract.java,v 1.16 2004/10/14 10:23:41 thomas Exp $
 * Created on 16.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;


import com.idega.block.contract.data.Contract;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/14 10:23:41 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.16 $
 */
public interface ChildCareContract extends IDOEntity {
    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getCreatedDate
     */
    public Date getCreatedDate();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getValidFromDate
     */
    public Date getValidFromDate();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getTerminatedDate
     */
    public Date getTerminatedDate();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getCareTime
     */
    public int getCareTime();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getChildID
     */
    public int getChildID();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getChild
     */
    public User getChild();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getApplicationID
     */
    public int getApplicationID();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getApplication
     */
    public ChildCareApplication getApplication();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getContractID
     */
    public int getContractID();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getContract
     */
    public Contract getContract();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getContractFileID
     */
    public int getContractFileID();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getContractFile
     */
    public ICFile getContractFile();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getSchoolClassMember
     */
    public SchoolClassMember getSchoolClassMember();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getSchoolClassMemberId
     */
    public int getSchoolClassMemberId();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getEmploymentTypeId
     */
    public int getEmploymentTypeId();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getEmploymentType
     */
    public EmploymentType getEmploymentType();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getInvoiceReceiverID
     */
    public int getInvoiceReceiverID();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#getInvoiceReceiver
     */
    public User getInvoiceReceiver();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setCreatedDate
     */
    public void setCreatedDate(Date createdDate);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setValidFromDate
     */
    public void setValidFromDate(Date validFromDate);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setTerminatedDate
     */
    public void setTerminatedDate(Date terminatedDate);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setCareTime
     */
    public void setCareTime(int careTime);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setChildID
     */
    public void setChildID(int childID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setChild
     */
    public void setChild(User child);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setApplicationID
     */
    public void setApplicationID(int applicationID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setApplication
     */
    public void setApplication(ChildCareApplication application);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setContractID
     */
    public void setContractID(int contractID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setContract
     */
    public void setContract(Contract contract);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setContractFileID
     */
    public void setContractFileID(int contractFileID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setContractFile
     */
    public void setContractFile(ICFile contractFile);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setSchoolClassMemberID
     */
    public void setSchoolClassMemberID(int schoolClassMemberID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setSchoolClassMember
     */
    public void setSchoolClassMember(SchoolClassMember schoolClassMember);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setTerminationDateAsNull
     */
    public void setTerminationDateAsNull(boolean setAsNull);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setEmploymentType
     */
    public void setEmploymentType(int employmentTypeID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setInvoiceReceiverID
     */
    public void setInvoiceReceiverID(int invoiceReciverID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setInvoiceReceiverID
     */
    public void setInvoiceReceiverID(Integer invoiceReciverID);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractBMPBean#setInvoiceReceiver
     */
    public void setInvoiceReceiver(User invoiceReciver);

}
