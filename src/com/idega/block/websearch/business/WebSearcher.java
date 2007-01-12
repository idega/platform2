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
		if (this.searcher != null) {
			try {
				this.searcher.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public WebSearchHitIterator search(String input) throws IOException, ParseException {
		//try {
		String indexPath = this.index.getIndexPath();
		Hits hits;
		WebSearchHitIterator searchHits = null;
		IndexReaderCache reader = new IndexReaderCache();//can't this be
														 // static?
		this.searcher = new IndexSearcher(reader.getReader(indexPath));
		Analyzer analyzer = new StopAnalyzer();
		if (this.phraseSearch) {
			input = "\"" + input + "\"";
		}
		BooleanQuery query = new BooleanQuery();
		BooleanQuery fQuery = new BooleanQuery();
		for (int i = 0; i < fields.length; i++) {
			fQuery.add(QueryParser.parse(input, fields[i], analyzer), false, false);
		}
		query.add(fQuery, true, false);
		if (this.categories != null) {
			query.add(QueryParser.parse(this.categories, "categories", analyzer), true, false);
		}
		if (this.from != 0) {
			if (this.to != 0) {
				this.dateFilter = new DateFilter("published", this.from, this.to);
			}
			else {
				this.dateFilter = DateFilter.After("published", this.from);
			}
		}
		else if (this.to != 0) {
			this.dateFilter = DateFilter.Before("published", this.to);
		}
		//System.out.println("Searching for: " + query.toString());
		if (this.dateFilter == null) {
			hits = this.searcher.search(query);
		}
		else {
			hits = this.searcher.search(query, this.dateFilter);
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
		this.categories = cat;
	}

	public void setFrom(long time) {
		this.from = time;
	}

	public void setFromDays(int days) {
		//System.out.println(new Date(System.currentTimeMillis() -
		// (long)1000*60*60*24*days));
		this.from = System.currentTimeMillis() - (long) 1000 * 60 * 60 * 24 * days;
	}

	public void setHitsPerSet(int hps) {
		this.hitsPerSet = hps;
	}

	public void setPhraseSearch(boolean f) {
		this.phraseSearch = f;
	}

	public void setTo(long time) {
		this.to = time;
	}
}