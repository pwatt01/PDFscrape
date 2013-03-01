package org.watt.apps.PDFScrape;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.lucene.LucenePDFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.lucene.util._TestUtil;

import com.thoughtworks.xstream.XStream;
import com.tripit.api.*;
import com.tripit.auth.*;



/**
 * Hello world!
 *
 */
public class App 
{


	public static void main( String[] args ) throws Exception{
		//get Activity List
		ParsePDF PP = new ParsePDF();
		ArrayList<Activity> list = PP.run(args);
		
		//TODO Connect to server (example)
//		Credential cred;
//		Client client;
//		cred = new OAuthCredential("85bffb1da0b56bffc2bc1c6dc8c9774a74a3c28f",
//					   "5dff1f8a303149c6dc74c106902cd66cb767755b",
//					   "ecf5eea2058aa9c99a8fac194e6fd8108a6bcdb6",
//					   "3ee134a46872c58f80524eb11980fbea9e91f34d");
//		client = new Client(cred,Client.DEFAULT_API_URI_PREFIX);
		//Perform list example
//		Map<String, String> listMap = new HashMap<String, String>();
//		listMap.put("traveler", "false");
//		listMap.put("format", "json");
//
//		Response r;
//		Type t = null;
//		t = Type.TRIP;
//		r = client.list(t, listMap);
//		System.out.println(r);
//		
		//TODO perform create
		//create and send a request for each 
		//Example->https://github.com/tripit/java_binding_v1/blob/master/Example.java
		for(Activity act : list){
			//HashMap<String,String> createMap = new HashMap<String,String>();
			//createMap.put("xml", "<Request><Trip>...</Trip></Request>");
			
			XStream xs = new XStream();
			xs.alias("ActivityObject", Activity.class);
			System.out.println(xs.toXML(act));
//			r = client.create(createMap);
//			System.out.println(r);
		}
	}
}
