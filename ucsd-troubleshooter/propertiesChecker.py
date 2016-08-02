import logging as l, Colourer

#Get the moduleID listed in the properties file
def getModuleIDFromProperties(fileContents):
	for i in fileContents:
		if("moduleID=" in i):
			return i.split("=")[1].rstrip()

	raise Exception()

#Get the 
def checkProperties(fileContents):
	l.log(2, "Checking module.properties...")
	moduleID = getModuleIDFromProperties(fileContents)
	l.info("\tThe moduleID listed in module.properties is: " + moduleID)
	print ""

	return moduleID