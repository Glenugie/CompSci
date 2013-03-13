class Array    
	attr_accessor :my_arr, :tmp
	
	def initialize myarray=[], temp=""
        @my_arr = myarray
		@@my_arr = @my_arr
		@tmp = temp
	end
		
	# 131 sum the integer values of array contents      
	def int_sum        
		self.inject(0){|sum,i| sum+i.to_i}      
	end      
	
	# find the maximum sub array in an array      
	def max_sub_array
		orderedArray = @@my_arr.sort
        top1 = orderedArray[orderedArray.size-1]
		top2 = orderedArray[orderedArray.size-2]
		top3 = orderedArray[orderedArray.size-3]
		
		top1I = @@my_arr.index(top1);
		top2I = @@my_arr.index(top2);
		top3I = @@my_arr.index(top3);
		orderList = [top1I, top2I, top3I].sort;
		
		return @@my_arr[orderList[0]..orderList[2]];
	end  

	def solve
		puts "array: #{my_arr.inspect}"      
		@tmp = "maximum sub-array: #{my_arr.max_sub_array.inspect}"
		puts "maximum sub-array: #{my_arr.max_sub_array.inspect}" 
	end
end

fb = Array.new([-1, 2, 5, -1, 3, -2, 1], "")
fb.solve
puts fb.tmp