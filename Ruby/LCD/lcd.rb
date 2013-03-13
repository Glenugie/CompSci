class LCD
	attr_accessor( :size, :spacing, :tmp )

	@@lcdDisplayData = {
	"0" => [ 1, 3, 3, 3, 1 ],
	"1" => [ 0, 1, 0, 1, 0 ],
	"2" => [ 1, 1, 1, 2, 1 ],
	"3" => [ 1, 1, 1, 1, 1 ],
	"4" => [ 0, 3, 1, 1, 0 ],
	"5" => [ 1, 2, 1, 1, 1 ],
	"6" => [ 1, 2, 1, 3, 1 ],
	"7" => [ 1, 1, 0, 1, 0 ],
	"8" => [ 1, 3, 1, 3, 1 ],
	"9" => [ 1, 3, 1, 1, 1 ],
	"A" => [ 1, 3, 1, 3, 0 ],
	"B" => [ 1, 3, 2, 3, 1 ],
	"C" => [ 1, 2, 0, 2, 1 ],
	"D" => [ 0, 1, 1, 3, 1 ],
	"E" => [ 1, 2, 1, 2, 1 ],
	"F" => [ 1, 2, 1, 2, 0 ],
	}

	@@lcdStates = [
	   "HORIZONTAL",
	   "VERTICAL",
	   "HORIZONTAL",
	   "VERTICAL",
	   "HORIZONTAL",
	   "DONE"
	]

	def initialize(size=1, spacing=1, tmp)
		@size = size
		@spacing = spacing
	end
	
	def display( digits )
		if (digits == "0") then @tmp = " - | | / | | - ";
		elsif (digits == "1") then @tmp = " | | ";
		elsif (digits == "2") then @tmp = " - | - | - ";
		elsif (digits == "3") then @tmp = " - | - | - ";
		elsif (digits == "4") then @tmp = " | | - | ";
		elsif (digits == "5") then @tmp = " - | - | - ";
		elsif (digits == "6") then @tmp = " - | - | | - ";
		elsif (digits == "7") then @tmp = " - | | ";
		elsif (digits == "8") then @tmp = " - | | - | | - ";
		elsif (digits == "9") then @tmp = " - | | - | - ";
		elsif (digits == "A") then @tmp = " - | | - | | ";
		elsif (digits == "B") then @tmp = " - | | |= | | - ";
		elsif (digits == "C") then @tmp = " - | | - ";
		elsif (digits == "D") then @tmp = " | - | | - ";
		elsif (digits == "E") then @tmp = " - | - | - ";
		elsif (digits == "F") then @tmp = " - | - | ";
		end
	end
end
d = LCD.new("1","1","")
puts d.display("0")