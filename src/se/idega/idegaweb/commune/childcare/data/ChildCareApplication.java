/*
 * $Id: ChildCareApplication.java,v 1.2 2004/10/07 14:20:15 thomas Exp $
 * Created on 6.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;


import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;

import com.idega.block.contract.data.Contract;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/07 14:20:15 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public interface ChildCareApplication extends IDOEntity, Case {
    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getCaseCodeKey
     */
    public String getCaseCodeKey();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getCaseCodeDescription
     */
    public String getCaseCodeDescription();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getProviderId
     */
    public int getProviderId();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getProvider
     */
    public School getProvider();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getFromDate
     */
    public Date getFromDate();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getChildId
     */
    public int getChildId();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getChild
     */
    public User getChild();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getQueueDate
     */
    public Date getQueueDate();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getMethod
     */
    public int getMethod();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getCareTime
     */
    public int getCareTime();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getChoiceNumber
     */
    public int getChoiceNumber();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getCheckId
     */
    public int getCheckId();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getCheck
     */
    public GrantedCheck getCheck();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getRejectionDate
     */
    public Date getRejectionDate();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getOfferValidUntil
     */
    public Date getOfferValidUntil();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getLastReplyDate
     */
    public Date getLastReplyDate();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getContractId
     */
    public int getContractId();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getContract
     */
    public Contract getContract();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getContractFileId
     */
    public int getContractFileId();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getContractFile
     */
    public ICFile getContractFile();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getPrognosis
     */
    public String getPrognosis();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getPresentation
     */
    public String getPresentation();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getPreSchool
     */
    public String getPreSchool();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getMessage
     */
    public String getMessage();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getQueueOrder
     */
    public int getQueueOrder();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getApplicationStatus
     */
    public char getApplicationStatus();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getHasPriority
     */
    public boolean getHasPriority();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getHasDateSet
     */
    public boolean getHasDateSet();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getHasQueuePriority
     */
    public boolean getHasQueuePriority();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getHasExtraContract
     */
    public boolean getHasExtraContract();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getExtraContractMessage
     */
    public String getExtraContractMessage();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getHasExtraContractOther
     */
    public boolean getHasExtraContractOther();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#getExtraContractMessageOther
     */
    public String getExtraContractMessageOther();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setProviderId
     */
    public void setProviderId(int id);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setProvider
     */
    public void setProvider(School provider);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setFromDate
     */
    public void setFromDate(Date date);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setChildId
     */
    public void setChildId(int id);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setChild
     */
    public void setChild(User child);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setQueueDate
     */
    public void setQueueDate(Date date);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setMethod
     */
    public void setMethod(int method);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setCareTime
     */
    public void setCareTime(int careTime);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setChoiceNumber
     */
    public void setChoiceNumber(int number);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setCheckId
     */
    public void setCheckId(int checkId);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setCheck
     */
    public void setCheck(GrantedCheck check);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setRejectionDate
     */
    public void setRejectionDate(Date date);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setOfferValidUntil
     */
    public void setOfferValidUntil(Date date);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setLastReplyDate
     */
    public void setLastReplyDate(Date date);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setContractId
     */
    public void setContractId(int id);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setContractId
     */
    public void setContractId(Integer id);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setContractFileId
     */
    public void setContractFileId(int id);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setContractFileId
     */
    public void setContractFileId(Integer id);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setContractFile
     */
    public void setContractFile(ICFile contractFile);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setPrognosis
     */
    public void setPrognosis(String prognosis);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setPresentation
     */
    public void setPresentation(String presentation);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setPreSchool
     */
    public void setPreSchool(java.lang.String preSchool);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setMessage
     */
    public void setMessage(String message);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setQueueOrder
     */
    public void setQueueOrder(int order);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setApplicationStatus
     */
    public void setApplicationStatus(char status);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setHasPriority
     */
    public void setHasPriority(boolean hasPriority);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setHasDateSet
     */
    public void setHasDateSet(boolean hasDateSet);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setHasQueuePriority
     */
    public void setHasQueuePriority(boolean hasPriority);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setRejectionDateAsNull
     */
    public void setRejectionDateAsNull(boolean setAsNull);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setHasExtraContract
     */
    public void setHasExtraContract(boolean hasExtraContract);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setExtraContractMessage
     */
    public void setExtraContractMessage(String message);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setHasExtraContractOther
     */
    public void setHasExtraContractOther(boolean hasExtraContractOther);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#setExtraContractMessageOther
     */
    public void setExtraContractMessageOther(String message);

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#isAcceptedByParent
     */
    public boolean isAcceptedByParent();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#isCancelledOrRejectedByParent
     */
    public boolean isCancelledOrRejectedByParent();

    /**
     * @see se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean#isActive
     */
    public boolean isActive();

}
