package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.GenericEntity;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.JoinCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

/**
 * @author gimmi
 */
public class RoomTypeBMPBean extends GenericEntity implements RoomType {

	public static String COLUMN_NAME = "TYPE_NAME";
	private static String COLUMN_VALID = "IS_VALID";

	public RoomTypeBMPBean() {
	  super();
	}

	public String getEntityName() {
		return "TB_ACC_ROOM_TYPE";
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", true, true, String.class);
		addAttribute(COLUMN_VALID, "valid", true, true, Boolean.class);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);	
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);	
	}
	
	public boolean getIsValid() {
		return getBooleanColumnValue(COLUMN_VALID);	
	}
	
	public void setIsValid(boolean isValid) {
		setColumn(COLUMN_VALID, isValid);	
	}
	
	public Collection ejbFindAll() throws FinderException {
		return this.idoFindAllIDsByColumnBySQL(COLUMN_VALID, "Y");	
	}
	
	public Collection ejbFindAllUsedBySuppliers(Collection suppliers) throws IDOCompositePrimaryKeyException, IDORelationshipException, FinderException {
		Table roomType = new Table(this);
		Table hotel = new Table(Hotel.class);
		Table product = new Table(Product.class);
		Table supplier = new Table(Supplier.class);
	
		Column prodCol = new Column(product, product.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column hotelCol = new Column(hotel, hotel.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column suppCol = new Column(supplier, supplier.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column valid = new Column(roomType, COLUMN_VALID);
		JoinCriteria jc = new JoinCriteria(prodCol, hotelCol);
		Column pkCol = new Column(roomType, getIDColumnName());
		pkCol.setAsDistinct();
		
		SelectQuery query = new SelectQuery(roomType);
		query.addColumn(pkCol);
		
		query.addJoin(roomType, hotel);
		query.addCriteria(jc);
		query.addJoin(product, supplier);
		query.addCriteria(new MatchCriteria(valid, MatchCriteria.EQUALS, true));
		query.addCriteria(new InCriteria(suppCol, suppliers));
		
		return idoFindPKsByQuery(query);
	}

}
