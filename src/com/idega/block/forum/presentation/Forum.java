package com.idega.block.forum.presentation;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.block.category.business.CategoryBusiness;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.forum.business.ForumBusiness;
import com.idega.block.forum.business.ForumTree;
import com.idega.block.forum.data.ForumData;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.event.IWPresentationState;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.StatefullPresentation;
import com.idega.presentation.StatefullPresentationImplHandler;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * Title:        Forum block<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmi?lun hf.<p>
 * Company:      idega margmi?lun hf.<p>
 * @author idega 2002 - idega team - <a href="mailto:laddi@idega.is">laddi@idega.is</a>
 * @version 1.2
 */

public class Forum extends CategoryBlock implements Builderaware, StatefullPresentation {
	protected int _objectID = -1;
	private int _selectedObjectID = -1;
	protected int _topicID = -1;
	private int _threadID = -1;
	private boolean _isAdmin = false;
	private boolean _hasAddPermission = true;
	private boolean _hasReplyPermission = true;
	private boolean _hasDeletePermission = false;
	private String _attribute;
	private int _iLocaleID;
	private int _bodyIndent = 2;
	
	protected int _firstThread = 1;
	protected int _lastThread = 10;
	private int _numberOfThreads = 10;
	private String _spaceBetween = "8";
	private boolean _showOverviewLink = true;
	protected boolean _showTopicName = false;
	private boolean _showResponses = true;
	private boolean iShowTopicInCollection = true;

	protected int _state = ForumBusiness.FORUM_TOPICS;
	private int _initialState = ForumBusiness.FORUM_TOPICS;
	private int _openLevel = 0;
	private Table _myTable;

	protected static final String HEADER_STYLE = "HeaderStyle";
	protected static final String LINK_STYLE = "LinkStyle";
	protected static final String TOPIC_LINK_STYLE = "TopicLinkStyle";
	protected static final String THREAD_LINK_STYLE = "ThreadLinkStyle";
	protected static final String TEXT_STYLE = "TextStyle";
	protected static final String SMALL_TEXT_STYLE = "SmallTextStyle";
	protected static final String HEADING_STYLE = "HeadingStyle";
	protected static final String INFORMATION_STYLE = "InformationStyle";
	protected static final String TABLE_TEXT_STYLE = "TableTextStyle";

	protected static final String LIGHT_ROW_STYLE = "LightRowStyle";
	protected static final String DARK_ROW_STYLE = "DarkRowStyle";
	protected static final String HEADER_ROW_STYLE = "HeaderRowStyle";
	protected static final String BODY_ROW_STYLE = "BodyRowStyle";
	
	protected String _headingColor;

	private String _width;
	private ICPage _page;
	private ICPage _threadPage;
	private Image _threadImage;
	private Image iNextImage;
	private Image iPreviousImage;

	protected static String AddPermission = "add";
	protected static String ReplyPermission = "reply";
	
	protected final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.forum";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwcb;
	protected ForumBusiness forumBusiness;
	
	private boolean ignoreObjectID = false;

	/**
	 * @todo implement Statehandling for Forum
	 */
	private StatefullPresentationImplHandler stateHandler = new ForumTree().getStateHandler();

	private String _authorWidth = "160";
	private String _replyWidth = "60";
	private String _dateWidth = "100";
	private String iThreadTopicWidth = "100";
	private String iUpdatedTopicWidth = "100";
	
	private boolean iShowForumLinks = true;
	private boolean userCanEditHisOwnThreads = false;
	private boolean showForumLinksOnTopOfThreadList = true;	

	public Forum() {
		setDefaultValues();
	}

	/**
	 * Temporary implementation
	 * returning stateClass for the ForumTree Object
	 */
	public Class getPresentationStateClass() {
		return this.stateHandler.getPresentationStateClass();
	}

	public IWPresentationState getPresentationState(IWUserContext iwuc) {
		return this.stateHandler.getPresentationState(this, iwuc);
	}

	public boolean hasDeletePermission(IWContext iwc) {
		return iwc.hasEditPermission(this);
	}
	
	public boolean hasAddPermission(IWContext iwc) {
		return iwc.hasPermission(AddPermission, this);
	}
	
	public boolean hasReplyPermission(IWContext iwc) {
		return iwc.hasPermission(ReplyPermission, this);
	}
	
