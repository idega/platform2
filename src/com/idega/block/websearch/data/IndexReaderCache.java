package com.idega.block.websearch.data;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.lucene.index.IndexReader;

/**
 Keep a cache of open IndexReader's, so that an index does not have to opened
 for each query.  The cache re-opens an index when it has changed so that
 additions and deletions are visible ASAP.
 */


public class IndexReaderCache {
	static Hashtable indexCache = new Hashtable(); // name->CachedIndex
	class CachedIndex { // a cache entry
		IndexReader reader; // an open reader
		long modified; // reader's mod. date
		
		CachedIndex(String name) throws IOException {
			modified = IndexReader.lastModified(name); // get mod. date
			reader = IndexReader.open(name); // open reader
		}
		
	}
	public IndexReaderCache() {
	}
	public IndexReader getReader(String name) throws IOException {
//		 look in cache
			CachedIndex index = (CachedIndex) indexCache.get(name);
		if (index != null && // check up-to-date
		 (index.modified == IndexReader.lastModified(name)))
			return index.reader; // cache hit
		else {
			System.out.println("create new");
			index = new CachedIndex(name); // cache miss
		}
		indexCache.put(name, index); // add to cache
		return index.reader;
	}
}
