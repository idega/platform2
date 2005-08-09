// idega 2000 - gimmi
package com.idega.block.poll.presentation;

import java.util.HashMap;
import java.util.Map;

import com.idega.block.poll.business.PollBusiness;
import com.idega.block.poll.business.PollFinder;
import com.idega.block.poll.business.PollListener;
import com.idega.block.poll.data.PollAnswer;
import com.idega.block.poll.data.PollEntity;
import com.idega.block.poll.data.PollQuestion;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

public class Poll2 extends Block implements Builderaware {

	private boolean _isAdmin;
	private int _pollID;
	private String _sAttribute = null;
	private int _iLocaleID;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.poll";

	private final static String TEXT_STYLE = "TextStyle";
	private final static String LINK_STYLE = "LinkStyle";
	private final static String QUESTION_STYLE = "QuestionStyle";
	private final static String ANSWER_STYLE = "AnswerStyle";
	private final static String BUTTON_STYLE = "ButtonStyle";
	private final static String RADIO_STYLE = "RadioStyle";

	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwbPoll;

	private Layer mainLayer;

	public static String _prmPollID = "po.poll_id";
	public static String _prmPollCollection = "po.poll_collection";
	public static String _prmShowVotes = "po.show_votes";
	public static String _prmNumberOfPolls = "po.number_of_votes";

	private boolean _newObjInst = false;
	private boolean _newWithAttribute = false;

	private String _votedColor;
	private String _whiteColor;

	private String _pollWidth;

	private int _numberOfShownPolls;
	private boolean _showVotes;

	private boolean _showCollection;

	private IWTimestamp _date;

	private Image _linkImage;
	private Image _linkOverImage;
	private Image _questionImage;

	private boolean _showInformation = false;

	private String _questionAlignment;
	private String _name;

	public static final int RADIO_BUTTON_VIEW = 1;
	public static final int LINK_VIEW = 2;

	private int _layout = LINK_VIEW;
		
	public Poll2() {
		setDefaultValues();
	}

	public Poll2(String sAttribute) {
		this();
		_pollID = -1;
		_sAttribute = sAttribute;
	}

	public Poll2(int pollID) {
		this();
		_pollID = pollID;
	}