	public void main(IWContext iwc) throws Exception {
		this._iwrb = getResourceBundle(iwc);
		this._iwb = getBundle(iwc);
		this._iwcb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

		this._isAdmin = iwc.hasEditPermission(this);
		this._iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		this._objectID = getICObjectInstanceID();

		getParameters(iwc);
		this._hasDeletePermission = hasDeletePermission(iwc);
		this._hasAddPermission = hasAddPermission(iwc);
		this._hasReplyPermission = hasReplyPermission(iwc);

		this.forumBusiness = new ForumBusiness();

		this._myTable = new Table();
		this._myTable.setCellpadding(0);
		this._myTable.setCellspacing(0);
		this._myTable.setBorder(0);
		this._myTable.setWidth(this._width);

		int row = 1;
		if (this._isAdmin) {
			this._myTable.add(getAdminPart(iwc), 1, row);
			row++;
		}

		Table table = getForum(iwc);
		if (table != null) {
			this._myTable.add(table, 1, row);
		}
		add(this._myTable);
	}

	private Table getForum(IWContext iwc) {
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth(Table.HUNDRED_PERCENT);

		switch (this._state) {
			case ForumBusiness.FORUM_TOPICS :
				getForumTopics(iwc, table);
				break;
			case ForumBusiness.FORUM_THREADS :
				getForumThreads(iwc, table);
				break;
			case ForumBusiness.FORUM_COLLECTION :
				getForumCollection(iwc, table);
				break;
			case ForumBusiness.TOPIC_COLLECTION :
				getTopicCollection(iwc, table);
				break;
		}

		return table;
	}

