package is.idega.idegaweb.atvr.supplier.application.data;

import javax.ejb.*;

public interface NewProductApplication extends com.idega.data.IDOEntity
{
 public java.lang.String getCountryOfOrigin();
 public void setApplicationSent(java.sql.Timestamp p0);
 public java.lang.String getStatus();
 public void setSupplierId(int p0);
 public void setDescription2(java.lang.String p0);
 public java.lang.String getProducer();
 public void setStatus(java.lang.String p0);
 public float getCarbonMonoxide();
 public java.lang.String getQuantity();
 public void setCountryOfOrigin(java.lang.String p0);
 public int getProductCategoryId();
 public com.idega.core.user.data.User getSupplier();
 public void setSuppliersProductId(java.lang.String p0);
 public java.lang.String getApplicationType();
 public java.lang.String getBarCode();
 public void setProducer(java.lang.String p0);
 public void setPrice(float p0);
 public void setDescription(java.lang.String p0);
 public void setQuantity(java.lang.String p0);
 public float getPrice(float p0);
 public java.lang.String getWeigth();
 public void setSupplier(com.idega.core.user.data.User p0);
 public java.lang.String getStrength();
 public void setWeigth(java.lang.String p0);
 public is.idega.idegaweb.atvr.supplier.application.data.ProductCategory getProductCategory();
 public java.lang.String getAmount();
 public void setApplicationType(java.lang.String p0);
 public java.lang.String getDescription();
 public void setCarbonMonoxide(float p0);
 public void initializeAttributes();
 public java.lang.String getSuppliersProductId();
 public int getSupplierId();
 public void setAmount(java.lang.String p0);
 public java.lang.String getDescription2();
 public java.sql.Timestamp getApplicationSent();
 public void setStrength(java.lang.String p0);
 public void setProductCategory(is.idega.idegaweb.atvr.supplier.application.data.ProductCategory p0);
 public void setBarCode(java.lang.String p0);
 public void setProductCategoryId(int p0);
}
