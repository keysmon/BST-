import java.util.*;

//
// An implementation of a binary search tree.
//
// This tree stores both keys and values associated with those keys.
//
// More information about binary search trees can be found here:
//
// http://en.wikipedia.org/wiki/Binary_search_tree
//
// Note: Wikipedia is using a different definition of
//       depth and height than we are using.  Be sure
//       to read the comments in this file for the
//	 	 height function.
//
class BinarySearchTree <K extends Comparable<K>, V>  {

	public static final int BST_PREORDER  = 1;
	public static final int BST_POSTORDER = 2;
	public static final int BST_INORDER   = 3;

	// These are package friendly for the TreeView class
	BSTNode<K,V>	root;
	int		count;

	int		findLoops;
	int		insertLoops;

	public BinarySearchTree () {
		root = null;
		count = 0;
		resetFindLoops();
		resetInsertLoops();
	}

	public int getFindLoopCount() {
		return findLoops;
	}

	public int getInsertLoopCount() {
		return insertLoops;
	}

	public void resetFindLoops() {
		findLoops = 0;
	}
	public void resetInsertLoops() {
		insertLoops = 0;
	}

	//
	// Purpose:
	//
	// Insert a new Key:Value Entry into the tree.  If the Key
	// already exists in the tree, update the value stored at
	// that node with the new value.
	//
	// Pre-Conditions:
	// 	the tree is a valid binary search tree
	//
	public void insert (K k, V v) {
		root = insert(root, k, v);
	}
	
	public BSTNode<K,V> insert(BSTNode<K,V> node, K k, V v) {
		if (node == null) {
			
			node = new BSTNode<K,V>(k,v);
			count++;
			return new BSTNode<K,V> (k,v);
		}
		insertLoops++;
		if(node.key==k) {
			node.value = v;
			return node;
		}else if (node.key.compareTo(k)<0) {
			node.right = insert(node.right, k, v);
			return node;
		}else if (node.key.compareTo(k)>0) {
			node.left = insert (node.left, k,v);
			return node;
		}
		
		return node;
		
	}
	//
	// Purpose:
	//
	// Return the value stored at key.  Throw a KeyNotFoundException
	// if the key isn't in the tree.
	//
	// Pre-conditions:
	//	the tree is a valid binary search tree
	//
	// Returns:
	//	the value stored at key
	//
	// Throws:
	//	KeyNotFoundException if key isn't in the tree
	//
	public V find (K key) throws KeyNotFoundException {
		return find(root,key).value;
	}
	
	public BSTNode<K,V> find (BSTNode<K,V> current, K key) throws KeyNotFoundException{
		
		if (current==null) {
			throw new KeyNotFoundException();
		}else {
			findLoops++;
			if (current.key.compareTo(key)==0) {
				return current;
			}else if (current.key.compareTo(key)<0) {
				return find(current.right,key);
			}else {
				return find(current.left, key);
			}
		}
	}
	//
	// Purpose:
	//
	// Return the number of nodes in the tree.
	//
	// Returns:
	//	the number of nodes in the tree.
	public int size() {
		return count;
	}

	//
	// Purpose:
	//	Remove all nodes from the tree.
	//
	public void clear() {
		root = null;
	}
	

	//
	// Purpose:
	//
	// Return the height of the tree.  We define height
	// as being the number of nodes on the path from the root
	// to the deepest node.
	//
	// This means that a tree with one node has height 1.
	//
	// Examples:
	//	See the assignment PDF and the test program for
	//	examples of height.
	//
	public int height() {
		return height(root);
	}
	public int height(BSTNode<K,V> current) {
		if (current == null) {
			return 0;
		}else {
			if (height(current.left)<height(current.right)) {
				return 1+height(current.right);
			
			}else {
				return 1+height(current.left);
			}
	
		}  
	}

	//
	// Purpose:
	//
	// Return a list of all the key/value Entrys stored in the tree
	// The list will be constructed by performing a level-order
	// traversal of the tree.
	//
	// Level order is most commonly implemented using a queue of nodes.
	//
	//  From wikipedia (they call it breadth-first), the algorithm for level order is:
	//
	//	levelorder()
	//		q = empty queue
	//		q.enqueue(root)
	//		while not q.empty do
	//			node := q.dequeue()
	//			visit(node)
	//			if node.left != null then
	//			      q.enqueue(node.left)
	//			if node.right != null then
	//			      q.enqueue(node.right)
	//
	// Note that we will use the Java LinkedList as a Queue by using
	// only the removeFirst() and addLast() methods.
	//
	public List<Entry<K,V>> entryList() {
		List<Entry<K, V>> 			l = new LinkedList<Entry<K,V>>();
		LinkedList<BSTNode<K,V>> list = new LinkedList<BSTNode<K,V>>();
		list.addLast(root);
		
		while (!list.isEmpty()) {
			BSTNode<K,V> temp  = list.removeFirst();
			l.add(new Entry<K,V>(temp.key,temp.value));
			if (temp.left!=null) {
				list.addLast(temp.left);
				
			}
			if(temp.right!=null)
				list.addLast(temp.right);
			
		}
		return l;
	}

	//
	// Purpose:
	//
	// Return a list of all the key/value Entrys stored in the tree
	// The list will be constructed by performing a traversal 
	// specified by the parameter which.
	//
	// If which is:
	//	BST_PREORDER	perform a pre-order traversal
	//	BST_POSTORDER	perform a post-order traversal
	//	BST_INORDER	perform an in-order traversal
	//
	public List<Entry<K,V> > entryList (int which) {
		List<Entry<K,V> > l = new LinkedList<Entry<K,V> >();
		
		if(which== BST_INORDER) {
			doInOrder(root, l);
			
		}else if(which ==BST_POSTORDER) {
			doPostOrder(root, l);
			
		}else if(which ==BST_PREORDER) {
			doPreOrder(root, l);
		}
		return l;
	}

	
	// Your instructor had the following private methods in his solution:
	private void doInOrder (BSTNode<K,V> n, List <Entry<K,V> > l) {
		if(n == null) { 
			return;
		}
		
		doInOrder(n.left,l);
		
		l.add(new Entry<K, V>(n.key, n.value));
	
		doInOrder(n.right,l);
		
	}
	private void doPreOrder (BSTNode<K,V> n, List <Entry<K,V> > l) {
		if(n == null) {
			return;
		}
		l.add(new Entry<K, V>(n.key, n.value));
		
		doPreOrder(n.left, l);
		
		doPreOrder(n.right,l);
	}
	 
	private void doPostOrder (BSTNode<K,V> n, List <Entry<K,V> > l) {
		if(n == null) {
			return;
		}
		doPostOrder(n.left, l);
		
		doPostOrder(n.right, l);
		
		l.add(new Entry<K, V>(n.key, n.value));
	}
	
}
