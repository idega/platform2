package com.idega.block.websearch.business;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DateFilter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import com.idega.block.websearch.data.IndexReaderCache;
import com.idega.block.websearch.data.WebSearchIndex;

/**
 * <p>
 * <code>WebSearcher</code> Searches are done through the WebSearcher class.
 * This class is a part of the websearch webcrawler and search engine block.
 * <br>
 * It is based on the <a href="http://lucene.apache.org">Lucene </a> java search
 * engine from the Apache group and loosly <br>
 * from the work of David Duddleston of i2a.com. <br>
 * 
 * @copyright Idega Software 2002
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson </a>
 */
public final class WebSearcher {

	private WebSearchIndex index;

	private int hitsPerSet;

	private String categories;

	private boolean phraseSearch;

	private long from;

	private long to;

	private DateFilter dateFilter;

	private static final String[] fields = { "title", "description", "keyword", "contents" };

	Searcher searcher;

	public WebSearcher(WebSearchIndex i) {
		this.index = i;
	}

	public void close() {
		if (searcher != null) {
			try {
				searcher.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public WebSearchHitIterator search(String input) throws IOException, ParseException {
		//try {
		String indexPath = index.getIndexPath();
		Hits hits;
		WebSearchHitIterator searchHits = null;
		IndexReaderCache reader = new IndexReaderCache();//can't this be
														 // static?
		searcher = new IndexSearcher(reader.getReader(indexPath));
		Analyzer analyzer = new StopAnalyzer();
		if (phraseSearch) {
			input = "\"" + input + "\"";
		}
		BooleanQuery query = new BooleanQuery();
		BooleanQuery fQuery = new BooleanQuery();
		for (int i = 0; i < fields.length; i++) {
			fQuery.add(QueryParser.parse(input, fields[i], analyzer), false, false);
		}
		query.add(fQuery, true, false);
		if (categories != null) {
			query.add(QueryParser.parse(categories, "categories", analyzer), true, false);
		}
		if (from != 0) {
			if (to != 0) {
				dateFilter = new DateFilter("published", from, to);
			}
			else {
				dateFilter = DateFilter.After("published", from);
			}
		}
		else if (to != 0) {
			dateFilter = DateFilter.Before("published", to);
		}
		//System.out.println("Searching for: " + query.toString());
		if (dateFilter == null) {
			hits = searcher.search(query);
		}
		else {
			hits = searcher.search(query, dateFilter);
		}
		searchHits = new WebSearchHitIterator(input, hits, (this.hitsPerSet != 0) ? this.hitsPerSet : 30);
		return searchHits;
		//} catch (Exception e) {
		//System.out.println(" caught a " + e.getClass() + "\n with message: "
		// + e.getMessage());
		//e.printStackTrace();
		//return null;
		//}
	}

	public void setCategories(String cat) {
		categories = cat;
	}

	public void setFrom(long time) {
		from = time;
	}

	public void setFromDays(int days) {
		//System.out.println(new Date(System.currentTimeMillis() -
		// (long)1000*60*60*24*days));
		from = System.currentTimeMillis() - (long) 1000 * 60 * 60 * 24 * days;
	}

	public void setHitsPerSet(int hps) {
		hitsPerSet = hps;
	}

	public void setPhraseSearch(boolean f) {
		phraseSearch = f;
	}

	public void setTo(long time) {
		to = time;
	}
}