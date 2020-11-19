
/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	AVLNode root;
	private int count = 0;//counts the number of nodes in the tree
	AVLNode max = new AVLNode(0,"0",0);//pointer to the node with minimum key in the tree
	AVLNode min = new AVLNode(0,"0",0);;//pointer to the node with maximum key in the tree
  
	/**
	
  * public boolean empty()
	   * complexity: O(1)
	   * returns true if and only if the tree is empty
	   * 
	   */
	public boolean empty()
	{
    if(this.root == null)
    {
    	return true;
    }
	return false; 
	}

 /**
   * public String search(int k)
   * complexity: O(log n)
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null

   */
	public String search(int k)
  {
	AVLNode x = this.root;
	while (x != null)
	{
	if (x.getKey() == k)
		{
		return x.getValue();
		}
	else if (x.getKey() > k)
		{
		x = x.left;
		}
	else
		{
		x = x.right;
		}
	}
	 return null;
  }

  /**
   * public int insert(int k, String i)
   * complexity: O(log n)
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   * change the min max count accordingly
   */
   public int insert(int k, String i)
   {
	count++; // count how many nodes
    AVLNode newNode = new AVLNode(k, i, 0); // define the input as a node
	if (this.getRoot() == null) // check if the tree is empty
	{
	this.root = newNode; // define the new root
	min = newNode;
	max = newNode;
	return 0;
	}
	if (k > max.key) // update the max
		max = newNode;
	if (k < min.key) // update the min
		min = newNode;
	int side = -1; // for the parent side
	AVLNode rootNow = this.root;
	while (rootNow.getKey() != -1)
	{
	if (rootNow.getKey() == newNode.getKey()) // we have the same key
	{
		count--; // change the number of nodes 
		return -1;
	}
	newNode.parent = rootNow; // define the tmp root to be parent of the new node
	if (rootNow.getKey() > newNode.getKey()) // check where to send the new node
	{ 
		rootNow = rootNow.left;   // sending the new node to the left
		side = 0;
	}
	else
	{
		rootNow = rootNow.right; // sending the new node to the right
		side = 1;
	}
	}
	if (side == 0) // check if the father from right or left
	{
		newNode.parent.setLeft(newNode); // set the new node as a right sun
	}
	else
	{
		newNode.parent.setRight(newNode); // set the new node as a left sun
	}  
      return rebalnceInsert(newNode);
   } 
  
   /**
    * public int rebalnceInsert(AVLNode node)
    * complexity: O(log n)
    * get the new node we just inserted
    * check balance factor and rotate accordingly
    * post: return number of balance action
    */
   private int rebalnceInsert(AVLNode node)
   {
	   int counter = 0; // count how many rotation / promote
	   int balanceFactorLeft = 0;
	   int balanceFactorRight = 0;
	   if (node.parent != null)
	   {
		   if (node.parent.left == node) // son from left
		   {   
		   balanceFactorLeft = node.parent.rank - node.rank;
		   balanceFactorRight = node.parent.rank - node.parent.right.rank;
		   }
		   else // son from right
		   {
			  balanceFactorLeft = node.parent.rank - node.parent.left.rank;
			  balanceFactorRight = node.parent.rank - node.rank; 
		   }
	   }
	   else
	   {
			  balanceFactorLeft = node.rank - node.left.rank;
			  balanceFactorRight = node.rank - node.right.rank; 
	   }

	   // case 1: 		// 0 1 or 1 0 		promote father
	   if ((balanceFactorLeft == 0 && balanceFactorRight == 1) || (balanceFactorRight == 0 && balanceFactorLeft == 1))
	   {
		   node.parent.rank++;
		   counter += 1 + rebalnceInsert(node.parent);
	   }
	   // case 2: 		// 0 2 or 2 0 		single or double rotate
	   if (balanceFactorLeft == 0 && balanceFactorRight == 2)
	   {
		   int balanceL = node.rank - node.left.rank;
		   int balanceR = node.rank - node.right.rank;
		   if (balanceR == 2 && balanceL == 1) // case 2.1: single rotate right
		   {
			   rightRotate(node.parent);
			   return 2;
		   }
		   if (balanceR == 1 && balanceL == 1) // can happen after join
		   {
			 node = rightRotate(node.parent);
			  return 2 + rebalnceInsert(node);
		   }
		   else // case 2.2: double rotate - left -> right
		   {
			   node = leftRotate(node);
			   rightRotate(node.parent);
			   return 5;
		   }
	   }	   
	   if (balanceFactorRight == 0 && balanceFactorLeft == 2)
		{
		   int balanceR = node.rank - node.right.rank;
		   int balanceL = node.rank - node.left.rank;
		   if (balanceR == 1 && balanceL == 2) // case 2.3: single rotate left
		   {
			leftRotate(node.parent); // maybe node.parent
			return 2;
		   }
		   if (balanceR == 1 && balanceL == 1)
		   {
			   node = leftRotate(node.parent);
			  return 2 + rebalnceInsert(node);	   
			   //leftRotate(node.parent);
		   }
		   else // case 2.4: double rotate - right -> left
		   {
			   node = rightRotate(node);
			   leftRotate(node.parent);
			   return 5;
		   }
		}
	   return counter;
   }
   
   /**
    * public AVLNode rightRotate (AVLNode node)
    * complexity: O(1)
    * @param get the new node which inserted
    * changing the pointer as we learn
    * changing the rank of the nodes
    * @return the new parent of the subtree
    */
     private AVLNode rightRotate (AVLNode node)
   {
	   AVLNode parent = node.parent;
	   AVLNode x = node.left;
	   x.parent = parent;
	   node.left = x.right;
	   x.right.parent = node;
	   x.right = node;
	   node.parent = x;
	   if (x.parent == null)
		   this.root = x;
	   else
	   {
		   if (parent.key < x.key)
			   parent.right = x;
		   else
			   parent.left = x;
	   }
	   node.setHeight(Math.max(node.left.rank, node.right.rank) +1);
	   x.setHeight(Math.max(x.left.rank, x.right.rank)+1);
	   return x;
   }

   /**
    * public AVLNode leftRotate (AVLNode node)
    * complexity: O(1)
    * @param get the new node which inserted
    * changing the pointer as we learn
    * changing the rank of the nodes
    * @return the new parent of the subtree
    */ 
     private AVLNode leftRotate (AVLNode node) 
   {
	   AVLNode parent = node.parent;	
	   AVLNode x = node.right;
	   x.parent = parent;
	   node.right = x.left;
	   x.left.parent = node;
	   x.left = node;
	   node.parent = x;
	   if (x.parent == null)
		   this.root = x;
	   else
	   {
		   if (parent.key < x.key)
			   parent.right = x;
		   else
			   parent.left = x;
	   }
	   node.setHeight(Math.max(node.left.rank, node.right.rank) +1);
	   x.setHeight(Math.max(x.left.rank, x.right.rank)+1);
	   return x;
   }
   	
   /**
   * public static boolean isLeaf(AVLNode node)
   * complexity: O(1)
   * public static boolean isLeaf(AVLNode node)
   * return true if the node is a leaf (if is two children's are virtual
   */
     private static boolean isLeaf(AVLNode node)
   {
	   if ((node.getLeft().getKey() == -1) && (node.getRight().getKey() == -1))
	   {
		   return true;
	   }
	   return false;
   }
  
   /**
   * public AVLNode findSuccessor(AVLNode node)
   * complexity: O(log n)
   * return the closest key that is bigger then the node we got
   * if the node we got is the biggest its return null
   */
   private AVLNode findSuccessor(AVLNode node)
   { 
	   if (node.key == max.key)
		   return null;
	   if (node.right.key != -1)
	   {
		   node = node.right;
		   while (node.left.key != -1)
		   {
			   node = node.left;
		   }
		   return node;
	   }
	   AVLNode tmp = node.parent;
	   while(tmp.left != node)
	   {
		   node = tmp;
		   tmp = tmp.parent;
	   }
	   return tmp;
	}

   /**
    * find the maximum of the given node O (log n)
   * @param get the AVLNode node
   * @return the maximum key of the node subtree
   */
   private static AVLNode findMaximum(AVLNode node) // help function for predecessor
   {
	   while (node.right.key != -1)
		   node = node.right;
	   return node;
   }
   
   /** 
 * public AVLNode  findPredecessor(AVLNode node)
   * return the closest key that is smaller then the node we got
   * if the node we got is the smallest its return null
   */
   private AVLNode findPredecessor(AVLNode node)
   {
	   if (node.key == min.key) // check if its the max node
		   return null;
	   if (node == this.root) // if the node is the root
	   {  // go right and all the way left
			node = node.right;
			while (node.left.key != -1)
			{
				  node = node.left;
			}
		   return node;
	   }
	   if (node.left.key != -1)
		   return findMaximum(node.left);
	   else
	   {
		   if (node.parent.right == node)
			   return node.parent;
		   else
		   {
			   AVLNode helpfind = node.parent;
			   while (helpfind.parent != null)
			   {
				   if (helpfind.parent.left == helpfind)
					   return helpfind;
				   helpfind = helpfind.parent;   
			   }
			   return helpfind;
		   }
	   }
	   
   }

   /**
   * public int delete(int k)
   * complexity: O(log n)
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if (k <= 0)
		   return -1;
	   AVLNode node = this.root;
	   if (node == null)
		   return -1;
	   count--; // update the number of nodes
	   if (count == 1)
	   {
		   this.root = null;
		   return 0;
	   }
	   while (node.key != -1)
	   {
		   if (node.getKey() == k) // we found the location
		   {
			   if (k == max.key)//update if the max has change
				   max = findPredecessor(node);
			   if (k == min.key) // update if the min has change
			   {
				   min = findSuccessor(node);
			   }
			   if (isLeaf(node)) // the node is a leaf
			   {
				   return deleteLeaf(node);
			   }
			   if (((node.left.key != -1) && (node.right.key == -1)) || ((node.right.key != -1) && (node.left.key == -1)))// unary node 
			   {
				   return deleteUnary(node);
			   }
			   // the node have 2 children 
			   return deleteBinary(node);
		   }
		   if (node.getKey() > k)
			   node = node.left;
		   else
			   node = node.right;
	       }
	   count++; // if we didnt find the node
	   return -1;
   }
   
   /**
    * 
    * @param the node is a leaf
    * Complexity O(log n)
    * @return how many demote or rotate we did by using rebalnceDelete on the node parent
    */
   private int deleteLeaf(AVLNode node)
   {
	   if (node == this.root) // the node is the root and its the only node
	   {
		   this.root = null; 
		   return 0; 
	   }
		  if (node.parent.getLeft() == node) // his parent from the right
			  node.parent.setLeft(new AVLNode(-1,null,-1)); // set new virtual  
		  else // his parent from the left
			  node.parent.setRight(new AVLNode(-1,null,-1));
		  return rebalnceDelete(node.parent);
   }
   
   /**
    * 
    * @param the node is a unary node
    * Complexity O(log n)
    * @return how many demote or rotate we did by using rebalnceDelete on the node parent
    */
   private int deleteUnary(AVLNode node)
   {
	   if (node == this.root)
	   {
		   if (node.right.key != -1)
		   {
			   this.root = node.right;
			   node.right.parent = null;
		   }
		   else
		   {
			   this.root = node.left;
			   node.left.parent = null;
		   }
		   return 0;
	   }
	   else
	   {
	   if (node.parent.getLeft() == node) // his parent from the right
	   {
		   if (node.left.key != -1)
		   {
			   node.parent.setLeft(node.left);
			   node.left.parent = node.parent;
		   }
		   else
		   {
			   node.parent.setLeft(node.right);
			   node.right.parent = node.parent;
		   }
	   }
	   else // his parent from the left
	   {
		   if (node.left.key != -1)
		   {
			   node.parent.setRight(node.left);
			   node.left.parent = node.parent;
		   }
		   else
		   {
			   node.parent.setRight(node.right);
			   node.right.parent = node.parent;
		   }
	   }
	   }
	   return rebalnceDelete(node.parent);
	   
   }

   /**
    * 
    * @param the node have 2 children
    *  Complexity O(log n)
    *  the node will be replace by his successor
    * @return how many demote or rotate we did by using rebalnceDelete on the node parent
    */
   private int deleteBinary(AVLNode node)
   {
		AVLNode successor = findSuccessor(node);
		 node.key = successor.getKey(); // switching the successor and the node
		 node.info = successor.getValue(); // switching the successor and the node
		if (isLeaf(successor)) // the successor is a leaf
		{
			return deleteLeaf(successor);
		}
		 // the successor is unary
		return deleteUnary(successor);
	 }

   
   /**
    * private int rebalnceDelete(AVLNode node)
    * complexity: O(log n)
    * get the parent of the new node which just deleted
    * check balance factor and rotate accordingly
    * post: return number of balance action
    */
   private int rebalnceDelete(AVLNode node)
   {
	   int counter = 0; // count the number of demote and rotate
 	   int balanceFactorLeft = 0;
 	   int balanceFactorRight = 0;
 	   if (node != null)
 	   {
 		   balanceFactorLeft = node.rank - node.left.rank;
 		   balanceFactorRight = node.rank - node.right.rank;

 	   // case 1: 		// 2 2   			demote node
 	   if (balanceFactorLeft == 2 && balanceFactorRight == 2)
 	   {
 		   node.rank--;
 		   counter += 1 + rebalnceDelete(node.parent);//problem solved or moved up
 	   }
 	   // case 2: 		// 3 1 or 1 3 		single or double rotate
 	   if (balanceFactorLeft == 3 && balanceFactorRight == 1)
 	   {
 		   int balanceL = node.right.rank - node.right.left.rank;
 		   int balanceR = node.right.rank - node.right.right.rank;
 		   if (balanceR == 1 && balanceL == 1)// case 2.a: single rotate left
 		   {
 			   leftRotate(node);
 			   return 3;
 		   }
 		   if (balanceR == 1 && balanceL == 2) // case 2.b: single rotate left
 		   {
 			   node = leftRotate(node);
 			   counter += 3 + rebalnceDelete(node.parent);//problem solved or moved up
 		   }
 		   if (balanceR == 2 && balanceL == 1) // case 2.c: double rotate: left -> right
 		   {
 			   node.right = rightRotate(node.right);
 			   node = leftRotate(node);	
 			   counter += 6 + rebalnceDelete(node.parent);//problem solved or moved up
 		   }
 	   }	   
 	   if (balanceFactorRight == 3 && balanceFactorLeft == 1)
 		{
 		   int balanceR = node.left.rank - node.left.right.rank;
 		   int balanceL = node.left.rank - node.left.left.rank;
 		   if (balanceR == 1 && balanceL == 1)// case 2.d: single rotate right
 		   {
 			   rightRotate(node);
 			   return 3;
 		   }
 		   if (balanceR == 1 && balanceL == 2) // case 2.d: single rotate right
 		   {
 			   rightRotate(node);
 			   counter += 3 + rebalnceDelete(node.parent);//problem solved or moved up
 		   }
 		   if (balanceR == 1 && balanceL == 2) // case 2.e: double rotate - left -> right
 		   {
 			   node.left = leftRotate(node.left);
 			   node = rightRotate(node);
 			   counter += 6 + rebalnceDelete(node.parent);//problem solved or moved up
 		   }
 		}
 		}
 	   return counter;
   }
   
   /**
    * public String min()
    * complexity: O(1)
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if (count == 0)
		   return null;
	   return min.getValue();
   }
 
   /**
    * public String max()
    * complexity: O(1)
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if (count == 0)
		   return null;
	   return max.getValue();
   }


  /**
   * public int[] keysToArray()
   * complexity: O(n)
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   public int[] keysToArray()
   {	
 	  int[] array = new int[count];
       recKeysToArray(array, root, 0);
       return array;
   }
   
   /**
    * public int[] keysToArray()
    * Recursively fills an array which contains all keys in subtree of node,
    * sorted by their respective keys,
    * or an empty array if the tree is empty.
    * Complexity: O(n)
    *
    * @param keysArray
    * @param node
    * @param index
    * @return
    */
   private int recKeysToArray(int[] keysArray, AVLNode node, int index) {
       if (node.getKey()!=-1) {
           index = recKeysToArray(keysArray, node.left, index);
           keysArray[index++] = node.key;
           index = recKeysToArray(keysArray, node.right, index);
       }
       return index;
   }  

  /**
   * public String[] infoToArray()
   * complexity: O(n)
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
   public String[] infoToArray()

   {
 	  String[] array = new String[count];
       recInfoToArray(array, root, 0);
       return array;
   }
  

   /**
  * private int recInfoToArray(String[] infoArray, AVLNode node, int index)
    * Recursively fills an array which contains all info in subtree of node,
    * sorted by their respective keys,
    * or an empty array if the tree is empty.
    * Complexity: O(n)
    *
    * @param infoArray
    * @param node
    * @param index
    * @return
    */
   private int recInfoToArray(String[] infoArray, AVLNode node, int index) {
       if (node.getKey()!=-1) {
           index = recInfoToArray(infoArray, node.left, index);
           infoArray[index++] = node.getValue();
           index = recInfoToArray(infoArray, node.right, index);
       }
       return index;
   }

   
     /**
    * public int getRoot()
    * complexity: O(1) 
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	   return this.root;
   }

   /**
    * public AVLNode nodeSearch(int k)
    * complexity: O(log n) 
    * @param get a key from the keys in the tree
    * @return the node that hold this key or null if its not in the tree
    */
   public AVLNode nodeSearch(int k)
	  {
		AVLNode node = this.root;
		while (node != null)
		{
		if (node.key == k)
			return node;
		else
			if (node.key > k)
				node= node.left;
		else
			node = node.right;
		}
		 return null;
	  }
 
   /**
    * public string split(int x)
    * complexity: O(log n) 
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	* precondition: search(x) != null
    * postcondition: none
    */   
   public AVLTree[] split(int x)
   {
	   AVLTree[] arr = new AVLTree[2];
	   AVLTree tHelpSmall = new AVLTree();
	   AVLTree tHelpBig = new AVLTree();
	   AVLTree tSmall = new AVLTree();
	   AVLTree tBig = new AVLTree();
	   AVLNode node = nodeSearch(x);//get a pointer to node with key x
	   if (node == this.root) // if the given node is the root of the tree
	   {
		   tBig.root = node.right;
		   tSmall.root = node.left;
		   arr[0] = tBig;
		   arr[1] = tSmall;
		   return arr;
	   }
	   else
	   {
		   if (node.left.key != -1) 
		   {
			tSmall.root = node.left;
			tSmall.root.parent = null;
		   }
		   if (node.right.key != -1)
		   {   
		   	tBig.root = node.right;
			tBig.root.parent = null;
		   }
	   }
	   boolean big = false;
	   AVLNode helpNode = node.parent;
	   while (helpNode != null)
	   {
		   if (helpNode.key > node.key)
			   big = true;
		   else
			   big = false;

		   if (big == false)// node is his right child
		   {
			   node = helpNode;
			   if (node.left.key != -1) 
			   {
			   tHelpSmall.root = node.left;
			   tHelpSmall.root.parent = null;
			   }
			   helpNode = node.parent;
			   tSmall.join(node, tHelpSmall);
			   tHelpSmall.root=null;
		   }
		   else// node is his left child
		   {
			   node = helpNode;
			   if (node.right.key != -1) 
			   {
			   tHelpBig.root = node.right;
			   tHelpBig.root.parent = null;
			   }
			   helpNode = node.parent;
			   tBig.join(node, tHelpBig);
			   tHelpBig.root=null; 
		   }
	   }
	   arr[0] = tSmall;
	   arr[1] = tBig;
	   return arr; 
   }  
   
   /**
    * public static void updateRank(AVLNode node)
    * complexity: O(1) 
    * update the rank of a node. 
	* precondition: search(x) != null
    */   
   public static void updateRank(AVLNode node)
   {
		while (node != null)
		{
			node.rank = 1 + Math.max(node.left.rank, node.right.rank);
			node = node.parent;
		}		
   }
   
   /**
   
   * public int join(IAVLNode x, AVLTree t)
   * complexity: O(log n) 
   * joins t and x with the tree. 	
   * Returns the complexity of the operation (rank difference between the tree and t)
   * precondition: keys(x,t) < keys() or keys(x,t) > keys()
   * postcondition: none
   */ 
   public int join(IAVLNode x, AVLTree t)// 
   {	
	   AVLNode y = (AVLNode)x; 
	   this.count += t.count + 1; // add the nodes to the counter
	   if (t.root == null)
	   {
		   this.insert(y.key, y.info);
		   return this.root.rank + 1;
	   }
	   if (this.root == null)
	   {
		   this.root = t.root;
		   this.insert(y.key, y.info);
		   return this.root.rank + 1;
	   }
	   int dif; 
	   if (this.root.rank >= t.root.rank)
		   dif = this.root.rank - t.root.rank + 1; // rank difference
	   else
	   {
		   dif = t.root.rank - this.root.rank; // if the rank of t is bigger consider t as the self tree
		   AVLNode tmp = this.root;
		   this.root = t.root;
		   t.root = tmp;	   
	   }
	   AVLNode node = this.root;
	   AVLNode nodet = t.root;
	   if (node.key > nodet.key)
	   {
		   while (node.key != -1 && node.rank > nodet.rank) // finding the place to add t
			   node = node.left;
		   y.parent = node.parent;
		   if (y.parent == null) // check if the given node going to be the new root
		   {
			   this.root = y;
		   }
		   else
		   {
			   y.parent.setLeft(y);
		   }
		   y.setLeft(nodet);
		   y.setRight(node);
		   nodet.setParent(y);
		   node.setParent(y);
		   this.min = t.min; // change the min of the tree to the new min
		   this.min.info = t.min.info;
	   }
	   else
	   {
			 while (node.key != -1 && node.rank > nodet.rank)// finding the place to add t
				 node = node.right;
			 y.setParent(node.parent);
			 if (y.parent == null)// check if the given node going to be the new root
				 this.root = y;
			 else
				 y.parent.setRight(y);
			 y.setLeft(node);
			 y.setRight(nodet);
			 node.parent = y;
			 nodet.setParent(y);
			 this.max = t.max;// change the max of the tree to the new max
			 this.max.info = t.max.info;
	   }
	   updateRank(y);
	   rebalnceDelete(y.parent);
	   rebalnceInsert(y);
	   return dif;
	   
   }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	}

   /**
   * public class AVLNode
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  int size;
	  int key;
	  int rank;
	  String info;
	  AVLNode left;
	  AVLNode right;
	  AVLNode parent;

     private AVLNode()
     {
     }
     /**
      * AVLNode constructor
      * complexity: O(1) 
      * @param key
      * @param info
      * @param rank
      */  
     public AVLNode(int key, String info, int rank)
	  {
		  if (key != -1)
		  {
		  this.rank = rank;
		  this.info = info;
		  this.key = key;
		  this.right = new AVLNode(-1,null,-1);//External node
		  this.left = new AVLNode(-1,null,-1);//External node
		  }
		  else
		  {
			  this.rank = rank;
			  this.info = info;
			  this.key = key;
			  this.parent = null;
			  this.right = null;
			  this.left = null;
		  }
		  
	  }
	  public int getKey()
	  {
		return this.key;
	  }

	  public String getValue()
	  {
		return this.info;
	  }
	  
	  public void setLeft(IAVLNode node)
	  {
		this.left=(AVLNode)node;
	  }
	  public IAVLNode getLeft()
	  {
		return this.left;
	  }
	  public void setRight(IAVLNode node)
	  {
	 	this.right=(AVLNode)node;
	  }
	  public IAVLNode getRight()
      {
		return this.right;
	  }
	  public void setParent(IAVLNode node)
	  {
		this.parent=(AVLNode)node;
	  }
	  public IAVLNode getParent()
	  {
	 	return this.parent;
	  }
		// Returns True if this is a non-virtual AVL node
	  public boolean isRealNode()
      {
		if(this.key==-1)
	    {
			return false;
	    }	
			return true;
	  }
      public void setHeight(int height)
      {
      this.rank=height;
      }
      public int getHeight()
      {
        return this.rank;
      }
  }
}

