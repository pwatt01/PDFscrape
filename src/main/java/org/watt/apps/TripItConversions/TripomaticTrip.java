package org.watt.apps.TripItConversions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.text.DateFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class TripomaticTrip implements Trip{


	public ArrayList<Activity> run(String[] args) throws IOException, ParseException{
		
		
		//initialise variables
		boolean activities = false,phone = false, hours = false, 
				admission = false, day = false,firstLine = true;
		String daynum = "",tripId="",dateInput = "";
		MyCircularFifoBuffer buf = new MyCircularFifoBuffer(3);
		Activity activity = null;
		ArrayList<Activity> activityList = new ArrayList<Activity>();
		Date startDate;
		
		//get user input
		System.out.println("Please enter the TripIt Trip ID \r\n" +
				"(located in the url, i.e. https://www.tripit.com/trip/show/id/65902138):");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		tripId = br.readLine();
		tripId="65902138";
		
		System.out.println("Please enter the starting date for your Tripomatic itinerary (dd/mm/yyyy)");
		br = new BufferedReader(new InputStreamReader(System.in));
//		dateInput = br.readLine();
		dateInput = "2/3/2013";
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			startDate =  df.parse(dateInput);
		} catch (ParseException e) {
			System.out.println("oops, your date is not formatted correctly. Please try again");
			throw e;
		} 
		System.out.println(startDate);
		
		
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
							activity = new Activity(tripId);
							activity.display_name = line;
							Calendar cal = Calendar.getInstance();
							cal.setTime(startDate);
							cal.add(Calendar.DATE, Integer.parseInt(daynum)-1);
							activity.StartDateTime.date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()).toString();
						}else{
							if(line.matches("[A-Z]")){
								//activity.order = line;
							}else if(line.startsWith("GPS: ")){
//								activity.GPS = line;
							}else if(line.contains(", USA")){
								activity.Address.address = line;
							}else if(line.startsWith("Phone:")){
								phone = true;			
							}else if(phone){
								activity.booking_site_phone = line;
								phone = false;
							}else if(line.startsWith("Opening hours:")){
								hours = true;
							}else if(hours){
								//activity.hours = line;
								hours = false;
							}else if(line.startsWith("Admission:")){
								admission = true;
							}else if(admission){
								activity.total_cost = line;
								admission = false;
							}else if(line.startsWith("on foot")){
								//activity.distance = line;
								firstLine = true;
								activityList.add(activity);
								activity = null;
							}else if(line != null){
								if(activity.notes.endsWith("- "))
									activity.notes = activity.notes.substring(0, activity.notes.length()-2) + line;
								else
									activity.notes += line + " ";
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
		return activityList;
	}
	
	
	
}
