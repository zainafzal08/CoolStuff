import praw
import re
import urllib2

def dimentionsCorrect(w,h,settings):
	if w == -1 or h == -1:
		return False
	minW = settings['minW']
	minH = settings['minH']
	maxW = settings['maxW']
	maxH = settings['maxH']
	minAR = settings['minAR']
	maxAR = settings['maxAR']
	sizeCorrect = False
	if w <= maxW and w >= minW  and h <= maxH and h >= minH:
		sizeCorrect = True
	aspectCorrect = False
	aspect = float(w)/float(h)
	if aspect >= minAR and aspect <= maxAR:
		aspectCorrect = True

	return sizeCorrect and aspectCorrect

def getNewImage(searchSize, settings):
	reddit = praw.Reddit(client_id='d1WOmr6R1RnV7g', client_secret='pLdRL1mQlb9Cwbb1zqjm31une_A', user_agent='web:redditImageScraper:v1 (by /u/zain_afz)')
	chosenSubmission = None
	options = reddit.subreddit('EarthPorn').new(limit=searchSize)
	for submission in options:
		sizeStr = re.search(r'\[(\d+)x(\d+)\]', submission.title);
		w = -1
		h = -1
		if sizeStr:
			w = int(sizeStr.group(1))
			h = int(sizeStr.group(2))
		if dimentionsCorrect(w,h,settings) and (isImg(submission.url) or isInsta(submission.url)): 
			chosenSubmission = submission
			break
	if chosenSubmission == None:
		return None
	img = {}
	if isInsta(submission.url):
		img['src'] = getInstaImg(submission.url,sizeStr.group(1),sizeStr.group(2))
		img['credit'] = chosenSubmission.url
	else:
		img['src'] = chosenSubmission.url
		img['credit'] = "https://www.reddit.com/u/"+chosenSubmission.author.name
	return img

def isImg(url):
	suffix = url[-3:]
	suffix = suffix.upper()
	if suffix == "PNG" or suffix == "JPG" or suffix == "PEG":
		return True
	return False

def isInsta(url):
	res = re.search(r'www\.instagram\.com',url)
	if res:
		return True
	return False

def getPage(link):
	r = urllib2.urlopen(link)
	page = r.read()
	r.close()
	return page

def getInstaImg(url,w,h):
	page = getPage(url)
	regex = r'src=\"([^\"]*instagram[^\"]*'+w+'x'+h+r'[^\"]*)\"'
	imgSrc = re.search(regex,page)
	if imgSrc:
		return imgSrc.group(1)
	return None

def simpleSearch(w,h):
	searchSize = 10
	img = None
	settings = {}
	settings['minW'] = w-200
	settings['minH'] = h-200
	settings['maxW'] = w+1000
	settings['maxH'] = h+1000
	settings['minAR'] = w/h - 0.3
	settings['maxAR'] = 3
	while img == None and searchSize <= 200:
		img = getNewImage(searchSize,settings);
		searchSize*=2
	if img == None:
		img = {
			"src": "http://cdn.wallpapersafari.com/17/34/42nLhc.jpg",
			"credit" : "https://wallpapersafari.com/free-mountain-wallpaper-backgrounds/"
		}
	return img

def getSimpleBackImage():
	searchSize = 10
	img = None
	settings = {}
	settings['minW'] = 1000
	settings['minH'] = 800
	settings['maxW'] = 3000
	settings['maxH'] = 3000
	settings['minAR'] = 1.5
	settings['maxAR'] = 3
	while img == None and searchSize <= 200:
		try:
			img = getNewImage(searchSize,settings);
		except():
			break
		searchSize*=2
	if img == None:
		img = {
			"src": "http://cdn.wallpapersafari.com/17/34/42nLhc.jpg",
			"credit" : "https://wallpapersafari.com/free-mountain-wallpaper-backgrounds/"
		}
	return img

if __name__ == "__main__":
	settings = {}
	settings['minW'] = 1000
	settings['minH'] = 800
	settings['maxW'] = 3000
	settings['maxH'] = 3000
	settings['minAR'] = 1.5
	settings['maxAR'] = 3
	print(getNewImage(50,settings))