	protected void getForumTopics(IWContext iwc, Table table) {
		iwc.removeSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD + "_" + this._objectID);
		iwc.removeSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD + "_" + this._objectID);

		Text topicText = getStyleText(this._iwrb.getLocalizedString("topics", "Topics"), HEADER_STYLE);
		Text threadsText = getStyleText(this._iwrb.getLocalizedString("threads", "Threads"), HEADER_STYLE);
		Text updatedText = getStyleText(this._iwrb.getLocalizedString("last_updated", "Last updated"), HEADER_STYLE);

		table.setWidth(2, getThreadTopicWidth());
		table.setWidth(3, getUpdatedTopicWidth());

		table.add(topicText, 1, 1);
		table.add(threadsText, 2, 1);
		table.add(updatedText, 3, 1);
		if (this._headingColor != null) {
			table.setRowColor(1, this._headingColor);
		}
		table.setRowStyleClass(1, getStyleName(HEADER_ROW_STYLE));

		Vector list = new Vector();
		list.addAll(this.getCategories());

		if (list != null) {
			Link topicLink = null;
			Text numberOfThreadsText = null;
			Text lastUpdatedText = null;
			ICCategory topic;
			int row = 2;

			for (int a = 0; a < list.size(); a++) {
				topic = (ICCategory) list.get(a);

				if (topic != null) {
					topicLink = getStyleLink(topic.getName(), TOPIC_LINK_STYLE);
					topicLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, topic.getID());
					topicLink.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
					topicLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);
					if (this._page != null) {
						topicLink.setPage(this._page);
					}

					int numberOfThreads = this.forumBusiness.getNumberOfThreads(topic);
					numberOfThreadsText = formatText(String.valueOf(numberOfThreads), TABLE_TEXT_STYLE);

					ForumData newestThread = this.forumBusiness.getNewestThreads(topic);
					if (newestThread != null) {
						lastUpdatedText = getThreadDate(iwc, newestThread, TABLE_TEXT_STYLE);
					}
					else {
						lastUpdatedText = null;
					}

					table.add(getThreadImage(),1,row);
					table.add(formatText(Text.NON_BREAKING_SPACE), 1, row);
					table.add(topicLink, 1, row);
					table.add(numberOfThreadsText, 2, row);
					if (lastUpdatedText != null) {
						table.add(lastUpdatedText, 3, row);
					}
					
					if (row % 2 == 0) {
						table.setRowStyleClass(row, getStyleName(LIGHT_ROW_STYLE));
					}
					else {
						table.setRowStyleClass(row, getStyleName(DARK_ROW_STYLE));
					}

					row++;
				}
			}
		}

		table.setColumnAlignment(2, "center");
		table.setColumnAlignment(3, "center");
	}

	/** Override to add below topic in ForumTreads view*/
	protected int addBelowTopic(IWContext iwc, ICCategory cat, Table table, int row) {return row;}

	protected void getForumThreads(IWContext iwc, Table table) {
		int row = 1;

		ICCategory topic = null;
		if (this._topicID != -1) {
			topic = CategoryFinder.getInstance().getCategory(this._topicID);
		}

		ForumData thread = null;
		try {
			thread = this.forumBusiness.getForumData(Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID)));
			this._threadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID));
		}
		catch (NumberFormatException e) {
			thread = null;
		}

		if (topic != null) {
			if (this._showTopicName) {
				Text topicText = formatText(topic.getName(), HEADER_STYLE);
				table.setRowColor(row, this._headingColor);
				table.setRowPadding(row, 2);
				table.add(topicText, 1, row++);
				//table.setBackgroundImage(1, row++, _iwb.getImage("shared/dotted.gif"));
			}
			
			row = addBelowTopic(iwc, topic, table, row);

			if (thread != null && thread.isValid()) {
				row = displaySelectedForum(iwc, table, row, thread, 0);
			}
			
			table.setHeight(1, row++, "20");
			if(doShowForumLinksOnTopOfThreadList()){
				table.add(getForumLinks(), 1, row++);
			}

			updateThreadCount(iwc);

			ForumData[] threads = this.forumBusiness.getThreads(topic);
			ForumData[] someThreads = this.forumBusiness.getThreads(threads, this._firstThread, this._lastThread);
			boolean hasNextThreads = this.forumBusiness.hasNextThreads(threads, this._lastThread);
			boolean hasPreviousThreads = this.forumBusiness.hasPreviousThreads(this._firstThread);

			table.add(getForumTree(iwc, someThreads), 1, row);

			if (hasNextThreads || hasPreviousThreads) {
				table.add(getNextPreviousTable(hasNextThreads, hasPreviousThreads), 1, ++row);
			}
		}
	}

	protected int displaySelectedForum(IWContext iwc, Table table, int row, ForumData thread, int depth) {
		//h�r � a� kalla � minni f�ll � sta� getThreadHeaderTable - henni ver�ur eytt
		table.add(getThreadHeaderTable(thread, iwc), 1, row++);
		table.setHeight(row++, 3);
		//table.setBackgroundImage(1, row++, _iwb.getImage("shared/dotted.gif"));
		table.add(getThreadBody(thread), 1, row);
		table.setCellpaddingLeft(1, row, this._bodyIndent);
		table.setCellpaddingRight(1, row++, this._bodyIndent);
		table.setHeight(row++, 3);
		table.add(getThreadLinks(iwc, thread), 1, row++);
		return row;
	}

	private void getForumCollection(IWContext iwc, Table table) {
		table.setCellpaddingAndCellspacing(0);
		int row = 1;

		Collection categories = this.getCategories();
		if (categories != null && categories.size() == 1) {
			this._topicID = ((ICCategory) new Vector(categories).get(0)).getID();
		}

		List list = this.forumBusiness.getThreadsInCategories(categories, this._numberOfThreads);
		if (list != null) {
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				ForumData thread = (ForumData) iter.next();

				if (this.iShowTopicInCollection) {
					ICCategory topic = thread.getTopic();
					table.add(getTopicLink(topic), 1, row);
					table.add(Text.getBreak(), 1, row);
				}
				table.add(getUser(thread), 1, row);
				table.add(formatText("," + Text.NON_BREAKING_SPACE), 1, row);
				table.add(getThreadDate(iwc, thread, TEXT_STYLE), 1, row);
				table.add(Text.getBreak(), 1, row);
				table.add(getThreadImage(), 1, row);
				table.add(Text.getNonBrakingSpace(), 1, row);
				table.add(getThreadLink(thread, TOPIC_LINK_STYLE), 1, row);
				if (this._showResponses) {
					table.add(formatText(Text.NON_BREAKING_SPACE), 1, row);
					table.add(getThreadResponses(thread), 1, row++);
				}
				else {
					row++;
				}
				table.setHeight(1, row++, this._spaceBetween);
			}
		}

		if (this.iShowForumLinks) {
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(getForumLinks(), 1, row);
		}
	}

	private void getTopicCollection(IWContext iwc, Table table) {
		int row = 1;
		table.setCellpaddingAndCellspacing(0);
		Table threadTable;

		Collection categories = getCategories();
		if (categories != null) {
			Iterator iter = categories.iterator();
			while (iter.hasNext()) {
				ICCategory category = (ICCategory) iter.next();
				table.add(getTopicLink(category), 1, row);
				table.add(Text.getNonBrakingSpace(), 1, row);
				table.add(getTopicThreadsText(category), 1, row);

				List threads = this.forumBusiness.getThreads(category, this._numberOfThreads);
				if (threads != null) {
					threadTable = new Table();
					threadTable.setCellpaddingAndCellspacing(0);
					threadTable.setWidth(Table.HUNDRED_PERCENT);
					int threadRow = 1;

					Iterator iter2 = threads.iterator();
					while (iter2.hasNext()) {
						ForumData thread = (ForumData) iter2.next();
						if (this._threadImage != null) {
							this._threadImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
							this._threadImage.setHorizontalSpacing(2);
							threadTable.add(this._threadImage, 1, threadRow);
						}
						threadTable.add(getThreadLink(thread, THREAD_LINK_STYLE), 1, threadRow);
						threadTable.add(formatText(Text.NON_BREAKING_SPACE), 1, threadRow);
						threadTable.add(getThreadResponses(thread), 1, threadRow++);
					}

					table.add(threadTable, 1, row++);
					table.setHeight(1, row++, this._spaceBetween);
				}
			}
		}
	}

	private Link getTopicLink(ICCategory category) {
		Link link = getStyleLink(category.getName(), TOPIC_LINK_STYLE);
		if (this._page != null) {
			link.setPage(this._page);
		}
		link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, category.getID());
		link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
		link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);

		return link;
	}

	private Text getTopicThreadsText(ICCategory topic) {
		int numberOfThreads = this.forumBusiness.getNumberOfThreads(topic);
		return formatText("(" + String.valueOf(numberOfThreads) + Text.NON_BREAKING_SPACE + this._iwrb.getLocalizedString("threads_lc", "threads") + ")", INFORMATION_STYLE);
	}

	private Link getThreadLink(ForumData thread, String styleName) {
		String headline = thread.getThreadSubject();
		if (headline == null) {
			headline = "";
		}

		Link link = getStyleLink(headline, styleName);
		if (this._page != null) {
			link.setPage(this._page);
		}
		link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, thread.getTopicID());
		link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
		link.addParameter(ForumBusiness.PARAMETER_THREAD_ID, String.valueOf(thread.getPrimaryKey()));
		//link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);

		return link;
	}

	private Text getThreadResponses(ForumData thread) {
		return formatText("(" + String.valueOf(thread.getNumberOfSubThreads()) + Text.NON_BREAKING_SPACE + this._iwrb.getLocalizedString("replies_lc", "replies") + ")", INFORMATION_STYLE);
	}

	protected PresentationObject getUser(ForumData thread) {
		Text text = formatText(this._iwrb.getLocalizedString("unknown", "Unknown"));
		if (thread.getUserID() != -1) {
			User user = UserBusiness.getUser(thread.getUserID());
			Email mail = UserBusiness.getUserMail(thread.getUserID());
			if (user != null && mail != null) {
				String name = user.getName();
				if (user.getDisplayName() != null && user.getDisplayName().length() > 0) {
					name = user.getDisplayName();
				}
				Link link = getStyleLink(name, LINK_STYLE);
				link.setURL("mailto:" + mail.getEmailAddress());
				return link;
			}
			else if (user != null) {
				Text userText = formatText(user.getName(), TEXT_STYLE);
				return userText;
			}
		}
		else if (thread.getUserName() != null && thread.getUserEMail() != null) {
			Link link = getStyleLink(thread.getUserName(), THREAD_LINK_STYLE);
			if (thread.getUserEMail() != null) {
				link.setURL("mailto:" + thread.getUserEMail());
			}
			return link;
		}
		else if (thread.getUserName() != null) {
			Text userText = formatText(thread.getUserName(), TEXT_STYLE);
			return userText;
		}
		return text;
	}

	protected Text getThreadDate(IWContext iwc, ForumData thread, String style) {
		IWTimestamp stamp = new IWTimestamp(thread.getThreadDate());
		return getFormattedDate(stamp, iwc, style);
	}
	
	protected Text getFormattedDate(IWTimestamp stamp, IWContext iwc, String style) {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
		Date date = new Date(stamp.getTimestamp().getTime());
		return formatText(format.format(date), style);
	}

	protected PresentationObject getForumTree(IWContext iwc, ForumData[] threads) {
		ForumTree tree = new ForumTree();
		/**
		 * @todo chage later: legacy-fix
		 */
		tree.setICObjectInstanceID(this.getICObjectInstanceID());
		System.out.println("Forum: tree.setICObjectInstanceID(" + this.getICObjectInstanceID() + ");");

		tree.setHeadingColor(this._headingColor);
		tree.setExtraHeadingColor(1, this._headingColor);
		tree.setExtraHeadingColor(2, this._headingColor);
		tree.setExtraHeadingColor(3, this._headingColor);
		tree.setColumns(2);
		tree.setParallelExtraColumns(3);
		tree.setFirstLevelNodes(threads);
		tree.setNestLevelAtOpen(this._openLevel);
		tree.setToShowTreeIcons(false);
		tree.setWidth("100%");
		tree.setExtraColumnWidth(1, this._authorWidth);
		tree.setExtraColumnWidth(2, this._replyWidth);
		tree.setExtraColumnWidth(3, this._dateWidth);
		tree.setIconDimensions("15", "12");
		tree.setTreeHeading(1, formatText(this._iwrb.getLocalizedString("thread", "Thread"), HEADER_STYLE));
		tree.setExtraColumnHeading(1, formatText(this._iwrb.getLocalizedString("author", "Author"), HEADER_STYLE));
		tree.setExtraColumnHeading(2, formatText(this._iwrb.getLocalizedString("replies", "Replies"), HEADER_STYLE));
		tree.setExtraColumnHeading(3, formatText(this._iwrb.getLocalizedString("date", "Date"), HEADER_STYLE));
		tree.setExtraColumnHorizontalAlignment(2, "center");
		tree.setToShowHeaderRow(true);
		tree.setTreePadding(2);
		tree.setTextStyleName(getStyleName(TABLE_TEXT_STYLE));
		tree.setLinkStyleName(getStyleName(THREAD_LINK_STYLE));
		tree.setLinkPage(getThreadPage());
		tree.setObjectInstanceID(this._objectID);
		tree.setThreadImage(this._threadImage);
		tree.setResourceBundle(this._iwrb);
		tree.setLightRowStyle(getStyleName(LIGHT_ROW_STYLE));
		tree.setDarkRowStyle(getStyleName(DARK_ROW_STYLE));
		return tree;
	}
