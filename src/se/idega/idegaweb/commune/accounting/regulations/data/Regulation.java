package se.idega.idegaweb.commune.accounting.regulations.data;


public interface Regulation extends com.idega.data.IDOEntity
{
 public java.lang.Integer getAmount();
 public java.lang.Integer getConditionOrder();
 public se.idega.idegaweb.commune.accounting.regulations.data.ConditionType getConditionType();
 public java.lang.String getLocalizationKey();
 public com.idega.block.school.data.SchoolType getMainActivity();
 public java.lang.String getName();
 public se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType getPaymentFlowType();
 public java.sql.Date getPeriodeFrom();
 public java.sql.Date getPeriodeTo();
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType getRegSpecType();
 public se.idega.idegaweb.commune.accounting.regulations.data.SpecialCalculationType getSpecialCalculation();
 public java.lang.Integer getVATEligible();
 public se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation getVATRegulation();
 public void initializeAttributes();
 public void setAmount(int p0);
 public void setConditionOrder(int p0);
 public void setConditionType(int p0);
 public void setLocalizationKey(java.lang.String p0);
 public void setMainActivity(int p0);
 public void setName(java.lang.String p0);
 public void setPaymentFlowType(int p0);
 public void setPeriodFrom(java.sql.Date p0);
 public void setPeriodTo(java.sql.Date p0);
 public void setRegSpecType(int p0);
 public void setSpecialCalculation(int p0);
 public void setVATEligible(int p0);
 public void setVATRegulation(int p0);
}
