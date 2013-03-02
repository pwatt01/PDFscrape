package org.watt.apps.TripItConversions;

public class Activity{
	String trip_id,display_name, GPS,booking_site_phone,notes,hours,total_cost,distance,order;
	StartDateTime StartDateTime;
	Address Address;
	
	public Activity(String tripID){
		trip_id = tripID;
		notes = "";
		StartDateTime = new StartDateTime();
		Address = new Address();
	}
	public  String toString() {
		return "Title: " + display_name +"\r\n" + 
				"Description: " + notes +"\r\n" +
				"Address: " + Address +"\r\n" +
				"Phone: " + booking_site_phone + "\r\n" +
				GPS +"\r\n" +
				"Hours: " + hours +"\r\n" +
				"Admission: " + total_cost +"\r\n" +
				"Order: " + order +"\r\n"+
				"Day: " + StartDateTime +"\r\n"+
				"Distance: " + distance +"\r\n"+"\r\n"; 

	}
	public static class StartDateTime{
		public String date, time;
		public String toString() {
			return "Date: " + date +"\r\n" + 
					"Time: " + time; 

		}
	}
	public static class Address{
		public String address;
		public String toString() {
			return "Address: " + address;

		}
	}
}
