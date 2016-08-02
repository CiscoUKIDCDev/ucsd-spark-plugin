import logging as l, Colourer, javaHandler

def checks(fileContents):
	l.log(2, "Checking Feature class...")
	tasks = checkArrayBasedMethods(fileContents, "public AbstractTask[] getTasks()");
	reports = checkArrayBasedMethods(fileContents, "public CloupiaReport[] getReports()")
	collectors = checkArrayBasedMethods(fileContents, "public CollectorFactory[] getCollectors()")

	'''
	if(tasks):
		l.log(1, "Ok")

	'''
	print ""



#Check the getTasks() method of the ACM class. 
def checkArrayBasedMethods(fileContents, signature):
	flag = True
	#print fileContents
	#signature = "public AbstractTask[] getTasks()"

	#line = javaHandler.findArrayDeclarations(fileContents, signature, "AbstractTask")[0]
	#print line

	#print javaHandler.getSizeOfArray(line)

	returns = javaHandler.getReturnedObjectsOfMethod(fileContents, signature)
	returns.sort()


	#If the getTasks() method contains returns multiple, different objects, report to the user that we can't reliabily check this. Yet...
	if(returns[0] != returns[len(returns)-1]):
		l.warning("\t" + signature + " cannot currently be reliably checked due to multiple return statements: " + ', '.join(str(p) for p in returns)) 
		flag = False
	elif(returns[0] == "{"):
		l.log(1, "\t" + signature + " returning full object inline: return new " + signature.split()[1] +" { .. } ")
		flag = True
	else:
		if(returns[0] == "null"):
			l.warning("\t" + signature + " returns null")
			return True
		#print returns[0]
		usages = javaHandler.getObjectUsage(fileContents, signature, returns[0]) 
		arrayDeclaration = javaHandler.getArrayDeclaration(usages)
		arraySize = javaHandler.getSizeOfArray(arrayDeclaration)
		#print arraySize
		#print usages

		assignments = javaHandler.getAssignmentsFromUsages(usages)

		for i in assignments:
			if(javaHandler.getNumberBetweenSquareBrackets(i) >= arraySize):
				l.critical("\tThe capacity of the returned array in " + signature + " is not large enough")
				flag = False

		if (flag == True):
			l.log(1, "\t" + str(signature) + " is OK")

		'''
		for use in usages:
			#print use
			n = javaHandler.getNumberBetweenSquareBrackets(use)
			print n


			try:
				if(int(n) > arraySize-1):
					l.critical("\tArray error")
				else:
					print "ok"
			except ValueError:
				print "Derp"
			'''

	return flag
