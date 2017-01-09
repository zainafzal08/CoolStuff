streetTypes=["STREET","ROAD","ROADS","TERRACE","ACCESS","ALLEY","ALLEYWAY","AMBLE","ANCHORAGE","APPROACH","ARCADE","ARTERY","AVENUE","BASIN","BANK","BAY","BEACH","BEND","BLOCK","BOUNDARY","BOWL","BOULEVARD","BRACE","BRANCH","BRAE","BREAK","BRIDGE","BROADWAY","BROW","BYPASS","BYWAY","CAUSEWAY","CENTRE","CENTREWAY","CHASE","CIRCLE","CIRCLET","CIRCUIT","CIRCUS","CLOSE","COLONNADE","COMMON","CONCOURSE","COPSE","CORNER","CORSO","COURT","COURSE","COURTYARD","COVE","CRESCENT","CREST","CREEK","CROSS","CROSSING","CROSSROAD","CROSSWAY","CRIEF","CRUISEWAY","CUL-DE-SAC","CUTTING","DALE","DELL","DEVIATION","DIP","DISTRIBUTOR","DOWNS","DRIVE","DRIVEWAY","EASEMENT","EDGE","ELBOW","END","ENTRANCE","ESPLANADE","ESTATE","EXPRESSWAY","EXTENSION","FAIRWAY","FIRE TRACK","FIRETRAIL","FLAT","FOLLOW","FOOTWAY","FORESHORE","FORMATION","FREEWAY","FRONT","FRONTAGE","GAP","GARDEN","GARDENS","GATE","GATEWAY","GATES","GLADE","GLEN","GRANGE","GREEN","GROUND","GROVE","GULLY","HEATH","HEIGHTS","HIGHROAD","HIGHWAY","HILL","INTERCHANGE","INTERSECTION","JUNCTION","KEY","LANDING","LANE","LANEWAY","LEES","LINE","LINK","LITTLE","LOCATION","LOOKOUT","LOOP","LOWER","MALL","MEANDER","MEW","MEWS","MOTORWAY","MOUNT","NOOK","OUTLOOK","PARADE","PARK","PARKLANDS","PARKWAY","PART","PASS","PATH","PATHWAY","PIAZZA","PIER","PLACE","PLATEAU","PLAZA","POCKET","POINT","PORT","PROMENADE","PURSUIT","QUAD","QUADRANGLE","QUADRANT","QUAY","QUAYS","RAILWAY SIDING","RAMBLE","RAMP","RANGE","REACH","RESERVE","REST","RETREAT","RETURN","RIDE","RIDGE","RIDGEWAY","RIGHT OF WAY","RING","RISE","RIVER","RIVERWAY","RIVIERA","ROADSIDE","ROADWAY","RONDE","ROSEBOWL","ROTARY","ROUND","ROUTE","ROW","RUE","RUN","SERVICE WAY","SIDING","SLOPE","SOUND","SPUR","SQUARE","STAIRS","STATE HIGHWAY","STEPS","STRAND","STRIP","SUBWAY","TARN","THOROUGHFARE","TOLLWAY","TOP","TOR","TOWERS","TRACK","TRAIL","TRAILER","TRIANGLE","TRUNKWAY","TURN","UNDERPASS","UPPER","VALE","VIADUCT","VIEW","VILLAS","VISTA","WADE","WALK","WALKWAY","WATERS","WAY","WHARF","WOOD","WYND","YARD"]
streetTypeSuffixs=["CENTRAL","EAST","LOWER","NORTH","NORTH EAST","NORTH WEST","SOUTH","SOUTH EAST","SOUTH WEST","UPPER","WEST"]
states=["NSW","VIC","QLD","WA","ACT","SA","NT","TAS"]
streetTypesAbrev=["Ally","Arc","Ave","Bvd","Bypa","Cct","Cl","Crn","Ct","Cres","Cds","Dr","Esp","Grn","Gr","Hwy","Jnc","Lane","Link","Mews","Pde","Pl","Rdge","Rd","Sq","St","Tc"]
abrevRef={"Ally":"Alley","Arc":"Arcade","Ave":"Avenue","Bvd":"Boulevard","Bypa":"Bypass","Cct":"Circuit","Cl":"Close","Crn":"Corner","Ct":"Court","Cres":"Crescent","Cds":"Cul-de-sac","Dr":"Drive","Esp":"Esplanade","Grn":"Green","Gr":"Grove","Hwy":"Highway","Jnc":"Junction","Lane":"Lane","Link":"Link","Mews":"Mews","Pde":"Parade","Pl":"Place","Rdge":"Ridge","Rd":"Road","Sq":"Square","St":"Street","Tce":"Terrace"}

