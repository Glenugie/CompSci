a = 0
b = 1
index = 2
maxDigits = 10

puts "How many digits should the Fibonacci sequence be displayed to?"
maxDigits = get.chomp

print a.to_s + "\t"
print b.to_s + "\t"

while index <= maxDigits
	b = a + b
	a = b - a
	print b.to_s + "\t"
	index += 1
	if (index % 5) == 0
		print "\n"
	end
end