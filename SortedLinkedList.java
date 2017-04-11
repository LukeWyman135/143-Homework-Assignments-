import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class that creates a LinkedList of sorted data
 */
public class SortedLinkedList<E extends Comparable<E>> implements ISortedList<E> {
    // Private fields
    private int size;
    private E value;
    private Node<E> head;
    private Node<E> tail;
    //Constructor for initializing a SortedLinkedList
    public SortedLinkedList(){
        head = null;
        tail = null;
    }

    /**
     * Gets the current number of nodes currently stored in the list
     * @return int - The current size of the linked list
     */
    public int size(){
        return size;
    }

    /**
     * Method to test if the list is currently holding onto any elements
     * @return boolean - True if the list is empty, otherwise false
     */
    public boolean isEmpty(){
        return(head == null);
    }

    /**
     * Gets the value of whatever object is currently being held at the first node in the list
     * @return E - The object that is currently being held at the beginning of the list
     */
    public E getHead(){
        return head.value;
    }

    /**
     * Get the value of whatever object is currently being held at the last node in the list
     * @return E - The object that is currently being held at the end of the list
     */
    public E getTail(){
        return tail.value;
    }

    /**
     * Method to find the index in the list where the passed in object is located
     * @param value The object that we want to find the index of
     * @return int - The index where the input object is located, -1 if it's not held in the list
     */
    public int indexOf(E value){
        int index = 0;
        Node<E> current = head;
        while(current != null){
            if(current.value.equals(value)){
                return index;
            }
            index++;
            current = current.next;
        }
        return -1;
    }

    /**
     * Method to check to see if an object is currently being held within the list
     * @param value The object that we want to know is contained in our list
     * @return boolean - True if the list contains the value passed in, otherwise false
     */
    public boolean contains(E value){
        return (indexOf(value) >= 0);
    }

    /**
     * Method that adds an object to the list. If the list is currently empty then the head and tail will be set to the object.
     * Otherwise the object will be placed in ascending order within the list
     * @param value The object that we want to add to the list
     */
    public void add(E value){
        //If empty add to the front of the list
        if(head == null){
            head = new Node(value);
            tail = head;
            size++;
            return;
        }
        //Find the right spot in the list
        SortedLinkedList.Node current = head;
        while(current != null && current.value.compareTo(value) < 0) {
            current = current.next;
        }
        //Conditionals for placing at the beginning, middle, and end of the list
        Node temp = new SortedLinkedList.Node(value);
        if(current == head) {
            temp.next = current;
            head.prev = temp;
            head = temp;
        } else if(current == null){
            temp.prev = tail;
            tail.next = temp;
            tail = temp;
        } else {
            temp.prev = current.prev;
            temp.next = current;
            current.prev.next = temp;
            current.prev = temp;
        }
        size++;
        return;
    }

    /**
     * Method to add all the contents of another SortedLinkedList to this.SortedLinkedList
     * @param other The list that we want to add the contents of to this.SortedLinkedList
     */
    public void addAll(ISortedList<E> other){
        //Use Iterator to grab values from the other list and add them to this.list
        for(E value: other){
            add(value);
        }
    }

    /**
     * Method to remove the first object in the list, head.next then becomes the new head of the list
     * @return E - The object that was previously held at the beginning of the list
     * @throws NullPointerException if user tries to remove a value from an empty list
     */
    public E removeHead() throws NullPointerException{
        if(isEmpty()){
            throw new NullPointerException();
        }
        E temp = head.value;
        head = head.next;
        size--;
        return temp;
    }

    /**
     * Method to remove the last object in the list, tail.prev then becomes the new tail of the list
     * @return E - The object that was previously held at the end of the list
     * @throws NullPointerException if user tries to remove a value from an empty list
     */
    public E removeTail() throws NullPointerException{
        if(isEmpty()){
            throw new NullPointerException();
        }
        E temp = tail.value;
        tail = tail.prev;
        size--;
        return temp;
    }

    /**
     * Method to remove a desired object from the list
     * @param value The object in the list that we want to remove
     * @return boolean - True if the value was successfully removed, otherwise false
     */
    public boolean remove(E value){
        if(isEmpty()){
            return false;
        }
        //Find the node in the list
        Node<E> current = head;
        while(current != null && !current.value.equals(value)){
            current = current.next;
        }
        if(current == null){
            return false;
        }
        Node<E> pred = current.prev;
        Node<E> succ = current. next;
        if(pred == null){
            head = succ;
        } else{
            pred.next = succ;
        }
        if(succ == null){
            tail = pred;
        } else{
            succ.prev = pred;
        }
        size--;
        return true;
    }

    /**
     * Clears the contents of the entire list, size is reset to 0
     */
    public void clear(){
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Method to give a string representation of the SortedLinkedList
     * @return String - The string representation of the SortedLinkedList
     */
    public String toString(){
        if(size == 0){
            return ("[]");
        } else{
            String result = "[" + head.value;
            Node<E> current = head.next;
            while(current != tail.next){
                result += ", " + current.value;
                current = current.next;
            }
            result += "]";
            return result;
        }
    }

    /**
     * Creates an Iterator that can be used over the SortedLinkedList
     * @return Iterator - An Iterator object to iterate over the SortedLinkedList
     */
    public Iterator<E> iterator(){
        return new SortedIterator();
    }

    /**
     * Private Class to create a Node object
     */
    private class Node<E extends Comparable<E>>{
        E value;
        Node<E> next;
        Node<E> prev;

        /**
         * Constructor for a new node object
         * @param value The object to be held in the node
         * @param next The next node that this.node will link to
         * @param prev The previous node that this.node will link to
         */
        Node(E value, Node next, Node prev){
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        /**
         * Constructor that sets next and prev to null
         * @param value The object to be held in the node
         */
        Node(E value){
            this(value, null, null);
        }
    }

    /**
     * Private class to implement the Iterator interface
     */
    private class SortedIterator implements Iterator<E>{
        //Private fields for SortedIterator
        private Node<E> current;
        private boolean removeOK;

        /**
         * Constructor for SortedIterator that sets current to the head of the linked list
         */
        public SortedIterator(){
            current = head;
            removeOK = false;
        }

        /**
         * Method to check to see if there is another object to be iterated over
         * @return boolean - True if there is another object to iterate over, otherwise false
         */
        public boolean hasNext(){
            return(current != tail.next);
        }

        /**
         * Method to get the next object that is held in the linked list
         * @return E - The next object in the Linked list
         * @throws NoSuchElementException if there are no objects to be iterated over
         */
        public E next() throws NoSuchElementException{
            if(!hasNext()){
                throw new NoSuchElementException("There is no element to iterate over");
            }
            E result = current.value;
            current = current.next;
            removeOK = true;
            return result;
        }

        /**
         * Method to remove an object that was just iterated over
         * @throws IllegalStateException if there is not an object to be removed
         */
        public void remove() throws IllegalStateException{
            if(!removeOK) {
                throw new IllegalStateException();
            }
            if(current.prev.prev == null){
                current.prev = null;
                head.next = current;
                head = current;
            } else{
                current.prev = current.prev.prev;
                current.prev.prev.next = current;
            }
            size--;
            removeOK = false;
        }
    }
}
