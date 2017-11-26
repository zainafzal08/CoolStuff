import re
import datetime
import sys

# View class holds info about what the gannt chart should look like
# Params
# 	w is width in pixels
# 	h is height, note this is overridden if the gantt is generated with autoHeight in pixels
# 	range_start and range_end are datetime date objects to define the view start and end dates
# 	zoom is "days" "months" or "hours" to define what 1 unit on the chart represents
# 	colorPalette is a dictionary outlining the following colors in hexadecimal
# 		TableA - first of the alternating colours on the gantt table
# 		TableB - second of the alternating colours on the gantt table
#		TableOpacity - the entire tables background opacity (excludes elements and text)
# 		datebarText - the colour of the datebar text for dates/times
# 		tableText - the color of the text used on the table for element labels and column names.
# Exceptions
#	NameError is raised when the given zoom/date range can not be displayed in the specified width
#   In this case increase width, decrease date range, or decrease zoom.

class View:
	def __init__(self, range_start, range_end, w, h, colorPalette, zoom):
		self.start = range_start
		self.end = range_end
		self.width = w
		self.height = h
		self.colLabelWidth = 100  # how much space the label text takes up
		self.colLabelOffset = 15  # the space between the text space and start of the gantt
		self.elemThickness = 21  # thickness of each gantt element
		self.rowH = 30 # thickness of each row
		self.colorPalette = colorPalette
		self.zoom = zoom
		neededUnits = unifyUnits(getDays(self.end,self.start),self.zoom)
		len = self.width/neededUnits
		if len < 80:
			print(len)
			raise NameError("Too many values ("+str(neededUnits)+") to represent in given width ("+str(self.width)+")")
		self.unitLen = len
	def getlabelOffset(self):
		return self.colLabelWidth+self.colLabelOffset
	def getUnitLen(self):
		return self.unitLen
	def getTextPos(self):
		return self.rowH - int(self.rowH / 3)
	def getNeededValues(self):
		return unifyUnits(getDays(self.end,self.start), self.zoom)

# Formats a rectangle html tag
# x and y is the position of the element in pixels
# w and h are width and height in pixels
# f is the fill colour in hex
# o is opacity as a float 0 to 1
# rounded is a boolean specifying if the rectangle should be rounded or not
def rect(x,y,w,h,f,o,rounded):
	if not rounded:
		return "<rect fill-opacity=\""+str(o)+"\" x=\""+str(x)+"\" y=\""+str(y)+"\" width=\""+str(w)+"\" height=\""+str(h)+"\" fill=\""+str(f)+"\"></rect>"
	else:
		return "<rect rx=\"8\" ry=\"8\" fill-opacity=\"" + str(o) + "\" x=\"" + str(x) + "\" y=\"" + str(y) + "\" width=\"" + str(w) + "\" height=\"" + str(h) + "\" fill=\"" + str(f) + "\"></rect>"


# formats text html
# x and y are position
# s is the string to display
# offset is the offset from the x,y given (how much it should be shifted right)
# color is text color in hex
# id is just how you choose to identify each element for interactivity
# 	This is what will be sent to the specified handler when the text is clicked on
#	If Null the text is not interactive (such as label text)
def text(x,y,s,offset,color, id):
	if not id:
		text = "<text fill='"+color+"' style=style=\"cursor: default; user-select: none; -webkit-font-smoothing: antialiased; font-family: Roboto; font-size: 14px;\" x=\""+str(x)+"\" y=\""+str(y)+"\" dx=\""+str(offset)+"\">"+s+"</text>"
	else:
		text = "<text onclick =\"showInfo('" + id + "')\" fill='" + color + "' style=style=\"cursor: default; user-select: none; -webkit-font-smoothing: antialiased; font-family: Roboto; font-size: 14px;\" x=\"" + str(x) + "\" y=\"" + str(y) + "\" dx=\"" + str(offset) + "\">" + s + "</text>"

	return text

# formats a gantt chart element
# make sure start is a x,y tuple at the top left of the desired element
# c is the colour of the element in hex
# dur is the legnth of the element in pixels
# thick is the y thickness of the element in pixels
# label is the label text on the element with color labelC
# rowH is the row Height in pixels
# id is just how you choose to identify each element for interactivity
# 	This is what will be sent to the specified handler when the element is clicked on
#	If Null the element is not interactive

