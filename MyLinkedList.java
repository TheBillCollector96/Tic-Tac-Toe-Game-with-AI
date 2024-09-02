
/**
 * Write a description of class MyLinkedList here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MyLinkedList<E>
{
    private Node<E> head;
    private Node<E> tail;
    
    public MyLinkedList(){
        head = null;
        tail = null;
    }
    
    public void prepend(E item){
        
        Node<E> newNode = new Node(item);
        newNode.next = head;
        head = newNode;
        
        if(tail == null) tail = newNode;
    }
    
    public void append(E item){
        
        Node<E> newNode = new Node(item);
        
        if(head == null){
            head = tail = newNode;
        }else{
            tail.next = newNode;
            tail = newNode;
        }
    }
    
    public E getLast(){
        
        if(tail != null) return tail.element;
        
        return null;
    }
    
    public E get(int index){
        
        if(!(index < getSize())) return null; //If index out of bounds
        
        Node<E> ptr = this.head;
        
        for(int i = 0; i < index; i++){
            ptr = ptr.next;    
        }
        
        return ptr.element;
    }
    
    public boolean contains(E item){
        
        //Checks if element is contained in the requested list
        for(Node<E> ptr = this.head; ptr != null; ptr = ptr.next)
            if(item.equals(ptr.element)) return true;
        
        return false;
    }
    
    public boolean checkWin(MyLinkedList paramList){
        
        int foundCount = 0;
        
        //Check if all elements in the parameter list are contained in the requested list
        for(Node<E> ptr = this.head; ptr != null; ptr = ptr.next)
            if(paramList.contains(ptr.element)) foundCount++;
        
        if(foundCount == 3)
            return true;
        else
            return false;
    }
    
    public boolean delete(E item){
        
        if(head == null) return false; //If list is empty
        
        Node<E> ptrPrv = null;
        Node<E> ptr = this.head;
        
       //Traverse through the linked list while comparing elements
        while(ptr != null && ((Comparable)ptr.element).compareTo(item) != 0){
            ptrPrv = ptr;
            ptr = ptr.next;
        }
        
        if(ptr == null) return false; //Item not found
        
        if(ptr == head)
            head = head.next;
        else
            ptrPrv.next = ptr.next;
        
        if(ptr == tail) tail = ptrPrv;
        
        return true;
    }
    
    public int getSize(){
        
        int size = 0;
        
        for(Node<E> ptr = this.head; ptr != null; ptr = ptr.next){
            size++;
        }
        
        return size;
    }
    
    public void clearList(){
        head = null;
        tail = null;
    }
    
    public boolean isEmpty(){
        
        if(this.head == null) return true;
        
        return false;
    }
    
    private static class Node<E>{
        
        E element;
        Node<E> next;
        
        public Node(E element){
            this.element = element;
            next = null;
        }
    }
}
