# Samuel Cauvin (CS1516) - 51010557

class Fiction_Generator	
	def initialize
		@storyList = ["", "", "", ""]
		@currentStory = ""
		@currentTitle = ""
		@currentDate = ""
	end

	def created_by
		return "tennessee jed"
	end
	
	def student_id
		return 123456789
	end
	
	def location_adj 
		return ["neo-noir", "alternate-history", "ancient", "post-apocalyptic", "dystopian", "VR-simulated", "metaphorical", "anachronistic", "leather-clad", "coal-powered", "dragon-filled", "shrill"]		
	end
	
	def location_noun
		return ["America", "Japan", "Soviet Russia", "Victorian Britain", "medieval Europe", "Aztec empire", "Atlantis", "terraformed Mars", "Antarctica", "one-way spaceflight", "Outer Rim world", "set from Road Warrior"]   		
	end
	
	def protagonist
		return ["flying message courier", "student of metaphysics", "milquetoast office drone", "schlub with mild OCD", "farm boy with dreams", "techno-obsessed geek", "brooding loner", "wisecracking mercenary", "idealistic revolutionary", "journeyman inventor", "collector of oddities", "author self-insert"]		
	end
	
	def discovery
		return ["magic diadem", "arcane prophecy", "dusty tome", "crazy old man", "alien artifact", "enchanted sword", "otherworldly portal", "dream-inducing drug", "encrypted data feed", "time-traveling soldier", "exiled angel", "talking fish"]
	end
	
	def adversary
		return ["a megalomaniacal dictator", "a government conspiracy", "a profit-obsessed corporation", "a sneering wizard", "supernatural monsters", "computer viruses made real", "murderous robots", "an army led by a sadist", "forces that encourage conformity", "a charismatic politician on the rise", "humanity\'s selfish nature", "his own insecurity vis-a-vis girls"]		
	end
	
	def assistant
		return ["sarcastic female techno-geek", "tomboyish female mechanic", "shape-shifting female assassin", "leather-clad female in shades", "girl who\'s always loved him", "bookish female scholar with mousy brown hair", "cherubic girl with pigtails and spunk", "female who inexplicably becomes attracted to the damaged protagonist for unstated reasons"]		
	end
	
	def	inventory
		return ["wacky pet", "welding gear", "closet full of assault rifles", "reference book", "cleavage", "facility with magic", "condescending tone", "discomfort in formal wear"]		
	end
	
	def conflict
		return ["a fistfight atop a tower", "a daring rescue preceding a giant explosion", "a heroic sacrifice that no one will ever remember", "a philosophical argument punctuated by violence", "a false victory with the promise of future danger", "the invocation of a spell at the last possible moment", "eternal love professed without irony", "the land restored to health", "authorial preaching through the mouths of the characters", "convoluted nonsense that squanders the readers\' goodwill", "wish-fulfillment solutions to real-world problems", "a cliffhanger for the sake of prompting a series"]	
	end
	
	def title_adj
		return ["Chrono", "Neuro", "Aero", "Cosmo", "Reve", "Necro", "Cyber", "Astro", "Psycho", "Steam", "Meta", "Black"]
	end
	
	def title_noun
		return ["punks", "mechs", "noiacs", "opolis", "nauts", "phages", "droids", "bots", "blades", "trons", "mancers", "Wars"]
	end
	
	def generate_identifier(location_adj)
		location_adj = location_adj.downcase
		if (location_adj[0,1] == "a" or location_adj[0,1] == "e" or location_adj[0,1] == "i" or location_adj[0,1] == "o" or location_adj[0,1] == "u")
			return "an"
		end
		return "a"
	end
	
	def title	
		@currentTitle = title_adj[rand(title_adj.length)] + " " + title_noun[rand(title_noun.length)]
		return @currentTitle
	end
	
	def story
		chosenLocationAdj = location_adj[rand(location_adj.length)]
		chosenDiscovery = discovery[rand(discovery.length)]
		@currentStory = "In " + generate_identifier(chosenLocationAdj) + " " + chosenLocationAdj + " " + location_noun[rand(location_noun.length)] + " a young " + protagonist[rand(protagonist.length)] + " stumbles across " + generate_identifier(chosenDiscovery) + " " + chosenDiscovery + " which spurs him into conflict with " + adversary[rand(adversary.length)] + " with the help of a " + assistant[rand(assistant.length)] + " and her " + inventory[rand(inventory.length)] + " culminating in " + conflict[rand(conflict.length)]
		return @currentStory
	end
	
	def story_list
		return @storyList
	end
	
	def swap_story(arraySlot)
		if (arraySlot >= 0 and arraySlot <= 3)
			puts "Story saved to slot " + (arraySlot+1).to_s
			@storyList[arraySlot] = @currentTitle + " - " + @currentDate + "\n" + @currentStory
			return true
		end
		return false
	end
	
	def clear_story_list
		@storyList = ["", "", "", ""]
	end
	
	def random_date
		@currentDate = (rand(28)+1).to_s + "-" + (rand(12)+1).to_s + "-" + (rand(400)+1800).to_s
		return @currentDate
	end
end