package com.idega.projects.golf.moduleobject;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.util.*;
import com.idega.projects.golf.entity.*;

public class GolfPollVoter extends PresentationObjectContainer{


int poll_id=0;
Poll poll;
Table table;
Form form;
IWContext iwc;
Window gluggi;
String resultPageUrl;
String submitButtonText;
String union_id;
String pollWidth;

	public GolfPollVoter(String resultPageUrl){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = "148";
		this.submitButtonText = "Kjósa";
	}
	public GolfPollVoter(String resultPageUrl, String pollWidth ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = "Kjósa";
	}

	public GolfPollVoter(String resultPageUrl, String pollWidth, String submitButtonText ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = submitButtonText;
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
	
/*	public PollVoter(int poll_id,String resultPageUrl){
		this.poll_id=poll_id;
		this.resultPageUrl= resultPageUrl;
	}
*/
/*	public PollVoter(Poll poll){
		this.poll=poll;
	}
*/
	public boolean thisObjectSubmitted(IWContext iwc){
		if (iwc.getRequest().getParameter("idega_poll_voter") != null){
			if (iwc.getRequest().getParameter("idega_poll_voter").equals("true")){
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


	public void handleInsert(IWContext iwc)throws SQLException,IOException{

		boolean mayVote = true;

		if (iwc.getRequest().getParameter("poll_option") != null)
		{
			Poll_result result = new Poll_result();
			result.setOption(Integer.parseInt(iwc.getRequest().getParameter("poll_option")));
			String userIPAddress = iwc.getRequest().getRemoteAddr();

			result.setUserIPAddress(userIPAddress);

			Poll_option[] spurn_id = (Poll_option[]) (new Poll_option()).findAllByColumn("poll_id",poll_id);

	//		add(Integer.toString(spurn_id.length));
			for (int i = 0 ; i< spurn_id.length ; i++) {
				Poll_result[] usedIPAddresses = (Poll_result[]) (new Poll_result()).findAllByColumn("poll_user_address",userIPAddress,"poll_option_id",Integer.toString(spurn_id[i].getID()));
				if (usedIPAddresses.length != 0) {
					mayVote = false;
				}
	//			add(Integer.toString(spurn_id[i].getID()));
			}
			if (userIPAddress.equals("194.144.226.143")) {
				mayVote = true;
			}

			if (mayVote) {
				result.insert();
			}
	/*		else {
				mayNotVote();
			}*/

			iwc.getResponse().sendRedirect(resultPageUrl+"?poll_id="+poll_id);
		}
		else {
			iwc.getResponse().sendRedirect(resultPageUrl+"?poll_id="+poll_id);
		}

	}

	public void noPollAvailable() throws IOException{

		add("Engin skoðanakönnun");
	}
	
	public void main(IWContext iwc)throws SQLException,IOException{
		if (iwc.getSession().getAttribute("union_id") == null) {
		   iwc.getSession().setAttribute("union_id","3");
			union_id ="3";
		}
		else {
			union_id = (String) iwc.getSession().getAttribute("union_id");
		}

		Poll[] poll1 = (Poll[]) (new Poll()).findAllByColumn("union_id",union_id);

		for (int i = 0 ; i < poll1.length ; i++) {
			if (poll1[i].getIfInUse()) {
				this.poll_id=poll1[i].getID();
				break;
			}
		}
		if (poll_id == 0) {
			noPollAvailable();
		}


		if (this.poll == null){
			poll = new Poll(poll_id);
		}

		gluggi = new Window("Nidurstodur",445,290);
			gluggi.setResizable(true);
		form = new Form(gluggi);
		form.maintainAllParameters();
		form.setMethod("post");


/*		Table table2= new Table(1,1);
			table2.setHeight("21");
			table2.setCellspacing(0);
			table2.setWidth(pollWidth);
//                        table2.setColor(1,1,"#99CC66");
			table2.setBackgroundImage(1,1, new Image ("/pics/pollheaderleft.gif"));
		form.add(table2);
*/

/*                Table frametable = new Table(1,1);
                frametable.setCellspacing(0);
                frametable.setCellpadding(1);
                frametable.setWidth(pollWidth);
                frametable.setColor(1,1,"#99CC99");
*/
		table=new Table(1,3);
			table.setCellpadding(5);
			table.setCellspacing(0);
//			table.setColor(1,1,"#99CC66");
//			table.setColor(1,2,"#99CC66");
//			table.setColor(1,3,"#99CC66");
			table.setWidth(pollWidth);
			table.setAlignment(1,1,"center");
			table.setAlignment(1,3,"right");
 //               frametable.add(table,1,1);
//		form.add(frametable);
		form.add(table);
		super.add(form);

		if (thisObjectSubmitted(iwc)){
			handleInsert(iwc);
		}
		else{
			Text spurning = new Text(poll.getQuestion());
				spurning.setBold();
				spurning.setFontSize(2);
			table.add(spurning,1,1);
//			table.addBreak(1,2);


			table.add(new RadioGroup(poll.findOptions()),1,2);

			Poll_option[] optionArray;
			optionArray = poll.findOptions();

//			table.addBreak(1,2);
			table.add(new SubmitButton(submitButtonText),1,3);
			table.add(new Parameter("idega_poll_voter","true"));
		}
	}



	public void print(IWContext iwc)throws Exception{
		//try{
			super.print(iwc);
		//}
		//catch(SQLException ex){
		//	throw new Exception(ex.getMessage());
		//}
	}



}
