package practise;

public class ReverseArrayWithRecursion {
	
	public void reverseArray(Integer[] inp){
		reverseArray(inp, inp.length-1, inp.length-1);
	}
	
	public void reverseArray(Integer[] array, int n, int elem){
		System.out.println("\n"+n+" "+elem);
		if(elem == array.length/2)
			return;
		swap(array, elem, n-elem);
		reverseArray(array, n, --elem);
	}
	
	private void swap(Integer[] array, int i, int j){
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	public static void main(String... args){
		Integer[] array = {1,2,3,4,5,6,7,8,9,10};
		System.out.println("Before Reversing: ");
		for(int i : array)
			System.out.print(" "+i);
		ReverseArrayWithRecursion reverseRecursion = new ReverseArrayWithRecursion();
		reverseRecursion.reverseArray(array);
		System.out.println("\nAfter Reversing: ");
		for(int i : array)
			System.out.print(" "+i);
	}

}
