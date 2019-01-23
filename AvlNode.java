/**
 * This class implements nodes for an AVL binary search tree of integer values.
 * Created as part of an assignment for Foundations of Algorithms, a graduate-
 * level course at Johns Hopkins University.
 *
 * @author Joseph Scheidt
 * @version Programming Assignment 1
 */
public class AvlNode {

    AvlNode left;
    AvlNode right;
    int height;
    int key;
    
    /**
     * A constructor for the class, for a root node.
     *
     * @param k the integer key value for this node
     */
    public AvlNode(int k) {
        
        //intialize pointers to null, height to 1, key to paramater value
        left = null;
        right = null;
        height = 1;
        key = k;
        
    }


}