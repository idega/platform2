package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2004/03/23 14:04:06 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public class CheckAmountBroadcastHomeImpl extends IDOFactory implements CheckAmountBroadcastHome {
	protected Class getEntityInterfaceClass(){
		return CheckAmountBroadcast.class;
	}
	
	public CheckAmountBroadcast create () throws CreateException{
		return (CheckAmountBroadcast) createIDO ();
	}
	
	public CheckAmountBroadcast findByPrimaryKey (final Object primaryKey)
		throws FinderException{
		return (CheckAmountBroadcast) findByPrimaryKeyIDO (primaryKey);
	}

	public CheckAmountBroadcast findLatestBySchoolCategoryId
		(final String schoolCategoryId) throws FinderException {
		 final IDOEntity entity = idoCheckOutPooledEntity ();
		 final Object primaryKey  = ((CheckAmountBroadcastBMPBean) entity)
				 .ejbFindLatestBySchoolCategoryId (schoolCategoryId);
		 idoCheckInPooledEntity (entity);
		 return findByPrimaryKey (primaryKey);
	}

	public Collection findOlderByTimestamp
		(final String schoolCategoryPK,	final Timestamp timestamp)
		throws FinderException {
		 final IDOEntity entity = idoCheckOutPooledEntity ();
		 final Collection primaryKeys = ((CheckAmountBroadcastBMPBean) entity)
				 .ejbFindOlderByTimestamp (schoolCategoryPK, timestamp);
		 idoCheckInPooledEntity (entity);
		 return getEntityCollectionForPrimaryKeys (primaryKeys);
	}
}
