x = input("What episode is the podcast up to?\n")
N = int(x)
x = input("What episode are you up to?\n")
M = int(x)
x = input("How many episodes come out in the on weeks?\n")
R = int(x)
x = input("How many weeks between on weeks?\n")
I = int(x)
x = input("How many episodes do you listen to per week\n")
P = int(x)
try:
	result = (-1*(N-M))/(R/I-P)
except:
	print("You will never catch up at that rate!")
	exit(1)
if result < 4:
	print("You will catch up in about %.2f weeks!"%(result))
elif result < 52:
	print("You will catch up in about %.2f weeks or ~%.2f month(s)!"%(result,result/4))

else:
	years = int(result/52)
	months = ((result/52)-years)*12
	if years > 1:
		print("You will catch up in about %d years and %.2f month(s)"%(years,months))
	else:
		print("You will catch up in about %d year and %.2f month(s)"%(years,months))

