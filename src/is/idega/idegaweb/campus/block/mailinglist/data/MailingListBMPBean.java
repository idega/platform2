package is.idega.idegaweb.campus.block.mailinglist.data;


import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.core.contact.data.Email;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class MailingListBMPBean
	extends com.idega.data.CategoryEntityBMPBean
	implements is.idega.idegaweb.campus.block.mailinglist.data.MailingList {

	private final static String TABLE_NAME = "cam_mail_list";
	private final static String NAME = "name";
	private final static String CREATED = "created_date";

	public MailingListBMPBean() {
		super();
	}

	public MailingListBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(this.getIDColumnName());
		addAttribute(NAME, "Name", true, true, String.class);
		addAttribute(CREATED, "Created", true, true, Timestamp.class);
		addManyToManyRelationShip(Email.class);

	}
	public String getEntityName() {
		return TABLE_NAME;
	}
	public String getName() {
		return getStringColumnValue(NAME);
	}
	public void setName(String name) {
		setColumn(NAME, name);
	}
	public Timestamp getCreated() {
		return (Timestamp) getColumnValue(CREATED);
	}
	public void setCreated(Timestamp created) {
		setColumn(CREATED, created);
	}

	public void addEmail(Email email) throws IDORelationshipException {
		super.idoAddTo(email);
	}

	public void addEmail(Collection emails) throws RemoteException {
		try {
			for (Iterator iter = emails.iterator(); iter.hasNext();) {
				Email email = (Email) iter.next();
				addEmail(email);
			}
		}
		catch (IDORelationshipException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public Collection getEmails() throws RemoteException {
		try {
			return super.idoGetRelatedEntities(Email.class);
		}
		catch (IDORelationshipException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public void removeEmail(Email email) throws RemoteException {
		try {
			super.idoRemoveFrom(email);
		}
		catch (IDORemoveRelationshipException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public void removeEmails() throws RemoteException {
		try {
			super.idoRemoveFrom(Email.class);
		}
		catch (IDORemoveRelationshipException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect());
	}


}
