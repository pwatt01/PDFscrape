package org.watt.apps.PDFScrape;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class ParsePDF {


	public ArrayList<Activity> run(String[] args) throws IOException{
		
		//initialise variables
		boolean activities = false,phone = false, hours = false, 
				admission = false, day = false,firstLine = true;
		String daynum = "";
		MyCircularFifoBuffer buf = new MyCircularFifoBuffer(3);
		Activity activity = null;
		ArrayList<Activity> activityList = new ArrayList<Activity>();
		
		//load PDF
		PDDocument doc = PDDocument.load(new File(args[0]));
		
		//get text from PDF
		PDFTextStripper ts = new PDFTextStripper();
		String text = ts.getText(doc);
		
		//SPlit PDF into lines
		String[] txtln = text.split("\\r\\n");
		

		//go through each line of PDF
		for(String line : txtln){
			buf.add(line);

			if(line.equals("ACTIVITIES")){//trigger to get activities
				activities = true;
			}else if(buf.toString().equals("DAY") && activities){ //trigger to get day num
				day = true;
				daynum = "";
			}else if(activities && day && !daynum.equals("")){

				if(line.equals("You have no plans for this day")){//for pages/days with not activity
					day = false;
				}
				else{
					if(!line.matches("\\d+")){
						if(firstLine){
							firstLine = false;
							activity = new Activity();
							activity.location_name = line;
							activity.StartDateTime = daynum;
						}else{
							if(line.matches("[A-Z]")){
								activity.order = line;
								if (line.equals("H"))
									System.out.println("");
							}else if(line.startsWith("GPS: ")){
								activity.GPS = line;
							}else if(line.contains(", USA")){
								activity.Address = line;
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

		//print out activities
		for(Activity act : activityList){
			System.out.println(act.toString());
		}
		
		return activityList;
	}
	
	
	
}
