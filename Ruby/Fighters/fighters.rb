require 'set'

class Fighters
	def initialize fighters, attacks
		@fighters = fighters
		@attacks = attacks
	end
	
	def conflict_free? cowboys
		conflictFree = true;
		i = 0;
		while (i < @attacks.length && conflictFree)
			j = 0;
			while (j < cowboys.length and conflictFree)
				if @attacks[i][0] == cowboys[j]
					k = 0;
					while (k < cowboys.length && conflictFree)
						if @attacks[i][1] == cowboys[k]
							conflictFree = false;
						end
						k += 1;
					end
				end
				j += 1;
			end
			i += 1;
		end
		return conflictFree
	end
	
	def defended? cowboy, group
		defended = false;
		i = 0;
		while (i < @attacks.length && !defended)
			if @attacks[i][1] == cowboy
				j = 0;
				while (j < group.length && !defended)
					k = 0;
					while (k < @attacks.length && !defended)
						if @attacks[k][0] == group[j] && @attacks[k][1] == @attacks[i][0]
							defended = true;
						end
						k += 1;
					end
					j += 1;
				end
			end
			i += 1;
		end
		return defended;
	end
	
	def self_defended? group
		selfDefended = false;
		if (conflict_free?(group) && !selfDefended)
			i = 0;
			while (i < group.length)
				if (alive_cowboy?(group[i]) && defended?(group[i],group))
					selfDefended = true;
				end
				i += 1;
			end
		end
		return selfDefended
	end
	
	def unconditionally_alive
		unconditionallyAlive = [];
		i = 0;
		while (i < @fighters.length)
			j = 0; unconA = true;
			while (j < @attacks.length)
				if @attacks[j][1] == @fighters[i]
					unconA = false;
				end
				j += 1;
			end
			if (unconA == true)
				unconditionallyAlive.push(@fighters[i]);
			end
			i += 1;
		end
		return unconditionallyAlive.to_set
	end
	
	def unconditionally_dead
		unconditionallyDead = [];

		return unconditionallyDead.to_set
	end
	
	def alive_cowboy?(cowboy)
		i = 0;
		while (i < @attacks.length)
			if (@attacks[i][1] == cowboy)
				return false;
			end
			i += 1;
		end
		return true;
	end
end

f = Fighters.new([:a,:b,:c,:d],[[:a,:b],[:b,:c],[:c,:a]]);
puts "Conflict free: "+f.conflict_free?([:d]).to_s;
puts "Defended: "+f.defended?(:c,[:a,:d]).to_s;
puts "Self Defended: "+f.self_defended?([:d]).to_s;
puts "Unconditionally Alive: "+f.unconditionally_alive.to_s;
puts "Unconditionally Dead: "+f.unconditionally_dead.to_s;