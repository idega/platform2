package com.idega.block.finance.data;

import java.sql.Date;
import java.sql.Timestamp;


public interface AccountEntry extends com.idega.data.IDOEntity,com.idega.block.finance.data.Entry
{
 public int getAccountId();
 public int getAccountKeyId();
 public int getCashierId();
 public int getEntryGroupId();
 public java.lang.String getEntryType();
 public java.lang.String getFieldNameAccountId();
 public java.lang.String getFieldNameLastUpdated();
 public java.lang.String getFieldNameStatus();
 public java.lang.String getInfo();
 public java.sql.Timestamp getLastUpdated();
 public java.lang.String getName();
 public float getNetto();
 public java.sql.Timestamp getPaymentDate();
 public int getRoundId();
 public java.lang.String getStatus();
 public java.lang.String getTableName();
 public float getTotal();
 public java.lang.String getType();
 public float getVAT();
 public void setAccountId(java.lang.Integer p0);
 public void setAccountId(int p0);
 public void setAccountKeyId(java.lang.Integer p0);
 public void setAccountKeyId(int p0);
 public void setCashierId(java.lang.Integer p0);
 public void setCashierId(int p0);
 public void setEntryGroupId(int p0);
 public void setEntryType(java.lang.String p0);
 public void setInfo(java.lang.String p0);
 public void setLastUpdated(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
 public void setNetto(float p0);
 public void setPaymentDate(java.sql.Timestamp p0);
 public void setPrice(java.lang.Float p0);
 public void setPrice(float p0);
 public void setRoundId(java.lang.Integer p0);
 public void setRoundId(int p0);
 public void setStatus(java.lang.String p0)throws java.lang.IllegalStateException;
 public void setTotal(java.lang.Float p0);
 public void setTotal(float p0);
 public void setVAT(java.lang.Float p0);
 public void setVAT(float p0);
 
 /*added 6/12/2004 - birna*/
 public int getAccountBook();
 public void setAccountBook(int accountBook);
 public Integer getInvoiceNumber();
 public void setInvoiceNumber(Integer invoiceNumber);
 public Date getFinalDueDate();
 public void setFinalDueDate(Timestamp finalDueDate);
 public int getUserId();
 public void setUserId(int userId);
 public Timestamp getDisallowanceDate();
 public void setDisallowanceDate(Timestamp disallowanceDate);
 public int getBatchNumber();
 public void setBatchNumber(int batchNr);
 public Date getDueDate();
 public void setDueDate(Date dueDate);
 public String getPenalIntrestCode();
 public void setPenalIntrestCode(String penalIntrestCode);
 public String getPenalIntrestRule();
 public void setPenalIntrestRule(String penalIntrestRule);
 public double getPenalIntrestProsent();
 public void setPenalIntrestProsent(double penalIntrestProsent);
 public String getPaymentCode();
 public void setPaymentCode(String paymentCode);
 public double getNotificationAndPaymentFee1();
 public void setNotificationAndPaymentFee1(double fee);
 public double getNotificationAndPaymentFee2();
 public void setNotificationAndPaymentFee2(double fee);
 public double getOtherCost();
 public void setOtherCost(double otherCost);
}
