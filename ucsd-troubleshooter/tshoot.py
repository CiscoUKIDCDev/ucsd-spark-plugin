import os, os.path, sys, json, Colourer, logging

FILEPATH = os.path.dirname(os.path.realpath(__file__))
os.chdir(FILEPATH)


#This is the block where each individual 'library' file will be called.
execfile(FILEPATH + "/tshoot/fileHandler.py")
execfile(FILEPATH + "/tshoot/propertiesChecker.py")
execfile(FILEPATH + "/tshoot/featureChecker.py")
execfile(FILEPATH + "/tshoot/ACMChecker.py")
execfile(FILEPATH + "/tshoot/buildChecker.py")



def checks():
	#logging.error("help")
	#moduleIDFromProperties = checkProperties(getPropertiesFile())
	#featureJSON = checkFeature(getFeatureFile())
	#pathToACM = getPathToACM(featureJSON)
	#checkACM(readFile(pathToACM))
	#moduleIDFromBuild = checkBuild("".join(getAntBuild()))

	compareModuleIDs(moduleIDFromProperties, moduleIDFromBuild)

def getPathToACM(jsonString):
	jsonCode = json.loads(jsonString)
	pathToClass = jsonCode['features'][0]
	return "src/" + pathToClass.replace(".", "/") + ".java"



def compareModuleIDs(fromProperties, fromAnt):
	if(fromProperties == fromAnt):
		print "ModuleIDs match"
	else:
		print "Your moduleIDs don't match!"
		print "In module.properties:    " + fromProperties
		print "In build.xml:            " + fromAnt


checks()