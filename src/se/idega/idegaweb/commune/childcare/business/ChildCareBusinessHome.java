/*
 * $Id: ChildCareBusinessHome.java 1.1 3.12.2004 laddi Exp $
 * Created on 3.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import is.idega.block.family.business.NoCustodianFound;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.business.Constants;
import se.idega.idegaweb.commune.care.business.AlreadyCreatedException;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.business.CareInvoiceBusiness;
import se.idega.idegaweb.commune.care.business.DefaultPlacementHelper;
import se.idega.idegaweb.commune.care.business.PlacementHelper;
import se.idega.idegaweb.commune.care.check.data.Check;
import se.idega.idegaweb.commune.care.check.data.GrantedCheck;
import se.idega.idegaweb.commune.care.data.CareTime;
import se.idega.idegaweb.commune.care.data.CareTimeHome;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import se.idega.idegaweb.commune.care.data.EmploymentType;
import se.idega.idegaweb.commune.care.data.EmploymentTypeHome;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosisHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueueHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;

import com.idega.block.contract.business.ContractService;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractTagHome;
import com.idega.block.pdf.ITextXMLHandler;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolAreaComparator;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.business.SchoolTypeComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.business.IBOHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.data.IDOStoreException;
import com.idega.exception.IWBundleDoesNotExist;
import com.idega.idegaweb.IWBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.database.ConnectionBroker;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.XmlPeer;


/**
 * Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface ChildCareBusinessHome extends IBOHome {

	public ChildCareBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
