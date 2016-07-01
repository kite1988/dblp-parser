
# What is this tool used for? 
This is a tool to parse dblp.xml (http://dblp.uni-trier.de/xml/) and store inproceedings into database.

To be specific, four key information, namely **author**, **conference**, **paper**, and **citation** under **<inproceedings>** will be extracted and stored in MySQL database as four tables. Please see dblp.sql for the database schema.

The initial code was written by me in early 2010 for my undergraduate thesis. With some modification, I released the code in Google Code on September 2013, and then migrated it to GitHub. I am still maintaining the code, e.g., fixing bugs as well as trying to implement some new features. Welcome to contact me for any questions/suggestion/bugs.

# Testing [Updated: Jul 1, 2016]
The code has been tested with four versions of dblp.xml, namely dblp-2002, dblp-2013, dblp-2014, dblp-2015 under JDK 1.7. It should also work well with JDK 1.5 and JDK 1.6. 


# How to use
1. Download dblp.xml and dblp.dtd from http://dblp.uni-trier.de/xml/, and save them at the same folder.


2. Restore the database with MySQL dump file (dblp.sql)
Suppose you have already installed MySQL database. 
You may first log to your mysql and then use "source" command to restore the database:
mysql>> source path_of_dblp.sql

If everything goes well, a database named dblp with four tables (i.e., author, citation, conference, paper) has be created.


3. Configure database connection
Please change dbUrl, user, password in [db/DBConnection.java] (https://github.com/kite1988/dblp-parser/blob/master/src/db/DBConnection.java) accoarding to your setting.


4. Run the parser
a. If you are using IDE, e.g., Eclipse.
You need to add mysql-connector-java-6.0-bin.jar to the build path. 
See this [post](http://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java) on how to do this.

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

