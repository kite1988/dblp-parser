package xml;

public class Conference {
	public String key;
	public String name;
	public String detail;

	public static final int OTHER = 0;
	public static final int PROCEEDING = 1;
	public static final int CONFNAME = 2;
	public static final int CONFDETAIL = 3;

	public static int getElement(String name) {
		if (name.equals("proceedings")) {
			return PROCEEDING;
		} else if (name.equals("booktitle")) {
			return CONFNAME;
		} else if (name.equals("title")) {
			return CONFDETAIL;
		} else {
			return OTHER;
		}
	}

	public static String getElementName(int i) {
		if (i == PROCEEDING) {
			return "inproceedings";
		} else if (i == CONFNAME) {
			return "booktitle";
		} else if (i == CONFDETAIL) {
			return "title";
		} else {
			return "other";
		}
	}

	public Conference() {
		this.key = "";
		this.name = "";
		this.detail = "";
	}
}
