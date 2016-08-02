import fileHandler, logging as log, Colourer, gstring, re, javaHandler

def checks(moduleID):
	log.log(2, "Checking Config files...")
	checkForTableID(moduleID)
	



def checkForTableID(moduleID):
	#string = "@PersistenceCapable(detachable = \"true\", table = \"foo_account\")"
	flag = True
	
	task_dict = {}

	table_dict = {}
	for f in fileHandler.getAllFilesMatching('*.java'):
		contents = fileHandler.readFile(f)

# Initial work on checking task/config names
#		try:
#			method = javaHandler.readMethod(contents, "public String getDisplayLabel()")
#			print method
#		except:
#			False
		try:
			method = javaHandler.readMethod(contents, "public TaskConfigIf getTaskConfigImplementation")
			if (method[0].split()[2] in task_dict.keys()):
				log.critical("Task config re-used: " + method[0].split()[2] + " in files: " + f + " & " + task_dict[method[0].split()[2]])
				flag = False
			else:
				task_dict[method[0].split()[2]] = f
		except:
			False
		for line in contents:
			if(("@PersistenceCapable" in line) and ("table" in line)):

				inBrackets = gstring.getTargetBetweenBrackets(line)
				attributes = inBrackets.split(",")
				tableAttribute = [s for s in attributes if "table" in s]
				a = re.findall('"([^"]*)"', tableAttribute[0])[0]
				if (a in table_dict.keys()):
					log.critical("\tTable already used: " + a + " (files: " + table_dict.get(str(a)) + " & " + f)
					flag = False
				else:
					table_dict[str(a)] = f

				if(not a.startswith((moduleID + "_"))):
					log.critical("\tThe table ID in class " + f + " is not prefixed the moduleID and an underscore: " + moduleID + "_")
	
	if (flag == False):
		log.critical("Table checking failed")
		exit(1)










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
