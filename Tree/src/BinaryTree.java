import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Avikaran on 10/3/2016.
 */

class TreeNode
{
    int data;
    TreeNode left;
    TreeNode right;
    TreeNode(int a)
    {
        data = a;
        left = null;
        right = null;
    }
}
public class BinaryTree {

    TreeNode root;

    BinaryTree()
    {
        root=null;
    }

    public void insert(int value)
    {
        TreeNode element = new TreeNode(value);

        if(root== null)
        {
            root = element;
            return;
        }

        TreeNode temp = root;
        TreeNode temp1 = temp;
        while(temp!=null)
        {
            temp1 = temp;
            if(temp.data>element.data)
            {
                temp = temp.left;
            }
            else
                temp = temp.right;
        }
        if(element.data<temp1.data)
        {
            temp1.left = element;
        }
        else
        {
            temp1.right = element;
        }


    }

    public void inOrder(TreeNode root)
    {
        if(root == null)
        {
            return;
        }
        inOrder(root.left);
        System.out.println(root.data);
        inOrder(root.right);
    }

    public void postOrder(TreeNode root)
    {
        if(root == null)
        {
            return;
        }
        postOrder(root.left);
        postOrder(root.right);
        System.out.println(root.data);
    }

    public void preOrder(TreeNode root)
    {
        if(root == null)
        {
            return;
        }
        System.out.println(root.data);
        preOrder(root.left);
        preOrder(root.right);

    }

    public void bfs(TreeNode root)
    {
        if(root == null)
            return;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while(!queue.isEmpty())
        {

            TreeNode n = queue.poll();
            System.out.println(n.data);
            if(n.left!=null)
            {
                queue.offer(n.left);
            }
            if(n.right!=null)
            {
                queue.offer(n.right);
            }

        }
    }

    public void maxWidth(TreeNode root)
    {
        int max = 0;
        if(root == null)
            return;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty())
        {
            int levelNodes = queue.size();
            if(levelNodes > max)
                max = levelNodes;

            while(levelNodes>0)
            {
                TreeNode n = queue.poll();
                if(n.left!=null)
                    queue.offer(n.left);
                if(n.right!=null)
                    queue.offer(n.right);
                levelNodes--;

            }
        }
        System.out.println("Maximum Width of the tree is : " + max);

    }

    public void minHeight(TreeNode root)
    {
        if(root==null)
            return;

        int minHeight=0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty())
        {
            minHeight++;
            int levelNodes = queue.size();
            while(levelNodes>0)
            {
                TreeNode n = queue.poll();
                if(n.left== null || n.right == null)
                {
                    System.out.println("Minimum height of the tree is : " + minHeight);
                    return;
                }
                if(n.left!=null)
                    queue.offer(n.left);
                if(n.right!=null)
                    queue.offer(n.right);
                levelNodes--;

            }
        }
    }

    public void maxHeight(TreeNode root)
    {
        if(root==null)
        {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();

        queue.offer(root);
        int maxHeight = 0;
        while(!queue.isEmpty())
        {
            maxHeight++;
            int levelNodes = queue.size();
            while(levelNodes>0)
            {
                TreeNode n = queue.poll();
                if(n.left!=null)
                    queue.offer(n.left);
                if(n.right!=null)
                    queue.offer(n.right);
                levelNodes--;

            }
        }
        System.out.print("Maximum height of the tree is : " + maxHeight);
    }

    public int minElementBinaryTree(TreeNode root, int min)
    {
        if(root == null)
            return min;

        if(root.data < min)
            min = root.data;

        min = minElementBinaryTree(root.left, min);
        return minElementBinaryTree(root.right, min);

    }


    public static void main(String[] args)
    {
        BinaryTree t = new BinaryTree();
        t.insert(5);
        t.insert(3);
        t.insert(4);
        t.insert(1);
        t.insert(9);
        t.insert(7);
        t.insert(6);
        t.insert(15);
		/*
		 *
		 * 		 5
		 * 	   /   \
		 *    3     9
		 *   / \   / \
		 *  1   4 7  15
		 *        /
		 *       6
		 */
        t.maxWidth(t.root);
        t.minHeight(t.root);
        t.maxWidth(t.root);
        System.out.println("Minimum element in the tree is : " + t.minElementBinaryTree(t.root, Integer.MAX_VALUE));


    }
}
