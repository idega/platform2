//idega 2000 - �gir og eiki
package com.idega.block.contract.data;
//import java.util.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.component.data.ICObjectInstanceHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOStoreException;
public class ContractCategoryBMPBean
	extends com.idega.data.GenericEntity
	implements com.idega.block.contract.data.ContractCategory {
	public ContractCategoryBMPBean() {
		super();
	}
	public ContractCategoryBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnName(), "Name", true, true, String.class);
		addAttribute(getColumnDescription(), "Description", true, true, String.class);
		addAttribute(getColumnCreated(), "Created", true, true, java.sql.Date.class);
		addAttribute(getColumnValid(), "Valid", true, true, Boolean.class);
		addAttribute(getColumnXMLTemplate(), "XML Template", true, true, String.class, 30000);
		addManyToManyRelationShip(com.idega.core.component.data.ICObjectInstance.class);
	}
	public void insertStartData() throws Exception {
		ContractCategory cat = ((ContractCategoryHome) IDOLookup.getHome(ContractCategory.class)).create();
		cat.setName("Default");
		cat.setDescription("Default Category for idegaWeb");
		cat.setValid(true);
		cat.store();
	}
	public static String getEntityTableName() {
		return "CON_CATEGORY";
	}
	public static String getColumnName() {
		return "NAME";
	}
	public static String getColumnDescription() {
		return "DESCRIPTION";
	}
	public static String getColumnCreated() {
		return "CREATED";
	}
	public static String getColumnValid() {
		return "VALID";
	}
	public static String getColumnXMLTemplate() {
		return "XML_TEMPL";
	}
	public String getEntityName() {
		return getEntityTableName();
	}
	public String getName() {
		return getNewsCategoryName();
	}
	public String getNewsCategoryName() {
		return getStringColumnValue(getColumnName());
	}
	public void setName(String name) {
		setCategoryName(name);
	}
	public void setCategoryName(String category_name) {
		setColumn(getColumnName(), category_name);
	}
	public String getDescription() {
		return getStringColumnValue(getColumnDescription());
	}
	public void setDescription(String description) {
		setColumn(getColumnDescription(), description);
	}
	public void setCreationDate(Date date) {
		setColumn(getColumnCreated(), date);
	}
	public Date getCreationDate() {
		return (Date) getColumnValue(getColumnCreated());
	}
	public boolean getValid() {
		return getBooleanColumnValue(getColumnValid());
	}
	public void setValid(boolean valid) {
		setColumn(getColumnValid(), valid);
	}
	public void setXMLTemplate(String template) {
		setColumn(getColumnXMLTemplate(), template);
	}
	public String getXMLTemplate() {
		return getStringColumnValue(getColumnXMLTemplate());
	}
	public boolean ejbHomeUpdateDescription(int id, String description) {
		try {
			ContractCategory cat =
				((ContractCategoryHome) IDOLookup.getHome(ContractCategory.class)).findByPrimaryKey(new Integer(id));
			cat.setDescription(description);
			cat.store();
			return true;
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}
	public Collection ejbFindAll() throws FinderException {
		return idoFindAllIDsBySQL();
	}
	public Collection getRelatedObjectInstances() throws FinderException, IDORelationshipException {
		return idoGetRelatedEntities(ICObjectInstance.class);
	}
	public Collection ejbFindByObjectInstance(ICObjectInstance instance)
		throws FinderException, IDORelationshipException {
		return idoGetReverseRelatedEntities(instance);
	}
	public ContractCategory ejbHomeCreate(int iCategoryId, int iObjectInstanceId, String Name, String info)throws javax.ejb.CreateException {
		
			ContractCategory cat = null;
			try {
				
				if (iCategoryId > 0) {
					cat =
						((ContractCategoryHome) IDOLookup.getHome(ContractCategory.class)).findByPrimaryKey(
							new Integer(iCategoryId));
				}
				else {
					cat = ((ContractCategoryHome) IDOLookup.getHome(ContractCategory.class)).create();
				}
				cat.setName(Name);
				cat.setDescription(info);
				cat.store();
				// Binding category to instanceId
				if (iObjectInstanceId > 0) {
					ICObjectInstance objIns =
						((ICObjectInstanceHome) IDOLookup.getHome(ICObjectInstance.class)).findByPrimaryKey(
							new Integer(iObjectInstanceId));
					// Allows only one category per instanceId
					objIns.removeFrom(ContractCategory.class);
					objIns.addTo(ContractCategory.class, ((Integer) cat.getPrimaryKey()).intValue());
				}
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			catch (IDOStoreException e) {
				e.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			catch (EJBException e) {
				e.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			catch (CreateException e) {
				e.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			catch (FinderException e) {
				e.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			catch (SQLException e) {
				e.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			return cat;
		
	}
}
