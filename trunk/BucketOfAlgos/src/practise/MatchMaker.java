package practise;

import java.util.*;

//{{"BOB M M A", "FRED M F A", "JIM F M A", "DAISY F F A"}, "BOB", 1}
public class MatchMaker{
  public static HashMap<String, User> nameUserMap = new HashMap<String, User>();
  @SuppressWarnings("unchecked")
  public static ArrayList<User>[] genderUserMap = (ArrayList<User>[])new ArrayList[2];
  
  static{
      genderUserMap[0] = new ArrayList<User>();
      genderUserMap[1] = new ArrayList<User>();
      
  }
  public String[] getBestMatches(String[] mem, String current, int sf){
      
      //Build hashtable
      for(String m: mem){
           String[] split = m.split("\\s");
           if(split.length < 4)
               return new String[0];
           User user = new User(split);
           nameUserMap.put(split[0], user);
           if(split[1].equals("M"))
          	 genderUserMap[0].add(user);
           else
          	 genderUserMap[1].add(user);
      }
      
      //Process current user
      if(!nameUserMap.containsKey(current))
          return new String[0];
      User currentUser = nameUserMap.get(current);
      ArrayList<User> potentialMatches = (currentUser.expectedG == 'M')?genderUserMap[0]:genderUserMap[1];
      
      ArrayList<User> matches = new ArrayList<User>();
      for(User u: potentialMatches){
           int sf1 = 0;
           for(int i = 0; i < currentUser.answers.length; i++){
               if(currentUser.answers[i] == u.answers[i])
                   sf1++;
               
           }
           if(sf1>=sf && !currentUser.name.equals(u.name)){
               u.sf = sf1;
               matches.add(u);
           }
      }
      
      Collections.sort(matches, new MatchComparator());
      
      String[] ret = new String[matches.size()];
      int cnt = 0;
      for(User u1: matches)
          ret[cnt++] = u1.name;
      
      return ret;
  }
  
  class MatchComparator implements Comparator<User>{
      public int compare(User u1, User u2){
          return u2.sf - u1.sf;
      }
  }
  
  class User{
       private final String name;
       private final char gender;
       private final char expectedG;
       private final char[] answers;
       private int sf;
       
       public User(String[] split){
            
            this.name = split[0];
            this.gender = split[1].charAt(0);
            this.expectedG = split[2].charAt(0);
            answers = new char[split.length-3];
            for(int i = 3; i<split.length; i++){
                answers[i-3] = split[i].charAt(0);
            }
       }
  }
}