def element(start, dur, thick, c, label, rowH, id, labelC):
	if thick > rowH:
		thick = rowH
	# note the curves are just a 5 pixel circle
	d = "M"+str(start[0]+5)+" "+str(start[1])+" " # move to top left
	d = d + "h "+str(dur-5) + " " # top
	d = d + "a 5 5 0 0 1 5 5 " # top right curve
	d = d + "v "+str(thick-10)+" " # right
	d = d + "a 5 5 0 0 1 -5 5 " # bottom right curve
	d = d + "h -"+str(dur-10) + " " # bottom
	d = d + "a 5 5 0 0 1 -5 -5 " # bottom left
	d = d + "v -"+str(thick-10)+" " # left
	d = d + "a 5 5 0 0 1 5 -5 " # top left curve

	offset = (dur/2) - (len(label) * 5)
	labelHTML = text(start[0], start[1]+(rowH/2), label,offset,labelC, id)
	path = "<path onclick=\"showInfo('"+id+"')\" fill=\""+c+"\" d=\""+d+"\"></path>"
	total = path + "\n" + labelHTML
	return total

#takes in a number in days and converts it to the correct units
#given a month, day or hour zoom level.
def unifyUnits(number, zoom):
	if zoom == "hours":
		return number * 24
	if zoom == "days":
		return number
	elif zoom == "months":
		return number/30

def getDays(dateA,dateB):
	result = (dateA-dateB).days
	if result == 0:
		result = ((dateA - dateB).seconds/60/60)/24
	return result
# checks if a test date is between two other dates
# range is a tuple (start, end)
def inRange(test, range):
	return (test <= range[1] and test >= range[0])


# Calculates when a element should start on the gantt chart
# And how long it should go for in pixels.
def calculateElemDimentions(view, elemStart, elemEnd):
	#default
	start = None
	duration = None
	range = (view.start, view.end)
	rangeEnd = view.end
	rangeStart = view.start

	#check if elem is even in range
	if(elemStart > rangeEnd or rangeStart > elemEnd):
		return None
	#check for cliff cases
	if(elemStart <= rangeStart and inRange(elemEnd, range)):
		dayDuration = getDays(elemEnd,rangeStart)
		duration = unifyUnits(dayDuration, view.zoom)
		start = 0
	elif(elemEnd > rangeEnd and inRange(elemStart, range)):
		dayDuration = getDays(rangeEnd,elemStart)
		duration = unifyUnits(dayDuration,view.zoom)
		start = unifyUnits(getDays(elemStart,rangeStart),view.zoom)
	#check for all in case
	elif(inRange(elemStart, range) and inRange(elemEnd, range)):
		dayDuration = getDays(elemEnd,elemStart)
		duration = unifyUnits(dayDuration,view.zoom)
		start = unifyUnits(getDays(elemStart,rangeStart),view.zoom)
	#check for 100 case
	elif(elemEnd >= rangeEnd and elemStart <= rangeStart):
		dayDuration = getDays(rangeEnd,rangeStart)
		duration = unifyUnits(dayDuration,view.zoom)
		start = 0
	#finish up
	return (start, duration)

# Generates the datebar above the gannt chart using the view data.
def generateDateBar(view):
	returnArray = []
	returnArray.append("<svg width=\""+str(view.width)+"\" height=\"50\">")
	returnArray.append(text(0,40,"Label",30,view.colorPalette['datebarText'], None))

	date = view.start
	x = view.getlabelOffset()
	for u in range(0,int(view.getNeededValues())):
		if view.zoom == "hours":
			value = date.strftime('%H%a')
			returnArray.append(text(x, 40, value,0, view.colorPalette['datebarText'], None))
			date = date + datetime.timedelta(seconds=3600)
		if view.zoom == "days":
			value = date.strftime('%d%b%y')
			returnArray.append(text(x, 40, value,0, view.colorPalette['datebarText'], None))
			date = date + datetime.timedelta(days=1)
		if view.zoom == "months":
			value = date.strftime('%b%y')
			returnArray.append(text(x, 40, value,0, view.colorPalette['datebarText'], None))
			if date.month == 1 or date.month == 3 or date.month == 5 or date.month == 7 or date.month == 8 or date.month == 10 or date.month == 12:
				date = date + datetime.timedelta(days=31)
			elif date.month == 4 or date.month == 6 or date.month == 9 or date.month == 11:
				date = date + datetime.timedelta(days=30)
			elif date.month == 2 and date.year % 4 == 0:
				date = date + datetime.timedelta(days=29)
			else:
				date = date + datetime.timedelta(days=28)
		x = x + view.unitLen
	returnArray.append("</svg><p></p>")
	if x > view.width:
		if view.zoom == "hours":
			view.end = view.end - datetime.timedelta(seconds=3600)
		elif view.zoom == "days":
			view.end = view.end - datetime.timedelta(days=1)
		elif view.zoom == "months":
			date = view.end
			if date.month == 1 or date.month == 3 or date.month == 5 or date.month == 7 or date.month == 8 or date.month == 10 or date.month == 12:
				date = date - datetime.timedelta(days=31)
			elif date.month == 4 or date.month == 6 or date.month == 9 or date.month == 11:
				date = date - datetime.timedelta(days=30)
			elif date.month == 2 and date.year % 4 == 0:
				date = date - datetime.timedelta(days=29)
			else:
				date = date - datetime.timedelta(days=28)
		returnArray = generateDateBar(view)
	return returnArray

