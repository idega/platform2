package is.idega.idegaweb.travel.block.search.data;


public interface ServiceSearchEngine extends com.idega.data.IDOEntity
{
 public void addSupplier(com.idega.block.trade.stockroom.data.Supplier p0)throws com.idega.data.IDOAddRelationshipException;
 public java.lang.String getCode();
 public boolean getIsValid();
 public java.lang.String getName();
 public int getStaffGroupID();
 public java.util.Collection getSuppliers()throws com.idega.data.IDORelationshipException;
 public void initializeAttributes();
 public void removeAllSuppliers()throws com.idega.data.IDORemoveRelationshipException;
 public void removeSupplier(com.idega.block.trade.stockroom.data.Supplier p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setCode(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setStaffGroupID(int p0);
}
