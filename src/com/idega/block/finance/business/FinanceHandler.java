package com.idega.block.finance.business;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.idega.util.idegaTimestamp;
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
  //public boolean executeAssessment(int iCategoryId,int iTariffGroupId,String assessmentName,int iCashierId,int iAccountKeyId,idegaTimestamp payDate);
  public boolean executeAssessment(int iCategoryId,int iTariffGroupId,String assessmentName,int iCashierId,int iAccountKeyId,idegaTimestamp payDate,idegaTimestamp start,idegaTimestamp end);
  public boolean rollbackAssessment(int iAssessmentRoundId);
  public Map getAttributeMap();
  public Collection listOfAssessmentTariffPreviews(int iTariffGroupId,idegaTimestamp start,idegaTimestamp end)throws java.rmi.RemoteException;

}
