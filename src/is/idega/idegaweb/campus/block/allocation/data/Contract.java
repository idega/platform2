/*
 * $Id: Contract.java,v 1.15 2004/09/18 17:38:08 aron Exp $
 * Created on 3.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.allocation.data;


import java.sql.Date;
import java.sql.Timestamp;


import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.Apartment;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/09/18 17:38:08 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.15 $
 */
public interface Contract extends IDOEntity {
    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setUserId
     */
    public void setUserId(int id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setUserId
     */
    public void setUserId(Integer id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getUserId
     */
    public Integer getUserId();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getUser
     */
    public User getUser();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setApplicantId
     */
    public void setApplicantId(int id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setApplicantId
     */
    public void setApplicantId(Integer id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getFileId
     */
    public Integer getFileId();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setFileId
     */
    public void setFileId(int id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setFileId
     */
    public void setFileId(Integer id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getApplicantId
     */
    public Integer getApplicantId();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getApplicant
     */
    public Applicant getApplicant();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setApartmentId
     */
    public void setApartmentId(int id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setApartmentId
     */
    public void setApartmentId(Integer id);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getApartmentId
     */
    public Integer getApartmentId();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getApartment
     */
    public Apartment getApartment();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setValidFrom
     */
    public void setValidFrom(Date date);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getValidFrom
     */
    public Date getValidFrom();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setValidTo
     */
    public void setValidTo(Date date);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getValidTo
     */
    public Date getValidTo();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setMovingDate
     */
    public void setMovingDate(Date date);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getMovingDate
     */
    public Date getMovingDate();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusDate
     */
    public void setStatusDate(Date date);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getStatusDate
     */
    public Date getStatusDate();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setDeliverTime
     */
    public void setDeliverTime(Timestamp stamp);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getDeliverTime
     */
    public Timestamp getDeliverTime();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setReturnTime
     */
    public void setReturnTime(Timestamp stamp);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getReturnTime
     */
    public Timestamp getReturnTime();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getResignInfo
     */
    public String getResignInfo();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setResignInfo
     */
    public void setResignInfo(String info);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getIsRented
     */
    public boolean getIsRented();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setIsRented
     */
    public void setIsRented(boolean rented);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setEnded
     */
    public void setEnded();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStarted
     */
    public void setStarted();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStarted
     */
    public void setStarted(Timestamp when);

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatus
     */
    public void setStatus(String status) throws IllegalStateException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#getStatus
     */
    public String getStatus();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusCreated
     */
    public void setStatusCreated();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusEnded
     */
    public void setStatusEnded();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusRejected
     */
    public void setStatusRejected();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusSigned
     */
    public void setStatusSigned();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusTerminated
     */
    public void setStatusTerminated();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusPrinted
     */
    public void setStatusPrinted();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusResigned
     */
    public void setStatusResigned();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusGarbage
     */
    public void setStatusGarbage();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusDenied
     */
    public void setStatusDenied();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusStorage
     */
    public void setStatusStorage();

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#setStatusFinalized
     */
    public void setStatusFinalized();

}
