package com.idega.block.finance.business;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.util.IWTimestamp;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public interface FinanceHandler {

  public String getAccountType();
  public List listOfAttributes();
  //public boolean executeAssessment(int iCategoryId,int iTariffGroupId,String assessmentName,int iCashierId,int iAccountKeyId,IWTimestamp payDate);
  public boolean executeAssessment(IWApplicationContext iwac,Integer categoryId,Integer tariffGroupId,String assessmentName,Integer cashierId,Integer accountKeyId,IWTimestamp payDate,IWTimestamp start,IWTimestamp end,Integer excessBatchID);
  public boolean rollbackAssessment(IWApplicationContext iwac,Integer assessmentRoundId);
  public Map getAttributeMap();
  public Collection listOfAssessmentTariffPreviews(IWApplicationContext iwac,Integer  tariffGroupId,IWTimestamp start,IWTimestamp end)throws java.rmi.RemoteException;
  public Collection getTariffsForAccountInGroup(Integer accountID,Integer tariffGroupID);
/**
 * Called when the a assessment is published
 * @param iwc
 * @param roundId
 */
public void publishAssessment(IWApplicationContext iwc, Integer roundId);
	
}
