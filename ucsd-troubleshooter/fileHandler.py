import os, os.path, sys, logging as l, Colourer, string, json, fnmatch
import glob

directory = sys.argv[0]

#Now working

#Return the contents of the module.properties file. This function depends on the project directory being set correctly in the .cfg file
def getPropertiesFile():
	dir = getProjectDir() + 'src/module.properties'
	if(sum(1 for x in glob.iglob(dir)) != 1):
		l.critical("There should be exactly one module.properties file in the /src folder of your project directory")
		raise Exception("Critical File Missing")

	for filename in glob.iglob(dir):
		return readFile(filename)

#Return the contents of the .feature file
def getFeatureFile():
	dir = getProjectDir() + '*.feature'
	if(sum(1 for x in glob.iglob(dir)) != 1):
		l.critical("There should be exactly one .feature file in your project directory")
		raise Exception("Critical File Missing")

	for filename in glob.iglob(dir):
		return readFile(filename)












def getAllFilesMatching(regex):
	matches = []
	for root, dirnames, filenames in os.walk(getProjectDir()):
		for filename in fnmatch.filter(filenames, regex):
			matches.append(os.path.join(root, filename))

	return matches














def getFeatureFileName():
	dir = getProjectDir() + '*.feature'
	for filename in glob.iglob(dir):
		return filename

#Return the contents of the build.xml file
def getAntBuild():
	dir = getProjectDir() + "build.xml"
	for filename in glob.iglob(dir):
		return readFile(filename)

#Return the class described in the .feature file as being the "feature" class. This will be the class that extends AbstractCloupiaModule
def getACMFile(jsonCode):
	return readFile(getACMFileFromJavaPath(jsonCode))


#Return the directories of all jdo.files files that exist within the project directory
def getJDOFiles():
	tR = []
	dir = getProjectDir() + "src/**/jdo.files"

	matches = []
	for root, dirnames, filenames in os.walk(getProjectDir() + 'src'):
		for filename in fnmatch.filter(filenames, 'jdo.files'):
			matches.append(os.path.join(root, filename))

	return matches




#Return the contents of a file at the directory 'path'
def readFile(path):
	with open(path) as f:
		return f.readlines()

#Return the project directory listed in the .cfg file. 
def getProjectDir():
	string = "".join(readFile("../shooter.cfg"))
	return ".." + json.loads(string)["projectDir"]

#Return all files that exist within a directory
def getAllDirsInPath(path):
	tR = list()
	for path, subdirs, files in os.walk(path):
		for filename in files:
			tR.append(path + "/" + filename)
		
	return tR


#Convert a java class location to a file location in the project directory, eg com.cloupia.feature.foo.FooModule maps to ../foo/src/com/cloupia/feature/foo/FooModule.java
def getACMFileFromJavaPath(jsonCode):
	path = "".join(jsonCode["features"])
	path = path.replace(".", "/")
	path = getProjectDir() + "src/" + path + ".java"

	return path

#Determines if a string is valid JSON
def is_json(myjson):
  try:
    json_object = json.loads(myjson)
  except ValueError, e:
    return False
  return True

'''
Get a list of files that exist within a directory. Can choose whether to include the file extension in the 
returned list.
'''
def getFilesInDir(path, extensions):
	tR = list()
	files = [f for f in os.listdir(path) if os.path.isfile(os.path.join(path, f))]
	for f in files:
		if(extensions):
			tR.append(f)
		else:
			if(not f.startswith(".")):
				name = f.split(".")[0]
				tR.append(name)

	return tR

#################################################################