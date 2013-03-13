def countOperation startNumber
	counter = 0
	workingNumber = startNumber
	while (workingNumber != 1)
		if ((workingNumber %2) == 1)
			workingNumber *= 3
			workingNumber += 1
		else
			workingNumber /= 2
		end
		counter += 1
	end
	return (counter+1)
end

rangeMin = ARGV[0].to_i
rangeMax = ARGV[1].to_i
largestCount = 0
currentCount = 0
workingMin = 0
if ((rangeMin % 2) == 0)
	workingMin = (rangeMin + 1)
else
	workingMin = rangeMin;	
end

i = workingMin
while (i <= rangeMax)
	currentCount = countOperation(i)
	if (currentCount > largestCount)
		largestCount = currentCount
	end
	i += 2
end

if ((rangeMax % 2) == 0)
	currentCount = countOperation(rangeMax);
	if (currentCount > largestCount)
		largestCount = currentCount
	end
end

puts rangeMin.to_s + " " + rangeMax.to_s + " " + largestCount.to_s