package xml;

import java.util.ArrayList;

public class Paper {
	private String key;
	private String title;
	private int year;
	private String conference;
	private ArrayList<String> authors;
	private ArrayList<String> citations;
	
	public static final int OTHER = 0;
	public static final int INPROCEEDING = 1;
	public static final int AUTHOR = 2;
	public static final int TITLE = 3;
	public static final int YEAR=4;
	public static final int CITE = 5;
	public static final int CONFERENCE = 6;	
	
	public static int getElement(String name)
	{
		if(name.equals("inproceedings"))
			return INPROCEEDING;
		else if(name.equals("author"))
			return AUTHOR;
		else if(name.equals("title"))
			return TITLE;
		else if(name.equals("year"))
			return YEAR;
		else if(name.equals("cite"))
			return CITE;
		else if(name.equals("booktitle"))
			return CONFERENCE;
		else
			return OTHER;
			
	}
	
	public static String getElementName(int i)
	{
		if(i==INPROCEEDING)
			return "inproceedings";
		else if(i==AUTHOR)
			return "author";
		else if(i==TITLE)
			return "name";
		else if(i==YEAR)
			return "year";
		else if(i==CITE)
			return "cite";
		else if(i==CONFERENCE)
			return "booktitle";
		else 
			return "other";
	}
	
	public Paper()
	{
		key = "";
		title ="";
		conference="";
		year = 0;
		authors = new ArrayList<String>();
		citations = new ArrayList<String>();
	}
	
	
	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getConference() {
		return conference;
	}
	public void setConference(String conference) {
		this.conference = conference;
	}
	public ArrayList<String> getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	public ArrayList<String> getCitations() {
		return citations;
	}
	public void setCitations(ArrayList<String> citations) {
		this.citations = citations;
	}
	
	

}
