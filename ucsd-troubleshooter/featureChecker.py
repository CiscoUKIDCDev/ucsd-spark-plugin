import string, json, logging as l, Colourer, inspect, fileHandler, os

'''Working as at 1457 on 10 MAR'''

#Convert a text file to JSON assuming it is correctly formed
def castToJSON(fileContents):
	string = "".join(fileContents)
	return json.loads(string)

#Get the libraries listed in the .feature file
def getLibrariesListed(json):
	return json["jars"]

#Get the ACM class location declared in the .feature file
def getACMClass(json):
	return json["features"]

def getModuleIDFromFileName(fileName):
	path = fileName.split("/")
	path = path[len(path)-1]
	path = path.split(".")[0]
	return path


def isValid(fileContents):
	if(fileHandler.is_json("".join(fileContents))):
		l.log(1, "\t.feature File is formed of valid JSON")
	else:
		l.critical("\t.feature File is not formed of valid JSON")

#Get the file name of the .feature file. This should match the moduleID

#Carry out the checks on the .feature file
def checkFeature(fileContents):
	l.log(2, "Checking .feature File...")
	jsonCode = castToJSON(fileContents)
	l.info("\tYour Listed ACM is: " + "".join(getACMClass(jsonCode)))

	l.info("\tThe enabled libraries are: ")
	for i in getLibrariesListed(jsonCode):
		l.info("\t\t" + i)
	#Don't remove this line - it handles success/failure 
	isValid(fileContents)

	print ""
	return jsonCode






































