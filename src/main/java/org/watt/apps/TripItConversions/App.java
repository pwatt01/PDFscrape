package org.watt.apps.TripItConversions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;


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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
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
		TripomaticTrip tt = new TripomaticTrip();
		ArrayList<Activity> list = tt.run(args);
		//TODO list all current trips and make a choice from them
		//TODO make choice to create trip if desired
		//TODO Connect to server (example)
		Credential cred;
		Client client;
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
		cred = new WebAuthCredential("pwatt01@gmail.com", "wallywatt");
		client = new Client(cred, Client.DEFAULT_API_URI_PREFIX);

		//perform create
		System.out.println("connecting to API and sending");
		XStream xs = new XStream((HierarchicalStreamDriver)new DomDriver("UTF-8", new XmlFriendlyNameCoder("_-", "_")));
		xs.alias("ActivityObject", Activity.class);
		xs.alias("Request", Request.class);

		for (Activity act : list){
			String xml = xs.toXML(new Request(act));
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n" + xml;

			//check xml
			// 1. Lookup a factory for the W3C XML Schema language
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

			// 2. Compile the schema. 
			URL schemaLocation = new URL("https://api.tripit.com/xsd/tripit-api-req-v1.xsd");
			Schema schema = factory.newSchema(schemaLocation);

			// 3. Get a validator from the schema.
			Validator validator = schema.newValidator();

			// 4. Parse the document you want to check.
			Source source = new StreamSource(new ByteArrayInputStream( xml.getBytes("UTF-8")));

			// 5. Check the document
			try {
				validator.validate(source);
				System.out.println(act.display_name + " is valid.");
				//build and send request
				HashMap<String,String> createMap = new HashMap<String,String>();
				createMap.put("xml", xml);
				Response r=client.create(createMap);
				System.out.println(r);
			}
			catch (SAXException ex) {
				System.err.println(act.display_name + " is not valid because ");
				System.err.println(ex.getMessage());
			}  

			
			
		}

		System.out.println("Done");
	}
}
