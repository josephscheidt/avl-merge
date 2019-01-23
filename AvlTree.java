/**
 * This class implements an AVL tree of integers, along with functions for 
 * constructing such a tree from a sorted array, sorting an AVL tree into 
 * an array, and merging two separate AVL trees.
 *
 * This implementation is intended to be a proof for hypotheses involving
 * asymptotic analysis of the given algorithms above, and as such is not
 * intended to be used for actual AVL tree implementation: fields are public,
 * getter and setter methods are eschewed, repeated values are unsupported,
 * etc.
 *
 * Created as part of an assignment for Foundations of Algorithms, a graduate-
 * level course at Johns Hopkins University.
 *
 * @author Joseph Scheidt
 * @version Programming Assignment 1
 */
public class AvlTree {
    
    //root node for the tree
    AvlNode root;
    
    //number of tree nodes
    int size;
    
    //array index for sorting tree to array
    private int arrayIndex;
    
    //comparison counter for algorithmic analysis
    static int comparisonCount = 0;
    
    /**
     * A simple function to return the height of a particular node.
     * This function helps avoid NullPointerExceptions.
     *
     * @param node the node whose height is measured
     * @return the height of the node
     */
    public static int height(AvlNode node) {
    
        if(node == null) {
            return 0;
        } else {
            return node.height;
        }
    
    }
    
    /**
     * A simple function to return the balance of a particular node.
     * This function helps avoid NullPointerExceptions.
     *
     * @param node the node whose balance is measured
     * @return the balance of the node
     */
    public static int balance(AvlNode node) {
        
        if(node == null) {
            return 0;
        } else {
            return height(node.right) - height(node.left);
        }
    
    
    }
    
    /**
     * A simple function to return the maximum value of a tree.
     *
     * @return the maximum value of a tree
     */
    public int max() {
    
        AvlNode rightMost = root;
        
        
        while(rightMost.right != null) {
        
            //increase comparison counter
            comparisonCount++;
            
            //traverse towards right of tree
            rightMost = rightMost.right;
        }
        
        //return max value
        return rightMost.key;
    
    }
    
    /**
     * A simple function to return the minimum value of a tree.
     *
     * @return the minimum value of a tree
     */
    public int min() {
    
        AvlNode leftMost = root;
        
        
        while(leftMost.left != null) {
        
            //increase comparison counter
            comparisonCount++;
            
            //traverse towards right of tree
            leftMost = leftMost.left;
        }
        
        //return min value
        return leftMost.key;
    
    }
    
    
    
    /**
     * A constructor for the class. Initializes a one node tree.
     *
     * @param key the integer key value for the root node.
     */
    public AvlTree(int key) {
       
        root = new AvlNode(key);
        size = 1;
       
    }
    
    /**
     * A constructor for the class. Takes a sorted integer array and builds
     * a complete AVL tree in O(n) time, using divide and conquer and
     * recursion on smaller arrays to assign each value to a node.
     *
     * This is the first of three experimental methods.
     *
     * @param data the integer array to transform into an AVL tree
     */
    public AvlTree(int[] data) {
    
        //initialize size
        size = 1;
        
        //initialize root using helper to find correct (balanced) value
        root = buildTree(data, 0, data.length - 1);
        
        //calculate height
        root.height = Math.max(height(root.left), height(root.right)) + 1;
    
    }
    
    /* a helper function, called recursively, to build an AVL tree from an
     * array of integers. It takes the middle value of the sorted array,
     * assigns it to a node, then calls itself on the left half of the array
     * and the right half of the arrary until each value is assigned a node.
     * Since each value is visited once, this takes O(n) time.
     */
    private AvlNode buildTree(int[] data, int beg, int end) {
        
        //find middle of subarray
        int middle = beg + (end - beg) / 2;
        
        //create node from middle value
        AvlNode node = new AvlNode( data[middle] );
        
        //increase comparison count
        comparisonCount++;
        
        //assign remaining values to left and right subtrees
        if(beg < middle) {
            
            size++;
            node.left = buildTree(data, beg, middle - 1);
            
        }
        
        //increase comparison count
        comparisonCount++;
        
        if(middle < end) {
            
            size++;
            node.right = buildTree(data, middle + 1, end);
            
        }
        
        //calcuate node's height
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        
        //return complete node
        return node;
        
    }
    
