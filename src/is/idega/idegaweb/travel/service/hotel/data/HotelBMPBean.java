package is.idega.idegaweb.travel.service.hotel.data;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.Collection;

import javax.ejb.FinderException;


import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductBMPBean;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierBMPBean;
import com.idega.core.data.Address;
import com.idega.core.data.AddressBMPBean;
import com.idega.core.data.PostalCode;
import com.idega.core.data.PostalCodeBMPBean;
import com.idega.data.*;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.travel.data.*;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelBMPBean extends GenericEntity implements Hotel {

  public HotelBMPBean() {
    super();
  }

  public HotelBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
    addAttribute(getNumberOfUnitsColumnName(), "Fjöldi eininga", true, true, Integer.class);
    addAttribute(getColumnNameMaxPerUnit(), "Fjoldi a einingu", true, true, Integer.class);
    addAttribute(getColumnNameRoomTypeId(), "room type", true, true, Integer.class, "one-to-one", RoomType.class);
  }

  public String getEntityName() {
    return getHotelTableName();
  }

  public int getNumberOfUnits() {
    return getIntColumnValue(getNumberOfUnitsColumnName());
  }

  public void setNumberOfUnits(int units) {
    setColumn(getNumberOfUnitsColumnName(), units);
  }
  
  public int getMaxPerUnit() {
  	return getIntColumnValue(getColumnNameMaxPerUnit());	
  }
  
  public void setMaxPerUnit( int maxPerUnit) {
  	setColumn(getColumnNameMaxPerUnit(), maxPerUnit);
  }
  
  public int getRoomTypeId() {
  	return getIntColumnValue(getColumnNameRoomTypeId());
  }
  
  public void setRoomTypeId(int roomTypeId) {
  	setColumn(getColumnNameRoomTypeId(), roomTypeId);	
  }

  public static String getHotelTableName() {return "TB_ACCOMOTATION";}
  public static String getNumberOfUnitsColumnName() {return "NUMBER_OF_UNITS";}
  public static String getColumnNameMaxPerUnit() {return "MAX_PER_UNIT";}
  public static String getColumnNameRoomTypeId() {return "ROOM_TYPE_ID";}

  public void setPrimaryKey(Object object) {
    super.setPrimaryKey(object);
  }

	public Collection ejbHomeFind(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] postalCodeId) throws FinderException {
		
		boolean postalCode = (postalCodeId != null && postalCodeId.length > 0); 
		boolean timeframe = (fromStamp != null && toStamp != null);
		boolean roomType = (roomTypeId != null && roomTypeId.length > 0);

		try {		
			String addressSupplierMiddleTableName = EntityControl.getManyToManyRelationShipTableName(Address.class, Supplier.class);
	
			String postalCodeTableName = IDOLookup.getEntityDefinitionForClass(PostalCode.class).getSQLTableName();//  PostalCodeBMPBean.getEntityName();
			String addressTableName = IDOLookup.getEntityDefinitionForClass(Address.class).getSQLTableName();
			String serviceTableName = ServiceBMPBean.getServiceTableName();
			String productTableName = ProductBMPBean.getProductEntityName();
			String supplierTableName = SupplierBMPBean.getSupplierTableName();
	
			String postalCodeTableIDColumnName = postalCodeTableName+"_id";
			String addressTableIDColumnName = addressTableName+"_id";
			String serviceTableIDColumnName = serviceTableName+"_id";
			String productTableIDColumnName = productTableName+"_id";
			String supplierTableIDColumnName = supplierTableName+"_id";
			
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct h.* from ").append(getHotelTableName()).append(" h, ")
			.append(serviceTableName).append(" s, ")
			.append(productTableName).append(" p");
			if (postalCode) {
				sql.append(", ").append(addressSupplierMiddleTableName).append(" asm, ")
				.append(addressTableName).append(" a, ")
				.append(supplierTableName).append(" su, ")
				.append(postalCodeTableName).append(" pc ");
			}
			
			sql.append(" where ")
			.append(" h.").append(getIDColumnName()).append(" = s.").append(serviceTableIDColumnName)
			.append(" AND s.").append(serviceTableIDColumnName).append(" = p.").append(productTableIDColumnName);
			
			if (postalCode) {
				sql.append(" AND asm.").append(supplierTableIDColumnName).append(" = su.").append(supplierTableIDColumnName)
				.append(" AND asm.").append(addressTableIDColumnName).append(" = a.").append(addressTableIDColumnName)
				.append(" AND p.").append(ProductBMPBean.getColumnNameSupplierId()).append(" = su.").append(supplierTableIDColumnName)
				// HARDCODE OF DEATH ... courtesy of AddressBMPBean
				.append(" AND a.postal_code_id = pc.").append(postalCodeTableIDColumnName)
				.append(" AND pc.").append(postalCodeTableIDColumnName).append(" in (");
				for (int i = 0; i < postalCodeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(postalCodeId[i]);
				}
				sql.append(")");
			}
			
			if (roomType) {
				sql.append(" AND h.").append(getColumnNameRoomTypeId()).append(" in (");
				for (int i = 0; i < roomTypeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(roomTypeId[i]);
				}			sql.append(")");
			}
			//sql.append(" order by ").append();
			
			
			//System.out.println(sql.toString());
			return this.idoFindPKsBySQL(sql.toString());
		}catch (IDOLookupException e) {
			return null;
		}
	}


}
