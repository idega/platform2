/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;



import se.idega.block.pki.data.NBSSignedEntity;
import se.idega.block.pki.presentation.NBSSigningBlock;

import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.builder.data.IBPage;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

import com.idega.presentation.text.Link;

import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;


/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChildCareContractSigner extends Block implements Builderaware{
	
	public final static String PAR_CONTRACT_ID = "ChildCareContractSigner_CONTRACT_ID";
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
		
	public ChildCareContractSigner() {
		super();
	}	
		
	public void main(IWContext iwc) {
		add(makeTableOfContracts(iwc));			
//		try{
//			control(iwc);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
	}	
	
	private static String ACTION = "se.idega.idegaweb.commune.childcare.presentation.ChildCareContractSigner.ACTION";
	private static String ACTION_FILLOUT = "FILLOUT";
	private static String ACTION_SIGN = "SIGN";	


	
		
	private void control(IWContext iwc) throws Exception{
		
		
		System.out.println("control()");	
		
		String action = iwc.getParameter(ACTION);
		
		System.out.println("ChildCareContract.Signing.ACTION: " + action);
		
		if (action == null){
			add(makeTableOfContracts(iwc));			
		
		} else if (action.equals(ACTION_FILLOUT)){
			Form formTable = makeFillOutForm(iwc);
			if (formTable == null){
				signContract(iwc);
			} else {
				add(formTable);
			}
			
		} else if (action.equals(ACTION_SIGN)){
			Contract contract = getContract(iwc);
			contract.setText(mergeFields(iwc));
			contract.store();
			signContract(iwc);
			
		} 
		System.out.println("control() done");	
	}
	

	private void signContract(IWContext iwc){
		Contract contract = getContract(iwc);		
			
		iwc.setSessionAttribute(NBSSigningBlock.NBS_SIGNED_ENTITY, 
			new NBSSignedEntity() {
				private Contract _contract = null;
					
				public Object init(Contract contract){
					_contract = contract;
					return this;
				}
					
				public void setXmlSignedData(String data) {
					_contract.setXmlSignedData(data);
				}
	
				public void setSignedBy(int userId) {
					//_contract.setUserId(userId); //This shall already be set 
				}
	
				public void setSignedDate(java.sql.Date time) {
					_contract.setSignedDate(time);
				}
	
				public void setSignedFlag(boolean flag) {
					_contract.setSignedFlag(new Boolean(flag));												
				}
	
				public void store() {
					_contract.store();
				}
	
				public String getText() {
					return _contract.getText();
				}
			}
			.init(contract)
		);
					
//		iwc.removeSessionAttribute(NBSSigningBlock.INIT_DONE);		
		add(new HiddenInput(ACTION, ""));
				
		iwc.forwardToIBPage(getParentPage(), getResponsePage());	
	}

	private Form makeTableOfContracts(IWContext iwc) {
		System.out.println("makeTableOfContracts()");			
		
		Collection contracts = ContractFinder.findContractsByUserId(iwc.getCurrentUser().getID());
		
		
		if (contracts == null){
			return new Form();
		}
		
		Iterator i = contracts.iterator();
		
		Table t = new Table(4, contracts.size());
		t.setCellpadding(2);
		t.setCellspacing(2);	
		t.setBorder(0);	
		
		int row = 1;
		
//		t.add(new Text("Text"), 1, 1);
//		t.add(new Text("Text"), 1, 1);
//		t.add(new Text("Text"), 1, 1);
		IWResourceBundle iwrb = getResourceBundle(iwc);		
		
		while (i.hasNext()){
			Contract contract = (Contract) i.next();

			System.out.println("Contract.id: " + contract.getID());
			
			String text = contract.getText();
			if (text == null){
				text = " ";
			}
			
			ContractCategory cat = ContractBusiness.findCategory(contract.getCategoryId().intValue());
			t.add(getContractIcon(iwc/*, contract*/), 1, row);
			t.add(new Text(cat.getName()), 2, row);
						
			if (contract.isSigned()) {
				t.add(new Text(iwrb.getLocalizedString("ccconsign_signed", "Signed") + contract.getSignedDate()), 4, row);
			}else {

				Link signBtn = new Link(iwrb.getLocalizedString("ccconsign_signcon","Sign Contract"));
				signBtn.setWindowToOpen(ChildCareWindow.class);
				signBtn.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_SIGN_CONTRACT);
				signBtn.setParameter(ChildCareAdminWindow.PARAMETER_CONTRACT_ID, ""+contract.getID());
				signBtn.setAsImageButton(true);
//				signBtn.setParameter(ACTION, ACTION_FILLOUT);
				t.add(signBtn, 3, row);				
			}
				
//			t.add(new Text(""+contract.getID()), 2, row);

//			t.add(new Text(escapeHTML(contract.getXmlSignedData())), 3, row);

			
			row ++;
		}
		setStyle(this, "font-size:10px");		
		setStyle(this, "font-family: sans-serif");
		
		Form f = new Form();
		f.add(t);
		return f;
	}
	/**
	 * 
	 * @param iwc
	 * @return the contract specified by the ChildCareContractSigner_CONTRACT_ID parameter, null if errors or no contract
	 */
	private Contract getContract(IWContext iwc) {
		int contractId;
		Contract contract = null;		
		try {
			contractId = Integer.parseInt(iwc.getParameter(PAR_CONTRACT_ID));
			contract = ((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHome(Contract.class))
					.findByPrimaryKey(contractId);	
		}catch(NumberFormatException ex){
			ex.printStackTrace();			
		} catch(FinderException ex){
			ex.printStackTrace();
		} catch(IDOLookupException ex){
			ex.printStackTrace();
		}
		return contract;		
	}
	/**
	 * 
	 * @param iwc
	 * @return null If no fields in text, Table with TextInput fields otherwise
	 * @throws XMLException
	 */
	private Form makeFillOutForm(IWContext iwc) throws XMLException{
		IWResourceBundle iwrb = getResourceBundle(iwc);			
		Form form = null;

		Contract contract = getContract(iwc);
		String text = contract.getText();
		
		try {
			XMLParser parser = new XMLParser();
			XMLDocument document = parser.parse(new StringReader("<dummy>" + text + "</dummy>"));
		
			XMLElement root = document.getRootElement();
			List fields = root.getChildren();
			if (fields.size() != 0){
				form = new Form();
				form.add(new Text(getResourceBundle(iwc).getLocalizedString("ccconsign_formHeading", "Please, fill out the contract fields")));
				Table table = new Table();
				form.add(table);
				Iterator i = fields.iterator();
				int row = 1;
				while (i.hasNext()){
					String field = ((XMLElement) i.next()).getName();
					table.add(new Text(field + ":"), 1, row);
					table.add(new TextInput(field), 2, row);
					row ++;
				}
				SubmitButton submit = new SubmitButton(iwrb.getLocalizedString("ccconsign_formSubmit", "Submit"), ACTION, ACTION_SIGN);
				submit.setAsImageButton(true);
				table.add(submit, 2, row);
				table.setAlignment(2, row, "right");	
				
				form.add(new HiddenInput(PAR_CONTRACT_ID, iwc.getParameter(PAR_CONTRACT_ID)));			
			}
		}catch (XMLException ex){
			ex.printStackTrace();
			throw ex;			
		}
		return form;
	}
	
	/**
	 * 
	 * @param iwc
	 * @return
	 * @throws XMLException
	 */
	private String mergeFields(IWContext iwc) throws XMLException{
		Contract contract = getContract(iwc);
		String text = contract.getText();
		StringBuffer merged = new StringBuffer();
		
		try {
			XMLParser parser = new XMLParser();
			XMLDocument document = parser.parse(new StringReader("<dummy>" + text + "</dummy>"));
		
			XMLElement root = document.getRootElement();
			Iterator it = root.getContent().iterator();
			while (it.hasNext()){
				Object obj = it.next();
				if (obj instanceof XMLElement) {
					merged.append(iwc.getParameter(((XMLElement) obj).getName()));
				}
//				else if (obj instanceof XMLCDATA) { ignore	}
				else if (obj instanceof String) {
					merged.append((String) obj);			
				}
			}	
		}catch (XMLException ex){
			ex.printStackTrace();
			throw ex;
		}
		return merged.toString();
	}


	public static void main(String[] aqrgs){
		String text = "Dette er en test <dato /> av <xml/> parser";
		
		try {
			XMLParser parser = new XMLParser();
			XMLDocument document = parser.parse(new StringReader("<dummy>" + text + "</dummy>"));
		
			XMLElement root = document.getRootElement();
			Iterator it = root.getContent().iterator();
			while (it.hasNext()){
				Object obj = it.next();
				if (obj instanceof XMLElement) {
					System.out.println(((XMLElement) obj).getName());
				}
//				else if (obj instanceof XMLCDATA) { ignore	}
				else if (obj instanceof String) {
					System.out.println(((String) obj));			
				}
			}	
						
			List fields = root.getChildren();
			Iterator i = fields.iterator();
			System.out.println(root.getText());			
			System.out.println(root.getName());		
			
			while (i.hasNext()){
				XMLElement f = (XMLElement) i.next();
				System.out.println(f.getText());
				System.out.println(f.getName());
			}
		}catch (XMLException ex){
			ex.printStackTrace();
			return;			
			
		}

		
		
	}
	/**
	 * @param iwc
	 * @param contract
	 * @return
	 */
	private PresentationObject getContractIcon(IWContext iwc/*, Contract contract*/)
	{
		//TODO Display a PDF link if possible
		Image image = getBundle(iwc).getImage("contracticon.gif");
		return image;
	}

	private static IBPage _page;	

	public void setResponsePage(IBPage page){
		System.out.println("******* S E T T I N G   Page: " + page.getName());		
		_page = page;
	}
	
	public IBPage getResponsePage(){
		return _page;
	}
	
	public static final String escapeHTML(String s){
		if (s == null){
			return "";
		}
		
	   StringBuffer sb = new StringBuffer();
	   int n = s.length();
	   for (int i = 0; i < n; i++) {
		  char c = s.charAt(i);
		  switch (c) {
			 case '<': sb.append("&lt;"); break;
			 case '>': sb.append("&gt;"); break;
			 case '&': sb.append("&amp;"); break;
			 case '"': sb.append("&quot;"); break;
			 case 'à': sb.append("&agrave;");break;
			 case 'À': sb.append("&Agrave;");break;
			 case 'â': sb.append("&acirc;");break;
			 case 'Â': sb.append("&Acirc;");break;
			 case 'ä': sb.append("&auml;");break;
			 case 'Ä': sb.append("&Auml;");break;
			 case 'å': sb.append("&aring;");break;
			 case 'Å': sb.append("&Aring;");break;
			 case 'æ': sb.append("&aelig;");break;
			 case 'Æ': sb.append("&AElig;");break;
			 case 'ç': sb.append("&ccedil;");break;
			 case 'Ç': sb.append("&Ccedil;");break;
			 case 'é': sb.append("&eacute;");break;
			 case 'É': sb.append("&Eacute;");break;
			 case 'è': sb.append("&egrave;");break;
			 case 'È': sb.append("&Egrave;");break;
			 case 'ê': sb.append("&ecirc;");break;
			 case 'Ê': sb.append("&Ecirc;");break;
			 case 'ë': sb.append("&euml;");break;
			 case 'Ë': sb.append("&Euml;");break;
			 case 'ï': sb.append("&iuml;");break;
			 case 'Ï': sb.append("&Iuml;");break;
			 case 'ô': sb.append("&ocirc;");break;
			 case 'Ô': sb.append("&Ocirc;");break;
			 case 'ö': sb.append("&ouml;");break;
			 case 'Ö': sb.append("&Ouml;");break;
			 case 'ø': sb.append("&oslash;");break;
			 case 'Ø': sb.append("&Oslash;");break;
			 case 'ß': sb.append("&szlig;");break;
			 case 'ù': sb.append("&ugrave;");break;
			 case 'Ù': sb.append("&Ugrave;");break;         
			 case 'û': sb.append("&ucirc;");break;         
			 case 'Û': sb.append("&Ucirc;");break;
			 case 'ü': sb.append("&uuml;");break;
			 case 'Ü': sb.append("&Uuml;");break;
			 case '®': sb.append("&reg;");break;         
			 case '©': sb.append("&copy;");break;   
			 case '€': sb.append("&euro;"); break;
			 // be carefull with this one (non-breaking whitee space)
			 case ' ': sb.append("&nbsp;");break;         
         
			 default:  sb.append(c); break;
		  }
	   }
	   return sb.toString();
	}	
	
}
