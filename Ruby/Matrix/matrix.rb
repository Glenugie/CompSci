i = 0
while i < 10000
	matrix = ""
	j = 0
	while j < 150
		matrix += rand(10).to_s + " "
		j += 1
	end
	puts matrix
	i += 1
end