/**
 * Created by Avikaran on 10/9/2016.
 */
public class ArraySearch {

    public static int partition(int[] array)
    {
        for(int i=1; i<array.length ; i++)
        {
            if(array[i]-array[i-1]>0)
                continue;
            else
                return i;
        }
        return 0;

    }

    public static int binarySearch(int [] array, int left, int right,  int key)
    {
        if(left>right)
        {
            //System.out.println("Not found");
            return -1;
        }

        int mid = (left+right)/2;

        if(array[mid] == key)
        {
            //System.out.println("Found at position : " + (mid+1));
            return mid;
        }

        if(key<array[mid])
             return binarySearch(array, left, mid-1, key);
        else
            return binarySearch(array, mid+1, right, key);



    }


    public static void main(String[] args)
    {
        int array[] = { 6,7,8,1,2,3,4,5};

        int key = 5;
        int change = partition(array);

        int found = binarySearch(array,0,change-1,key);
        if(found==-1)
        {
            found = binarySearch(array, change,array.length-1, key);
        }
        System.out.println("Found at : " + found);

    }
}
