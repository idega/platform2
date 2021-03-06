package com.idega.block.category.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;

import com.idega.core.component.business.ICObjectBusiness;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.EntityControl;
import com.idega.data.EntityFinder;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.MetaDataCapable;
import com.idega.data.SimpleQuerier;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

public class ICCategoryBMPBean extends com.idega.data.TreeableEntityBMPBean implements ICCategory, Category, MetaDataCapable {
	
	
private static final String IC_CATEGORY_IC_OBJECT_INSTANCE_MIDDLE_TABLE_NAME = "IC_CATEGORY_IC_OBJECT_INSTANCE";
    private String IC_CATEGORY_COLUMN_NAME = ICCategoryBMPBean.getEntityTableName() + "_ID";
	private static String IC_OBJECT_INSTANCE_COLUMN_NAME = "IC_OBJECT_INSTANCE_ID";
	private static String TREE_ORDER_COLUMN_NAME = "TREE_ORDER";
	
	public ICCategoryBMPBean() {
		super();
	}
	
	public ICCategoryBMPBean(int id) throws SQLException {
		super(id);
	}
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnBusinessId(), "Business id", true, true, Integer.class, MANY_TO_ONE, com.idega.core.data.ICBusiness.class);
		addAttribute(getColumnParentId(), "Parent id", true, true, Integer.class, MANY_TO_ONE, ICCategory.class);
		addAttribute(getColumnLocaleId(), "Locale id", true, true, Integer.class, MANY_TO_ONE, ICLocale.class);
		addAttribute(getColumnOwnerGroup(), "Owner group", true, true, Integer.class);
		addAttribute(getColumnName(), "Name", true, true, String.class);
		addAttribute(getColumnDescription(), "Description", true, true, String.class);
		addAttribute(getColumnType(), "Type", true, true, String.class, 50);
		addAttribute(getColumnCreated(), "Created", true, true, java.sql.Timestamp.class);
		addAttribute(getColumnInvalidationDate(), "Invalidation date", true, true, java.sql.Timestamp.class);
		addAttribute(getColumnValid(), "Valid", true, true, Boolean.class);
		addManyToManyRelationShip(ICObjectInstance.class,IC_CATEGORY_IC_OBJECT_INSTANCE_MIDDLE_TABLE_NAME);
		// Gimmi 8.04.2003
		addManyToManyRelationShip(com.idega.core.file.data.ICFile.class);
		addMetaDataRelationship();
	}

	public static String getColumnInvalidationDate() {
		return "INVALIDATED";
	}

	public void insertStartData() {
		//String table = com.idega.data.EntityControl.getManyToManyRelationShipTableName(ICCategory.class, ICObjectInstance.class);
		String table = IC_CATEGORY_IC_OBJECT_INSTANCE_MIDDLE_TABLE_NAME;
		String sql = "ALTER TABLE " + table + " ADD " + TREE_ORDER_COLUMN_NAME + " INTEGER";
		System.out.println(sql);
		try {
			idoExecuteTableUpdate(sql);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static String getEntityTableName() {
		return "IC_CATEGORY";
	}
	public static String getColumnBusinessId() {
		return "IC_BUSINESS_ID";
	}
	public static String getColumnLocaleId() {
		return "IC_LOCALE_ID";
	}
	public static String getColumnParentId() {
		return "IC_PARENT_ID";
	}
	public static String getColumnName() {
		return "NAME";
	}
	public static String getColumnDescription() {
		return "DESCRIPTION";
	}
	public static String getColumnType() {
		return "CAT_TYPE";
	}
	public static String getColumnCreated() {
		return "CREATED";
	}
	public static String getColumnValid() {
		return "VALID";
	}
	public static String getColumnOwnerGroup() {
		return "OWNER_GROUP";
	}
	public String getEntityName() {
		return getEntityTableName();
	}
	public int getBusinessId() {
		return getIntColumnValue(getColumnBusinessId());
	}
	public void setBusinessId(int id) {
		setColumn(getColumnBusinessId(), id);
	}
	public int getParentId() {
		return getIntColumnValue(getColumnParentId());
	}
	public void setParentId(int id) {
		setColumn(getColumnParentId(), id);
	}
	public int getLocaleId() {
		return getIntColumnValue(getColumnLocaleId());
	}
	public void setLocaleId(int id) {
		setColumn(getColumnLocaleId(), id);
	}
	public String getName() {
		return getStringColumnValue(getColumnName());
	}
	public void setName(String name) {
		setColumn(getColumnName(), name);
	}
	public String getDescription() {
		return getStringColumnValue(getColumnDescription());
	}
	public void setDescription(String description) {
		setColumn(getColumnDescription(), description);
	}
	public boolean getValid() {
		return getBooleanColumnValue(getColumnValid());
	}
	public void setValid(boolean valid) {
		setColumn(getColumnValid(), valid);
	}
	public java.sql.Timestamp getCreated() {
		return (java.sql.Timestamp) getColumnValue(getColumnCreated());
	}
	public void setCreated(java.sql.Timestamp created) {
		setColumn(getColumnCreated(), created);
	}

	public java.sql.Timestamp getInvalidationDate() {
		return (java.sql.Timestamp) getColumnValue(getColumnInvalidationDate());
	}
	public void setInvalidationDate(java.sql.Timestamp date) {
		setColumn(getColumnInvalidationDate(), date);
	}

	public String getType() {
		return getStringColumnValue(getColumnType());
	}
	public void setType(String type) {
		setColumn(getColumnType(), type);
	}
	public String getCategoryType() {
		return "no_type";
	}
	public void setDefaultValues() {
		setType(getCategoryType());
	}

	public String getName(Locale locale) {
		try {
			return getCategoryTranslation(locale).getName();
		}
		catch (RemoteException e) {

		}
		return getName();
	}

	public String getDescription(Locale locale) {
		try {
			return getCategoryTranslation(locale).getDescription();
		}
		catch (RemoteException e) {

		}
		return getDescription();

	}

	public List ejbHomeGetListOfCategoryForObjectInstance(ICObjectInstance obj, boolean order) throws FinderException {
		StringBuffer sql = new StringBuffer();
		sql.append("Select c.* from ").append(EntityControl.getManyToManyRelationShipTableName(ICCategory.class, ICObjectInstance.class)).append(" mt, ").append(ICCategoryBMPBean.getEntityTableName()).append(" c");
		sql.append(" where mt.").append(IC_OBJECT_INSTANCE_COLUMN_NAME).append(" = ").append(obj.getID());
		sql.append(" and mt.").append(this.IC_CATEGORY_COLUMN_NAME).append(" = c.").append(this.IC_CATEGORY_COLUMN_NAME);
		if (order) {
			sql.append(" order by mt.").append(TREE_ORDER_COLUMN_NAME); //.append(" desc");
		} 
        
		return EntityFinder.getInstance().findAll(ICCategory.class, sql.toString());
	}
    
    /**
     * basically returns the same as ejbHomeGetListOfCategoryForObjectInstance() but 
     * does include only root categories in the list 
     * 
     * @param obj
     * @param order
     * @return
     * @throws FinderException
     */
    public List ejbHomeGetListOfRootCategoryForObjectInstance(ICObjectInstance obj, boolean order) throws FinderException {
        StringBuffer sql = new StringBuffer();
        /*
        select cat.* 
        from IC_CATEGORY cat 
        
            LEFT JOIN IC_CATEGORY_tree tree 
            ON cat.IC_CATEGORY_ID= tree.child_IC_CATEGORY_ID 
            
            LEFT JOIN IC_CATEGORY_IC_OBJECT_INSTANCE mt 
            ON cat.IC_CATEGORY_ID= mt.IC_CATEGORY_ID 
        where   
            tree.child_ic_category_id is null    
            and mt.IC_OBJECT_INSTANCE_ID = 9 
        */
        sql.append("select cat.* from ");
        sql.append(getEntityTableName()).append(" cat ");
        sql.append("LEFT JOIN ").append(getEntityTableName()).append("_tree tree ");
        sql.append("ON cat.").append(getIDColumnName()).append(" = tree.child_").append(getIDColumnName()).append(" ");  
        sql.append("LEFT JOIN ").append(EntityControl.getManyToManyRelationShipTableName(ICCategory.class, ICObjectInstance.class)).append(" mt "); ////
        sql.append("ON cat.").append(getIDColumnName()).append(" = mt.").append(getIDColumnName()).append(" "); 
        sql.append("where tree.child_").append(getIDColumnName()).append(" is null ");
        sql.append("and mt.").append(IC_OBJECT_INSTANCE_COLUMN_NAME).append(" = ").append(obj.getID()).append(" ");
        if (order) {
            sql.append(" order by mt.").append(TREE_ORDER_COLUMN_NAME); 
        } 
        
        return EntityFinder.getInstance().findAll(ICCategory.class, sql.toString());
    }
    
    
	public int ejbHomeGetOrderNumber(Category category, ICObjectInstance instance) throws javax.ejb.FinderException {
		return ejbHomeGetOrderNumber(category, Integer.toString(instance.getID()));
	}
	public int ejbHomeGetOrderNumber(Category category, String objectInstanceId) throws javax.ejb.FinderException {
		try {
			String query = "SELECT TREE_ORDER FROM " + 
			               EntityControl.getManyToManyRelationShipTableName(ICCategory.class, ICObjectInstance.class) + 
                           " WHERE " + IC_OBJECT_INSTANCE_COLUMN_NAME + " = " + objectInstanceId +  
                           " AND " + this.IC_CATEGORY_COLUMN_NAME + " = " + category.getID();
            String[] res = SimpleQuerier.executeStringQuery(query);
			if (res == null || res.length == 0 || res[0] == null) {
				return 0;
			}
			return Integer.parseInt(res[0]);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new FinderException(e.getMessage());
		}
	}
	public boolean ejbHomeSetOrderNumber(Category category, ICObjectInstance instance, int orderNumber) throws com.idega.data.IDOException {
		/** @todo laga thegar timi gefst */
		//    if (orderNumber == 0) {
		//      return this.idoExecuteTableUpdate("UPDATE "+EntityControl.getManyToManyRelationShipTableName(ICCategory.class, ICObjectInstance.class)+" SET "+TREE_ORDER_COLUMN_NAME+" AS null WHERE "+IC_OBJECT_INSTANCE_COLUMN_NAME+" = "+instance.getID()+" AND "+IC_CATEGORY_COLUMN_NAME+" = "+category.getID());
		//    }else {
		return this.idoExecuteTableUpdate("UPDATE " + EntityControl.getManyToManyRelationShipTableName(ICCategory.class, ICObjectInstance.class) + " SET " + TREE_ORDER_COLUMN_NAME + " = " + orderNumber + " WHERE " + IC_OBJECT_INSTANCE_COLUMN_NAME + " = " + instance.getID() + " AND " + this.IC_CATEGORY_COLUMN_NAME + " = " + category.getID());
		//    }
	}
	/**
	 * 
	 * @param Category type
	 * @return Collection of category roots of the type
	 * @throws FinderException
	 */
	public Collection ejbFindRootsByType(String type) throws FinderException {

		String sql = getRootsJoinedSQl(type);
		// String sql = getRootsNestedSQL(type);
		return super.idoFindPKsBySQL(sql);
	}
	/**
	 * faster method than the nested one, and works on mysql
	 * SELECT cat.* FROM ic_category cat
	 *	LEFT JOIN ic_category_tree tree ON cat.ic_category_id=tree.child_ic_category_id
	 *	where cat.cat_type = 'news'
	 *	and tree.child_ic_category_id is null
	 *
	 * @param type
	 * @return
	 */
	private String getRootsJoinedSQl(String type) {
		StringBuffer sql = new StringBuffer("SELECT cat.* FROM ");
		sql.append(getEntityTableName()).append(" cat ");
		sql.append(" LEFT JOIN ").append(getEntityTableName()).append("_tree tree ");
		sql.append(" ON cat.").append(getIDColumnName()).append("= tree.");
		sql.append("child_").append(getIDColumnName());
		sql.append(" where cat.").append(getColumnType()).append(" = '").append(type).append("'");
		sql.append(" and tree.").append("child_").append(getIDColumnName()).append(" is null ");
		return sql.toString();
	}
	/**
	 * select * from IC_CATEGORY where CAT_TYPE= 'news'  and IC_CATEGORY_ID not in ( select child_IC_CATEGORY_ID from IC_CATEGORY_tree )"
	 * 
	 * @param type
	 * @return
	 */
	private String getRootsNestedSQL(String type) {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityTableName());

		sql.append(" where ").append(getColumnType()).append("= '").append(type).append("' ");
		sql.append(" and ").append(getIDColumnName()).append(" not in (");
		sql.append(" select ").append("child_").append(getIDColumnName());
		sql.append(" from ").append(getEntityTableName()).append("_tree )");
		return sql.toString();
	}

	public Collection ejbFindAllByObjectInstance(int iObjectInstanceID) throws FinderException {
		ICObjectInstance obj = ICObjectBusiness.getInstance().getICObjectInstance(iObjectInstanceID);
		return ejbFindAllByObjectInstance(obj);
	}

	public Collection ejbFindAllByObjectInstance(ICObjectInstance instance) throws FinderException {
		try {
			return idoGetRelatedEntities(instance);
		}
		catch (IDORelationshipException ex) {
			throw new FinderException(ex.getMessage());
		}
	}

	public ICCategoryTranslation getCategoryTranslation(Locale locale) throws RemoteException {
		try {
			ICCategoryTranslationHome home = (ICCategoryTranslationHome) IDOLookup.getHome(ICCategoryTranslation.class);
			return home.findByCategoryAndLocale(((Integer) this.getPrimaryKey()).intValue(), ICLocaleBusiness.getLocaleId(locale));
		}
		catch (FinderException ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

	/** Gimmi 8.04.2003*/
	public void setOwnerGroupId(int ownerGroupId) {
		this.setColumn(getColumnOwnerGroup(), ownerGroupId);
	}

	public int getOwnerGroupId() {
		return getIntColumnValue(getColumnOwnerGroup());
	}

	public void addFile(ICFile file) throws IDOAddRelationshipException {
		this.idoAddTo(file);
	}

	public void removeFile(ICFile file) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(file);
	}

	public Collection getFiles() throws IDORelationshipException {
		return this.idoGetRelatedEntities(ICFile.class);
	}

	/**
	 * Returns a collection of categories that are of the type getCategoryType()
	 * @return Collection of categories
	 * @throws FinderException
	 */
  public Collection ejbHomeFindAll() throws FinderException {
		Table table = new Table(this);
		Column column = new Column(table, getColumnType());
		Column valid = new Column(table, getColumnValid());
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(column, MatchCriteria.EQUALS, getCategoryType()));
		query.addCriteria(new MatchCriteria(valid, MatchCriteria.NOTEQUALS, false));
		return idoFindPKsBySQL(query.toString());
  }

}
