package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceRecord extends com.idega.data.IDOEntity,com.idega.data.IDOLegacyEntity
{
 public int getDays();
 public int getInvoiceheader();
 public java.lang.String getNotes();
 public int getOrderId();
 public java.lang.String getPostingDetails();
 public float getSum();
 public java.lang.String getText();
 public int getUserId();
 public void initializeAttributes();
 public void setDays(int p0);
 public void setInvoiceHeader(int p0);
 public void setNotes(java.lang.String p0);
 public void setOrderId(int p0);
 public void setPostingDetails(java.lang.String p0);
 public void setSum(float p0);
 public void setText(java.lang.String p0);
 public void setUserId(int p0);
}
