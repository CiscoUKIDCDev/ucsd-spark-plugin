import fileHandler, logging as log, Colourer, hashlib, gstring

'''
def check(fileContents, fileDirectoryOfJDODotFiles):
	#print fileContents
	#print fileDirectoryOfJDODotFiles

	#log.info("\tChecking " + fileDirectoryOfJDODotFiles + "...")
	#confirmAllFilesInJDOFiles(fileContents, fileDirectoryOfJDODotFiles)

	checkAllJavaFiles()
'''

def check():
	log.log(2, "Checking jdo.files...")
	javas = getClassesRequiringEnhancement()
	files = getClassesListedForEnhancement()

	classesThatNeedListing = list(set(javas) - set(files))

	if(len(classesThatNeedListing) == 0):
		log.log(1, "\tAll classes that require JDOEnhancement are listed")
	else:
		log.warning("\tThe following classes are not listed in a jdo.files for JDOEnhancement:")
		for i in classesThatNeedListing:
			log.warning('\t\t' + i)



def getClassesListedForEnhancement():
	files = fileHandler.getAllFilesMatching("jdo.files")
	#Go through and get all of the files that are LISTED for enhancement
	newList = []
	for f in files:
		if("src" in f):
			newList.append(f)

	files = newList

	listOfClasses = []
	for f in files:
		fileContents = fileHandler.readFile(f)
		classes = getJDOClassesInFile(fileContents)
		for c in classes:
			listOfClasses.append(c)




	return listOfClasses

	



def getClassesRequiringEnhancement():
	javas = fileHandler.getAllFilesMatching("*.java")
	#Go through and get all the files that REQUIRE enhancement
	requiresEnhancement = []

	for f in javas:
		if(hasAnnotations(f)):
			className = gstring.getClassNameFromDir(f)
			requiresEnhancement.append(className)

	return requiresEnhancement
























def confirmAllFilesInJDOFiles(fileContents, fileDirectoryOfJDODotFiles):
		#Work out the directory of the folder that we're in.
	directory = fileDirectoryOfJDODotFiles.replace(' ', '')[:-9]

	#Get all of the files that exist in that folder
	fileNames = fileHandler.getFilesInDir(directory, True)

	#Get the files that are listed for enhancement in the current jdo.files
	classesListedInFile = getJDOClassesInFile(fileContents)

	#For every file that exists in the folder...
	for f in fileNames:
		#Recognise that the file path to that file is the directory in which it sits and the filename itself.
		path = directory + f

		#If the file requies JDO enhancement...
		if(hasAnnotations(path)):
			#Find out what should be in the JDO.files file based on the filename
			shouldBe = f.replace(' ', '')[:-5] 

			#Is the one in the folder in the file?
			if(not shouldBe in classesListedInFile):
				log.error("\t\t" + shouldBe + " is not listed in a jdo.files but requires enhancement")


def getJDOClassesInFile(fileContents):
	tR = []
	for line in fileContents:
		if("//" not in line):
			line = line.replace("+", "")
			line = line.replace("\n", "")
			tR.append(line)
	return tR


def hasAnnotations(filePath):
	sub = "import javax.jdo.annotations"
	contents = fileHandler.readFile(filePath)
	return len([s for s in contents if sub in s]) != 0