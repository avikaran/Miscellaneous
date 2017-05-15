import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avikaran on 9/28/2016.
 *
 * Description : Program to find the best days to buy/sell stocks
 */
public class Solution {



    public static void maxProfit(int[] stockPrices)
    {
        //10 15 9 100

        int buyIndex = 0;
        int sellIndex = 1;
        int min=0;

        int profit ;
        int maxProfit= Integer.MIN_VALUE;

        if(stockPrices.length<2)
        {
            return ;
        }


        for( int i=1; i<stockPrices.length; i++ )
        {

            profit = stockPrices[i] - stockPrices[min];
            if(profit>maxProfit)
            {
                maxProfit = profit;
                sellIndex = i;
                buyIndex = min;
            }
            if(stockPrices[i]<stockPrices[min])
            {
                min = i;
            }

        }
        System.out.println("Buy on day " + (buyIndex+1) + " and sell on day " + (sellIndex+1));


    }

    public static void main(String[] args)
    {
        maxProfit( new int[]{10,9,9,8});
    }



}