    /**
     * A method to turn an AVL tree into a sorted array of integers. Using
     * recursion, it goes in the order of left subtree, current node, right
     * subtree. Since each node is visited once, this takes O(n) time.
     *
     * This is the second of three experimental methods.
     *
     * @return a sorted integer array
    */
    public int[] treeToArray() {
        
        //initalize return array using size of tree
        int[] returnArray = new int[size];
        
        //initialize index and call recursive function on root
        arrayIndex = 0;
        
        sortTree(root, returnArray);
        
        return returnArray;
        
    }
    
    /* A helper function to recursively store nodes of the tree in the array.
     * The index keeps track of where each element should go in the array.
     * The order is left child, node, right child, to successfully build
     * a sorted array.
     */
    private void sortTree(AvlNode node, int[] treeArray) {
        
        //add 1 to comparison counter
        comparisonCount++;
        
        //add left subtree to array
        if(node.left != null) {
        
            sortTree(node.left, treeArray);
        
        }
        
        //add this node's key value to array and increase index
        treeArray[arrayIndex] = node.key;
        arrayIndex++;
        
        //add 1 to comparison counter
        comparisonCount++;
        
        //add right subtree to array
        if(node.right != null) {
        
            sortTree(node.right, treeArray);
        
        }
    
    }
    
