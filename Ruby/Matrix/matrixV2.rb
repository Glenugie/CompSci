def genCharacter()
	return rand(10).to_s
end

#Defining Matrix Constants
MATRIX_ROWS = 72
MATRIX_COLS = 106
NUMBER_CHANCE = 4
TOTAL_STEPS = 100000
PAUSE_DURATION = 0.1

#10k Steps - Execution time ~30 mins

row = 0
matrix = []
while (row < MATRIX_ROWS)
	column = 0
	rowArray = ""
	while (column < MATRIX_COLS)
		if (rand(NUMBER_CHANCE) == 1)
			rowArray +=  genCharacter + " "
		else
			rowArray += "  "
		end
		column += 1
	end
	matrix.push rowArray
	row += 1
end

stepIncrement = 0
while (stepIncrement < TOTAL_STEPS)
	matrix.pop
	matrix = matrix.reverse
	column = 0
	rowArray = ""
	while (column < MATRIX_COLS)
		if (rand(NUMBER_CHANCE) == 1)
			rowArray +=  genCharacter + " "
		else
			rowArray += "  "
		end
		column += 1
	end
	matrix.push rowArray
	matrix = matrix.reverse
	system("cls")
	puts matrix.join("\n")
	sleep(PAUSE_DURATION)
	stepIncrement += 1	
end