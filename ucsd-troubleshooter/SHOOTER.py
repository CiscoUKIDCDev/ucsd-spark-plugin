import os, os.path, sys, json, Colourer, logging as l

import glob
import fileHandler
import propertiesChecker
import featureChecker
import buildChecker
import ACMChecker
import JDOFilesChecker
import configChecker

#Set the current working directory to the ucsd-troubleshooter folder.
os.chdir(os.path.dirname(os.path.realpath(__file__)))

#print fileHandler.getPropertiesFile()

#print fileHandler.getPropertiesFile()


#A test method to demonstrate which levels of logging correspond to which colours
def test():
	l.error("error")
	l.critical("critical")
	l.warning("warning")
	l.log(1, "log at 1")
	l.info("info")

#Carry out the required checks on the .feature file
def checkFeature():
	#Do the .feature file checks and store the returned JSON
	
	json = featureChecker.checkFeature(fileHandler.getFeatureFile())
	return json

#Carr out the required checks on the feature class.
def checkACM(featureJSON):
	fileContents = 0
  	try:
		fileContents = fileHandler.getACMFile(featureJSON)
		ACMChecker.checks(fileContents)
	except IOError, e:
		l.critical("The class description in the .feature file does not match the file path of the corresponding .java file. Ie: com.cloupia.feature.foo.FooModule should map to /src/cloupia/feature/foo/FooModule.java")
		sys.exit()
	#print ACMChecker.checks(fileContents)

#Check the build.xml file 
def checkAnt():
	return buildChecker.checkBuild(fileHandler.getAntBuild())

#Check the module.properties file
def checkProperties():
	#Do the checks for module.properties and store the moduleID for comparison
	return propertiesChecker.checkProperties(fileHandler.getPropertiesFile())

#Check all jdo.files files 
def checkJDOFiles():
	'''
	l.info("Checking all jdo.files...")
	#This function gets every jdo.files file that exists within the project
	allFiles = fileHandler.getJDOFiles()

	#For each one of the jdo.files that we find, pass the file's contents and the directory in which it resides.
	for f in allFiles:
		JDOFilesChecker.check(fileHandler.readFile(f), f)
		'''

	
	JDOFilesChecker.check()

	#for jdofiles in allFiles:
	#	JDOFilesChecker.check(fileHandler.readFile(jdofiles))

def checkConfigFiles(moduleID):
	print ""
	configChecker.checks(moduleID)





#Check that moduleIDs match across all declared instances.
def checkModuleIDMatch(one, two):
	if(one == two):
		l.log(1, "Module IDs match across module.properties, .feature, Ant Build, and Config files")
	else:
		l.critical("ModuleIDs across module.properties, .feature, build.xml, and Config files do not match")


def checks():

	propertiesMID = checkProperties()
	l.info("")
	featureJSON = checkFeature()
	l.info("")
	checkACM(featureJSON)
	l.info("")
	antMID = checkAnt()
	l.info("")
	checkJDOFiles()
	l.info("")
	featureMID = featureChecker.getModuleIDFromFileName(fileHandler.getFeatureFileName())
	checkConfigFiles(propertiesMID)
	#print fileHandler.getACMFile(featureJSON['features'])

	#print os.path.exists(fileHandler.getACMFileFromJavaPath(featureJSON, propertiesMID))
	l.info("")
	l.log(1, "Checks completed")


checks()



