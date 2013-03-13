class PascalSolver
	attr_accessor(:tmp);
		
	def initialize
		@tmp = "";
	end

    def pascal(n)
		@@triangle = [1,1];
		
		(2..n-1).each do |row|
			@@triangle = generateRow(row);
			rowValue = "";
			(0..n).each do |value|
				if (value != 0)
					rowValue += " ";
				end
				rowValue += @@triangle[value].to_s;
			end
			@tmp = rowValue;
			puts @tmp;
		end
    end
	
	def generateRow(row)
		currentRow = [1];
		(1..(row-1)).each do |column|
			currentRow << (@@triangle[column-1] + @@triangle[column]);
		end
		currentRow << 1;
		return currentRow;
	end
end

d = PascalSolver.new;
d.pascal(10);