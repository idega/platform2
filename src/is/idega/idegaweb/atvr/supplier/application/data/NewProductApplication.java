package is.idega.idegaweb.atvr.supplier.application.data;


public interface NewProductApplication extends com.idega.data.IDOEntity
{
 public java.lang.String getAmount();
 public java.sql.Timestamp getApplicationSent();
 public java.lang.String getApplicationType();
 public java.lang.String getBarCode();
 public float getCarbonMonoxide();
 public java.lang.String getCountryOfOrigin();
 public java.lang.String getDescription();
 public java.lang.String getDescription2();
 public float getPrice();
 public java.lang.String getProducer();
 public is.idega.idegaweb.atvr.supplier.application.data.ProductCategory getProductCategory();
 public int getProductCategoryId();
 public java.lang.String getQuantity();
 public java.lang.String getStatus();
 public java.lang.String getStrength();
 public com.idega.core.user.data.User getSupplier();
 public int getSupplierId();
 public java.lang.String getSuppliersProductId();
 public java.lang.String getWeigth();
 public void initializeAttributes();
 public void setAmount(java.lang.String p0);
 public void setApplicationSent(java.sql.Timestamp p0);
 public void setApplicationType(java.lang.String p0);
 public void setBarCode(java.lang.String p0);
 public void setCarbonMonoxide(float p0);
 public void setCountryOfOrigin(java.lang.String p0);
 public void setDescription(java.lang.String p0);
 public void setDescription2(java.lang.String p0);
 public void setPrice(float p0);
 public void setProducer(java.lang.String p0);
 public void setProductCategory(is.idega.idegaweb.atvr.supplier.application.data.ProductCategory p0);
 public void setProductCategoryId(int p0);
 public void setQuantity(java.lang.String p0);
 public void setStatus(java.lang.String p0);
 public void setStrength(java.lang.String p0);
 public void setSupplier(com.idega.core.user.data.User p0);
 public void setSupplierId(int p0);
 public void setSuppliersProductId(java.lang.String p0);
 public void setWeigth(java.lang.String p0);
}
