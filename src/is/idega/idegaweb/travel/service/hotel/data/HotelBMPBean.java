package is.idega.idegaweb.travel.service.hotel.data;

import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceBMPBean;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.ProductBMPBean;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierBMPBean;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.EntityControl;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.util.IWTimestamp;


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
    addAttribute(getNumberOfUnitsColumnName(), "Fj�ldi eininga", true, true, Integer.class);
    addAttribute(getColumnNameMaxPerUnit(), "Fjoldi a einingu", true, true, Integer.class);
    addAttribute(getColumnNameRoomTypeId(), "room type", true, true, Integer.class, "one-to-one", RoomType.class);
    addAttribute(getColumnNameRating(), "rating", true, true, Float.class);
    addManyToManyRelationShip(RoomType.class,  "TB_ACC_TB_ACC_ROOM_TYPE");
    addManyToManyRelationShip(HotelType.class, "TB_ACC_TB_ACC_ACC_TYPE");
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
  
  public Collection getRoomTypes() throws IDORelationshipException {
  	Collection coll = this.idoGetRelatedEntities(RoomType.class);
  	if (coll == null || coll.isEmpty()) {
  		int tmp = getIntColumnValue(getColumnNameRoomTypeId());
  		log("HotelBMPBean : backwards compatability fix for roomTypes");
  		if (tmp > 0) {
  			addRoomTypeId(tmp);
  			coll = this.idoGetRelatedEntities(RoomType.class);
  		}
  	}
  	return coll;
  }
  
  public void setRoomTypeIds(int[] roomTypeIds) throws IDORemoveRelationshipException, IDOAddRelationshipException {
  	this.idoRemoveFrom(RoomType.class);
  	if (roomTypeIds != null && roomTypeIds.length > 0) {
  		for (int i = 0; i < roomTypeIds.length; i++) {
  			addRoomTypeId(roomTypeIds[i]);
  		}
  	}
  }
    
  public void setHotelTypeIds(int[] hotelTypeIds) throws IDOAddRelationshipException, IDORemoveRelationshipException {
  	this.idoRemoveFrom(HotelType.class);
  	if (hotelTypeIds != null && hotelTypeIds.length > 0) {
  		for (int i = 0; i < hotelTypeIds.length; i++) {
  			addHotelTypeId(new Integer(hotelTypeIds[i]));
  		}
  	}
  }
  
  public void addHotelTypeId(Object primaryKey) throws IDOAddRelationshipException {
  	this.idoAddTo(HotelType.class, primaryKey);
  }
  
  public void addRoomTypeId(int roomTypeId) throws IDOAddRelationshipException {
  	addRoomTypeId(new Integer(roomTypeId));
  }
  
  public void addRoomTypeId(Object primaryKey) throws IDOAddRelationshipException {
  	this.idoAddTo(RoomType.class, primaryKey);
  }
  
  public Collection getHotelTypes() throws IDORelationshipException {
  	return  this.idoGetRelatedEntities(HotelType.class);
  }
  
  public void setRating(float rating) {
  	setColumn(getColumnNameRating(), rating);
  }

  public float getRating() {
  	return getFloatColumnValue(getColumnNameRating());
  }
  
  public static String getHotelTableName() {return "TB_ACCOMOTATION";}
  public static String getNumberOfUnitsColumnName() {return "NUMBER_OF_UNITS";}
  public static String getColumnNameMaxPerUnit() {return "MAX_PER_UNIT";}
  public static String getColumnNameRoomTypeId() {return "ROOM_TYPE_ID";}
  public static String getColumnNameRating() {return "RATING";}
  public void setPrimaryKey(Object object) {
    super.setPrimaryKey(object);
  }

  /**
   * Used only for the wait period, will be removed later
   */
	public Collection ejbFind(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Collection postalCodes, Object[] supplierId, String supplierName) throws FinderException {
		return ejbFind(fromStamp, toStamp, roomTypeId, new Object[]{}, postalCodes, supplierId, -1, -1, supplierName);
	}  
  
	public Collection ejbFind(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] hotelTypeId, Collection postalCodes, Object[] supplierId, float minRating, float maxRating, String supplierName) throws FinderException {
		
		boolean postalCode = (postalCodes != null && !postalCodes.isEmpty()); 
		boolean timeframe = (fromStamp != null && toStamp != null);
		boolean roomType = (roomTypeId != null && roomTypeId.length > 0);
		boolean hotelType = ( hotelTypeId != null && hotelTypeId.length > 0);
		boolean supplier = (supplierId != null && supplierId.length > 0);
		boolean name = (supplierName != null && !supplierName.equals(""));

		try {		
			String addressSupplierMiddleTableName = EntityControl.getManyToManyRelationShipTableName(Address.class, Supplier.class);
			String roomTypeHotelMiddleTableName = EntityControl.getManyToManyRelationShipTableName(RoomType.class, Hotel.class);
			String hotelTypeHotelMiddleTableName = EntityControl.getManyToManyRelationShipTableName(HotelType.class, Hotel.class);
			
			String postalCodeTableName = IDOLookup.getEntityDefinitionForClass(PostalCode.class).getSQLTableName();//  PostalCodeBMPBean.getEntityName();
			String addressTableName = IDOLookup.getEntityDefinitionForClass(Address.class).getSQLTableName();
			String serviceTableName = ServiceBMPBean.getServiceTableName();
			String productTableName = ProductBMPBean.getProductEntityName();
			String supplierTableName = SupplierBMPBean.getSupplierTableName();
			String roomTypeTableName = IDOLookup.getEntityDefinitionForClass(RoomType.class).getSQLTableName();
			String hotelTypeTableName = IDOLookup.getEntityDefinitionForClass(HotelType.class).getSQLTableName();
	
			String postalCodeTableIDColumnName = postalCodeTableName+"_id";
			String addressTableIDColumnName = addressTableName+"_id";
			String serviceTableIDColumnName = serviceTableName+"_id";
			String productTableIDColumnName = productTableName+"_id";
			String supplierTableIDColumnName = supplierTableName+"_id";
			String roomTypeTableIDColumnName = null;
			String hotelTypeTableIDColumnName = null;
			try {
				roomTypeTableIDColumnName = IDOLookup.getEntityDefinitionForClass(RoomType.class).getPrimaryKeyDefinition().getField().getSQLFieldName();
				hotelTypeTableIDColumnName = IDOLookup.getEntityDefinitionForClass(HotelType.class).getPrimaryKeyDefinition().getField().getSQLFieldName();
			} catch (IDOCompositePrimaryKeyException e1) {
				roomTypeTableIDColumnName = roomTypeTableName+"_id";
				hotelTypeTableIDColumnName = hotelTypeTableName+"_id";
			}
			
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct h.* from ").append(getHotelTableName()).append(" h, ")
			.append(serviceTableName).append(" s, ")
			.append(productTableName).append(" p");
			
			if (postalCode || supplier) {
				sql.append(", ").append(supplierTableName).append(" su");
			}	
			if (roomType) {
				sql.append(", ").append(roomTypeHotelMiddleTableName).append(" rth");
			}
			if (hotelType) {
				sql.append(", ").append(hotelTypeHotelMiddleTableName).append(" hth");
			}

			if (postalCode) {
				sql.append(", ").append(addressSupplierMiddleTableName).append(" asm, ")
				.append(addressTableName).append(" a, ")
				.append(postalCodeTableName).append(" pc ");
			}
			
			sql.append(" where ")
			.append(" h.").append(getIDColumnName()).append(" = s.").append(serviceTableIDColumnName)
			.append(" AND s.").append(serviceTableIDColumnName).append(" = p.").append(productTableIDColumnName)
			.append(" AND p.").append(ProductBMPBean.getColumnNameIsValid()).append(" = 'Y'");
			
			if (supplier) {
				sql.append(" AND su."+supplierTableIDColumnName+"= p."+supplierTableIDColumnName);
				sql.append(" AND su.").append(supplierTableIDColumnName).append(" in (");
				for (int i = 0; i < supplierId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(supplierId[i].toString());
				}
				sql.append(")");
				if(name) {
					sql.append(" AND su.").append(SupplierBMPBean.COLUMN_NAME_NAME_ALL_CAPS ).append(" like ").append("'%" + supplierName.toUpperCase() + "%'");
				}
			}

			if (postalCode) {
				sql.append(" AND asm.").append(supplierTableIDColumnName).append(" = su.").append(supplierTableIDColumnName)
				.append(" AND asm.").append(addressTableIDColumnName).append(" = a.").append(addressTableIDColumnName)
				.append(" AND p.").append(ProductBMPBean.getColumnNameSupplierId()).append(" = su.").append(supplierTableIDColumnName)
				// HARDCODE OF DEATH ... courtesy of AddressBMPBean
				.append(" AND a.postal_code_id = pc.").append(postalCodeTableIDColumnName)
				.append(" AND pc.").append(postalCodeTableIDColumnName).append(" in (");
				Iterator iter = postalCodes.iterator();
				while (iter.hasNext()) {
					sql.append(iter.next().toString());
					if (iter.hasNext()) {
						sql.append(", ");
					}
				}
//				for (int i = 0; i < postalCodeId.length; i++) {
//					if (i != 0) {
//						sql.append(", ");
//					}
//					sql.append(postalCodeId[i]);
//				}
				sql.append(")");
			}
			
			if (roomType) {
				sql.append(" AND h.").append(getIDColumnName()).append("= rth.").append(getIDColumnName());
				sql.append(" AND  rth.").append(roomTypeTableIDColumnName).append(" in (");
				for (int i = 0; i < roomTypeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(roomTypeId[i]);
				}			
				sql.append(") ");
			}

			if (hotelType) {
				sql.append(" AND h.").append(getIDColumnName()).append("= hth.").append(getIDColumnName());
				sql.append(" AND  hth.").append(hotelTypeTableIDColumnName).append(" in (");
				for (int i = 0; i < hotelTypeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(hotelTypeId[i]);
				}			
				sql.append(") ");
				
			}
			
			if (minRating > -1) {
				sql.append(" AND h.").append(getColumnNameRating()).append(" >= ").append(minRating);
			}

			if (maxRating > -1) {
				sql.append(" AND h.").append(getColumnNameRating()).append(" <= ").append(maxRating);
			}
			//sql.append(" order by ").append();
			
//			System.out.println(sql.toString());
			return this.idoFindPKsBySQL(sql.toString());
		}catch (IDOLookupException e) {
			return null;
		}
	}


}
