package com.idega.block.forum.presentation;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.block.forum.business.ForumBusiness;
import com.idega.block.forum.business.ForumTree;
import com.idega.block.forum.data.ForumData;
import com.idega.block.presentation.CategoryBlock;
import com.idega.block.text.business.TextFormatter;
import com.idega.core.builder.data.ICPage;
import com.idega.core.category.business.CategoryBusiness;
import com.idega.core.category.business.CategoryFinder;
import com.idega.core.category.data.ICCategory;
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
	private int _topicID = -1;
	private int _threadID = -1;
	private boolean _isAdmin = false;
	private boolean _hasAddPermission = true;
	private boolean _hasReplyPermission = true;
	private boolean _hasDeletePermission = false;
	private String _attribute;
	private int _iLocaleID;

	private int _firstThread = 1;
	private int _lastThread = 10;
	private int _numberOfThreads = 10;
	private String _spaceBetween = "8";
	private boolean _showOverviewLink = true;
	private boolean _showTopicName = true;
	private boolean _showResponses = true;

	private int _state = ForumBusiness.FORUM_TOPICS;
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

	protected static final String LIGHT_ROW_STYLE = "LightRowStyle";
	protected static final String DARK_ROW_STYLE = "DarkRowStyle";
	
	/*private boolean _styles = true;
	private String _headerStyle;
	private String _linkStyle;
	private String _linkHoverStyle;
	private String _topicLinkStyle;
	private String _topicLinkHoverStyle;
	private String _threadLinkStyle;
	private String _threadLinkHoverStyle;
	protected String _textStyle;
	protected String _headingStyle;
	private String _informationStyle;*/

	private String _headingColor;

	private String _width;
	private ICPage _page;
	private ICPage _threadPage;
	private Image _threadImage;

	/*protected String _topicName;
	private String _threadName;
	private String _linkName;*/

	protected static String AddPermission = "add";
	protected static String ReplyPermission = "reply";
	
	protected final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.forum";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwcb;
	protected ForumBusiness forumBusiness;

	/**
	 * @todo implement Statehandling for Forum
	 */
	private StatefullPresentationImplHandler stateHandler = new ForumTree().getStateHandler();

	private String _authorWidth = "160";
	private String _replyWidth = "60";
	private String _dateWidth = "100";

	public Forum() {
		setDefaultValues();
	}

	/**
	 * Temporary implementation
	 * returning stateClass for the ForumTree Object
	 */
	public Class getPresentationStateClass() {
		return stateHandler.getPresentationStateClass();
	}

	public IWPresentationState getPresentationState(IWUserContext iwuc) {
		return stateHandler.getPresentationState(this, iwuc);
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
		_iwrb = getResourceBundle(iwc);
		_iwb = getBundle(iwc);
		_iwcb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

		_isAdmin = iwc.hasEditPermission(this);
		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		_objectID = getICObjectInstanceID();

		getParameters(iwc);
		_hasDeletePermission = hasDeletePermission(iwc);
		_hasAddPermission = hasAddPermission(iwc);
		_hasReplyPermission = hasReplyPermission(iwc);

		forumBusiness = new ForumBusiness();

		_myTable = new Table();
		_myTable.setCellpadding(0);
		_myTable.setCellspacing(0);
		_myTable.setBorder(0);
		_myTable.setWidth(_width);

		int row = 1;
		if (_isAdmin) {
			_myTable.add(getAdminPart(iwc), 1, row);
			row++;
		}

		Table table = getForum(iwc);
		if (table != null)
			_myTable.add(table, 1, row);
		add(_myTable);
	}

	private Table getForum(IWContext iwc) {
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth(Table.HUNDRED_PERCENT);

		switch (_state) {
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
		iwc.removeSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD + "_" + _objectID);
		iwc.removeSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD + "_" + _objectID);

		Text topicText = getStyleText(_iwrb.getLocalizedString("topics", "Topics"), HEADING_STYLE);
		Text threadsText = getStyleText(_iwrb.getLocalizedString("threads", "Threads"), HEADING_STYLE);
		Text updatedText = getStyleText(_iwrb.getLocalizedString("last_updated", "Last updated"), HEADING_STYLE);

		table.setWidth(2, "60");
		table.setWidth(3, "90");

		table.add(topicText, 1, 1);
		table.add(threadsText, 2, 1);
		table.add(updatedText, 3, 1);
		table.setRowColor(1, _headingColor);

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
					topicLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);

					int numberOfThreads = forumBusiness.getNumberOfThreads(topic);
					numberOfThreadsText = formatText(String.valueOf(numberOfThreads), TEXT_STYLE);

					ForumData newestThread = forumBusiness.getNewestThreads(topic);
					if (newestThread != null) {
						lastUpdatedText = getThreadDate(iwc, newestThread);
					}
					else
						lastUpdatedText = null;

					table.add(topicLink, 1, row);
					table.add(numberOfThreadsText, 2, row);
					if (lastUpdatedText != null)
						table.add(lastUpdatedText, 3, row);
					
					if (row % 2 == 0) {
						table.setRowStyleClass(row, LIGHT_ROW_STYLE);
					}
					else {
						table.setRowStyleClass(row, DARK_ROW_STYLE);
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

	private void getForumThreads(IWContext iwc, Table table) {
		int row = 1;

		ICCategory topic = null;
		if (_topicID != -1)
			topic = CategoryFinder.getInstance().getCategory(_topicID);

		ForumData thread = null;
		try {
			thread = forumBusiness.getForumData(Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID)));
			_threadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID));
		}
		catch (NumberFormatException e) {
			thread = null;
		}

		if (topic != null) {
			if (_showTopicName) {
				Text topicText = formatText(topic.getName(), HEADER_STYLE);
				table.setRowColor(row, _headingColor);
				table.setRowPadding(row, 2);
				table.add(topicText, 1, row++);
				//table.setBackgroundImage(1, row++, _iwb.getImage("shared/dotted.gif"));
			}
			
			row = addBelowTopic(iwc, topic, table, row);

			if (thread != null && thread.isValid()) {
				table.add(getThreadHeaderTable(thread, iwc), 1, row++);
				table.setHeight(row++, 3);
				//table.setBackgroundImage(1, row++, _iwb.getImage("shared/dotted.gif"));
				table.add(getThreadBodyTable(thread), 1, row++);
				table.setHeight(row++, 3);
				table.add(getThreadLinks(iwc, thread), 1, row++);
			}

			table.setHeight(1, row++, "20");
			table.add(getForumLinks(), 1, row++);

			updateThreadCount(iwc);

			ForumData[] threads = forumBusiness.getThreads(topic);
			ForumData[] someThreads = forumBusiness.getThreads(threads, _firstThread, _lastThread);
			boolean hasNextThreads = forumBusiness.hasNextThreads(threads, _lastThread);
			boolean hasPreviousThreads = forumBusiness.hasPreviousThreads(_firstThread);

			table.add(getForumTree(someThreads), 1, row);

			if (hasNextThreads || hasPreviousThreads)
				table.add(getNextPreviousTable(hasNextThreads, hasPreviousThreads), 1, ++row);
		}
	}

	private void getForumCollection(IWContext iwc, Table table) {
		table.setCellpaddingAndCellspacing(0);
		int row = 1;

		Collection categories = this.getCategories();
		if (categories != null && categories.size() == 1) {
			_topicID = ((ICCategory) new Vector(categories).get(0)).getID();
		}

		List list = forumBusiness.getThreadsInCategories(categories, _numberOfThreads);
		if (list != null) {
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				ForumData thread = (ForumData) iter.next();

				table.add(getUser(thread), 1, row);
				table.add(formatText("," + Text.NON_BREAKING_SPACE), 1, row);
				table.add(getThreadDate(iwc, thread), 1, row);
				table.add(Text.getBreak(), 1, row);
				table.add(getThreadLink(thread, TOPIC_LINK_STYLE), 1, row);
				if (_showResponses) {
					table.add(formatText(Text.NON_BREAKING_SPACE), 1, row);
					table.add(getThreadResponses(thread), 1, row++);
				}
				else {
					row++;
				}
				table.setHeight(1, row++, _spaceBetween);
			}
		}

		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(getForumLinks(), 1, row);
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

				List threads = forumBusiness.getThreads(category, _numberOfThreads);
				if (threads != null) {
					threadTable = new Table();
					threadTable.setCellpaddingAndCellspacing(0);
					threadTable.setWidth(Table.HUNDRED_PERCENT);
					int threadRow = 1;

					Iterator iter2 = threads.iterator();
					while (iter2.hasNext()) {
						ForumData thread = (ForumData) iter2.next();
						if (_threadImage != null) {
							_threadImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
							_threadImage.setHorizontalSpacing(2);
							threadTable.add(_threadImage, 1, threadRow);
						}
						threadTable.add(getThreadLink(thread, THREAD_LINK_STYLE), 1, threadRow);
						threadTable.add(formatText(Text.NON_BREAKING_SPACE), 1, threadRow);
						threadTable.add(getThreadResponses(thread), 1, threadRow++);
					}

					table.add(threadTable, 1, row++);
					table.setHeight(1, row++, _spaceBetween);
				}
			}
		}
	}

	private Link getTopicLink(ICCategory category) {
		Link link = getStyleLink(category.getName(), TOPIC_LINK_STYLE);
		if (_page != null)
			link.setPage(_page);
		link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, category.getID());
		link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
		link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);

		return link;
	}

	private Text getTopicThreadsText(ICCategory topic) {
		int numberOfThreads = forumBusiness.getNumberOfThreads(topic);
		return formatText("(" + String.valueOf(numberOfThreads) + Text.NON_BREAKING_SPACE + _iwrb.getLocalizedString("threads_lc", "threads") + ")", INFORMATION_STYLE);
	}

	private Link getThreadLink(ForumData thread, String styleName) {
		String headline = thread.getThreadSubject();
		if (headline == null)
			headline = "";

		Link link = getStyleLink(headline, styleName);
		if (_page != null)
			link.setPage(_page);
		link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, thread.getTopicID());
		link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
		link.addParameter(ForumBusiness.PARAMETER_THREAD_ID, thread.getID());
		//link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);

		return link;
	}

	private Text getThreadResponses(ForumData thread) {
		return formatText("(" + String.valueOf(thread.getNumberOfResponses()) + Text.NON_BREAKING_SPACE + _iwrb.getLocalizedString("replies_lc", "replies") + ")", INFORMATION_STYLE);
	}

	private PresentationObject getUser(ForumData thread) {
		Text text = formatText(_iwrb.getLocalizedString("unknown", "Unknown"));
		if (thread.getUserID() != -1) {
			User user = UserBusiness.getUser(thread.getUserID());
			Email mail = UserBusiness.getUserMail(thread.getUserID());
			if (user != null && mail != null) {
				String name = user.getName();
				if (user.getDisplayName() != null && user.getDisplayName().length() > 0)
					name = user.getDisplayName();
				Link link = getStyleLink(name, THREAD_LINK_STYLE);
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
			if (thread.getUserEMail() != null)
				link.setURL("mailto:" + thread.getUserEMail());
			return link;
		}
		else if (thread.getUserName() != null) {
			Text userText = formatText(thread.getUserName(), TEXT_STYLE);
			return userText;
		}
		return text;
	}

	protected Text getThreadDate(IWContext iwc, ForumData thread) {
		IWTimestamp stamp = new IWTimestamp(thread.getThreadDate());
		return getFormattedDate(stamp, iwc);
	}
	
	protected Text getFormattedDate(IWTimestamp stamp, IWContext iwc) {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
		Date date = new Date(stamp.getTimestamp().getTime());
		return formatText(format.format(date));
	}

	private ForumTree getForumTree(ForumData[] threads) {
		ForumTree tree = new ForumTree();
		/**
		 * @todo chage later: legacy-fix
		 */
		tree.setICObjectInstanceID(this.getICObjectInstanceID());
		System.out.println("Forum: tree.setICObjectInstanceID(" + this.getICObjectInstanceID() + ");");

		tree.setHeadingColor(_headingColor);
		tree.setExtraHeadingColor(1, _headingColor);
		tree.setExtraHeadingColor(2, _headingColor);
		tree.setExtraHeadingColor(3, _headingColor);
		tree.setColumns(2);
		tree.setParallelExtraColumns(3);
		tree.setFirstLevelNodes(threads);
		tree.setNestLevelAtOpen(_openLevel);
		tree.setToShowTreeIcons(false);
		tree.setWidth("100%");
		tree.setExtraColumnWidth(1, _authorWidth);
		tree.setExtraColumnWidth(2, _replyWidth);
		tree.setExtraColumnWidth(3, _dateWidth);
		tree.setIconDimensions("15", "12");
		tree.setTreeHeading(1, formatText(_iwrb.getLocalizedString("thread", "Thread"), HEADING_STYLE));
		tree.setExtraColumnHeading(1, formatText(_iwrb.getLocalizedString("author", "Author"), HEADING_STYLE));
		tree.setExtraColumnHeading(2, formatText(_iwrb.getLocalizedString("replies", "Replies"), HEADING_STYLE));
		tree.setExtraColumnHeading(3, formatText(_iwrb.getLocalizedString("date", "Date"), HEADING_STYLE));
		tree.setExtraColumnHorizontalAlignment(2, "center");
		tree.setToShowHeaderRow(true);
		tree.setTreePadding(2);
		tree.setTextStyleName(getStyleName(TEXT_STYLE));
		tree.setLinkStyleName(THREAD_LINK_STYLE);
		tree.setLinkPage(getThreadPage());
		tree.setObjectInstanceID(_objectID);
		tree.setThreadImage(_threadImage);
		tree.setResourceBundle(_iwrb);
		tree.setLightRowStyle(getStyleName(LIGHT_ROW_STYLE));
		tree.setDarkRowStyle(getStyleName(DARK_ROW_STYLE));
		return tree;
	}

	private Table getThreadHeaderTable(ForumData thread, IWContext iwc) {
		Table table = new Table(2, 1);
		table.setWidth("100%");
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

		Image image;
		if (_threadImage == null) {
			image = _iwb.getImage("shared/thread.gif");
		}
		else {
			image = (Image) _threadImage.clone();
		}
		image.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
		image.setPaddingRight(2);

		String headlineString = thread.getThreadSubject();
		if (headlineString == null)
			headlineString = "";
		Text headline = formatText(headlineString, HEADING_STYLE);

		IWTimestamp stamp = new IWTimestamp(thread.getThreadDate());
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
		Date date = new Date(stamp.getTimestamp().getTime());
		Text dateText = formatText(format.format(date));

		table.add(image, 1, 1);
		table.add(headline, 1, 1);
		table.add(getUser(thread), 2, 1);
		table.add(formatText("," + Text.NON_BREAKING_SPACE), 2, 1);
		table.add(dateText, 2, 1);

		return table;
	}

	private Text getThreadBodyTable(ForumData thread) {
		String bodyString = thread.getThreadBody();
		if (bodyString == null)
			bodyString = "";
		Text body = formatText(TextFormatter.formatText(bodyString, 1, "100%"));

		return body;
	}

	private Table getNextPreviousTable(boolean hasNext, boolean hasPrevious) {
		Table table = new Table();
		if (hasPrevious) {
			Link link = new Link(_iwb.getImage("shared/previous.gif"));
			link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
			link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			link.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (_firstThread - _numberOfThreads));
			link.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (_lastThread - _numberOfThreads));
			link.addParameter(ForumBusiness.PARAMETER_THREAD_ID, _threadID);
			link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);
			table.add(link, 1, 1);
			table.add(Text.NON_BREAKING_SPACE, 1, 1);

			Link previousLink = new Link(_iwrb.getLocalizedString("previous_threads", "Previous"));
			previousLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
			previousLink.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			previousLink.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (_firstThread - _numberOfThreads));
			previousLink.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (_lastThread - _numberOfThreads));
			previousLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID, _threadID);
			previousLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);
			table.add(previousLink, 1, 1);
		}
		if (hasNext) {
			Link link = new Link(_iwb.getImage("shared/next.gif", "Next"));
			link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
			link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			link.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (_firstThread + _numberOfThreads));
			link.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (_lastThread + _numberOfThreads));
			link.addParameter(ForumBusiness.PARAMETER_THREAD_ID, _threadID);
			link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);
			table.add(link, 2, 1);
			table.add(Text.NON_BREAKING_SPACE, 2, 1);

			Link nextLink = new Link(_iwrb.getLocalizedString("next_threads"));
			nextLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
			nextLink.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
			nextLink.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD, (_firstThread + _numberOfThreads));
			nextLink.addParameter(ForumBusiness.PARAMETER_LAST_THREAD, (_lastThread + _numberOfThreads));
			nextLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID, _threadID);
			nextLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);
			table.add(nextLink, 2, 1);
		}

		return table;
	}

	private Table getThreadLinks(IWContext iwc, ForumData thread) {
		Table table = new Table();
		int column = 1;

		Image replyImage = _iwb.getImage("shared/reply.gif");
		replyImage.setHorizontalSpacing(2);
		replyImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
		table.add(replyImage, column, 1);

		if (_hasReplyPermission) {
			Link reply = getStyleLink(_iwrb.getLocalizedString("reply", "Reply"), LINK_STYLE);
			reply.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
			reply.addParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID, thread.getID());
			reply.setWindowToOpen(ForumThreadEditor.class);
			table.add(reply, column++, 1);
		}
		else {
			Text text = getStyleText(_iwrb.getLocalizedString("reply", "Reply"), SMALL_TEXT_STYLE);
			table.add(text, column++, 1);
		}

		if (thread.getUserID() != -1 && iwc.getUserId() == thread.getUserID() && thread.getChildCount() == 0) {
			Image editImage = _iwb.getImage("shared/edit.gif");
			editImage.setHorizontalSpacing(2);
			editImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			table.add(editImage, column, 1);

			Link edit = getStyleLink(_iwrb.getLocalizedString("edit", "Edit"), LINK_STYLE);
			edit.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
			edit.addParameter(ForumBusiness.PARAMETER_THREAD_ID, thread.getID());
			edit.addParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID, thread.getParentThreadID());
			edit.setWindowToOpen(ForumThreadEditor.class);
			table.add(edit, column++, 1);
		}

		if (_hasDeletePermission) {
			Image deleteImage  = _iwb.getImage("shared/delete.gif");
			deleteImage.setHorizontalSpacing(2);
			deleteImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			table.add(deleteImage, column, 1);

			Link delete = getStyleLink(_iwrb.getLocalizedString("delete", "Delete"), LINK_STYLE);
			delete.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
			delete.addParameter(ForumBusiness.PARAMETER_THREAD_ID, thread.getID());
			delete.addParameter(ForumBusiness.PARAMETER_MODE, ForumBusiness.PARAMETER_DELETE);
			delete.setWindowToOpen(ForumThreadEditor.class);
			table.add(delete, column++, 1);
		}

		return table;
	}

	private Table getForumLinks() {
		Table table = new Table();
		int column = 1;

		if (_topicID != -1) {
			Image newImage = _iwb.getImage("shared/new.gif");
			newImage.setHorizontalSpacing(2);
			newImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			table.add(newImage, column, 1);

			if (_hasAddPermission) {
				Link newLink = getStyleLink(_iwrb.getLocalizedString("new_thread"), LINK_STYLE);
				newLink.setWindowToOpen(ForumThreadEditor.class);
				newLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, _topicID);
				table.add(newLink, column++, 1);
			}
			else {
				Text newText = getStyleText(_iwrb.getLocalizedString("new_thread"), SMALL_TEXT_STYLE);
				table.add(newText, column++, 1);
			}
		}

		if (_showOverviewLink) {
			Image overviewImage = _iwb.getImage("shared/forum.gif");
			overviewImage.setHorizontalSpacing(2);
			overviewImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
			table.add(overviewImage, column, 1);
			table.add(getOverviewLink(), column, 1);
		}

		return table;
	}

	private Link getOverviewLink() {
		Link overView = getStyleLink(_iwrb.getLocalizedString("topic_overview", "Topic overview"), LINK_STYLE);
		overView.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_TOPICS);
		if (_page != null)
			overView.setPage(_page);

		return overView;
	}

	protected Table getAdminPart(IWContext iwc) {
		Table adminTable = new Table(2, 1);
		adminTable.setCellpadding(0);
		adminTable.setCellspacing(0);

		Link categoryLink = this.getCategoryLink();
		categoryLink.setPresentationObject(_iwcb.getImage("shared/edit.gif"));
		adminTable.add(categoryLink);

		return adminTable;
	}

	private void updateThreadCount(IWContext iwc) {
		try {
			_firstThread = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_FIRST_THREAD));
		}
		catch (Exception e) {
			try {
				_firstThread = Integer.parseInt((String) iwc.getSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD + "_" + _objectID));
			}
			catch (Exception ex) {
				_firstThread = 1;
			}
		}
		iwc.setSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD + "_" + _objectID, Integer.toString(_firstThread));

		try {
			_lastThread = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_LAST_THREAD));
		}
		catch (Exception e) {
			try {
				_lastThread = Integer.parseInt((String) iwc.getSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD + "_" + _objectID));
			}
			catch (Exception ex) {
				_lastThread = _numberOfThreads;
			}
		}
		iwc.setSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD + "_" + _objectID, Integer.toString(_lastThread));
	}

	private void getParameters(IWContext iwc) {
		try {
			_selectedObjectID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID));
		}
		catch (NumberFormatException e) {
			_selectedObjectID = -1;
		}

		try {
			_state = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_STATE));
		}
		catch (NumberFormatException e) {
		}

		try {
			_topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));
		}
		catch (NumberFormatException e) {
			_topicID = -1;
		}

		if (_selectedObjectID != -1 && _selectedObjectID != _objectID)
			_state = _initialState;
	}

	protected Text formatText(String textString) {
		return formatText(textString, TEXT_STYLE);
	}

	protected Text formatText(String textString, String style) {
		Text text = getStyleText(textString, style);
		return text;
	}

	private void setDefaultValues() {
		_width = Table.HUNDRED_PERCENT;
		_headingColor = "#eeeeee";
	}
	
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(HEADER_STYLE, "font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold");
		map.put(TEXT_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000;");
		map.put(SMALL_TEXT_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000;");
		map.put(HEADING_STYLE, "font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold");
		map.put(INFORMATION_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #999999;");

		map.put(LINK_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000; text-decoration: none;");
		map.put(LINK_STYLE+":hover", "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000; text-decoration: underline;");
		map.put(TOPIC_LINK_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline; font-weight:bold;");
		map.put(TOPIC_LINK_STYLE+":hover", "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline; font-weight:bold;");
		map.put(THREAD_LINK_STYLE, "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline;");
		map.put(THREAD_LINK_STYLE+":hover", "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline;");
		
		map.put(LIGHT_ROW_STYLE, "background-color:#FFFFFF;padding:2px;border-bottom:1px solid #000000");
		map.put(DARK_ROW_STYLE, "background-color:#CDCDCD;padding:2px;border-bottom:1px solid #000000");
		
		return map;
	}

	public void setNumberOfDisplayedThreads(int numberOfThreads) {
		_numberOfThreads = numberOfThreads;
	}

	public void setWidth(String width) {
		_width = width;
	}

	public void setPage(ICPage page) {
		_page = page;
	}

	public void setSpaceBetween(int spaceBetween) {
		_spaceBetween = String.valueOf(spaceBetween);
	}

	public void setShowOverviewLink(boolean showOverview) {
		_showOverviewLink = showOverview;
	}

	public void setShowTopicName(boolean showTopic) {
		_showTopicName = showTopic;
	}

	public void setShowResponses(boolean showResponses) {
		_showResponses = showResponses;
	}

	public void setLayout(int layout) {
		_state = layout;
		_initialState = layout;
	}

	public void setThreadImage(Image image) {
		_threadImage = image;
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
		_openLevel = openLevel;
	}

	public void setLinkStyles(String style, String hoverStyle) {
	}

	public void setThreadLinkStyles(String style, String hoverStyle) {
	}

	public void setTopicLinkStyles(String style, String hoverStyle) {
	}

	public void setHeadingColor(String color) {
		_headingColor = color;
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
		_authorWidth = authorWidth;
	}

	/**
	 * Sets the dateWidth.
	 * @param dateWidth The dateWidth to set
	 */
	public void setDateWidth(String dateWidth) {
		_dateWidth = dateWidth;
	}

	/**
	 * Sets the replyWidth.
	 * @param replyWidth The replyWidth to set
	 */
	public void setReplyWidth(String replyWidth) {
		_replyWidth = replyWidth;
	}

	/**
	 * @return
	 */
	public ICPage getPage() {
		return _page;
	}

	/**
	 * @return
	 */
	public ICPage getThreadPage() {
		return _threadPage;
	}

	/**
	 * @param page
	 */
	public void setThreadPage(ICPage page) {
		_threadPage = page;
	}

	/**
	 * @return
	 */
	public int getTopicID() {
		return _topicID;
	}

	public Object clone() {
		Forum obj = null;
		try {
			obj = (Forum) super.clone();
			obj.stateHandler = stateHandler;
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
} // Class Forum