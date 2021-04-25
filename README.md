# File searcher

This is a small application written in scala without any external libraries. 
Main purpose of this program is to search for user provided words in a set of files,
which are loaded at the start of the application. The user ca provide any directory 
with files at the initial startup.

## How to run

The set up is pretty straight forward. The application can be run from sbt with:
```bash
> sbt
> runMain searcher.Main src/main/resources/textFiles
 ```
Or just run:
```bash
> sbt "runMain searcher.Main src/main/resources/textFiles"
 ```

From there you can search for any words separated by a space. 
The application will check how many words were actually in a file and generate a 
procentage representation of it. Is none of the words were found in any file 
you will receive a message: "No matches found". In order to close the prompt you 
have to write ":quit". An example of a search would look like this:

```bash
search> I hear the trains are comming
File name: vol04.iss0064-0118.txt : 100 %
File name: vol09.iss0050-0100.txt : 83 %
File name: tr823.txt : 67 %
File name: howtobbs.txt : 67 %
File name: locale.txt : 50 %
File name: pldd.txt : 50 %
File name: iconv.txt : 50 %
File name: ldd.txt : 50 %
File name: sprof.txt : 50 %
File name: Untitled PDF.pdf : 33 %
search> 
 ```