	public void main(IWContext iwc) throws Exception {
		_iwrb = getResourceBundle(iwc);
		_iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		_iwbPoll = getBundle(iwc);

		_isAdmin = iwc.hasEditPermission(this);
		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		_date = new IWTimestamp();

		PollEntity poll = null;

		if (_pollID <= 0) {
			String sPollID = iwc.getParameter(_prmPollID);
			if (sPollID != null)
				_pollID = Integer.parseInt(sPollID);
			else if (getICObjectInstanceID() > 0) {
				_pollID = PollFinder.getRelatedEntityId(getICObjectInstance());
				if (_pollID <= 0) {
					_newObjInst = true;
					PollBusiness.savePoll(_pollID, -1, getICObjectInstanceID(), null);
				}
			}
		}

		if (_newObjInst) {
			_pollID = PollFinder.getRelatedEntityId(((com.idega.core.component.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(getICObjectInstanceID()));
		}

		if (_pollID > 0) {
			poll = ((com.idega.block.poll.data.PollEntityHome) com.idega.data.IDOLookup.getHomeLegacy(PollEntity.class)).findByPrimaryKeyLegacy(_pollID);
		}
		else if (_sAttribute != null) {
			poll = PollFinder.getPoll(_sAttribute);
			if (poll != null) {
				_pollID = poll.getID();
			}
			_newWithAttribute = true;
		}

		if (_isAdmin) {
			getMainLayer().add(getAdminPart(_pollID, _newObjInst, _newWithAttribute));		
		}

		getMainLayer().add(getPoll(iwc, poll));
	
		add(getMainLayer());
	}

	private Layer getAdminPart(int pollID, boolean newObjInst, boolean newWithAttribute) {
		Image editImage = _iwb.getImage("shared/edit.gif");
		
		Link adminLink = new Link(editImage);
		adminLink.setWindowToOpen(PollAdminWindow.class, this.getICObjectInstanceID());
		adminLink.addParameter(PollAdminWindow.prmID, pollID);
		if (newObjInst)
			adminLink.addParameter(PollAdminWindow.prmObjInstId, getICObjectInstanceID());
		else if (newWithAttribute) adminLink.addParameter(PollAdminWindow.prmAttribute, _sAttribute);

		Layer adminPart = new Layer();
		adminPart.setStyleClass("adminPart");
		adminPart.addChild(adminLink);
		
		return adminPart;
	}

	private PresentationObject getPoll(IWContext iwc, PollEntity poll) {
		LocalizedText locText = null;
		PollQuestion pollQuestion = PollBusiness.getQuestion(poll);
		IWTimestamp after;
		boolean pollByDate = false;

		if (pollQuestion != null) {
			if (pollQuestion.getEndTime() != null) {
				after = new IWTimestamp(pollQuestion.getEndTime());
				if (_date.isLaterThan(after)) {
					pollQuestion = PollBusiness.getPollByDate(poll, _date);
					pollByDate = true;
				}
			}
		}
		else {
			pollQuestion = PollBusiness.getPollByDate(poll, _date);
			pollByDate = true;
		}

		if (pollQuestion != null) {
			locText = TextFinder.getLocalizedText(pollQuestion, _iLocaleID);
			if (pollByDate) {
				PollBusiness.setPollQuestion(poll, pollQuestion);
			}
		}

		PresentationObject obj = null;

		if (locText != null) {
			switch (_layout) {
				case RADIO_BUTTON_VIEW:
					obj = getRadioButtonView(locText, pollQuestion);
					break;
				case LINK_VIEW:
					obj = getLinkView(iwc, locText, pollQuestion);
					break;
			}
			return obj;
		}
		else {
			return new Form();
		}
	}

	private Form getRadioButtonView(LocalizedText locText, PollQuestion pollQuestion) {
		Form form = new Form();
		form.setWindowToOpen(PollResult.class);
		
		Paragraph questionParagraph = new Paragraph();
		questionParagraph.setStyleClass("question");
		Text question = new Text(locText.getHeadline());				
		questionParagraph.add(question);		
		form.addChild(questionParagraph);
		
		PollAnswer[] answers = PollBusiness.getAnswers(pollQuestion.getID());
		boolean hasAnswers = false;	
		
		Lists listOfAnswers = new Lists();
		listOfAnswers.setListOrdered(false);
		
		if (answers != null) {
			
			for (int a = 0; a < answers.length; a++) {
				LocalizedText locAnswerText = TextFinder.getLocalizedText(answers[a], _iLocaleID);
				if (locAnswerText != null) {
					//add radio button and label to layout
					hasAnswers = true;
					ListItem listItem = new ListItem();	
					RadioButton radioButton = new RadioButton(PollBusiness._PARAMETER_POLL_ANSWER, String.valueOf(answers[a].getID()));
					listItem.add(radioButton);					
					
					Label label = new Label(locAnswerText.getHeadline(), radioButton);					
					listItem.add(label);
					
					listOfAnswers.addChild(listItem);
				}
			}
		}
		
		if (hasAnswers) {
			form.add(listOfAnswers);
		}
		
		if (_showCollection) {
			GenericButton collectionLink = getOlderPollsButton();
			form.addChild(collectionLink);
		}
		
		SubmitButton voteButton = new SubmitButton(_iwrb.getLocalizedString("vote", "Vote")); 
		voteButton.setStyleClass("vote");
		form.getChildren().add(voteButton);
		
		form.add(new Parameter(PollBusiness._PARAMETER_POLL_VOTER, PollBusiness._PARAMETER_TRUE));
		form.add(new Parameter(PollBusiness._PARAMETER_POLL_QUESTION, Integer.toString(pollQuestion.getID())));
		if (_showVotes) {
			form.add(new Parameter(Poll._prmShowVotes, PollBusiness._PARAMETER_TRUE));
		}
		else {
			form.add(new Parameter(Poll._prmShowVotes, PollBusiness._PARAMETER_FALSE));
		}		
			
		return form;
	}

	private PresentationObject getLinkView(IWContext iwc, LocalizedText locText, PollQuestion pollQuestion) {
		PresentationObject pollContainer = new PresentationObjectContainer();
		
		//question
		Paragraph questionParagraph = new Paragraph();
		questionParagraph.setStyleClass("question");
		Text question = new Text(locText.getHeadline());		
		if (_questionImage != null) {
			questionParagraph.add(_questionImage);			
		}		
		questionParagraph.add(question);		
		pollContainer.addChild(questionParagraph);	
		
		PollAnswer[] answers = PollBusiness.getAnswers(pollQuestion.getID());
		
		boolean canVote = true;
		if (iwc.getParameter(PollBusiness._PARAMETER_POLL_VOTER) != null) canVote = false;
		if (canVote) canVote = PollBusiness.canVote(iwc, pollQuestion.getID());
		
		Lists listOfAnswers = new Lists();
		listOfAnswers.setListOrdered(false);			
		
		if (canVote) { //user can vote (has not voted yet)
			boolean hasAnswers = false;

			if (answers != null) {
				for (int a = 0; a < answers.length; a++) {
					LocalizedText locAnswerText = TextFinder.getLocalizedText(answers[a], _iLocaleID);
					
					ListItem listItem = new ListItem();	
					
					if (locAnswerText != null) {
						hasAnswers = true;
						
						Link answerLink = getStyleLink(locAnswerText.getHeadline(), LINK_STYLE);
						answerLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION, pollQuestion.getID());
						answerLink.addParameter(PollBusiness._PARAMETER_POLL_ANSWER, answers[a].getID());
						answerLink.addParameter(PollBusiness._PARAMETER_POLL_VOTER, PollBusiness._PARAMETER_TRUE);
						answerLink.addParameter(PollBusiness._PARAMETER_CLOSE, PollBusiness._PARAMETER_TRUE);
						answerLink.setEventListener(PollListener.class);
						
						if (_name != null) answerLink.setStyle(_name);
						
						if (_linkImage != null) {							
							Image image = new Image(_linkImage.getMediaURL(iwc));
							image.setVerticalSpacing(3); //TODO is it really necessary, shouldn't it be defined in CSS?
							
							if (_linkOverImage != null) {
								image.setOverImage(_linkOverImage);
								_linkOverImage.setVerticalSpacing(3);
								answerLink.setMarkupAttribute("onMouseOver", "swapImage('" + image.getName() + "','','" + _linkOverImage.getMediaURL(iwc) + "',1)");
								answerLink.setMarkupAttribute("onMouseOut", "swapImgRestore()");
							}		
							
							listItem.add(image);
							listItem.add(answerLink);							
						} else {							
							listItem.add(answerLink);
						}
						
						listOfAnswers.addChild(listItem);
						
					}
				}
			}
			
			if (hasAnswers) {
				pollContainer.addChild(listOfAnswers);
			}				
			
		} else { //user has woted, let's show results to user
			
			int total = 0;
			
			if (answers != null) {
				if (answers.length > 0) {
					for (int i = 0; i < answers.length; i++) {
						total += answers[i].getHits();
					}
					for (int i = 0; i < answers.length; i++) {
						LocalizedText answerLocText = TextFinder.getLocalizedText(answers[i], _iLocaleID);
						
						ListItem listItem = new ListItem();
						
						if (answerLocText != null) {
							
							float percent = 0;
							if (answers[i].getHits() > 0) percent = ((float) answers[i].getHits() / (float) total) * 100;

							Text answerText = new Text(answerLocText.getHeadline());
							if (_showVotes || _isAdmin) {
								answerText.addToText(" (" + Integer.toString(answers[i].getHits()) + ")");
							}
							Text percentText = new Text(com.idega.util.text.TextSoap.decimalFormat(percent, 1) + "%");							
							
							Paragraph answerParagraph = new Paragraph();
							answerParagraph.setStyleClass("answer");
							answerParagraph.add(answerText);
							listItem.add(answerParagraph);							

							Layer barLayer = new Layer();
							barLayer.setStyleClass("percentageBar");
							
							Layer gaugeLayer = new Layer();	
							gaugeLayer.setStyleClass("percentageLevel");
							gaugeLayer.setStyleAttribute("width: " + Integer.toString((int) percent) + "%;");
							
							barLayer.add(gaugeLayer);
							listItem.add(barLayer);							
							
							Paragraph percentParagraph = new Paragraph();
							percentParagraph.setStyleClass("percent");
							percentParagraph.add(percentText);
							listItem.add(percentParagraph);	
							
						}
					
						listOfAnswers.add(listItem);						
					}					
					pollContainer.addChild(listOfAnswers);					
				}
				String information = PollBusiness.getLocalizedInformation(pollQuestion.getID(), _iLocaleID);
				if (information != null && _showInformation) {
					Text informationText = new Text(information);
					Paragraph informationParagraph = new Paragraph();
					informationParagraph.setStyleClass("information");
					informationParagraph.add(informationText);
					pollContainer.addChild(informationParagraph);
				}				
			}		
		}		

		if (_showCollection) {
			GenericButton collectionLink = getOlderPollsButton();
			pollContainer.addChild(collectionLink);
		}
		
		return pollContainer;	
		
	}

	private GenericButton getOlderPollsButton() {
		GenericButton collectionLink = new GenericButton("", _iwrb.getLocalizedString("older_polls", "Older polls"));
		collectionLink.setStyleClass("olderPolls");
		collectionLink.setWindowToOpen(PollResult.class);
		collectionLink.addParameterToWindow(Poll._prmPollID, _pollID);
		collectionLink.addParameterToWindow(Poll._prmPollCollection, PollBusiness._PARAMETER_TRUE);
		collectionLink.addParameterToWindow(Poll._prmNumberOfPolls, _numberOfShownPolls);
		if (_showVotes) {
			collectionLink.addParameterToWindow(Poll._prmShowVotes, PollBusiness._PARAMETER_TRUE);
		}
		else {
			collectionLink.addParameterToWindow(Poll._prmShowVotes, PollBusiness._PARAMETER_FALSE);
		}
		return collectionLink;
	}

	private void setDefaultValues() {
		_pollWidth = "100%";
		_numberOfShownPolls = 3;
		_showVotes = true;
		_showCollection = true;
		_questionAlignment = "left";
		_pollID = -1;
		_votedColor = "#104584";
		_whiteColor = "#FFFFFF";
	}

	public boolean deleteBlock(int ICObjectInstanceId) {
		PollEntity poll = PollFinder.getObjectInstanceFromID(ICObjectInstanceId);
		return PollBusiness.deletePoll(poll);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void setWidth(int pollWidth) {
		_pollWidth = Integer.toString(pollWidth);
	}

	public void setWidth(String pollWidth) {
		_pollWidth = pollWidth;
	}

	public void setNumberOfShownPolls(int numberOfShownPolls) {
		_numberOfShownPolls = numberOfShownPolls;
	}

	public void showVotes(boolean showVotes) {
		_showVotes = showVotes;
	}

	public void showCollection(boolean collection) {
		_showCollection = collection;
	}

	public void setQuestionImage(Image image) {
		_questionImage = image;
	}

	public void setLinkImage(Image image) {
		_linkImage = image;
	}

	public void setLinkOverImage(Image image) {
		_linkOverImage = image;
	}

	public void setLayout(int layout) {
		_layout = layout;
	}

	public void setShowInformation(boolean showInformation) {
		_showInformation = showInformation;
	}

	public void setQuestionAlignment(String alignment) {
		_questionAlignment = alignment;
	}

	public void setVotedColor(String color) {
		_votedColor = color;
	}

	public void setWhiteColor(String color) {
		_whiteColor = color;
	}

	public Object clone() {
		Poll2 obj = null;
		try {
			obj = (Poll2) super.clone();

			if (this.mainLayer != null) {
				obj.mainLayer = (Layer) this.mainLayer.clone();
			}
			if (this._linkImage != null) {
				obj._linkImage = (Image) this._linkImage.clone();
			}
			if (this._linkOverImage != null) {
				obj._linkOverImage = (Image) this._linkOverImage.clone();
			}
			if (this._questionImage != null) {
				obj._questionImage = (Image) this._questionImage.clone();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		String returnString = iwc.getParameter(PollBusiness._PARAMETER_POLL_VOTER);

		if (returnString == null)
			returnString = "";
		else {
			returnString = "";//minimise the number of states cached
			setCacheable(false);//do this when you want to be sure to go through
													// main(iwc) and no cache.
			invalidateCache(iwc, cacheStatePrefix + Boolean.TRUE);
			invalidateCache(iwc, cacheStatePrefix + Boolean.FALSE);
		}

		try {
			_pollID = PollFinder.getRelatedEntityId(getICObjectInstance());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		String canVote = String.valueOf(PollBusiness.canVote(iwc, _pollID));
		if (canVote.equals("false")) returnString = "";

		return cacheStatePrefix + canVote + returnString;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(TEXT_STYLE, "font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; text-decoration: none;");
		map.put(QUESTION_STYLE, "font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11pt; font-weight: bold");
		map.put(ANSWER_STYLE, "font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; text-decoration: none;");
		map.put(BUTTON_STYLE, "font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; border: 1px solid #000000;");
		map.put(RADIO_STYLE, "font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; width: 12px; height: 12px;");
		map.put(LINK_STYLE, "font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; text-decoration: none;");
		map.put(LINK_STYLE+":hover", "font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; text-decoration: none;");
		
		return map;
	}

	private Layer getMainLayer() {
		if (mainLayer == null) {
			mainLayer = new Layer();
			mainLayer.setStyleClass("poll");
		}
		return mainLayer;
	}

	private void setMainLayer(Layer mainLayer) {
		this.mainLayer = mainLayer;
	}

	public String getMainStyleClass() {
		return getMainLayer().getStyleClass();
	}

	public void setMainStyleClass(String mainStyleClass) {
		getMainLayer().setStyleClass(mainStyleClass);
	}
}