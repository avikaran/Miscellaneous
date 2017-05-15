/**
 * Created by Avikaran on 10/3/2016.
 */

class Node
{
    int data;
    Node next;
    Node(int a)
    {
        data = a;
        next = null;
    }
}
public class MyLinkedList
{

    Node head;
    MyLinkedList()
    {
        head = null;
    }

    public void add(int a)
    {
        Node element = new Node(a);
        if(head==null)
        {
            head = element;
        }
        else
        {
            if(element.data<head.data)
            {
                element.next = head;
                head = element;
            }
            else
            {
                boolean inserted = false;
                Node temp = head;
                Node temp1 = head;
                while(temp!=null)
                {
                    if(element.data<temp.data)
                    {
                        element.next = temp1.next;
                        temp1.next = element;
                        inserted = true;
                        break;

                    }
                    temp1 = temp;
                    temp = temp.next;
                }
                if(inserted==false)
                {
                    temp1.next = element;
                }
            }
        }

    }

    public void remove(int k)
    {
        if(head==null)
        {
            return;
        }
        if(head.data==k)
        {
            head = head.next;
            return;
        }

        Node temp=head;
        Node temp1 = temp;
        while(temp!=null)
        {
            if(temp.data==k)
            {
                temp1.next = temp.next;
                temp.next = null;
                return;
            }
            temp1 = temp;
            temp = temp.next;
        }

    }

    public void reverse()
    {
        Node left = null;
        Node current = head;
        Node right = current.next;

        while(right!=null)
        {
            current.next = left;
            left = current;
            current = right;
            right = right.next;
        }
        current.next = left;
        head = current;

    }

    public void traverse()
    {
        System.out.println();
        Node temp = head;
        while(temp!=null)
        {
            System.out.print(temp.data);
            if(temp.next!=null)
            {
                System.out.print("-->");
            }
            temp = temp.next;

        }
    }


    public static void main(String[] args)
    {
        MyLinkedList l1 = new MyLinkedList();
        l1.add(4);
        l1.add(1);
        l1.add(9);
        l1.add(5);
        l1.add(2);
        l1.add(8);
        l1.add(3);
        l1.traverse();

        l1.remove(10);
        l1.traverse();
        l1.remove(4);
        l1.traverse();

        l1.reverse();
        l1.traverse();

    }

}
