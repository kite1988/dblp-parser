package xml;

public class Conference {
	private String key;
	private String name;
	private String detail;
	
	public static final int OTHER = 0;
	public static final int PROCEEDING = 1;
	public static final int CONFNAME = 2;
	public static final int CONFDETAIL =3;
	
	public static int getElement(String name)
	{
		
		if(name.equals("proceedings"))
			return PROCEEDING;
		else if(name.equals("booktitle"))
			return CONFNAME;
		else if(name.equals("title"))
			return CONFDETAIL;
		else return OTHER;
			
	}
	
	public static String getElementName(int i)
	{
		if(i==PROCEEDING)
			return "inproceedings";
		else if(i==CONFNAME)
			return "booktitle";
		else if(i==CONFDETAIL)
			return "title";
		else 
			return "other";
	}
	
	
	public Conference()
	{
		this.key ="";
		this.name = "";
		this.detail="";
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	

}
