import praw
import re
import requests
import json
import random
from time import strftime, gmtime

def dimentionsCorrect(w,h,settings):
	targetW = settings["targetW"]
	targetH = settings["targetH"]
	widthDelta = targetW - w
	heightDelta = targetH - h
	delta = float(max(widthDelta,heightDelta))
	if widthDelta > heightDelta:
		normalDelta = delta/w
	else:
		normalDelta = delta/h
	if abs(normalDelta) < settings["criticalDelta"]:
		return True
	return False

def getPage(link):
	r = requests.get(link)
	return r.text

def getLinkDump(page, catagory):
	l = "https://www.pexels.com/search/%s/?"%(catagory)
	base="https://www.pexels.com"
	params = "page=%d&format=js&seed=%s"%(page,re.sub(' ',"%20",strftime("%Y-%m-%d %H:%M:%S +0000", gmtime())))
	l += params
	raw = getPage(l)
	rawLinks = re.findall("<a [^>]*>",raw)
	links = []
	for link in rawLinks:
		try:
			photo = re.search("/photo/[^/]+/",link).group(0)
			links.append(base+photo)
		except:
			continue
	return links

def extractImg(link):
	raw = getPage(link)

	try:
		dim = re.search("\d+ x \d+ pixels",raw).group(0)
		l = re.search("<source srcset=\"([^\"]*)\"", raw).group(1).split("?")[0]
	except:
		return None

	w = int(dim.split(" ")[0])
	h = int(dim.split(" ")[2])
	img = {
		"src" : l,
		"credit" : link,
		"w" : w,
		"h" : h
	}
	return img

def getNewImage(settings):
	page = random.randint(1,50)
	links = getLinkDump(page,"landscapes")
	links += getLinkDump(page,"christmas") # tis the season to be jolly
	random.shuffle(links)
	links = links[0:settings["maxSearch"]]
	matchImg = None
	for link in links:
		img = extractImg(link)
		if dimentionsCorrect(img["w"],img["h"],settings):
			matchImg = img
			break
	return matchImg

def simpleSearch(w,h):
	img = None
	settings = {}
	settings['targetW'] = w
	settings['targetH'] = h
	settings['criticalDelta'] = 0.5
	settings['maxSearch'] = 10
	img = getNewImage(settings)
	if img == None:
		img = {
			"src": "http://cdn.wallpapersafari.com/17/34/42nLhc.jpg",
			"credit" : "https://wallpapersafari.com/free-mountain-wallpaper-backgrounds/",
			"w" : "1920",
			"h" : "1200"
		}
	return img

if __name__ == "__main__":
	print(simpleSearch(2560,1600))
