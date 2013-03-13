def generateNumeral(number)
print number.to_s + " = "
	generateNumeral = ""
	counter = 0
	while (number > 0)
		if (counter == 10)
			counter = 0
			generateNumeral += " \n "
		end
		
		if ((number - 1000) >= 0)
			generateNumeral += "M"
			number -= 1000
		elsif ((number - 500) >= 0)
			generateNumeral += "D"
			number -= 500
		elsif ((number - 100) >= 0)
			generateNumeral += "C"
			number -= 100
		elsif ((number - 50) >= 0)
			generateNumeral += "L"
			number -= 50		
		elsif ((number - 10) >= 0)
			generateNumeral += "X"
			number -= 10
		elsif ((number - 9) % 10 == 0)
			generateNumeral += "IX"
			number -= 9
		elsif ((number - 4) % 5 == 0)
			generateNumeral += "IV"
			number -= 4
		elsif ((number - 5) >= 0)
			generateNumeral += "V"
			number -= 5
		elsif ((number - 1) >= 0)
			generateNumeral += "I"
			number -= 1
		end
		counter += 1
	end

	puts generateNumeral + " in Roman Numerals"
	puts ""
end

userQuit = false
while userQuit == false
	puts "Enter the number to convert to Roman Numerals"
	print "> "
	number = gets.chomp
	if (number.downcase == "quit")
		userQuit = true
	elsif (number.downcase == "test")
		puts "Start at which value?"
		print "> "
		minValue = gets.chomp.to_i
		puts ""
		puts "Test up to which value?"
		print "> "
		maxValue = gets.chomp.to_i
		if (minValue < 1)
			minValue = 1
		end
	
		index = minValue
		while (index <= maxValue)
			generateNumeral(index)
			index += 1
		end
	else
		number = number.to_i
		generateNumeral(number)		
	end
end
system("cls")