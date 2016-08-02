import fileHandler, logging as l, Colourer

#Takes a method signature, eg public static void main(String[] args), and returns the code contained within the method.
def readMethod(fileContents, signature):
	bracketCount = 0

	linesToReturn = []
	flag = False
	for line in fileContents:
		if(bracketCount == 0 and flag):
			return linesToReturn

		if(flag):
			linesToReturn.append(line)
			if("{" in line):
				bracketCount = bracketCount + 1
				#print "{ detected: " + line + ". bracketCount updated to: " + str(bracketCount)
			if("}" in line):
				bracketCount = bracketCount - 1
				#print "} detected: " + line + ". bracketCount updated to: " + str(bracketCount)

		if(signature in line):
			#print "PING! FOUND IT!"
			flag = True
			bracketCount = bracketCount + 1

	raise Exception("The method with signature \"" + signature, "\" does not exist.")




def getNameOfArray(line):
	tR = line.split(" ")[1]
	if(tR != ""):
		return tR
	else:
		raise Exception("Java syntax convention states that there isn't a double space in the line: " + line )


#Primitive function to determine the size of an array. Must be in the form ClassName[] arrayName = new ClassName[x], where x is the number we're aiming to get.
#This function does not support array assignments, eg ClassName[] arrayName; arrayName = new ClassName[x]; 
def getSizeOfArray(line):
	chunkWithSize = line.split(" ")[4]
	s = chunkWithSize
	return getNumberBetweenSquareBrackets(s)

def getNumberBetweenSquareBrackets(line):
	s = line
	return s[s.find("[")+1:s.find("]")]



def getReturnedObjectsOfMethod(fileContents, signature):
	lines = readMethod(fileContents, signature)
	match = "return"
	tR = []

	for line in lines:
		if(match in line):
			line = line.strip()
			line = line.split(" ")
			line.pop(0)
			line = line[len(line)-1].replace(";", "")
			tR.append(line)

	return tR



def getObjectUsage(fileContents, signature, objectName):
	lines = readMethod(fileContents, signature)
	tR = []
	for line in lines:
		if(objectName in line):
			tR.append(line.strip())

	return tR


def getArrayDeclaration(objectUsages):
	match = "[]"

	for line in objectUsages:
		if(match in line):
			return line


def getAssignmentsFromUsages(usages):
	tR = []
	for use in usages:
		if(("return" not in use) and ("[]" not in use) and ("//" not in use)):
			tR.append(use)

	return tR

	






''' DEPRECATED:
def findArrayDeclarations(fileContents, signature, arrayClass):
	methodContents = readMethod(fileContents, signature)
	match = arrayClass + "[]"
	tR = []

	for line in methodContents:
		if(match in line):
			tR.append(line.strip())


	return tR

'''















