require 'set';
class MazeSolver
	attr_accessor( :res, :visited );

	def initialize()
		@res = "";
		@visited;
		@upperLimit;
		@lowerLimit;
	end

	def double(value)
		value *= 2;
		visit?(value);
		return value;
	end
	
	def halve(value)
		if ((value % 2) == 0)
			value /= 2;
		else
			value = double(value);
			value = add2(value);
			value /= 2;
		end
		visit?(value);
		return value;
	end
	
	def add2(value)
		value += 2;
		visit?(value);
		return value;
	end
	
	def visit?(value)
		if (@visited.add?(value) == nil)
			temp = @visited.to_a;
			while (temp[(temp.length-1)] != value)
				temp.pop();
			end
			@visted = temp.to_set;
			forceTerminate();
		end
	end
	
	def forceTerminate()
		@res = @visited.to_a.to_s;
		puts "Program has jammed, cannot find soluion. Solution so far:";
		puts @res;
		exit;
	end
	
	def solve start, finish
		r = Random.new;
		@visited = Set.new;
		visit?(start);
		@upperLimit = finish * 3;
		@lowerLimit = start - (start * 2);
		
		#Solve maze
		if ((start*2 != finish) && (start/2 != finish) && (start+2 != finish))
			newValue = start;
			while (newValue != finish)
				if ((finish % 2) == 0)
					if (finish > start)
						while ((newValue * 2) <= finish)
							newValue = double(newValue);
						end
						while (newValue != finish)
							newValue = add2(newValue);
						end
					else
						while ((newValue / 2) >= finish)
							newValue = halve(newValue);
						end
						while (newValue != finish)
							newValue = add2(newValue);
						end
					end	
				else
					if (finish > start)
						while ((newValue * 2) <= (finish * 2))
							newValue = double(newValue);
						end
						while (newValue != (finish * 2))
							newValue = add2(newValue);
						end
						newValue = halve(newValue);
					else
						while ((newValue / 2) >= (finish * 2))
							newValue = halve(newValue);
						end
						while (newValue != (finish * 2))
							newValue = add2(newValue);
						end
						newValue = halve(newValue);
					end	
				end
			end
		end
		
		@res = @visited.to_a.to_s;
		puts @res;
	end
end

d = MazeSolver.new();
d.solve(9,2);