# generates a gantt chart in HTML ready to be displayed.
# data is a array of tuples in the form below. Each tuple represents 1 element to be displayed
#   (col_name, start_date, end_date, colour, label, id)
# 	The id is what your server will be sent back if the element is clicked on
# View is a ganntchart view object, See class for more info.
# if the height in the view is None, it is overridden with the calculated height for the given dataset
# scrollable is a boolean and defines if this will generate a scrollable gantt chart or one with a static height.

def generateGantt(data, view, scrollable):
	seen={}
	count=0
	# Count number of unique columns.
	# also generate a array of the unique coloums.
	for d in data:
		cn = d[0]
		if cn not in seen:
			seen[cn] = 1
			count+=1
	allCols = seen
	rows = count
	rawGanttHTML = []
	# Autogenerated height
	svgH = len(seen) * view.rowH
	# CSS
	rawGanttHTML.append("<style>")
	rawGanttHTML.append(getCSS(scrollable, view.width, view.height))
	rawGanttHTML.append("</style>")
	# Datebar
	rawGanttHTML.append("<div class=\"datebar\">")
	datebar = generateDateBar(view)
	rawGanttHTML = rawGanttHTML + datebar
	rawGanttHTML.append("</div>")
	# Gannt Chart Set up
	rawGanttHTML.append("<div class=\"chart_div\">")
	rawGanttHTML.append("<svg width="+str(view.width)+" height="+str(svgH)+">")
	rawGanttHTML.append("<g>")
	# Background Strips
	y = 0
	even = True
	for r in range(0,rows):
		if even:
			rawGanttHTML.append(rect(0,y,view.width,view.rowH,view.colorPalette['tableA'],view.colorPalette['tableOpacity'],False))
			even = False
		else:
			rawGanttHTML.append(rect(0,y,view.width,view.rowH,view.colorPalette['tableB'],view.colorPalette['tableOpacity'],False))
			even = True
		y = y + view.rowH
	# Render each row
	# note this is a O(n^2) solution, if program runs slow, improve this first.
	y=0
	txtY=view.getTextPos()
	for c in sorted(allCols.keys()):
		rawGanttHTML.append(text(0,txtY,c,15, view.colorPalette['tableText'], None))
		for r in data:
			if r[0] == c:
				# Get the data on how to display the given element
				temp = calculateElemDimentions(view, r[1],r[2])
				if not temp:
					continue
				# Extract info
				originStart = temp[0]
				duration = temp[1]
				unitLen = view.getUnitLen()
				shapeStartX = view.getlabelOffset() + (originStart*unitLen)

				# original method where i drew elements using rectangles which looked ugly af
				# avoids the use of svg paths
				# rawGanttHTML.append(rect(shapeStart,currentRowPos,duration*unitLen,rowH-2*colElementPadding,r[3],1,True))

				# more complex path method, looks a bit nicer.
				elemPadding = int((view.rowH-view.elemThickness)/2)
				start=(shapeStartX,y+elemPadding)
				rawGanttHTML.append(element(start, float(float(duration)*float(unitLen)), view.elemThickness ,r[3],r[4], view.rowH, r[5],"#FFFFFF"))
		y = y + view.rowH
		txtY = txtY + view.rowH
	# Ending tags
	rawGanttHTML.append("</g>")
	rawGanttHTML.append("</svg>")
	rawGanttHTML.append("</div>")
	# Return the gannt chart html
	return rawGanttHTML



# advanced (still in testing)

def getJavaScript():
	return '''<script>
function showInfo(id){
	$.get("/getInfo", {data: id},function(response){
		document.getElementById("info-box").innerHTML = ""
		var para = document.getElementById("info-box");
		//first heading
		var content = document.createElement("p")
		var bold = document.createElement("b")
		var text = document.createTextNode("Information")
		bold.appendChild(text)
		content.appendChild(bold)
		//link data
		var p1 = document.createElement("pre")
		p1.className = "control-button info-box"
		p1.appendChild(document.createTextNode(response.L1))
		var p2 = document.createElement("pre")
		p2.className = "control-button info-box"
		p2.appendChild(document.createTextNode(response.L2))
		//final
		para.appendChild(content)
		para.appendChild(p1)
		para.appendChild(p2)
	}, 'json');
}
</script>'''

def getCSS(scrollable, w, h):
	base = ".chart_div {\n"
	base = base + "    height: "+str(h)+"px;\n"
	base = base + "    width: " +str(w) + "px;\n"
	if scrollable:
		base = base + "    overflow-y: scroll;\n"
	else:
		base = base + "    overflow-y: hidden;\n"
	base = base + "    overflow-x: hidden;\n"
	base = base + "}\n"
	if scrollable:
		base = base + "::-webkit-scrollbar {display:none;}\n"
	return base

