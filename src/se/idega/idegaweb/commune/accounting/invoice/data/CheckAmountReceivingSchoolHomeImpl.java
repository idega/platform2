package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2004/03/22 13:01:14 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class CheckAmountReceivingSchoolHomeImpl extends IDOFactory
	implements CheckAmountReceivingSchoolHome {
	protected Class getEntityInterfaceClass(){
		return CheckAmountReceivingSchool.class;
	}
	
	public CheckAmountReceivingSchool create () throws CreateException{
		return (CheckAmountReceivingSchool) createIDO ();
	}
	
	public CheckAmountReceivingSchool findByPrimaryKey (final Object primaryKey)
		throws FinderException{
		return (CheckAmountReceivingSchool) findByPrimaryKeyIDO (primaryKey);
	}

	public Collection findEmailedProvidersByCheckAmountBroadcast
		(final CheckAmountBroadcast broadcastInfo) throws FinderException {
		 final IDOEntity entity = idoCheckOutPooledEntity ();
		 final Collection primaryKeys
				 = ((CheckAmountReceivingSchoolBMPBean) entity)
				 .ejbFindEmailedProvidersByCheckAmountBroadcast (broadcastInfo);
		 idoCheckInPooledEntity (entity);
		 return getEntityCollectionForPrimaryKeys (primaryKeys);
	}

	public Collection findPaperMailedProvidersByCheckAmountBroadcast
		(final CheckAmountBroadcast broadcastInfo) throws FinderException {
		 final IDOEntity entity = idoCheckOutPooledEntity ();
		 final Collection primaryKeys
				 = ((CheckAmountReceivingSchoolBMPBean) entity)
				 .ejbFindPaperMailedProvidersByCheckAmountBroadcast (broadcastInfo);
		 idoCheckInPooledEntity (entity);
		 return getEntityCollectionForPrimaryKeys (primaryKeys);
	}

	public Collection findIgnoredProvidersByCheckAmountBroadcast
		(final CheckAmountBroadcast broadcastInfo) throws FinderException {
		 final IDOEntity entity = idoCheckOutPooledEntity ();
		 final Collection primaryKeys
				 = ((CheckAmountReceivingSchoolBMPBean) entity)
				 .ejbFindIgnoredProvidersByCheckAmountBroadcast (broadcastInfo);
		 idoCheckInPooledEntity (entity);
		 return getEntityCollectionForPrimaryKeys (primaryKeys);
	}

}
