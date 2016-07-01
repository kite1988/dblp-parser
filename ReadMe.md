
# What is this tool used for? 
This is a tool to parse dblp.xml (http://dblp.uni-trier.de/xml/) and store inproceedings into MySQL database.

To be specific, four key information, namely **author**, **conference**, **paper**, and **citation** under **\<inproceedings\>** will be extracted and stored in MySQL database as four tables. Please see [dblp.sql](https://github.com/kite1988/dblp-parser/blob/master/dblp.sql) for the database schema.

The initial code was written by me in early 2010 for my undergraduate thesis. With some modifications, I released the code in [Google Code](https://code.google.com/archive/p/dblp-parser) on September 2013, and later migrated it to GitHub. I am still actively maintaining the code, e.g., fixing bugs as well as trying to implement new features. Therefore, welcome to contact me for any questions/suggestion/bug reports!

# Testing [Updated: Jul 1, 2016]
  The code has been tested with four versions of dblp.xml, namely dblp-2002, dblp-2013, dblp-2014, dblp-2015 under JDK 1.7. It should also work well with JDK 1.5 and JDK 1.6. 
  
  
# How to use
1. Download dblp.xml and dblp.dtd from http://dblp.uni-trier.de/xml/, and save them at the same folder.


2. Restore the database with MySQL dump file (dblp.sql)

  Suppose you have already installed MySQL database. 
  You may first log to your MySQL and then use "source" command to restore the database:
  ```
  mysql>> source path_of_dblp.sql
  ```

  If everything goes well, a database named dblp with four tables (i.e., author, citation, conference, paper) has be created.


3. Configure database connection

  Please change dbUrl, user, password in [db/DBConnection.java] (https://github.com/kite1988/dblp-parser/blob/master/src/db/DBConnection.java) according to your own database setting.


4. Run the parser

  * Using Eclipse

    You need to add mysql-connector-java-6.0-bin.jar to project build path. 
    See this [post] (http://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java)) on how to do this.

    And then run Parser.java with 
    * program argument: the path of dblp.xml
    * VM arguemnts: -Xmx1G -DentityExpansionLimit=2500000

    See this [post] (http://www.cs.colostate.edu/helpdocs/eclipseCommLineArgs.html) if you are not sure how to specify arguments in Eclipse.


  * Using command line

    Similarly, you need to add mysql-connector-java-6.0-bin.jar to the classpath, and set program and VM arguments. The command will be something like:
    ```
    java -cp mysql-connector-java-6.0-bin.jar -Xmx1G -DentityExpansionLimit=2500000 Parser [path_of_dblp.xml]
    ```

**All done!** The program will run for a while. On my old desktop, it takes 974 seconds to parse dblp-2014.xml.

### Known issues
* Out of memory error
  
  If the program raises this error, please increase the memeory allocation to this program by specifying JVM argument -Xmx\[memory size\] (e.g., -Xmx1G).

* Too small entity limit error

  > The parser has encountered more than "64,000" entity expansions in this document; this is the limit imposed by the application

  Please specify another JVM argument -DentityExpansionLimit=2500000

# More
  To know more about the xml structure of dblp, you may read:

- My previous article posted in CSDN blog (in Chinese)

  http://blog.csdn.net/kite1988/article/details/5186628

- DBLP — Some Lessons Learned, authored by Michael Ley
  
  http://dblp.uni-trier.de/xml/docu/dblpxml.pdf

- DBLP XML DTD
  
  http://dblp.uni-trier.de/xml/dblp.dtd
