package com.idega.block.finance.business;

import java.util.List;
import java.util.Map;
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
  public boolean executeAssessment(int iTariffGroupId,String assessmentName,int iCashierId,idegaTimestamp payDate);
  public Map getAttributeMap();
  public List listOfAssessmentTariffPreviews(int iTariffGroupId);

}