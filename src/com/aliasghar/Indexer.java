package com.aliasghar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Taghizadeh on 13/04/2016.
 */
public class Indexer {
    private final static String luceneIndexPath = "index";
    static Analyzer analyzer;
    static Directory index;
    static IndexWriterConfig config;
    static IndexWriter w;
    static Indexer indexer;

    private Indexer(){}

    public static Indexer getIndexer() throws IOException {
        if(indexer == null)
            indexer = new Indexer();
        analyzer = new PersianAnalyzer();
        config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        index = FSDirectory.open(new File(luceneIndexPath).toPath());
        w = new IndexWriter(index, config);
        return indexer;
    }

    public void close() throws IOException {
        w.close();
    }

    public synchronized void add(String name,String place,String DistanceToAirport,String Facilities,String Description) throws IOException {
        Document document=new Document();
        document.add(new TextField("Hname",name, Field.Store.YES));
        document.add(new TextField("Hplace",place, Field.Store.YES));
        document.add(new TextField("Hdistance",DistanceToAirport, Field.Store.YES));
        document.add(new TextField("HFacilities",Facilities, Field.Store.YES));
        document.add(new TextField("HFacilities",Description, Field.Store.YES));
        w.addDocument(document);
    }
}
