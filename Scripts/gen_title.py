import sys
import enchant
import random

d = enchant.Dict("en_US")

def validSentence(s):
	global d
	valid = True
	for w in s.split(" "):
		valid = valid and d.check(w)
	return valid

def gen(s):
	s = s.lower()
	s = list(s)
	options = []
	for i,c in enumerate(s):
		if c == " ":
			continue
		for cc in range(97,123):
			if c == chr(cc):
				continue
			cpy = list(s)
			cpy[i] = chr(cc)
			options.append("".join(cpy))
	# filter
	final = []
	for option in options:
		if validSentence(option):
			final.append(option)
	rand = random.randint(0,len(final)-1)
	for e in final:
		print(e)



gen(" ".join(sys.argv[1:]))