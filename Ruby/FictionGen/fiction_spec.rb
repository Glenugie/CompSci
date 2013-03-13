require 'fiction_gen'
  
  describe Fiction_Generator do
    
    before(:all) do
      @fict_gen = Fiction_Generator.new
      
    end
    
    it "should return the student name" do
    @fict_gen.created_by.should == "tennessee jed"
    end
    
    it "should return the student id" do
    @fict_gen.student_id.should==123456789
    end
    
    it "should load up location adjectives" do
      @fict_gen.location_adj.size.should == 12
    end
    
     it "should load up location nouns" do
      @fict_gen.location_noun.size.should == 12
     end
     
      it "should load up protagonist" do
      @fict_gen.protagonist.size.should == 12
      end
      
       it "should load up discovery" do
      @fict_gen.discovery.size.should == 12
       end
       
        it "should load up adversary" do
      @fict_gen.adversary.size.should == 12
        end
        
         it "should load up assistant" do
      @fict_gen.assistant.size.should == 8
    end
        
         it "should load up inventory" do
      @fict_gen.inventory.size.should == 8
         end
         
          it "should load up conflict" do
      @fict_gen.conflict.size.should == 12
          end
          
           it "should load up title adjectives" do
      @fict_gen.title_adj.size.should == 12
           end
       it "should load up title noun" do
      @fict_gen.title_noun.size.should == 12
    end
    
    it "should randomly generate a title for the story" do
    @fict_gen.title.should be_true
    end
    
    it "should randomly generate a story using an item from each array" do
     @fict_gen.story.should be_true
    end
    
     after(:all) do
      puts "this fiction generator was created by #{@fict_gen.created_by}"
      puts "who has the student id of #{@fict_gen.student_id}"
      puts "your story is called #{@fict_gen.title} "
      puts "and it goes like this:"
      puts @fict_gen.story
      

    end
        
  end
