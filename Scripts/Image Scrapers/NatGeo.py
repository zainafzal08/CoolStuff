import praw
import re
import urllib2
import json
import random

def dimentionsCorrect(w,h,settings):
	targetW = settings["targetW"]
	targetH = settings["targetH"]
	widthDelta = targetW - w
	heightDelta = targetH - h
	delta = float(max(widthDelta,heightDelta))

	# this doesn't care about data size because
	# the largest image from this database is not
	# that big
	if delta <= 0:
		return True
	if widthDelta > heightDelta:
		normalDelta = delta/w
	else:
		normalDelta = delta/h
	if normalDelta < settings["criticalDelta"]:
		return True
	return False

def getPage(link, key, user):
	h = {
		"apiauth-apikey" : key,
		"apiauth-apiuser" : user
	}
	request = urllib2.Request(link, headers=h)
	r = urllib2.urlopen(request)
	page = r.read()
	r.close()
	return page

def getNewImage(w,h,settings):
	# This doesn't seem to change?
	apiKey = "9fa5d22ad7b354fe0f9be5597bcf153df56e2ca5"
	apiUser = "pod_archive"
	page = str(random.randint(1,10))
	link = "https://relay.nationalgeographic.com/proxy/distribution/feed/v1?\
format=jsonapi&content_type=featured_image&fields=image,uri&\
collection=fd5444cc-4777-4438-b9d4-5085c0564b44\
&subjects=af70464f-6035-346a-bb85-479b03df4a5e\
&publication_datetime__from=2009-01-01T18:30:02Z&page="+page+"&limit=48"
	j = json.loads(getPage(link,apiKey, apiUser))
	rands = range(0,len(j["data"]))
	random.shuffle(rands)
	for i in range(0,int(settings["maxSearch"])):
		imgIndex = rands[i]
		sizes = j["data"][imgIndex]["attributes"]["image"]["renditions"]
		ar = float(j["data"][imgIndex]["attributes"]["image"]["aspect_ratio"])
		for size in sizes:
			w = int(size["width"])
			h = int(int(w) * (1/ar))
			if(dimentionsCorrect(w,h,settings)):
				img = {
					"src": size["uri"],
					"credit" : "https://www.nationalgeographic.com/photography/photo-of-the-day/archive/"
				}
				return img	
	return None

def simpleSearch(w,h):
	img = None
	settings = {}
	settings['targetW'] = w
	settings['targetH'] = h
	settings['criticalDelta'] = 0.5
	settings['maxSearch'] = 10
	img = getNewImage(w,h,settings)
	if img == None:
		img = {
			"src": "http://cdn.wallpapersafari.com/17/34/42nLhc.jpg",
			"credit" : "https://wallpapersafari.com/free-mountain-wallpaper-backgrounds/"
		}
	return img

if __name__ == "__main__":
	print(simpleSearch(2560,1600))
