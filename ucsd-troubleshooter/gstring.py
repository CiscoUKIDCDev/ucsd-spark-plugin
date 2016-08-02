#Before you say it - I know. It's the 'G' of "George" and "String" concatenated, I promise...

def getClassNameFromDir(path):

	#path = "../foo/src/com/cloupia/feature/foo/accounts/FooAccount.java"

	path = path.split("/")
	path = path[len(path)-1]
	path = path.split(".")[0]

	return path


def getTargetBetweenBrackets(line):
	s = line
	return s[s.find("(")+1:s.find(")")]


def getTargetThatsSurrounded(line, left, right):
	s = line
	return s[s.find(left)+1:s.find(right)]