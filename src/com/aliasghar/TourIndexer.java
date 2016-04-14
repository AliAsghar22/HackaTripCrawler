package com.aliasghar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Taghizadeh on 14/04/2016.
 */
public class TourIndexer {
    private final static String luceneIndexPath = "index";
    static Analyzer analyzer;
    static Directory index;
    static IndexWriterConfig config;
    static IndexWriter w;
    static TourIndexer indexer;

    private TourIndexer(){}

    public static TourIndexer getIndexer() throws IOException {
        if(indexer == null)
            indexer = new TourIndexer();
        analyzer = new PersianAnalyzer();
        config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        index = FSDirectory.open(new File(luceneIndexPath).toPath());
        w = new IndexWriter(index, config);
        return indexer;
    }

    public void close(){

    }

    public void add(){

    }
}
