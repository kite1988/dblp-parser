package xml;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import db.DBConnection;

public class Parser {

	private static Connection conn;
	// private static PreparedStatement ps;
	private int curElement = -1; // 指示当前的element
	private int ancestor = -1;
	// private boolean parentIsInproceedings = false;
	// private ArrayList<Paper> pList = new ArrayList<Paper>();
	private Paper paper;
	private Conference conf;
	int line = 0;

	// private HashMap<String, String> elementMap = new HashMap<String,
	// String>();

	private class ConfigHandler extends DefaultHandler {

		public void startElement(String namespaceURI, String localName,
				String rawName, Attributes atts) throws SAXException {
			// System.out.print("<" + rawName + ">");

			// if(elementMap.get(rawName)==null)
			// elementMap.put(rawName, rawName);

			// 第一次开始解析
			// if(ancestor==-1||ancestor==0)
			// ancestor = Element.getElement(rawName);

			if (rawName.equals("inproceedings")) {
				ancestor = Element.INPROCEEDING;
				curElement = Paper.INPROCEEDING;
				paper = new Paper();

				String key = atts.getValue("key");
				// System.out.println("paper key: "+key);
				paper.setKey(key);
			} else if (rawName.equals("proceedings")) {
				ancestor = Element.PROCEEDING;
				curElement = Conference.PROCEEDING;
				conf = new Conference();

				String key = atts.getValue("key");
				// System.out.println("conf key: "+key);
				conf.setKey(key);

			}

			if (ancestor == Element.INPROCEEDING) {
				curElement = Paper.getElement(rawName);
			} else if (ancestor == Element.PROCEEDING) {
				curElement = Conference.getElement(rawName);
			} else if (ancestor == -1) {
				ancestor = Element.OTHER;
				curElement = Element.OTHER;
			} else
				curElement = Element.OTHER;

			line++;

		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (ancestor == Element.INPROCEEDING) {
				String str = new String(ch, start, length).trim();

				if (curElement == Paper.AUTHOR)
					paper.getAuthors().add(str);
				else if (curElement == Paper.CITE)
					paper.getCitations().add(str);
				else if (curElement == Paper.CONFERENCE)
					paper.setConference(str);
				else if (curElement == Paper.TITLE)
					paper.setTitle(str);
				else if (curElement == Paper.YEAR)
					paper.setYear(Integer.parseInt(str));
			} else if (ancestor == Element.PROCEEDING) {
				// System.out.println("parse conf");
				// System.out.println(new String(ch,start,length));

				String str = new String(ch, start, length).trim();

				if (curElement == Conference.CONFNAME)
					conf.setName(str);
				else if (curElement == Conference.CONFDETAIL)
					conf.setDetail(str);
			}// else
			// System.out.println(new String(ch,start,length));
		}

		public void endElement(String namespaceURI, String localName,
				String rawName) throws SAXException {
			// System.out.println("<" + rawName + "/>");

			if (Element.getElement(rawName) == Element.INPROCEEDING) {
				ancestor = -1;

				// 把paper存入数据库
				try {
					// System.out.println("Title:"+paper.getTitle()+"year:"+paper.getYear()+"
					// conf:"+paper.getConference());
					if (paper.getTitle().equals("")
							|| paper.getConference().equals("")
							|| paper.getYear() == 0) {
						// System.out.println("Line:" + line);
						// System.out.println("Title:" + paper.getTitle()
						// + " year:" + paper.getYear() + " conf:"
						// + paper.getConference()) ;

						System.exit(0);
					}

					PreparedStatement stmt = conn
							.prepareStatement("insert into temp_inproc(title,year,conference,id) values(?,?,?,?)");

					stmt.setString(1, paper.getTitle());
					stmt.setInt(2, paper.getYear());
					stmt.setString(3, paper.getConference());
					stmt.setString(4, paper.getKey());
					stmt.execute();
					stmt.close();

					// System.out.println("author: "+paper.getAuthors().size());

					for (int i = 0; i < paper.getAuthors().size(); i++) {
						PreparedStatement stmt2 = conn
								.prepareStatement("insert into temp_author(name,pId) values(?,?)");
						// System.out.println("begin insert author");
						stmt2
								.setString(1, paper.getAuthors().get(i)
										.toString());
						stmt2.setString(2, paper.getKey());
						// System.out.println("begin insert author2");
						stmt2.execute();
						// System.out.println("begin insert author3");
						stmt2.close();
					}
					// System.out.println("end of insert author");

					for (int i = 0; i < paper.getCitations().size(); i++) {
						PreparedStatement stmt3 = conn
								.prepareStatement("insert into temp_citation(pId1,pId2) values(?,?)");
						stmt3.setString(1, paper.getKey());
						stmt3.setString(2, paper.getCitations().get(i));

						if (paper.getCitations().get(i).equals("..."))
							continue;
						stmt3.execute();

						stmt3.close();

					}

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("line:" + line);
					System.exit(0);
				}
			}

			else if (Element.getElement(rawName) == Element.PROCEEDING) {
				ancestor = -1;
				// 存入会议的信息
				try {
					if (conf.getName().equals(""))
						conf.setName(conf.getDetail());
					if (conf.getKey().equals("") || conf.getName().equals("")
							|| conf.getDetail().equals("")) {
						System.out.println("Line:" + line);
						// System.out.println("conf: "+conf.getKey()+"
						// "+conf.getName()+" "+conf.getDetail());
						System.exit(0);
					}
					PreparedStatement stmt = conn
							.prepareStatement("insert into temp_conf(id,name,detail) values(?,?,?)");
					stmt.setString(1, conf.getKey());
					stmt.setString(2, conf.getName());
					stmt.setString(3, conf.getDetail());

					// System.out.println(stmt.toString());

					stmt.execute();
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("line:" + line);
					System.exit(0);
				}
			}
		}

		private void Message(String mode, SAXParseException exception) {
			System.out.println(mode + " Line: " + exception.getLineNumber()
					+ " URI: " + exception.getSystemId() + "\n" + " Message: "
					+ exception.getMessage());
		}

		public void warning(SAXParseException exception) throws SAXException {

			Message("**Parsing Warning**\n", exception);
			throw new SAXException("Warning encountered");
		}

		public void error(SAXParseException exception) throws SAXException {

			Message("**Parsing Error**\n", exception);
			throw new SAXException("Error encountered");
		}

		public void fatalError(SAXParseException exception) throws SAXException {

			Message("**Parsing Fatal Error**\n", exception);
			throw new SAXException("Fatal Error encountered");
		}

	}

	Parser(String uri) {
		try {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			ConfigHandler handler = new ConfigHandler();
			parser.getXMLReader().setFeature(
					"http://xml.org/sax/features/validation", true);
			parser.parse(new File(uri), handler);

			/*
			 * Iterator i = elementMap.values().iterator();
			 * System.out.println("element的size: " + elementMap.size()); while
			 * (i.hasNext()) { String str = (String) i.next();
			 * System.out.println("<" + str + ">"); }
			 */
		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error in XML parser configuration: "
					+ e.getMessage());
		}

	}

	public static void main(String[] args) throws SQLException,
			ClassNotFoundException {
		// if (args.length < 1) {
		// System.out.println("Usage: java Parser [input]");
		// System.exit(0);
		// }
		conn = DBConnection.getConn();
		Long start = System.currentTimeMillis();
		Parser p = new Parser("G:\\学习\\毕业论文\\dblp\\dblp-2002-10.xml");
		Long end = System.currentTimeMillis();
		System.out.println("time: " + (end - start));

		conn.close();
	}
}
