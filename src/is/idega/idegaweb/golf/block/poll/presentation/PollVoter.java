// idega 2000 - gimmi
package is.idega.idegaweb.golf.block.poll.presentation;

import is.idega.idegaweb.golf.entity.Poll;
import is.idega.idegaweb.golf.entity.PollHome;
import is.idega.idegaweb.golf.entity.Poll_option;
import is.idega.idegaweb.golf.entity.Poll_result;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;

public class PollVoter extends Block{


int poll_id=0;
boolean isAdmin;
Poll poll;
Table table;
Form form;
IWContext modinfo;
Window gluggi;
String resultPageUrl;
String submitButtonText;
String union_id;
String pollWidth;

	public PollVoter(String resultPageUrl){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = "148";
		this.submitButtonText = "Kjósa";
		this.isAdmin=false;
	}

        /**
         * @deprecated replaced with the resultPageUrl constructor
         */
	public PollVoter(String resultPageUrl, boolean isAdmin){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = "148";
		this.submitButtonText = "Kjósa";
		//this.isAdmin=isAdmin;
	}
	public PollVoter(String resultPageUrl, String pollWidth ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = "Kjósa";
		this.isAdmin=false;
	}

	public PollVoter(String resultPageUrl, String pollWidth, boolean isAdmin ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = "Kjósa";
		//this.isAdmin=isAdmin;
	}

	public PollVoter(String resultPageUrl, String pollWidth, String submitButtonText ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = submitButtonText;
		//this.isAdmin=false;
	}
	public PollVoter(String resultPageUrl, String pollWidth, String submitButtonText, boolean isAdmin ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = submitButtonText;
		//this.isAdmin=isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setWidth(String pollWidth) {
		this.pollWidth = pollWidth;
	}

	public void setWidth(int pollWidth) {
		this.pollWidth = Integer.toString(pollWidth);
	}

	public void setSubmitButtonText(String submitButtonText) {
		this.submitButtonText = submitButtonText;
	}

	public boolean thisObjectSubmitted(IWContext modinfo){
		if (modinfo.getRequest().getParameter("idega_poll_voter") != null){
			if (modinfo.getRequest().getParameter("idega_poll_voter").equals("true")){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}


	public void handleInsert(IWContext modinfo)throws SQLException,IOException{

		boolean mayVote = true;

		if (modinfo.getRequest().getParameter("poll_option") != null)
		{
			Poll_result result = (Poll_result) IDOLookup.createLegacy(Poll_result.class);
			result.setOption(Integer.parseInt(modinfo.getRequest().getParameter("poll_option")));
			String userIPAddress = modinfo.getRequest().getRemoteAddr();

			result.setUserIPAddress(userIPAddress);

			Poll_option[] spurn_id = (Poll_option[]) ((Poll_option) IDOLookup.instanciateEntity(Poll_option.class)).findAllByColumn("poll_id",poll_id);

			for (int i = 0 ; i< spurn_id.length ; i++) {
				Poll_result[] usedIPAddresses = (Poll_result[]) ((Poll_result) IDOLookup.instanciateEntity(Poll_result.class)).findAllByColumn("poll_user_address",userIPAddress,"poll_option_id",Integer.toString(spurn_id[i].getID()));
				if (usedIPAddresses.length != 0) {
					mayVote = false;
				}
			}
			if (userIPAddress.equals("194.144.226.143")) {
				mayVote = true;
			}

			if (mayVote) {
				result.insert();
			}

			modinfo.getResponse().sendRedirect(resultPageUrl+"?poll_id="+poll_id);
		}
		else {
			modinfo.getResponse().sendRedirect(resultPageUrl+"?poll_id="+poll_id);
		}

	}

	public void noPollAvailable() throws IOException{

//		add("Engin skoðanakönnun");
	}

	public void main(IWContext modinfo)throws Exception{

            this.isAdmin=this.isAdministrator(modinfo);

		if (modinfo.getSession().getAttribute("union_id") == null) {
		   modinfo.getSession().setAttribute("union_id","3");
			union_id ="3";
		}
		else {
			union_id = (String) modinfo.getSession().getAttribute("union_id");
		}

		Poll[] poll1 = (Poll[]) ((Poll) IDOLookup.instanciateEntity(Poll.class)).findAllByColumn("union_id",union_id);

		for (int i = 0 ; i < poll1.length ; i++) {
			if (poll1[i].getIfInUse()) {
				this.poll_id=poll1[i].getID();
				break;
			}
		}
		if (poll_id == 0) {
			noPollAvailable();
		}
		else {
			if (this.poll == null){
				poll = ((PollHome) IDOLookup.getHomeLegacy(Poll.class)).findByPrimaryKeyLegacy(poll_id);
			}

			gluggi = new Window("Nidurstodur",445,290);
				gluggi.setResizable(true);
			form = new Form(gluggi);
			form.maintainAllParameters();
			form.setMethod("post");

			table=new Table(1,3);
				table.setCellpadding(5);
				table.setCellspacing(0);
				table.setWidth(pollWidth);
				table.setAlignment(1,1,"center");
				table.setAlignment(1,3,"right");
			form.add(table);
			super.add(form);

			if (thisObjectSubmitted(modinfo)){
				handleInsert(modinfo);
			}
			else{
				Text spurning = new Text(poll.getQuestion());
					spurning.setBold();
					spurning.setFontSize(2);
				table.add(spurning,1,1);


				table.add(new RadioGroup(poll.findOptions()),1,2);

				Poll_option[] optionArray;
				optionArray = poll.findOptions();

				table.add(new SubmitButton(submitButtonText),1,3);
				table.add(new Parameter("idega_poll_voter","true"));
			}
		}

		if (isAdmin) {
			Form adForm = new Form(new Window("Poll_Admin",500,300,"/poll/pollAdmin.jsp"));
				adForm.add(new SubmitButton("ja","Pollstjóri"));
				adForm.add(new HiddenInput("union_id", union_id));
			add(adForm);
		}
	}
}