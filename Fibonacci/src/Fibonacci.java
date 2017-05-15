/**
 * Created by Avikaran on 10/3/2016.
 */
public class Fibonacci {


    public static int recursiveFibonacci(int n)
    {
        if(n==0 || n==1)
            return n;

        return (recursiveFibonacci(n-1) + recursiveFibonacci(n-2));
    }

    public static void displayFibonacci(int n)
    {
        int first = 0;
        int second = 1;
        System.out.println(first);
        System.out.println(second);
        int third;
        int count=3;
        while(count<=n)
        {
            third = first + second;
            System.out.println(third);
            first = second;
            second = third;
            count++;
        }
    }
    public static void main(String[] args)
    {
        System.out.println("Fibonacci series : ");
        displayFibonacci(5);

        System.out.println(recursiveFibonacci(4));
    }

}
