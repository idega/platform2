package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.presentation.TravelBlock;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.core.builder.data.ICPage;
import com.idega.core.component.data.ICObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.util.text.TextSoap;


/**
 * @author gimmi
 */
public class SearchFormSelector extends TravelBlock {

	List searchForms = null;
	IWResourceBundle iwrb;
	
	ICPage targetPage;

	
	public SearchFormSelector() {
		super();
	}
	

	public void main(IWContext iwc) throws Exception {
		init(iwc);
		add(getMenu(iwc));
	}
	
	private void init(IWContext iwc) throws Exception {
		iwrb = getTravelSessionManager(iwc).getIWResourceBundle();
		IWBundle bundle = getTravelSessionManager(iwc).getIWBundle();
		Collection ICObjectList = bundle.getICObjectsList(ServiceSearch.IC_OBJECT_TYPE);
		Iterator iter = ICObjectList.iterator();
		ICObject object;
		if (searchForms == null) {
			searchForms = new Vector();
			while (iter.hasNext()) {
				object = (ICObject) iter.next();
				try {
					Class tmpClass = Class.forName(object.getClassName());
					searchForms.add( (AbstractSearchForm) tmpClass.newInstance() );
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Form getMenu(IWContext iwc) {
		Form form = new Form();
		if (targetPage != null) {
			form.setPageToSubmitTo(targetPage);
		}
		
		DropdownMenu forms = new DropdownMenu(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM);
		getStyleObject(forms, ServiceSearch.STYLENAME_INTERFACE);

		forms.setToSubmit();
		form.add(forms);
		
		if (searchForms != null && !searchForms.isEmpty() ) {
			Iterator iter = searchForms.iterator();
			AbstractSearchForm bsf;
			while (iter.hasNext()) {
				bsf = (AbstractSearchForm) iter.next();
				forms.addMenuElement(IWMainApplication.getEncryptedClassName(bsf.getClassName()), bsf.getServiceName(iwrb));
			}
			if (iwc.isParameterSet(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM)) {
				forms.setSelectedElement(iwc.getParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM));
			}
			else {
				forms.setSelectedElement(IWMainApplication.getEncryptedClassName( ((AbstractSearchForm)searchForms.get(0)).getClassName()));
			}
		}	
		return form;
	}
	
	public void setTargetPage(ICPage targetPage) {
		this.targetPage = targetPage;
	}

	public void setValidSearchEngines(String validSearchEngines) {
		searchForms = new Vector();
		AbstractSearchForm ss;
		validSearchEngines = TextSoap.findAndCut(validSearchEngines, " ");
		StringTokenizer st = new StringTokenizer(validSearchEngines, ",");
		
		while (st.hasMoreTokens()) {
			try {
				Class tmpClass = Class.forName(st.nextToken());
				ss = (AbstractSearchForm) tmpClass.newInstance();
				searchForms.add( ss );
			} catch (ClassNotFoundException e) {
				System.err.println("ServiceSearch couldnt instanciate class : "+validSearchEngines);
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