    /**
     * A method to insert a key value into the tree. After the new node is
     * created, the method bubbles back up the path of the insert, to rotate
     * nodes as necessary in order to maintain the balanced properties of the
     * tree.
     *
     * @param node The node to test for insertion
     * @param key The key value for the node
     * @return the updated node
     */
    public AvlNode insert(AvlNode node, int key) {
        
        //increase comparison counter
        comparisonCount++;
        
        //create new node if empty, otherwise send to right or left child
        if(node == null) {
            node = new AvlNode(key);
            //increase size of tree
            size++;
            return node;
        } else if(node.key > key) {
            node.left = insert(node.left, key);
        } else if(node.key < key) {
            node.right = insert(node.right, key);
        } else {
            return node;
        }
        
        //update height for all parent nodes
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        
        //check balance of children
        int balance = balance(node);
        
        //increase comparison counter
        comparisonCount++;
        
        //rebalance tree if unbalanced
        
        //two cases if tree is left heavy
        if (balance < -1 && key < node.left.key) {
            return rotateRight(node);
        } else if (balance < -1 && key > node.left.key) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
            
        //two cases if tree is right heavy
        } else if (balance > 1 && key > node.right.key) {
            return rotateLeft(node);
        } else if (balance > 1 && key < node.right.key) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        //otherwise return updated node
        return node;
    
    }
    
    /*
     * Private helper function for rotating nodes to the left and rebalancing
     * tree. Temporary pointers are used in order to facilitate switching of 
     * node positions and preserving subtrees.
     */
    private AvlNode rotateLeft(AvlNode node) {
        
        //temporary pointers
        AvlNode newParent = node.right;
        AvlNode newLeftRightSubtree = newParent.left;
        
        //rotate nodes
        newParent.left = node;
        node.right = newLeftRightSubtree;
        
        //update heights
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newParent.height = Math.max(height(newParent.left), height(newParent.right)) + 1;
    
        //return new parent node
        return newParent;
    }

    /*
     * Private helper function for rotating nodes to the right  and rebalancing
     * tree. Temporary pointers are used in order to facilitate switching of 
     * node positions and preserving subtrees.
     */    
    private AvlNode rotateRight(AvlNode node) {
        
        //temporary pointers
        AvlNode newParent = node.left;
        AvlNode newRightLeftSubtree = newParent.right;
        
        //rotate nodes
        newParent.right = node;
        node.left = newRightLeftSubtree;
        
        //update heights
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newParent.height = Math.max(height(newParent.left), height(newParent.right)) + 1;
    
        //return new parent node
        return newParent;
   
    }
    
    /**
     * A method for merging two AVL trees of size m and n. If the range of values
     * in the AVL trees do not overlap, the method sorts each to an array, merges
     * the arrays, and transforms the array back into an AVL tree in O(m+n) time.
     * Otherwise, the smaller of the two trees is sorted into an array, and the
     * elements of that array are inserted one by one into the other tree, which
     * takes O(n lg(m + n)) time.
     *
     * This is the main experimental method and third of three.
     *
     * @param tree1 The first tree to be merged
     * @param tree2 The second tree to be merged
     * @return The merged AVL tree
     */
    public static AvlTree mergeTrees(AvlTree tree1, AvlTree tree2) {
    
        //increase comparison counter
        comparisonCount++;
        
        //check if tree conditions are right for simple sorted array merge
        if(tree1.max() < tree2.min() || tree2.max() < tree1.min()) {
        
            //initialize returnArray and index counter
            int[] mergeArray = new int[tree1.size + tree2.size];
            int index = 0;
            
            int[] tree1Array = tree1.treeToArray();
            int[] tree2Array = tree2.treeToArray();
            
            for(int i = 0; i < tree1Array.length; i++) {
                //increase comparison counter
                comparisonCount++;
            
                //copy tree1 to returnArray
                mergeArray[i] = tree1Array[i];
                index++;
            }
            
            for(int i = 0; i < tree2Array.length; i++) {
                //increase comparison counter
                comparisonCount++;
                
                //copy tree2 to returnArray
                mergeArray[i + index] = tree2Array[i];
            }
            
            //return tree built from mergeArray
            return new AvlTree(mergeArray);
            
        }
        
        //otherwise find smaller of two trees and insert element by element
        AvlTree smaller;
        AvlTree larger;
        
        //increase comparison counter
        comparisonCount++;
        
        //find smaller and larger of trees
        if(tree1.size < tree2.size) {
            smaller = tree1;
            larger = tree2;
        } else {
            smaller = tree2;
            larger = tree1;
        }
        
        //sort smaller tree into array
        int[] insertArray = smaller.treeToArray();
        
        //insert smaller tree's elements into larger tree
        for(int i = 0; i < insertArray.length; i++) {
        
            larger.root = larger.insert(larger.root, insertArray[i]);
        
        }
    
        //return completely merged tree
        return larger;
    
    }
    
    
    
    
    /**
     * A testing method for proof of working implementation, which will
     * be ran and recorded on video.
     *
     */
    public static void videoTest() {
        
        //reset comparison counter
        comparisonCount = 0;
        
        //build tree with insert function
        AvlTree test1 = new AvlTree(1);
        
        test1.root = test1.insert(test1.root, 3);
        test1.root = test1.insert(test1.root, 5);
        test1.root = test1.insert(test1.root, 7);
        test1.root = test1.insert(test1.root, 9);
        test1.root = test1.insert(test1.root, 11);
        test1.root = test1.insert(test1.root, 13);
        test1.root = test1.insert(test1.root, 15);
        test1.root = test1.insert(test1.root, 17);
        test1.root = test1.insert(test1.root, 19);
        
        //sort tree to array and print
        int[] printarray = test1.treeToArray();
        
        System.out.println("Testing insert function - Tree 1");
        System.out.print("Sorted tree: ");
        
        for(int i = 0; i < printarray.length; i++) {
        
            System.out.print(printarray[i] + " ");
        
        }  
        
        System.out.println();
        System.out.println("Number of comparisons needed to build and sort: " 
                           + comparisonCount);
        System.out.println();
        
        //reset comparison counter
        comparisonCount = 0;
        
        //build tree from array
        int[] testArray = new int[] {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};
        
        AvlTree test2 = new AvlTree(testArray);
        
        //sort tree into array and print
        printarray = test2.treeToArray();
        
        System.out.println("Testing array-to-tree function - Tree 2");
        System.out.print("Sorted tree: ");
        
        for(int i = 0; i < printarray.length; i++) {
        
            System.out.print(printarray[i] + " ");
        
        }
        
        System.out.println();
        System.out.println("Number of comparisons needed to build and sort: " 
                           + comparisonCount);
        System.out.println();
        
        //reset comparison counter
        comparisonCount = 0;
        
        //merge two previous trees, sort to array and print
        System.out.println("Testing merge function - Tree 1 + Tree 2");
        System.out.print("Sorted tree: ");
        
        AvlTree merged = mergeTrees(test1, test2);
        
        printarray = merged.treeToArray();
        
        for(int i = 0; i < printarray.length; i++) {
        
            System.out.print(printarray[i] + " ");
        
        }
        
        System.out.println();
        
        System.out.println("Number of comparisons needed to merge and sort: " 
                           + comparisonCount);
        
                
    }
    
    /**
     * A method to help test hypothetical asymptotic analyses for the three
     * experimental methods: building a tree from a sorted array, sorting a
     * tree into an array, and merging two trees. Given data set size k, the
     * three types of tree merges tested are:
     *
     * Tree 1 - {1 to k/2}; Tree 2 - {k/2 to k} (no overlap)
     * Tree 1 - {1 to .2k}; Tree 2 - {.2k to k} (unbalanced set sizes)
     * Tree 1 - {1,3,5,7,...k-1}; Tree 2 - {2,4,6,8,...k} (balanced sets)
     *
     * In addition, build and sort to array functions are tested for trees
     * of size k.
     *
     * Number of comparisons needed for each function will be printed to the 
     * console.
     *
     * @param k The size of the data set to be tested (must be multiple of 10)
     */
    public static void treeTest(int k) {
    
        //test array to tree and sort tree to array functions
        int[] testArray = new int[k];
        
        for(int i = 0; i < k; i++) {
           testArray[i] = i;
        }
        
        //reset comparison count
        comparisonCount = 0;
        
        AvlTree testTree = new AvlTree(testArray);
        
        System.out.println("Number of comparisons needed to build tree from sorted " +
                           "array of size " + k + " is " + comparisonCount);
        System.out.println();
        
        //reset comparison count
        comparisonCount = 0;
        
        testArray = testTree.treeToArray();
        
        System.out.println("Number of comparisons needed to sort tree of size " +
                           + k + " into an array is " + comparisonCount);
        System.out.println();
        
        //run test of tree merge with type 1 datasets
        int[] type1left = new int[k/2];
        int[] type1right = new int[k/2];
        
        for(int i = 0; i < k/2; i++) {
           type1left[i] = i;
           type1right[i] = i + k/2;
        }
        
        AvlTree left = new AvlTree(type1left);
        AvlTree right = new AvlTree(type1right);
        
        //reset comparison counter
        comparisonCount = 0;
        
        mergeTrees(left, right);
        
        System.out.println("Number of comparisons needed to merge type 1 trees of combined size " +
                           + k + "  is " + comparisonCount);
        System.out.println();
        
        //run test of trees with type 2 datasets
        int[] type2left = new int[k*2/10 + 1];
        int[] type2right = new int[k*8/10 - 1];
        
        for(int i = 0; i < k*2/10; i++) {
            type2left[i] = i;
        }
        //add last element
        type2left[k*2/10] = k;  
        
        for(int i = 0; i < k*8/10 - 1; i++) {
            type2right[i] = i + k*2/10;
        }
        
        left = new AvlTree(type2left);
        right = new AvlTree(type2right);
        
        //reset comparison counter
        comparisonCount = 0;
        
        mergeTrees(left, right);
        
        System.out.println("Number of comparisons needed to merge type 2 trees of combined size " +
                           + k + "  is " + comparisonCount);
        System.out.println();
        
        //run test of type 3 datasets
        int[] type3left = new int[k/2];
        int[] type3right = new int[k/2];
        
        for(int i = 0; i < k/2; i++) {
            type3left[i] = 2*i+1;
            type3right[i] =2*i;
        }
        
        left = new AvlTree(type3left);
        right = new AvlTree(type3right);
        
        //reset comparison counter
        comparisonCount = 0;
        
        mergeTrees(left, right);
        
        System.out.println("Number of comparisons needed to merge type 3 trees of combined size " +
                           + k + "  is " + comparisonCount);
        System.out.println();       
    
    }
    
    /**
     * The application method.
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
    
      //run test of functions to show proper insertion and sorting
      videoTest();
    
    }
    
}