package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolContentBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.core.business.UserGroupBusiness;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.Group;

/**
 * @author gimmi
 */
public class SchoolViewer extends Block {

  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
  private IWResourceBundle _iwrb;
  private IWBundle _iwb;


	public void main( IWContext iwc ) throws RemoteException{
		init( iwc );
		
		schoolList( iwc );
	}

	private void init( IWContext iwc ) {
		_iwrb = super.getResourceBundle( iwc );
		_iwb = super.getBundle( iwc );
	}

	private void schoolList( IWContext iwc ) throws RemoteException {
		Table table = new Table();
		int row = 1;
		
		/** @todo baeta vid header i tofluna */
		Collection coll = getSchoolBusiness( iwc ).findAllSchools();
		if ( coll != null && coll.size() > 0 ) {
			School school;
			Link link;
			Iterator iter = coll.iterator();
			while ( iter.hasNext() ) {
				++row;
				school = (School) iter.next();
				link = new Link( school.getName() );
				link.addParameter( getSchoolContentBusiness( iwc ).getParameterSchoolId() , school.getPrimaryKey().toString() );
				table.add ( link, 1, row );
				if (getSchoolContentBusiness(iwc).hasEditPermission(school, iwc)) {
					table.add( SchoolContentEditor.getLink(school) , 2, row);
				}
			}
		}
		

		
		add( table );
	}	
	
	
	private SchoolBusiness getSchoolBusiness( IWApplicationContext iwac ) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance( iwac ,SchoolBusiness.class );
	}
	protected SchoolContentBusiness getSchoolContentBusiness(IWUserContext iwuc) throws RemoteException{
		return (SchoolContentBusiness) IBOLookup.getSessionInstance( iwuc, SchoolContentBusiness.class);
	}
}