//changed from private to protected because og ForumFlatLayout - ac
	private Table getThreadHeaderTable(ForumData thread, IWContext iwc) {
		Table table = new Table(2, 1);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

		table.add(getThreadImage(), 1, 1);
		table.add(getThreadLink(thread, THREAD_LINK_STYLE), 1, 1);
		table.add(getUser(thread), 2, 1);
		table.add(formatText("," + Text.NON_BREAKING_SPACE), 2, 1);
		table.add(getThreadDate(iwc, thread, INFORMATION_STYLE), 2, 1);

		return table;
	}
	
	protected Text getThreadSubject(ForumData thread) {
		String headlineString = thread.getThreadSubject();
		if (headlineString == null) {
			headlineString = "";
		}
		Text headline = formatText(headlineString, HEADING_STYLE);
		return headline;
	}

	//changed from private to protected because of ForumFlatLayout - ac
	protected Text getThreadBody(ForumData thread) {
		String bodyString = thread.getThreadBody();
		if (bodyString == null) {
			bodyString = "";
		}
		Text body = formatText(TextSoap.formatText(bodyString));

		return body;
	}
	
	protected Image getThreadImage() {
		Image image;
		if (this._threadImage == null) {
			image = this._iwb.getImage("shared/thread.gif");
		}
		else {
			image = (Image) this._threadImage.clone();
		}
		image.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
		image.setPaddingRight(2);
		return image;
	}
