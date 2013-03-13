require './fiction_gen.rb'

generator = Fiction_Generator.new
userQuit = false

system("cls") # Clear the environment to maximise space
puts "Welcome to David Malki's 'The Electro-Plasmic Hydrocephalic Genre-Fiction Generator 2000' in Ruby by Samuel Cauvin (51010557)"
puts "For help using this program, type help. To exit, type quit"
puts ""

while !userQuit
	puts "Enter command: "
	print "> "
	userResponse = gets.chomp.strip.downcase
	
	if userResponse == "help"
		puts "help - Displays this command list"
		puts "quit - Exits this program"
		puts "generate - Generates a new story, giving you the option to save it"
		puts "list - Lists all currently saved stories"
		puts "populate - Generates 4 random stories and saves them over any existing stories"
		puts "delete - Erases any saved stories"
		puts "reset - Clears the screen, but retains saved stories"
	elsif userResponse == "quit"
		puts "Exiting program"
		userQuit = true
	elsif userResponse == "list"
		index = 0
		storyFound = false
		while (index < 4)
			if (generator.story_list[index] != "")
				puts "\n" + generator.story_list[index]
				storyFound = true
			end
			index += 1
		end
		
		if !storyFound
			puts "No stories saved"
		end
	elsif userResponse == "generate"
		puts generator.title + " - " + generator.random_date
		puts generator.story

		puts ""
		
		puts "Would you like to save this story? (Y/N)"
		print "> "
		saveResponse = gets.chomp.strip.downcase
		if (saveResponse == "y")
			index = 0
			storySaved = false
			while (index < 4 and !storySaved)
				if (generator.story_list[index] == "")
					generator.swap_story(index)
					storySaved = true
				end
				index += 1
			end
			
			if !storySaved #There weren't any free slots
				puts "No available save slots, would you like to overwrite a slot? (Y/N)"
				print "> "
				overwriteResponse = gets.chomp.strip.downcase
				if (overwriteResponse == "y") 
					index = 0
					while (index < 4)
						puts "\nStory " + (index+1).to_s + ": " + generator.story_list[index]
						index += 1
					end
					
					overwriteChoice = -1
					while (overwriteChoice < 1 or overwriteChoice > 4)
						puts "\nEnter the index of the story you wish to overwrite (1-4)"
						print "> "
						overwriteChoice = gets.chomp.strip.to_i
						if (overwriteChoice >= 1 and overwriteChoice <= 4)
							generator.swap_story((overwriteChoice-1))
						else
							puts "Invalid story index, valid indexes are 1 to 4 inclusive. Overwrite failed"
						end
					end
				else
					puts "Story not saved"
				end
			end
		end
	elsif userResponse == "populate"
		index = 0
		while index < 4
			generator.title
			generator.random_date
			generator.story
			generator.swap_story(index)
			index += 1
		end
	elsif userResponse == "delete"
		generator.clear_story_list
		puts "Stories deleted"
	elsif userResponse == "reset"
		system("cls")
		
		puts "Welcome to David Malki's 'The Electro-Plasmic Hydrocephalic Genre-Fiction Generator 2000' in Ruby by Samuel Cauvin (51010557)"
		puts "For help using this program, type help. To exit, type quit"
		puts ""
	else
		puts "Unrecognised command, type help for a list of commands"
	end
	puts ""
end