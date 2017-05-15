/**
 * Created by Avikaran on 10/3/2016.
 */
public class Sorting
{

    /***********************************
     * Bubble sort function.
     * @param array
     * @return array
     * Complexity : O(n^2)
     ***********************************/
    static int[] bubbleSort(int [] array)
    {
        for(int i=0; i<array.length; i++)
        {
            for(int j=0; j<array.length-i-1; j++)
            {
                if(array[j]>array[j+1])
                {
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }
        return array;
    }


    static int[] selectionSort(int [] array)
    {

        int minPos = 0;
        for(int i = 0; i< array.length; i++)
        {

            minPos = i;
            for(int j = i+1; j<array.length; j++)
            {
                if(array[j]<array[minPos])
                {
                    minPos = j;
                }
            }
            if(array[i] != array[minPos])
            {
                int temp = array[i];
                array[i] = array[minPos];
                array[minPos] = temp;
            }
        }
        return array;
    }

    static int[] insertionSort(int[] array)
    {
        int holePosition = 1;
        int holeValue;
        for(int i=1 ; i<array.length; i++)
        {
            holePosition=i;
            holeValue = array[holePosition];
            for(int j=holePosition-1; j>=0; j--)
            {
                if(array[j]>array[holePosition])
                {
                    int temp = array[j];
                    array[j] = array[holePosition];
                    array[holePosition] = temp;
                    holePosition = j;
                }
                else
                {
                    break;
                }
            }

        }
        return array;
    }

    /*public static void merge(int[] array, int left, int mid, int right)
    {
        int tempArray[] = new int[right-left+1];

        for(int i=left, j=mid, k=0; k<right-left+1; k++)
        {
            if(i<=mid-1 && j<=right)
            {
                if(array[i]<array[j])
                {
                    tempArray[k]=array[i];
                    i++;
                }
                else
                {
                    tempArray[k] = array[j];
                    j++;
                }
            }
            else {
                if (i <= mid - 1) {
                    tempArray[k] = array[i];
                    i++;
                }
                if (j <= right) {
                    tempArray[k] = array[j];
                    j++;
                }
            }
        }

        for(int k=0, i=left; k<right-left+1; k++,i++)
        {
            array[i] = tempArray[k];
        }
        //return array;
    }

    public static void mergeSort(int[] array, int left, int right)
    {
        if(left>=right)
        {
            return ;
        }

        int mid = (left+right)/2;
        mergeSort(array, left, mid-1);
        mergeSort(array, mid, right);
        merge(array, left, mid, right);



    }*/

    public static int[] leftHalf(int[] array)
    {
        int sizeLeft = array.length/2;
        int[] left = new int[sizeLeft];
        for(int i=0;i<sizeLeft; i++)
        {
            left[i]=array[i];
        }
        return left;

    }

    public static int[] rightHalf(int[] array)
    {
        int sizeLeft = array.length/2;
        int sizeRight = array.length-sizeLeft;
        int[] right = new int[sizeRight];
        for(int i=0; i<sizeRight; i++)
        {
            right[i] = array[sizeLeft+i];
        }
        return right;
    }

    public static void mergeSort(int[] array)
    {
        if(array.length>1)
        {
            int[] left = leftHalf(array);
            int[]right = rightHalf(array);

            mergeSort(left);
            mergeSort(right);
            merge(array, left,right);
        }
    }

    public static void merge(int[] array, int[] left, int[] right)
    {
        int pos1 = 0;
        int pos2 = 0;
        for(int i=0; i<array.length; i++)
        {


            if( pos2>=right.length || pos1<left.length && (left[pos1]<right[pos2]) )
            {
                array[i] = left[pos1];
                pos1++;
            }
            else
            {
                array[i] = right[pos2];
                pos2++;
            }
        }
    }
    private static void displayArray(int[] array)
    {
        for(int i=0; i<array.length; i++)
        {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }


    public static void main(String[] args)
    {
        int[] array = { 9,8,7,7,6,5,4,3,2,1};

        //array = insertionSort(array);
        mergeSort(array);
        //mergeSort(array, 0, 9);
        displayArray(array);


    }


}
