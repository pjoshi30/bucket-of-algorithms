package practise;

public class SubArrayMatch{
    
    private final int[] array;
    
    public SubArrayMatch(int[] inp){
        array = inp;
    }
    
    public int[] matchSubArray(int target){
        
        int currentSum = 0;
        int[] ret = new int[2];
        for (int i = 0; i < array.length; i++){
        
            if(currentSum == target)
                break;
            currentSum += array[i];
            ret[1] = i;
            if (currentSum > target)
                currentSum = adjust(ret, currentSum, target);
        }
        return (currentSum==target)?ret: null;
    }
    
    private int adjust(int[] arr, int currSum, int target){
        int i = arr[0];
        while(currSum > target && i <= arr[1]){
            currSum-=array[i++];
        }
        arr[0] = i;
        return currSum;
    }
    
    public static void main(String... args){
        int[] inp = {1, 5, 7, 8, 9, 3};
        SubArrayMatch sam = new SubArrayMatch(inp);
        int target = 12;
        int[] ret = sam.matchSubArray(target);
        if (ret == null)
        	System.out.println("Not Found!");
        else
         for(int i = ret[0]; i <= ret[1]; i++)
           	System.out.print(" "+inp[i]);
        
    }
}
    
    
    
    
    
    
    
