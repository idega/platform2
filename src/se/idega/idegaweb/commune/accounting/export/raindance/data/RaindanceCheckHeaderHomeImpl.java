/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;


import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class RaindanceCheckHeaderHomeImpl extends IDOFactory implements
		RaindanceCheckHeaderHome {
	protected Class getEntityInterfaceClass() {
		return RaindanceCheckHeader.class;
	}

	public RaindanceCheckHeader create() throws javax.ejb.CreateException {
		return (RaindanceCheckHeader) super.createIDO();
	}

	public RaindanceCheckHeader findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (RaindanceCheckHeader) super.findByPrimaryKeyIDO(pk);
	}

	public RaindanceCheckHeader findBySchoolCategory(
			SchoolCategory schoolCategory) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((RaindanceCheckHeaderBMPBean) entity)
				.ejbFindBySchoolCategory(schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public RaindanceCheckHeader findBySchoolCategory(String schoolCategory)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((RaindanceCheckHeaderBMPBean) entity)
				.ejbFindBySchoolCategory(schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
