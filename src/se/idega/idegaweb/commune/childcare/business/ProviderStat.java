/*
 * $Id: ProviderStat.java,v 1.3 2005/09/28 14:14:32 dainis Exp $
 * Created on 8.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.util.Date;

/**
 * 
 *  Last modified: $Date: 2005/09/28 14:14:32 $ by $Author: dainis $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.3 $
 */
public class ProviderStat{
    
    private Integer providerID;
    private String providerName;
    private Integer prognosisID;
    private Date lastUpdate;
    private Integer threeMonthsPrognosis;
    private Integer oneYearPrognosis;
    private Integer threeMonthsPriority;
    private Integer oneYearPriority;
    private Integer providerCapacity;
    private Integer queueTotal;
    private Integer vacancies;
    private Boolean queueSortedByBirthdate;
   

    /**
     * @return Returns the lastUpdate.
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }
    /**
     * @param lastUpdate The lastUpdate to set.
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /**
     * @return Returns the oneYearPriority.
     */
    public Integer getOneYearPriority() {
        return oneYearPriority;
    }
    /**
     * @param oneYearPriority The oneYearPriority to set.
     */
    public void setOneYearPriority(Integer oneYearPriority) {
        this.oneYearPriority = oneYearPriority;
    }
    /**
     * @return Returns the oneYearPrognosis.
     */
    public Integer getOneYearPrognosis() {
        return oneYearPrognosis;
    }
    /**
     * @param oneYearPrognosis The oneYearPrognosis to set.
     */
    public void setOneYearPrognosis(Integer oneYearPrognosis) {
        this.oneYearPrognosis = oneYearPrognosis;
    }
    /**
     * @return Returns the prognosisID.
     */
    public Integer getPrognosisID() {
        return prognosisID;
    }
    /**
     * @param prognosisID The prognosisID to set.
     */
    public void setPrognosisID(Integer prognosisID) {
        this.prognosisID = prognosisID;
    }
    
    public boolean hasPrognosis(){
        return this.getPrognosisID()!=null && getPrognosisID().intValue()>0;
    }
    /**
     * @return Returns the providerCapacity.
     */
    public Integer getProviderCapacity() {
        return providerCapacity;
    }
    /**
     * @param providerCapacity The providerCapacity to set.
     */
    public void setProviderCapacity(Integer providerCapacity) {
        this.providerCapacity = providerCapacity;
    }
    /**
     * @return Returns the providerID.
     */
    public Integer getProviderID() {
        return providerID;
    }
    /**
     * @param providerID The providerID to set.
     */
    public void setProviderID(Integer providerID) {
        this.providerID = providerID;
    }
    /**
     * @return Returns the providerName.
     */
    public String getProviderName() {
        return providerName;
    }
    /**
     * @param providerName The providerName to set.
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    /**
     * @return Returns the threeMonthsPriority.
     */
    public Integer getThreeMonthsPriority() {
        return threeMonthsPriority;
    }
    /**
     * @param threeMonthsPriority The threeMonthsPriority to set.
     */
    public void setThreeMonthsPriority(Integer threeMonthsPriority) {
        this.threeMonthsPriority = threeMonthsPriority;
    }
    /**
     * @return Returns the threeMonthsPrognosis.
     */
    public Integer getThreeMonthsPrognosis() {
        return threeMonthsPrognosis;
    }
    /**
     * @param threeMonthsPrognosis The threeMonthsPrognosis to set.
     */
    public void setThreeMonthsPrognosis(Integer threeMonthsPrognosis) {
        this.threeMonthsPrognosis = threeMonthsPrognosis;
    }
    /**
     * @return Returns the queueTotal.
     */
    public Integer getQueueTotal() {
        return queueTotal;
    }
    /**
     * @param queueTotal The queueTotal to set.
     */
    public void setQueueTotal(Integer queueTotal) {
        this.queueTotal = queueTotal;
    }
    
    /**
     * @return Returns the vacancies.
     */
    public Integer getVacancies() {
    	return vacancies;
    }
    
    /**
     * @param vacancies The vacancies to set.
     */
    public void setVacancies(Integer vacancies) {
    	this.vacancies = vacancies;
    }
    
    
    public Boolean getQueueSortedByBirthdate() {
        return queueSortedByBirthdate;
    }
    public void setQueueSortedByBirthdate(Boolean queueSortedByBirthdate) {
        this.queueSortedByBirthdate = queueSortedByBirthdate;
    }
    public void setQueueSortedByBirthdate(String queueSortedByBirthdate) {
        if (queueSortedByBirthdate != null) {
            if (queueSortedByBirthdate.equalsIgnoreCase("Y")) {
                this.queueSortedByBirthdate = Boolean.TRUE;
                return;
            }
        }
        this.queueSortedByBirthdate = Boolean.FALSE;
    }
    
}
