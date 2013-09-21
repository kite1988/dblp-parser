/*
 * This is the program that parses dblp xml using sax.
 * 
 * Note: only <inproceedings> and its children are the interested elements, and 
 * other elements will be ignored.
 * 
 * Authors, conference, papers, and the citations will be extracted and 
 * stored to the database. See dblp.sql for the database schema.
 * 
 * 
 * To know more about the xml structure of dblp, you may read:
 * 
 *  1. My previous Chinese article posted in CSDN blog. 
 *     http://blog.csdn.net/kite1988/article/details/5186628
 *     
 * 	2. DBLP — Some Lessons Learned, authored by Michael Ley.
 *     http://dblp.uni-trier.de/xml/docu/dblpxml.pdf
 *     
 *  3. DBLP XML DTD
 *     http://dblp.uni-trier.de/xml/dblp.dtd
 *  
 *  If you have any questions, feel free to drop me an email.
 *  Tao Chen (chentaokite AT gmail dot com)
 *  
 */

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
	private Connection conn;
	private int curElement = -1;
	private int ancestor = -1;
	private Paper paper;
	private Conference conf;
	int line = 0;
	PreparedStatement stmt_inproc, stmt_conf, stmt_author, stmt_cite;

	private class ConfigHandler extends DefaultHandler {
		public void startElement(String namespaceURI, String localName,
				String rawName, Attributes atts) throws SAXException {
			if (rawName.equals("inproceedings")) {
				ancestor = Element.INPROCEEDING;
				curElement = Paper.INPROCEEDING;
				paper = new Paper();
				paper.key = atts.getValue("key");
			} else if (rawName.equals("proceedings")) {
				ancestor = Element.PROCEEDING;
				curElement = Conference.PROCEEDING;
				conf = new Conference();
				conf.key = atts.getValue("key");
			}

			if (ancestor == Element.INPROCEEDING) {
				curElement = Paper.getElement(rawName);
			} else if (ancestor == Element.PROCEEDING) {
				curElement = Conference.getElement(rawName);
			} else if (ancestor == -1) {
				ancestor = Element.OTHER;
				curElement = Element.OTHER;
			} else {
				curElement = Element.OTHER;
			}

			line++;
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (ancestor == Element.INPROCEEDING) {
				String str = new String(ch, start, length).trim();
				if (curElement == Paper.AUTHOR) {
					paper.authors.add(str);
				} else if (curElement == Paper.CITE) {
					paper.citations.add(str);
				} else if (curElement == Paper.CONFERENCE) {
					paper.conference = str;
				} else if (curElement == Paper.TITLE) {
					paper.title = str;
				} else if (curElement == Paper.YEAR) {
					paper.year = Integer.parseInt(str);
				}
			} else if (ancestor == Element.PROCEEDING) {
				String str = new String(ch, start, length).trim();
				if (curElement == Conference.CONFNAME) {
					conf.name = str;
				} else if (curElement == Conference.CONFDETAIL) {
					conf.detail = str;
				}
			}
		}

		/*
		 * Special handle for child elements <sub>, <sup>, <i> and <tt> in
		 * title. For simplicity, just replace them with a whitespace.
		 */
		public String parseTitle(String title) {
			return title.replaceAll(
					"<sub>|</sub>|<sup>|</sup>|<i>|</i>|<tt>|</tt>", " ");
		}

		public void endElement(String namespaceURI, String localName,
				String rawName) throws SAXException {
			if (Element.getElement(rawName) == Element.INPROCEEDING) {
				ancestor = -1;
				try {
					if (paper.title.equals("") || paper.conference.equals("")
							|| paper.year == 0) {
						System.exit(0);
					}
					stmt_inproc.setString(1, paper.title);
					stmt_inproc.setInt(2, paper.year);
					stmt_inproc.setString(3, paper.conference);
					stmt_inproc.setString(4, paper.key);
					stmt_inproc.addBatch();

					for (int i = 0; i < paper.authors.size(); i++) {
						stmt_author.setString(1, paper.authors.get(i)
								.toString());
						stmt_author.setString(2, paper.key);
						stmt_author.addBatch();
					}

					for (int i = 0; i < paper.citations.size(); i++) {
						stmt_cite.setString(1, paper.key);
						stmt_cite.setString(2, paper.citations.get(i));

						if (paper.citations.get(i).equals("..."))
							continue;
						stmt_cite.addBatch();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("line:" + line);
					System.exit(0);
				}
			} else if (Element.getElement(rawName) == Element.PROCEEDING) {
				ancestor = -1;
				try {
					if (conf.name.equals(""))
						conf.name = conf.detail;
					if (conf.key.equals("") || conf.name.equals("")
							|| conf.detail.equals("")) {
						System.out.println("Line:" + line);
						System.exit(0);
					}
					stmt_conf.setString(1, conf.key);
					stmt_conf.setString(2, conf.name);
					stmt_conf.setString(3, conf.detail);
					stmt_conf.addBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("line:" + line);
					System.exit(0);
				}
			}

			if (line % 1000 == 0) {
				try {
					stmt_inproc.executeBatch();
					stmt_conf.executeBatch();
					stmt_author.executeBatch();
					stmt_cite.executeBatch();
				} catch (SQLException e) {
					e.printStackTrace();
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

	Parser(String uri) throws SQLException {
		try {
			conn = DBConnection.getConn();
			stmt_inproc = conn
					.prepareStatement("insert into paper(title,year,conference,paper_key) values (?,?,?,?)");

			stmt_author = conn
					.prepareStatement("insert into author(name,paper_key) values (?,?)");

			stmt_cite = conn
					.prepareStatement("insert into citation(paper_cite_key,paper_cited_key) values (?,?)");

			stmt_conf = conn
					.prepareStatement("insert into conference(conf_key,name,detail) values (?,?,?)");

			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			ConfigHandler handler = new ConfigHandler();
			parser.getXMLReader().setFeature(
					"http://xml.org/sax/features/validation", true);
			parser.parse(new File(uri), handler);
			conn.close();
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
		Long start = System.currentTimeMillis();
		Parser p = new Parser(args[0]);
		Long end = System.currentTimeMillis();
		System.out.println("Used: " + (end - start) / 1000 + " seconds");
	}
}
