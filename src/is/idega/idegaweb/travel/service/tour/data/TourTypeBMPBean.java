package is.idega.idegaweb.travel.service.tour.data;

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
public class TourTypeBMPBean extends GenericEntity implements TourType {

	private static String ENTITY_NAME = "TB_TOUR_TYPE";
	private static String COLUMN_NAME = "TB_TOUR_TYPE_NAME";
	private static String COLUMN_TOUR_CATEGORY = "TB_TOUR_CATEGORY";
	private static String COLUMN_LOCALIZATION_KEY = "LOCALIZATION_KEY";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_NAME, "primKey", true, true, String.class);
		this.addAttribute(COLUMN_LOCALIZATION_KEY, "locKEy", true, true, String.class);
		
		this.addOneToOneRelationship(COLUMN_TOUR_CATEGORY, TourCategory.class);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}
	
	public void setLocalizationKey(String key) {
		setColumn(COLUMN_LOCALIZATION_KEY, key);
	}
	
	public String getTourCategory() {
		return getStringColumnValue(COLUMN_TOUR_CATEGORY);
	}
	
	public void setTourCategory(String category) {
		setColumn(COLUMN_TOUR_CATEGORY, category);
	}
	
	public Collection ejbFindByCategory(String category) throws FinderException {
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_TOUR_CATEGORY, category, COLUMN_NAME);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return idoFindAllIDsBySQL();
	}
	
	public Collection ejbFindByCategoryUsedBySuppliers(String category, Collection suppliers) throws IDOCompositePrimaryKeyException, IDORelationshipException, FinderException {
		Table tourType = new Table(this);
		Table tour = new Table(Tour.class);
		Table product = new Table(Product.class);
		Table supplier = new Table(Supplier.class);
	
		Column prodCol = new Column(product, product.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column tourCol = new Column(tour, tour.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column suppCol = new Column(supplier, supplier.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column categoryCol = new Column(tourType, COLUMN_TOUR_CATEGORY);
		JoinCriteria jc = new JoinCriteria(prodCol, tourCol);
		Column pkCol = new Column(tourType, getIDColumnName());
		pkCol.setAsDistinct();
		
		SelectQuery query = new SelectQuery(tourType);
		query.addColumn(pkCol);
		
		query.addJoin(tourType, tour);
		query.addCriteria(jc);
		query.addJoin(product, supplier);
		query.addCriteria(new MatchCriteria(categoryCol, MatchCriteria.EQUALS, category));
		query.addCriteria(new InCriteria(suppCol, suppliers));
		query.addOrder(tourType, COLUMN_NAME, true);
		return idoFindPKsByQuery(query);	
	}
	
}