import re
import random
# Address class
# Can be built with a 1 line long address
# or an array of the 9 elements in a address
# address="" or array=[]
class Address:
	def __init__(self, Id, **optionalParamaters):
		if "address" in optionalParamaters:
			array = splitAddress(optionalParamaters["address"])
		elif "array" in optionalParamaters:
			array = optionalParamaters["array"]
		self.id = Id
		self.street_sub_type = array[0]
		self.street_sub_num = array[1]
		self.street_num = array[2]
		self.street_name = array[3]
		self.street_type = array[4]
		self.street_type_suffix = array[5]
		self.locality = array[6]
		self.state = array[7]
		self.postcode = array[8]
	def getFullAddress(self):
		output = ""
		array = [self.street_sub_num,self.street_type,self.street_num ,self.street_name,self.street_type,self.street_type_suffix,self.locality,self.state,self.postcode]
		for s in array:
			if s:
				output = output + " " + s
		output = output.strip()
		return output

#Primative,not good for any complex cases, just here to get the initial payload from the excel spreadsheet
def splitAddress(input):
	input = input.strip()
	input = input.strip()
	output=[]
	output.append(None)
	output.append(None)
	output.append(input.split(" ")[0])
	streetFull = re.sub(input.split(" ")[0],'', input)
	streetFull = streetFull.split(",")[0]
	done = False
	for poss in streetTypes:
		poss = poss.lower()
		m = re.search("\s+"+poss,streetFull.lower())
		n = re.search(poss+"\s+",streetFull.lower())
		k = re.search(poss,streetFull.lower())
		if m or n or k:
			streetFull = re.sub(poss.lower(), '', streetFull.lower()).strip()
			output.append(streetFull)
			output.append(poss)
			done=True
			break
	if done == False:
		for poss in streetTypesAbrev:
			poss = poss.lower()
			m = re.search("\s+" + poss, streetFull.lower())
			n = re.search(poss + "\s+", streetFull.lower())
			k = re.search(poss, streetFull.lower())
			if m or n or k:
				streetFull = re.sub(poss.lower(), '', streetFull.lower()).strip()
				output.append(streetFull)
				output.append(abrevRef[poss.title()])
				break
	output.append(None)
	output.append(input.split(",")[1].strip())
	output.append(input.split(",")[2].strip())
	output.append(input.split(",")[3].strip())
	return output

# takes in a address instance, pushes it to the database "ADDRESSES" table

def newAddress(db, address):
	cur = db.cursor()
	Aq = "INSERT INTO ADDRESSES(ID, SUB_ADDRESS_NUMBER, SUB_ADDRESS_TYPE, STREET_NUMBER, STREET_NAME, STREET_TYPE, STREET_TYPE_SUFFIX, LOCALITY, STATE, POSTCODE) VALUES(:i,:san,:sat,:snum,:sn,:st,:sts,:l,:s,:p)"
	cur.execute(Aq, i=address.id, san=address.street_sub_num, sat=address.street_sub_type, snum=address.street_num, sn=address.street_name, st=address.street_type, sts=address.street_type_suffix,l=address.locality, s=address.state, p=address.postcode)
	db.commit()
	cur.close()


def getAddress(db, addressID):
	cur = db.cursor()
	Aq = "SELECT ID, SUB_ADDRESS_NUMBER, SUB_ADDRESS_TYPE, STREET_NUMBER, STREET_NAME, STREET_TYPE, STREET_TYPE_SUFFIX, LOCALITY, STATE, POSTCODE FROM ADDRESSES WHERE ID = :id"
	results = cur.execute(Aq, id=addressID).fetchall()
	output = Address(results[0][0],array=results[0][1:10])
	cur.close()
	return output