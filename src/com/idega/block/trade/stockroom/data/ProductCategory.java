package com.idega.block.trade.stockroom.data;

import com.idega.data.IDORemoveRelationshipException;
import java.util.List;
import javax.ejb.*;

public interface ProductCategory extends com.idega.core.data.ICCategory
{
 public void setDefaultValues();
 public void setCategoryType(String type);
 public void removeProducts(List products) throws IDORemoveRelationshipException;

}
