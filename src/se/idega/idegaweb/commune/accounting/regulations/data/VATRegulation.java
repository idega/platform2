package se.idega.idegaweb.commune.accounting.regulations.data;


public interface VATRegulation extends com.idega.data.IDOEntity
{
 public java.lang.String getDescription();
 public se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType getPaymentFlowType();
 public java.lang.String getPeriodFrom();
 public java.lang.String getPeriodTo();
 public se.idega.idegaweb.commune.accounting.regulations.data.ProviderType getProviderType();
 public int getVATPercent();
 public void initializeAttributes();
}
