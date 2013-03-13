class FizzBuzz
	attr_accessor :tmp
	
	def initialize 
		@tmp = ""
	end
	
	def doFizzBuzz
		numbers = [1,2,3,4,5,6,7,8,9,10]
		numbers.each do |i|
			if ((i % 3) == 0 && (i % 5) == 0)
				numbers[i-1] = "FizzBuzz"
			elsif ((i % 3) == 0)
				numbers[i-1] = "Fizz"
			elsif ((i % 5) == 0)
				numbers[i-1] = "Buzz"
			else
				numbers[i-1] = i.to_s
			end
			@tmp += numbers[i-1]
		end
    end
end

fb = FizzBuzz.new()
fb.doFizzBuzz
puts fb.tmp