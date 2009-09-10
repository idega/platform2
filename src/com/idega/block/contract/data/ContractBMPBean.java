/*
 * $Id: ContractBMPBean.java,v 1.24.2.1 2007/01/12 19:32:16 idegaweb Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.contract.data;
import java.io.StringReader;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.util.IWTimestamp;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;
/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ContractBMPBean extends com.idega.data.GenericEntity implements com.idega.block.contract.data.Contract {
	private static final String name_ = "con_contract";
	private static final String category_ = "con_category_id";
	private static final String userId_ = "ic_user_id";
	private static final String validFrom_ = "valid_from";
	private static final String validTo_ = "valid_to";
	private static final String status_ = "status";
	private static final String created_ = "created";
	private static final String statusDate_ = "status_date";
	private static final String text_ = "text";
	private static final String signedData_ = "signed_data";
	private static final String signedFlag_ = "signed_flag";
	private static final String signedDate_ = "signed_date";
	private static final String signedBy_ = "signed_by";
	public static final String statusCreated = "C";
	public static final String statusPrinted = "P";
	public static final String statusSigned = "S";
	public static final String statusRejected = "R";
	public static final String statusTerminated = "T";
	public static final String statusEnded = "E";
	public static final String statusResigned = "U";
	public static String getColumnNameStatus() {
		return status_;
	}
	public static String getColumnNameValidTo() {
		return validTo_;
	}
	public static String getColumnNameValidFrom() {
		return validFrom_;
	}
	public static String getColumnNameUserId() {
		return userId_;
	}
	public static String getColumnNameStatusDate() {
		return statusDate_;
	}
	public static String getColumnNameCreationDate() {
		return created_;
	}
	public static String getColumnNameContractEntityName() {
		return name_;
	}
	public static String getColumnNameCategoryId() {
		return category_;
	}
	public static String getColumnNameText() {
		return text_;
	}
	public static String getColumnNameSignedData() {
		return signedData_;
	}
	public static String getColumnNameSignedFlag() {
		return signedFlag_;
	}
	public static String getColumnNameSignedDate() {
		return signedDate_;
	}
	public ContractBMPBean() {
	}
	public ContractBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(category_, "Category", true, true, java.lang.Integer.class, "many-to-one", com.idega.block.contract.data.ContractCategory.class);
		addAttribute(userId_, "Contract maker", true, true, java.lang.Integer.class, "one-to-many", com.idega.core.user.data.User.class);
		addAttribute(validFrom_, "Valid from", true, true, java.sql.Date.class);
		addAttribute(validTo_, "Valid to", true, true, java.sql.Date.class);
		addAttribute(statusDate_, "Status changed", true, true, java.sql.Date.class);
		addAttribute(status_, "Status", true, true, java.lang.String.class, 1);
		addAttribute(created_, "Created", true, true, java.sql.Date.class);
		addAttribute(text_, "Text", true, true, java.lang.String.class, 30000);
		addAttribute(signedData_, "XML Signed Data", true, true, java.lang.String.class, 30000);
		addAttribute(signedFlag_, "Signed Flag", true, true, java.lang.Boolean.class);
		addAttribute(signedDate_, "Signed Date", true, true, java.sql.Date.class);
		addAttribute(signedBy_, "Signed By", true, true, java.lang.Integer.class, "one-to-many", com.idega.core.user.data.User.class);
		addManyToManyRelationShip(com.idega.core.file.data.ICFile.class);
		addMetaDataRelationship();
	}
	public String getEntityName() {
		return (name_);
	}
	public void setUserId(int id) {
		setColumn(userId_, id);
	}
	public void setUserId(Integer id) {
		setColumn(userId_, id);
	}
	public Integer getUserId() {
		return (getIntegerColumnValue(userId_));
	}
	public void setSignedBy(Integer id) {
		setColumn(signedBy_, id);
	}
	public Integer getSignedBy(Integer id) {
		return (getIntegerColumnValue(signedBy_));
	}
	public void setValidFrom(Date date) {
		setColumn(validFrom_, date);
	}
	public Date getValidFrom() {
		return ((Date)getColumnValue(validFrom_));
	}
	public void setValidTo(Date date) {
		setColumn(validTo_, date);
	}
	public Date getValidTo() {
		return ((Date)getColumnValue(validTo_));
	}
	public void setCreationDate(Date date) {
		setColumn(created_, date);
	}
	public Date getCreationDate() {
		return (Date)getColumnValue(created_);
	}
	public void setStatusDate(Date date) {
		setColumn(statusDate_, date);
	}
	public Date getStatusDate() {
		return (Date)getColumnValue(statusDate_);
	}
	public void setCategoryId(int id) {
		setColumn(category_, id);
	}
	public void setCategoryId(Integer id) {
		setColumn(category_, id);
	}
	public Integer getCategoryId() {
		return getIntegerColumnValue(category_);
	}
	public void setStatus(String status) throws IllegalStateException {
		if ((status.equalsIgnoreCase(statusCreated)) || (status.equalsIgnoreCase(statusEnded)) || (status.equalsIgnoreCase(statusRejected)) || (status.equalsIgnoreCase(statusSigned)) || (status.equalsIgnoreCase(statusTerminated)) || (status.equalsIgnoreCase(statusResigned)) || (status.equalsIgnoreCase(statusPrinted))) {
			setColumn(status_, status);
			setStatusDate(IWTimestamp.RightNow().getSQLDate());
		}
		else {
			throw new IllegalStateException("Undefined state : " + status);
		}
	}
	public String getStatus() {
		return ((String)getColumnValue(status_));
	}
	public void setStatusCreated() {
		setStatus(statusCreated);
	}
	public void setStatusEnded() {
		setStatus(statusEnded);
	}
	public void setStatusRejected() {
		setStatus(statusRejected);
	}
	public void setStatusSigned() {
		setStatus(statusSigned);
	}
	public void setStatusTerminated() {
		setStatus(statusTerminated);
	}
	public void setStatusPrinted() {
		setStatus(statusPrinted);
	}
	public void setStatusResigned() {
		setStatus(statusResigned);
	}
	public void setXmlSignedData(java.lang.String XMLSignedData) {
		setColumn(signedData_, XMLSignedData);
	}
	public void setText(java.lang.String text) {
		setColumn(text_, text);
	}
	public java.lang.String getXmlSignedData() {
		return ((String)getColumnValue(signedData_));
	}
	public java.lang.String getText() {
		return ((String)getColumnValue(text_));
	}
	public java.lang.Boolean getSignedFlag() {
		return ((Boolean)getColumnValue(signedFlag_));
	}
	public void setSignedFlag(java.lang.Boolean p0) {
		setColumn(signedFlag_, p0);
	}
	public boolean isSigned() {
		return getSignedFlag() != null && getSignedFlag().booleanValue();
	}
	public void setSignedDate(java.sql.Date time) {
		setColumn(signedDate_, time);
	}
	public Date getSignedDate() {
		return (Date)getColumnValue(signedDate_);
	}

	/**
	 * Returns all xml fields in the contract text that have not yet got a value.
	 */
	public Set getUnsetFields() {
		Set fields = new HashSet();
		try {
			XMLParser parser = new XMLParser();
			XMLDocument document = parser.parse(new StringReader("<dummy>" + getText() + "</dummy>"));

			XMLElement root = document.getRootElement();
			List xmlFields = root.getChildren();
			Iterator i = xmlFields.iterator();
			while (i.hasNext()) {
				fields.add(((XMLElement)i.next()).getName());
			}

		} catch (XMLException ex) {
			ex.printStackTrace();
		}

		return fields;
	}


	public static void main(String args[]){
		Map map = new HashMap();
		map.put("name", "Roar");
		System.out.println(ContractBMPBean.setUnsetFields(map, "Enter the name: <name/>. Age: <age/>."));
	}

	/**
	 * Gives value to xml-fields in the contract text. The xml field is substituted with the text value.
	 * If no value is given in the fieldValues parameter, the xml field is left unchanged.
	 */
	public void setUnsetFields(Map fieldValues) {
		String text = setUnsetFields(fieldValues, getText());
		setText(text);
		store();
		
	}

	private static String setUnsetFields(Map fieldValues, String text) {
		String retValue = null;
		StringBuffer merged = new StringBuffer();

		try {
			XMLParser parser = new XMLParser();
			XMLDocument document = parser.parse(new StringReader("<dummy>" + text + "</dummy>"));
			XMLElement root = document.getRootElement();
			Iterator it = root.getContent().iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof XMLElement) {
					
					String name = ((XMLElement)obj).getName();
					String value = (String) fieldValues.get(name);
//					System.out.println("    (name: " + name + ", value: " + value + ")");
					merged.append(value != null ? value : "<" + name + "/>");
				} else if (obj instanceof String) {
					merged.append((String) obj);
				} 
			}
			retValue = merged.toString();				
		} catch (XMLException ex) {
			ex.printStackTrace();
		}
		return retValue;		
	}

	public Contract ejbHomeCreate(int userID, int iCategoryId, IWTimestamp ValFrom, IWTimestamp ValTo, String sStatus, Map map) {
		try {
			Contract C = ((ContractHome)IDOLookup.getHome(Contract.class)).create();
			C.setStatus(sStatus);
			C.setValidFrom(ValFrom.getSQLDate());
			if (userID != -1) {
				C.setUserId(userID);
			}
			if (ValTo != null) {
				C.setValidTo(ValTo.getSQLDate());
			}
			C.setCategoryId(iCategoryId);
			Iterator it = null;
			if (map != null) {
				it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry me = (Map.Entry)it.next();
					C.setMetaData(me.getKey().toString(), me.getValue().toString());
				}
			}
			C.store();
			//	ContractWriter.writeText(C.getID(), iCategoryId);
			return C;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Contract ejbHomeCreate(int iCategoryId, IWTimestamp ValFrom, IWTimestamp ValTo, String sStatus, Map map) {
		return ejbHomeCreate(-1, iCategoryId, ValFrom, ValTo, sStatus, map);
	}
	public Contract ejbHomeCreate(int userId, int iCategoryId, IWTimestamp ValFrom, IWTimestamp ValTo, String sStatus, String text) {
		try {
			Contract C = ((ContractHome)IDOLookup.getHome(Contract.class)).create();
			C.setStatus(sStatus);
			C.setText(text);
			C.setValidFrom(ValFrom.getSQLDate());
			C.setUserId(userId);
			if (ValTo != null) {
				C.setValidTo(ValTo.getSQLDate());
			}
			C.setCategoryId(iCategoryId);
			C.store();
			//				ContractWriter.writeText(C.getID(), iCategoryId);
			return C;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Collection ejbFindAllByCategory(int iCategoryID) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(ContractBMPBean.getColumnNameCategoryId(), iCategoryID));
	}

	public Collection ejbFindAllByUser(int userID) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(ContractBMPBean.getColumnNameUserId(), userID));
	}
	public Collection ejbFindAllByCategoryAndStatus(int iCategoryID, String status) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(ContractBMPBean.getColumnNameCategoryId(), iCategoryID).appendAndEquals(getColumnNameStatus(), status));
	}
	public boolean ejbHomeSetStatus(int conID, String status) throws FinderException, IDOLookupException {
		Contract C = ((ContractHome)IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(conID));
		C.setStatus(status);
		C.store();
		return true;
	}
	public int ejbHomeGetCountByCategory(int catID) throws IDOException {
		return this.idoGetNumberOfRecords(idoQueryGetSelectCount().appendWhereEquals(getColumnNameCategoryId(), catID));
	}

	public Collection ejbHomeFindFiles(int contractID) throws FinderException, IDORelationshipException {
		return idoGetRelatedEntities(ICFile.class);
	}

	public void addFileToContract(ICFile file) throws IDOAddRelationshipException {
		idoAddTo(file);
	}
	
	public void removeFileFromContract(ICFile file) throws IDORemoveRelationshipException{
		idoRemoveFrom(file);
	}
	
	public void removeAllFiles() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(ICFile.class);
	}
}
