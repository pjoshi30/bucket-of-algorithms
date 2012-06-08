package practise;

import java.util.ArrayList;
import java.util.Arrays;

public class MedianOfMedianQuickSelect {

	public int select(int[] arr, int k) {
		int n = arr.length;
		if (n <= 10) {
			insertionSort(arr);
			return arr[k];
		}
		int lengthOf5PartitionArr = (int) Math.ceil(((double) n / 5.0));
		int[] arrayOf5Partitions = new int[lengthOf5PartitionArr];
		int p = 0;
		for (int i = 0; i < lengthOf5PartitionArr && p < n; i++) {
			int[] arrPartition = new int[((n - p) < 5) ? n - p : 5];
			System.arraycopy(arr, p, arrPartition, 0, ((n - p) < 5) ? n - p : 5);
			arrayOf5Partitions[i] = select(arrPartition, 2);
			p += 5;
		}

		int median = select(arrayOf5Partitions, n / 10);
		ReturnedArray partitionedArr = new ReturnedArray(arr, median);
		if (k <= partitionedArr.left.length)
			return select(partitionedArr.left, k);
		else if (k > partitionedArr.left.length + 1)
			return select(partitionedArr.right, k - partitionedArr.left.length
					- 1);
		else
			return median;
	}

	private void insertionSort(int[] tmp) {

		for (int i = 0; i < tmp.length; i++) {
			for (int j = i; j > 0 && tmp[j] < tmp[j - 1]; j--) {
				// XOR swap
				tmp[j] = tmp[j] ^ tmp[j - 1];
				tmp[j - 1] = tmp[j] ^ tmp[j - 1];
				tmp[j] = tmp[j] ^ tmp[j - 1];
			}
		}
	}

//	private void printArray(int[] inp) {
//		for (int val : inp)
//			System.out.print(" " + val);
//		System.out.println(" ");
//	}


	private class ReturnedArray {
		int[] left;
		int[] right;

		public ReturnedArray(int[] tmp, int median) {
			int length = tmp.length;
			ArrayList<Integer> leftList = new ArrayList<Integer>();
			ArrayList<Integer> rightList = new ArrayList<Integer>();
			for (int i = 0; i < length; i++) {
				if (tmp[i] < median)
					leftList.add(tmp[i]);
				else if (tmp[i] > median)
					rightList.add(tmp[i]);
			}
			left = new int[leftList.size()];
			int i = 0;
			for (int num : leftList)
				left[i++] = num;
			i = 0;
		    right = new int[rightList.size()];
		    for(int num: rightList)
		    	right[i++] = num;
		}
	}

	public static void main(String... args) {
		int[] inp = { 4, 5, 1, 3, 7, 2, 9, 8, 6, 17, 89, 77, 101, 54, 76, 34,
				71, 5, 1 };
		int k = 18;
		int res = new MedianOfMedianQuickSelect().select(inp, k);
		System.out.println("Kth largest element: " + res);
		Arrays.sort(inp);
		System.out.println("Kth largest element: " + inp[k]);
	}
}
