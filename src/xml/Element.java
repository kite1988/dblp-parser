package xml;

public class Element {
	public static final int OTHER =0;
	public static final int INPROCEEDING = 1;
	public static final int PROCEEDING = 2;
	
	
	public static int getElement(String name)
	{
		if(name.equals("inproceedings"))
			return 1;
		else if(name.equals("proceedings"))
			return 2;
		else
			return 0;
			
	}
	
	public static String getElementName(int i)
	{
		if(i==INPROCEEDING)
			return "inproceedings";
		else if(i==PROCEEDING)
			return "proceedings";
		else 
			return "other";
	}
	

}
