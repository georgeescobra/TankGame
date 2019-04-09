#!/usr/bin/env python3

import random
import sys
# 1280 x 960
x = 40
y = 30
print(sys.argv)
fName = sys.argv[1] + ".txt"
w = "1" #regulat wall
g = "0" #grass
d = "2" #breakable wall

grid = [ [None]*x for _ in range(y) ]

for i in range( y):
	for j in range(x):
		if i == 0 or i == (y - 1) or j == 0 or j == (x-1):
			grid[i][j] = w
		elif random.randint(1,100) < 20:
			grid[i][j] = w if random.randint(0,1) % 2 == 0 else d
		else:
			grid[i][j] = g
with open (fName, "w+") as toWrite:
    for i in range(len(grid)):
        [toWrite.write(grid[i][j]) for j in range(len(grid[i])) ]
        toWrite.write("\n")
