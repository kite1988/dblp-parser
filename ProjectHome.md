This is a tool that parses dblp.xml (http://dblp.uni-trier.de/xml/)  file and stores inproceedings into database.

Authors, conference, papers, and the citations under inproceedings will be extracted and stored in database as four tables.
See dblp.sql for the database schema.

This code was first written by me in early 2010 for my undergraduate thesis, and then was slightly modified in September 2013.
I will keep this project updated, and welcome to email me any questions/suggestion/bugs at: Tao Chen (chentaokite AT gmail dot com)

**README** [Updated: Apr 20, 2014]

The detailed steps of using this tool is available at  (https://code.google.com/p/dblp-parser/source/browse/ReadMe.txt).

**Testing** [Updated: Apr 29, 2014]

The code has been tested with three versions of dblp.xml, namely dblp-2002, dblp-2013 and dblp-2014, under JDK 1.7. It should be working well with JDK 1.5 and JDK 1.6.

**More**

To know more about the xml structure of dblp, you may read:

1. My previous Chinese article posted in CSDN blog.
> http://blog.csdn.net/kite1988/article/details/5186628

2. DBLP — Some Lessons Learned, authored by Michael Ley.
> http://dblp.uni-trier.de/xml/docu/dblpxml.pdf

3. DBLP XML DTD
> http://dblp.uni-trier.de/xml/dblp.dtd



---

Known issues [Updated: Nov 18, 2013]:

**1. Out Of Memory Error**

If the default memory for JVM is too small, please increase the allocation by specifying
JVM argument -Xmx`[memory size]` (e.g., -Xmx1G).

**2. Too small entity limit**

The parser has encountered more than "64,000" entity expansions in this document; this is the limit imposed by the application

Please specify another JVM argument `-DentityExpansionLimit=2500000`