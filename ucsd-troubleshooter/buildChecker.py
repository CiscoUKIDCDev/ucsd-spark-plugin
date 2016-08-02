import xml.etree.ElementTree as ET, logging as l, Colourer

def checkBuild(fileContent):
	l.log(2, "Checking build.xml ...")
	tR = ""
	content = "\n".join(fileContent)

	tree = ET.fromstring(content)

	for neighbor in tree.iter('property'):
		if(neighbor.attrib["name"] == "moduleID"):
			tR = neighbor.attrib["value"]
			l.info("\tThe Module ID listed in build.xml is: " + tR)

	print ""
	return tR