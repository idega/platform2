/*
 * $Id: ChildCareContract.java,v 1.1 2004/10/14 10:53:12 thomas Exp $
 * Created on 16.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import java.sql.Date;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.EmploymentType;


import com.idega.block.contract.data.Contract;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/14 10:53:12 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface ChildCareContract extends IDOEntity {
    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getCreatedDate
     */
    public Date getCreatedDate();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getValidFromDate
     */
    public Date getValidFromDate();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getTerminatedDate
     */
    public Date getTerminatedDate();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getCareTime
     */
    public int getCareTime();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getChildID
     */
    public int getChildID();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getChild
     */
    public User getChild();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getApplicationID
     */
    public int getApplicationID();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getApplication
     */
    public ChildCareApplication getApplication();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getContractID
     */
    public int getContractID();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getContract
     */
    public Contract getContract();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getContractFileID
     */
    public int getContractFileID();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getContractFile
     */
    public ICFile getContractFile();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getSchoolClassMember
     */
    public SchoolClassMember getSchoolClassMember();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getSchoolClassMemberId
     */
    public int getSchoolClassMemberId();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getEmploymentTypeId
     */
    public int getEmploymentTypeId();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getEmploymentType
     */
    public EmploymentType getEmploymentType();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getInvoiceReceiverID
     */
    public int getInvoiceReceiverID();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#getInvoiceReceiver
     */
    public User getInvoiceReceiver();

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setCreatedDate
     */
    public void setCreatedDate(Date createdDate);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setValidFromDate
     */
    public void setValidFromDate(Date validFromDate);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setTerminatedDate
     */
    public void setTerminatedDate(Date terminatedDate);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setCareTime
     */
    public void setCareTime(int careTime);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setChildID
     */
    public void setChildID(int childID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setChild
     */
    public void setChild(User child);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setApplicationID
     */
    public void setApplicationID(int applicationID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setApplication
     */
    public void setApplication(ChildCareApplication application);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setContractID
     */
    public void setContractID(int contractID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setContract
     */
    public void setContract(Contract contract);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setContractFileID
     */
    public void setContractFileID(int contractFileID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setContractFile
     */
    public void setContractFile(ICFile contractFile);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setSchoolClassMemberID
     */
    public void setSchoolClassMemberID(int schoolClassMemberID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setSchoolClassMember
     */
    public void setSchoolClassMember(SchoolClassMember schoolClassMember);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setTerminationDateAsNull
     */
    public void setTerminationDateAsNull(boolean setAsNull);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setEmploymentType
     */
    public void setEmploymentType(int employmentTypeID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setInvoiceReceiverID
     */
    public void setInvoiceReceiverID(int invoiceReciverID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setInvoiceReceiverID
     */
    public void setInvoiceReceiverID(Integer invoiceReciverID);

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareContractBMPBean#setInvoiceReceiver
     */
    public void setInvoiceReceiver(User invoiceReciver);

}
