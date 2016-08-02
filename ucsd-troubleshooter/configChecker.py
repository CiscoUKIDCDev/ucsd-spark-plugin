import fileHandler, logging as log, Colourer, gstring, re

def checks(moduleID):
	log.log(2, "Checking Config files...")
	checkForTableID(moduleID)
	



def checkForTableID(moduleID):
	#string = "@PersistenceCapable(detachable = \"true\", table = \"foo_account\")"



	for f in fileHandler.getAllFilesMatching('*.java'):
		contents = fileHandler.readFile(f)

		for line in contents:
			if(("@PersistenceCapable" in line) and ("table" in line)):

				inBrackets = gstring.getTargetBetweenBrackets(line)
				attributes = inBrackets.split(",")
				tableAttribute = [s for s in attributes if "table" in s]
				a = re.findall('"([^"]*)"', tableAttribute[0])[0]

				if(not a.startswith((moduleID + "_"))):
					log.critical("\tThe table ID in class " + f + " is not prefixed the moduleID and an underscore: " + moduleID + "_")

	#Between quote marks working
	#












	'''
	for f in fileHandler.getAllFilesMatching('*.java'):
		contents = fileHandler.readFile(f)

		for line in contents:

			if(("@PersistenceCapable" in line) and ("table" in line)):
				inBrackets = gstring.getTargetBetweenBrackets(line)
				inBrackets = inBrackets.split(",")
				for attribute in inBrackets:
					if("table" in attribute):
						tableAttribute = attribute.strip()
						print tableAttribute
	'''