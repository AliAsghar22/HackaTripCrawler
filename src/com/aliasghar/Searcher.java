package com.aliasghar;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.parser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by ${Microsoft} on 14/04/2016.
 */
public class Searcher {

    public JSONObject getHotelByplace(String title){
        try {
            JSONObject names = new JSONObject();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("title", new StandardAnalyzer());
            Query query = parser.parse(title);
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                JSONObject detail = new JSONObject();
                detail.put("address",is.doc(scoreDoc.doc).get("Haddress"));
                detail.put("Description",is.doc(scoreDoc.doc).get("HDescription"));
                detail.put("DistanceToAirport",is.doc(scoreDoc.doc).get("Hdistance"));
                detail.put("place",is.doc(scoreDoc.doc).get("Hplace"));
                detail.put("stars",is.doc(scoreDoc.doc).get("Hstar"));
                detail.put("Facilities",is.doc(scoreDoc.doc).get("HFacilities"));
                detail.put("image",is.doc(scoreDoc.doc).get("Himage"));
                names.put("name",detail);
            }
            return names;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
