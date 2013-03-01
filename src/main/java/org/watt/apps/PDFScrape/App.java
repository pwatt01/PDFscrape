package org.watt.apps.PDFScrape;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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


/**
 * Hello world!
 *
 */
public class App 
{
	private static class Activity{
		String title, GPS,phone,description,hours,admission,day,distance,order,address;
		public Activity(){
			description = "";
		}
		public String toString() {
			return "Title: " + title +"\r\n" + 
					"Description: " + description +"\r\n" +
					"Address: " + address +"\r\n" +
					"Phone: " + phone + "\r\n" +
					GPS +"\r\n" +
					"Hours: " + hours +"\r\n" +
					"Admission: " + admission +"\r\n" +
					"Order: " + order +"\r\n"+
					"Day: " + day +"\r\n"+
					"Distance: " + distance +"\r\n"+"\r\n"; 

		}
	}
	public static void main( String[] args ) throws Exception
	{
		System.out.println( "Hello World!" );
		System.out.println(args[0]);
		PDDocument doc = PDDocument.load(new File(args[0]));
		PDFTextStripper ts = new PDFTextStripper();
		String text = ts.getText(doc);
		String[] txtln = text.split("\\r\\n");
		boolean activities = false,phone = false, hours = false, admission = false;;
		boolean day = false,firstLine = true;;
		String daynum = "";
		MyCircularFifoBuffer buf = new MyCircularFifoBuffer(3);
		Activity activity = null;
		ArrayList<Activity> activityList = new ArrayList<Activity>();
		for(String line : txtln){
			buf.add(line);

			if(line.equals("ACTIVITIES")){//trigger to get activities
				activities = true;
			}else if(buf.toString().equals("DAY") && activities){ //trigger to get day num
				day = true;
				daynum = "";
			}else if(activities && day && !daynum.equals("")){

				if(line.equals("You have no plans for this day")){
					day = false;
				}
				else{
					if(!line.matches("\\d+")){
						//System.out.println(line);
						if(firstLine){
							firstLine = false;
							activity = new Activity();
							activity.title = line;
							activity.day = daynum;
						}else{
							if(line.matches("[A-Z]")){
								activity.order = line;
								if (line.equals("H"))
									System.out.println("");
							}else if(line.startsWith("GPS: ")){
								activity.GPS = line;
							}else if(line.contains(", USA")){
								activity.address = line;
							}else if(line.startsWith("Phone:")){
								phone = true;			
							}else if(phone){
								activity.phone = line;
								phone = false;
							}else if(line.startsWith("Opening hours:")){
								hours = true;
							}else if(hours){
								activity.hours = line;
								hours = false;
							}else if(line.startsWith("Admission:")){
								admission = true;
							}else if(admission){
								activity.admission = line;
								admission = false;
							}else if(line.startsWith("on foot")){
								activity.distance = line;
								firstLine = true;
								activityList.add(activity);
								activity = null;
							}else if(line != null){
								if(activity.description.endsWith("- "))
									activity.description = activity.description.substring(0, activity.description.length()-2) + line;
								else
									activity.description += line + " ";
							}
						}
					}else if(activity != null){
						firstLine = true;
						activityList.add(activity);
						activity = null;
						day = false;
					}

				}
			}else if(activities && day){ //record day number
				daynum = line;
			}
		}
		doc.close(); 

		for(Activity act : activityList){
			System.out.println(act.toString());
		}
		//        //lucene
		//        
		//        Document doc = LucenePDFDocument.getDocument(new File(args[0]));
		//        List<Fieldable> list = doc.getFields();
		//        Iterator<Fieldable> itr = list.iterator();
		//        
		//        while(itr.hasNext()){
		//        	Fieldable obj = itr.next();
		//        	System.out.println(obj.toString());
		//        }
		//        Directory directory = new RAMDirectory();
		//        Version ver = Version.LUCENE_36;
		//        Analyzer analyzer =  new StandardAnalyzer(ver);
		//        IndexWriter iwriter = new IndexWriter(directory, new IndexWriterConfig(ver, analyzer));       
		//        iwriter.addDocument(doc);
		//        iwriter.close();
		//    
		//        
		//        // Now search the index:
		//        IndexReader ireader = IndexReader.open(directory); // read-only=true
		//        IndexSearcher isearcher = new IndexSearcher(ireader);
		//        // Parse a simple query that searches for "text":
		//        QueryParser parser = new QueryParser(ver, "fieldname", analyzer);
		//        Query query = parser.parse("text");
		//        ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		//       
		//        // Iterate through the results:
		//        for (int i = 0; i < hits.length; i++) {
		//          Document hitDoc = isearcher.doc(hits[i].doc);
		//         System.out.println(hitDoc.toString());
		//        }
		//        isearcher.close();
		//        ireader.close();
		//        directory.close();
		//        

	}
}
