import sys
from random import randint

vowels = ["a","e","i","o","u"]
f = open("/usr/share/dict/words","r")
raw = f.read()
f.close()
l = raw.split("\n")
w = l[randint(0,len(l)-1)]
a = randint(0,len(w)-1)
b = a
while a == b:
	b = randint(0,len(w))
v1 = vowels[randint(0,len(vowels)-1)]
v2 = vowels[randint(0,len(vowels)-1)]
w = list(w)
w.insert(a,v1)
w.insert(b,v2)
print("".join(w))
