
# What does this tool doing? 
This is a tool to parse dblp.xml (http://dblp.uni-trier.de/xml/) and store inproceedings into database.

Authors, conference, papers, and the citations under inproceedings will be extracted and stored in database as four tables. See dblp.sql for the database schema.

This code was first written by me in early 2010 for my undergraduate thesis, and then was slightly modified in September 2013. I will keep this project updated, and welcome to email me any questions/suggestion/bugs at: Tao Chen (chentaokite AT gmail dot com)

# Testing
The code has been tested with three versions of dblp.xml, namely dblp-2002, dblp-2013 and dblp-2014, under JDK 1.7. It should be working well with JDK 1.5 and JDK 1.6. [Updated: Apr 29, 2014]


# How to use
1. Download dblp.xml and dblp.dtd from http://dblp.uni-trier.de/xml/, and save them at the same folder.


2. Restore the database with mysql dump file (dblp.sql)
Suppose you have already installed mysql database. 
You may first log to your mysql and then use "source" command to restore the database:
mysql>> source path_of_dblp.sql

If everything goes well, a database named dblp with four tables (i.g., author, citation, conference, paper) will be created.


3. Configure database connection
Please change the settings (i.g., dbUrl, user, password) in db/DBConnection.java accordingly.


4. Run the parser
a. If you are using IDE, e.g., Eclipse.
You need to add mysql-connector-java-6.0-bin.jar to the build path. 
See this post if you need any help.
http://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java)

And then run Parser.java with proper program argument (the path of dblp.xml) and VM
arguments (-Xmx1G -DentityExpansionLimit=2500000). You may see this post on how to specify 
arguments in Eclipse.
http://www.cs.colostate.edu/helpdocs/eclipseCommLineArgs.html

b. If you are using command line
Similarly, you need to add mysql-connector-java-6.0-bin.jar in the classpath, and set
the arguments. The command will be something like:
java -cp mysql-connector-java-6.0-bin.jar -Xmx1G -DentityExpansionLimit=2500000 Parser [path_of_dblp.xml]

The program will run for a while. For example, it takes 974 seconds to parse dblp-2014.xml using my desktop.

# Known Issues
1. Out Of Memory Error
If the default memory for JVM is too small, please increase the allocation by specifying JVM argument -Xmx[memory size] (e.g., -Xmx1G).

2. Too small entity limit
The parser has encountered more than "64,000" entity expansions in this document; this is the limit imposed by the application
Please specify another JVM argument -DentityExpansionLimit=2500000

# More
To know more about the xml structure of dblp, you may read:

- My previous Chinese article posted in CSDN blog.
http://blog.csdn.net/kite1988/article/details/5186628

- DBLP — Some Lessons Learned, authored by Michael Ley.
http://dblp.uni-trier.de/xml/docu/dblpxml.pdf

- DBLP XML DTD
http://dblp.uni-trier.de/xml/dblp.dtd
Known issues [Updated: Nov 18, 2013]:

