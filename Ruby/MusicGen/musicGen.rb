#require 'win32/sound'
#include Win32

#Instance variables
RANDOM_GENERATION = false
arrayLength = 10
notePitch = 100
actionLength = 0.25
betweenActionLength = 0.05

if (RANDOM_GENERATION == true)
	arrayLength = (rand(450) + 50)
end

index = 0
array = Array.new(arrayLength)
while index < arrayLength
	if rand(2) == 1
		array[index] = 1
	else
		array[index] = 0
	end
	index += 1
end

#Processing
index = 0
while index < arrayLength
	if (RANDOM_GENERATION == true)
		notePitch = (rand(10000) + 37)
		actionLength = (rand(0.9) + 0.1)
		betweenActionLength = rand(0.1)
	end
		
	if array[index] == 1
		#Sound.beep(notePitch, (actionLength * 1000))
		puts " \a "
	else
		sleep(actionLength)
	end
	
	sleep(betweenActionLength)
	
	index += 1
end

