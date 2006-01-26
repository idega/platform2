/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;


import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class EconomaCheckHeaderHomeImpl extends IDOFactory implements
		EconomaCheckHeaderHome {
	protected Class getEntityInterfaceClass() {
		return EconomaCheckHeader.class;
	}

	public EconomaCheckHeader create() throws javax.ejb.CreateException {
		return (EconomaCheckHeader) super.createIDO();
	}

	public EconomaCheckHeader findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (EconomaCheckHeader) super.findByPrimaryKeyIDO(pk);
	}

	public EconomaCheckHeader findBySchoolCategory(SchoolCategory schoolCategory)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((EconomaCheckHeaderBMPBean) entity)
				.ejbFindBySchoolCategory(schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public EconomaCheckHeader findBySchoolCategory(String schoolCategory)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((EconomaCheckHeaderBMPBean) entity)
				.ejbFindBySchoolCategory(schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