//protected because ForumFlatLayout uses it!
	protected Table getNextPreviousTable(boolean hasNext, boolean hasPrevious) {
		Table table = new Table(2, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		
		if (hasPrevious) {
			Image image = null;
			if (this.iPreviousImage != null) {
				image = this.iPreviousImage;
			}
			else {
				image = this._iwb.getImage("shared/previous.gif", "Previous");
			}
			image.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			
			Link previous = new Link(image);
			previous.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, this._topicID);
			previous.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			previous.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (this._firstThread - this._numberOfThreads));
			previous.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (this._lastThread - this._numberOfThreads));
			previous.addParameter(ForumBusiness.PARAMETER_THREAD_ID, this._threadID);
			previous.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);

			Link previousLink = new Link(this._iwrb.getLocalizedString("previous_threads", "Previous"));
			previousLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, this._topicID);
			previousLink.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			previousLink.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (this._firstThread - this._numberOfThreads));
			previousLink.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (this._lastThread - this._numberOfThreads));
			previousLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID, this._threadID);
			previousLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);
			previousLink.setStyleClass(getStyleName(LINK_STYLE));

			table.add(previous, 1, 1);
			table.add(Text.NON_BREAKING_SPACE, 1, 1);
			table.add(previousLink, 1, 1);
		}
		if (hasNext) {
			Image image = null;
			if (this.iNextImage != null) {
				image = this.iNextImage;
			}
			else {
				image = this._iwb.getImage("shared/next.gif", "Next");
			}
			image.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			
			Link next = new Link(image);
			next.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, this._topicID);
			next.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			next.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (this._firstThread + this._numberOfThreads));
			next.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (this._lastThread + this._numberOfThreads));
			next.addParameter(ForumBusiness.PARAMETER_THREAD_ID, this._threadID);
			next.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);

			Link nextLink = new Link(this._iwrb.getLocalizedString("next_threads", "Next"));
			nextLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, this._topicID);
			nextLink.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			nextLink.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (this._firstThread + this._numberOfThreads));
			nextLink.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (this._lastThread + this._numberOfThreads));
			nextLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID, this._threadID);
			nextLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);
			nextLink.setStyleClass(getStyleName(LINK_STYLE));
			
			table.add(nextLink, 2, 1);
			table.add(Text.getNonBrakingSpace(), 2, 1);
			table.add(next, 2, 1);
		}

		return table;
	}
	
	//changed from private to protected because of ForumFlatLayout - ac
	protected Table getThreadLinks(IWContext iwc, ForumData thread) {
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(2);
		int column = 1;

		if (this._hasReplyPermission) {
			ThreadReplyLink replyLink = new ThreadReplyLink(thread);
			replyLink.setImagePadding(2);
			replyLink.setImageAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);

			table.add(replyLink, column++, 1);
		}

		if (getUserCanEditHisOwnThreads() && thread.getUserID() != -1 && iwc.getUserId() == thread.getUserID() && thread.getChildCount() == 0) {
			ThreadEditLink editLink = new ThreadEditLink(thread);
			editLink.setImageAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			editLink.setImagePadding(2);

			table.add(editLink, column++, 1);
		}

		if (this._hasDeletePermission) {
			ThreadDeleteLink deleteLink = new ThreadDeleteLink(thread);
			deleteLink.setImageAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			deleteLink.setImagePadding(2);

			table.add(deleteLink, column++, 1);
		}

		return table;
	}

	private Table getForumLinks() {
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(2);
		int column = 1;

		if (this._topicID != -1) {
			ThreadNewLink newLink = new ThreadNewLink();
			newLink.setImagePadding(2);
			newLink.setImageAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			table.add(newLink, column++, 1);
		}

		if (this._showOverviewLink) {
			TopicOverviewLink overview = new TopicOverviewLink();
			overview.setImagePadding(2);
			overview.setImageAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			if (this._page != null) {
				overview.setPage(this._page);
			}
			
			table.add(overview, column, 1);
		}

		return table;
	}

	protected Table getAdminPart(IWContext iwc) {
		Table adminTable = new Table(2, 1);
		adminTable.setCellpadding(0);
		adminTable.setCellspacing(0);

		Link categoryLink = this.getCategoryLink();
		categoryLink.setPresentationObject(this._iwcb.getImage("shared/edit.gif"));
		adminTable.add(categoryLink);

		return adminTable;
	}
	//protected because ForumFlatLayout uses it!
	protected void updateThreadCount(IWContext iwc) {
		try {
			this._firstThread = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_FIRST_THREAD));
		}
		catch (Exception e) {
			try {
				this._firstThread = Integer.parseInt((String) iwc.getSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD + "_" + this._objectID));
			}
			catch (Exception ex) {
				this._firstThread = 1;
			}
		}
		iwc.setSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD + "_" + this._objectID, Integer.toString(this._firstThread));

		try {
			this._lastThread = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_LAST_THREAD));
		}
		catch (Exception e) {
			try {
				this._lastThread = Integer.parseInt((String) iwc.getSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD + "_" + this._objectID));
			}
			catch (Exception ex) {
				this._lastThread = this._numberOfThreads;
			}
		}
		iwc.setSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD + "_" + this._objectID, Integer.toString(this._lastThread));
	}

	private void getParameters(IWContext iwc) {
		try {
			this._selectedObjectID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID));
		}
		catch (NumberFormatException e) {
			this._selectedObjectID = -1;
		}

		if (this._state != ForumBusiness.FORUM_COLLECTION) {
			try {
				this._state = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_STATE));
			}
			catch (NumberFormatException e) {
			}
		}
		
		try {
			this._topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));
		}
		catch (NumberFormatException e) {
			this._topicID = -1;
		}

		if (this._selectedObjectID != -1 && this._selectedObjectID != this._objectID && !this.ignoreObjectID) {
			this._state = this._initialState;
		}
	}

	protected Text formatText(String textString) {
		return formatText(textString, TEXT_STYLE);
	}

	protected Text formatText(String textString, String style) {
		Text text = getStyleText(textString, style);
		return text;
	}

	public void setDefaultValues() {
		this._width = Table.HUNDRED_PERCENT;
		this._headingColor = "#eeeeee";
	}
	
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(HEADER_STYLE, "font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold");
		map.put(TEXT_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000;");
		map.put(SMALL_TEXT_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000;");
		map.put(HEADING_STYLE, "font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold");
		map.put(INFORMATION_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #999999;");
		map.put(TABLE_TEXT_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #999999;");

		map.put(LINK_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000; text-decoration: none;");
		map.put(LINK_STYLE+":hover", "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000; text-decoration: underline;");
		map.put(TOPIC_LINK_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline; font-weight:bold;");
		map.put(TOPIC_LINK_STYLE+":hover", "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline; font-weight:bold;");
		map.put(THREAD_LINK_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline;");
		map.put(THREAD_LINK_STYLE+":hover", "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline;");
		
		map.put(LIGHT_ROW_STYLE, "background-color:#FFFFFF;padding:2px;border-bottom:1px solid #000000");
		map.put(DARK_ROW_STYLE, "background-color:#CDCDCD;padding:2px;border-bottom:1px solid #000000");
		map.put(HEADER_ROW_STYLE, "background-color:#DFDFDF;padding:2px;");
		map.put(BODY_ROW_STYLE, "background-color:#FFFFFF;padding:2px;");
		
		return map;
	}

	public void setNumberOfDisplayedThreads(int numberOfThreads) {
		this._numberOfThreads = numberOfThreads;
	}

	public void setWidth(String width) {
		this._width = width;
	}

	public void setPage(ICPage page) {
		this._page = page;
	}

	public void setSpaceBetween(int spaceBetween) {
		this._spaceBetween = String.valueOf(spaceBetween);
	}

	public void setShowOverviewLink(boolean showOverview) {
		this._showOverviewLink = showOverview;
	}

	public void setShowTopicName(boolean showTopic) {
		this._showTopicName = showTopic;
	}

	public void setShowResponses(boolean showResponses) {
		this._showResponses = showResponses;
	}

	public void setLayout(int layout) {
		this._state = layout;
		this._initialState = layout;
	}

	public void setThreadImage(Image image) {
		this._threadImage = image;
	}

	public void setHeaderStyle(String style) {
	}

	public void setHeadingStyle(String style) {
	}

	public void setInformationStyle(String style) {
	}

	public void setTextStyle(String style) {
	}

	public void setDefaultOpenLevel(int openLevel) {
		this._openLevel = openLevel;
	}

	public void setLinkStyles(String style, String hoverStyle) {
	}

	public void setThreadLinkStyles(String style, String hoverStyle) {
	}

	public void setTopicLinkStyles(String style, String hoverStyle) {
	}

	public void setHeadingColor(String color) {
		this._headingColor = color;
	}

	public boolean deleteBlock(int ICObjectInstanceID) {
		return CategoryBusiness.getInstance().disconnectBlock(ICObjectInstanceID);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public String getCategoryType() {
		return "forum";
	}

	public void registerPermissionKeys() {
		registerPermissionKey(AddPermission);
		registerPermissionKey(ReplyPermission);
	}

	public boolean getMultible() {
		return true;
	}

	/**
	 * Sets the authorWidth.
	 * @param authorWidth The authorWidth to set
	 */
	public void setAuthorWidth(String authorWidth) {
		this._authorWidth = authorWidth;
	}

	/**
	 * Sets the dateWidth.
	 * @param dateWidth The dateWidth to set
	 */
	public void setDateWidth(String dateWidth) {
		this._dateWidth = dateWidth;
	}

	/**
	 * Sets the replyWidth.
	 * @param replyWidth The replyWidth to set
	 */
	public void setReplyWidth(String replyWidth) {
		this._replyWidth = replyWidth;
	}

	/**
	 * @return
	 */
	public ICPage getPage() {
		return this._page;
	}

	/**
	 * @return
	 */
	public ICPage getThreadPage() {
		return this._threadPage;
	}

	/**
	 * @param page
	 */
	public void setThreadPage(ICPage page) {
		this._threadPage = page;
	}

	/**
	 * @return
	 */
	public int getTopicID() {
		return this._topicID;
	}

	public Object clone() {
		Forum obj = null;
		try {
			obj = (Forum) super.clone();
			obj.stateHandler = this.stateHandler;
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
	
	/**
	 * @param indent The _bodyIndent to set.
	 */
	public void setBodyIndent(int indent) {
		this._bodyIndent = indent;
	}
	
	/**
	 * @param ignoreObjectID The ignoreObjectID to set.
	 */
	public void setIgnoreObjectID(boolean ignoreObjectID) {
		this.ignoreObjectID = ignoreObjectID;
	}
	
	protected String getAuthorWidth() {
		return this._authorWidth;
	}
	
	protected String getDateWidth() {
		return this._dateWidth;
	}
	
	protected String getReplyWidth() {
		return this._replyWidth;
	}
	protected boolean hasDeletePermission() {
		return this._hasDeletePermission;
	}
	protected boolean hasReplyPermission() {
		return this._hasReplyPermission;
	}
	public void setThreadTopicWidth(String threadTopicWidth) {
		this.iThreadTopicWidth = threadTopicWidth;
	}
	public void setUpdatedTopicWidth(String updatedTopicWidth) {
		this.iUpdatedTopicWidth = updatedTopicWidth;
	}
	protected String getThreadTopicWidth() {
		return this.iThreadTopicWidth;
	}
	protected String getUpdatedTopicWidth() {
		return this.iUpdatedTopicWidth;
	}
	public void setNextImage(Image nextImage) {
		this.iNextImage = nextImage;
	}
	public void setPreviousImage(Image previousImage) {
		this.iPreviousImage = previousImage;
	}
	public void setShowForumLinks(boolean showForumLinks) {
		this.iShowForumLinks = showForumLinks;
	}
	
	public void setShowTopicInCollection(boolean showTopicInCollection) {
		this.iShowTopicInCollection = showTopicInCollection;
	}
	/**
	 * @return Returns the userCanEditHisOwnThreads.
	 */
	public boolean getUserCanEditHisOwnThreads() {
		return this.userCanEditHisOwnThreads;
	}
	/**
	 * @param userCanEditHisOwnThreads The userCanEditHisOwnThreads to set.
	 */
	public void setUserCanEditHisOwnThreads(boolean userCanEditHisOwnThreads) {
		this.userCanEditHisOwnThreads = userCanEditHisOwnThreads;
	}

	/**
	 * @return Returns the showForumLinksOnTopOfThreadList.
	 */
	public boolean doShowForumLinksOnTopOfThreadList() {
		return this.showForumLinksOnTopOfThreadList;
	}
	/**
	 * @param showForumLinksOnTopOfThreadList The showForumLinksOnTopOfThreadList to set.
	 */
	public void setToShowForumLinksOnTopOfThreadList(boolean showForumLinksOnTopOfThreadList) {
		this.showForumLinksOnTopOfThreadList = showForumLinksOnTopOfThreadList;
	}
} // Class Forum