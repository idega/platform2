/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class ExportBusinessHomeImpl extends IBOHomeImpl implements
		ExportBusinessHome {
	protected Class getBeanInterfaceClass() {
		return ExportBusiness.class;
	}

	public ExportBusiness create() throws javax.ejb.CreateException {
		return (ExportBusiness) super.createIBO();
	}

}
