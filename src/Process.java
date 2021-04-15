import java.io.*;

public class Process {
	public void read(String input,String output) {	//read the file and separate the process then orient the corresponding functions
		 AvlTree tree = new AvlTree();//create tree
		 BufferedReader read;
		 BufferedWriter yaz;
	     File dosya=new File(input);
	     File dosya2=new File(output);
	     String str="";
	     String process[];
	     try{
	    	 read=new BufferedReader(new FileReader(dosya));
	    	 yaz=new BufferedWriter(new FileWriter(dosya2));
	    	 while(read.ready()){//process are separating
	            str=read.readLine();
	            process=str.split(" ");
	            if(process[0].equals("insert"))
	            {
	            	addnode( tree , Integer.parseInt(process[1]) );
	            }
	            else if(process[0].equals("delete"))
	            {
	            	deletenode( tree , Integer.parseInt(process[1]) );
	            }
	            else if(process[0].equals("show"))
	            {
	            	if(process[1].equals("preorder"))
	            	{
	            		preorder(tree.root,yaz);
	            		yaz.newLine();
	            	}
	            	else if(process[1].equals("postorder"))
	            	{
	            		postorder(tree.root,yaz);
	            		yaz.newLine();
	            	}
	            	else if(process[1].equals("inorder"))
	            	{
	            		inorder(tree.root,yaz);
	            		yaz.newLine();
	            	}
	            }
	         }
	         read.close();
	         yaz.close();
	      }catch(Exception e){
	         e.printStackTrace();
	      }
	}//end of menu

	private static AvlNode insert(AvlNode node, int data) {//insertion
		
		if (node==null) //data's location be found
			node = new AvlNode(data);//create node and assign the value of node that data
		
		else {
			if (data < node.getElement())//look the tree's left child's if data is less than the value of node
				node.setLeft( insert(node.getLeft(), data) );
			else if(data > node.getElement())
				node.setRight( insert(node.getRight(), data) );//look the tree's right child's if data is less than the value of node
			//else=>do nothing
		}
		
		node.setHeight( max( height( node.getLeft() ), height( node.getLeft() ) ) + 1 );//calibrate the height of the node
		return(node);
	}//end of insert
	
	
	private static void update(AvlNode node)//this function calibrate the all node of the tree's height
	{
		if(node!=null)
		{
			update(node.getLeft());
			update(node.getRight());
			node.setHeight( max( height( node.getLeft() ), height( node.getRight() ) ) + 1 );
		}
	}//end of update
	
	private static AvlNode control(AvlNode node,AvlNode root)
	{
		
		if(node!=null)
		{
			control(node.getLeft(),root);//walk in left child's
			control(node.getRight(),root);//walk in right child's
			update(root);//caution for delete process
			if( height( node.getLeft() ) - height( node.getRight() ) == 2 )//distance of the depth of the node's(left node's depth is big than right) 
			{
				if(height(node.getLeft().getLeft()) >= height(node.getLeft().getRight()))//is overage element in the ill-balanced node's left tree's left 
				{
					AvlNode parent=findParent(root, node);//find the parent
					node = rotateLeft( node );//rotate
					if(parent!=null)//assess the father
					{
						if( node.getElement() > parent.getElement() )
							parent.setRight(node);
						else if( node.getElement() < parent.getElement() )
							parent.setLeft(node);		
					}
				}
				else if(height(node.getLeft().getLeft()) < height(node.getLeft().getRight()))//is overage element in the ill-balanced node's left tree's right 
				{
					AvlNode parent=findParent(root, node);//find parent
	            	node = doubleRotateLeft( node );//rotate
	            	if(parent!=null)//assess the father
					{
						if( node.getElement() > parent.getElement() )
							parent.setRight(node);
						else if( node.getElement() < parent.getElement() )
							parent.setLeft(node);
							
					}      
		        }
		    }
			else if( height( node.getRight() ) - height( node.getLeft() ) == 2 )//distance of the depth of the node's(right node's depth is big than left) 
			{
				if( height(node.getRight().getRight()) >= height(node.getRight().getLeft()) )//is overage element in the ill-balanced node's right tree's right 
				{
					AvlNode parent=findParent(root, node);//find parent
					node = rotateRight( node );//rotate
					if(parent!=null)//assess the father
					{
						if(node.getElement() > parent.getElement())
							parent.setRight(node);
						else if(node.getElement() < parent.getElement())
							parent.setLeft(node);
					}
				}
	            else if(height(node.getRight().getRight()) < height(node.getRight().getLeft()))//is overage element in the ill-balanced node's left tree's left 
	            {
	            	AvlNode parent=findParent(root, node);//find parent
	            	node = doubleRotateRight( node );//rotate
	            	if(parent!=null)//assess the father
					{
						if(node.getElement() > parent.getElement())
							parent.setRight(node);
						else if(node.getElement() < parent.getElement())
							parent.setLeft(node);					
					}
	            }
			}
		}
		return node;
	}//end of control
	
	private static AvlNode findParent(AvlNode root, AvlNode node){
		AvlNode current = root;
		AvlNode parent = null;

       while(true){
           if (current == null)//there is no node in tree
               return null;

           if( current.getElement() == node.getElement() ){//find the parent
               break;
           }

           if (current.getElement() > node.getElement()){//walk in left child
               parent = current;
               current = current.getLeft();
           }
           else{ //walk in right child
               parent = current;
               current = current.getRight();
           }       
       }
       return parent;
   }//end of findParent

	private static AvlNode findNode(int data,AvlNode node)
	{
		  if (data == node.getElement())//find
             return node;
		  else if (data < node.getElement()) {//walk in the left
             if (node.getLeft() == null)
                   return null;
             else
             {
           	  node=node.getLeft();
           	  return findNode(data, node);
             }  
		  } 
		  else if (data > node.getElement()) {//walk in the right
             if (node.getRight() == null)
                   return null;
             else{
                 node=node.getRight();
           	  return findNode(data, node);
             }
       }
       return null;    
	}
	
	
	private static void delete(AvlTree tree,int data,AvlNode node)
	{
		AvlNode parent = new AvlNode(-1);
		AvlNode deletedData= new AvlNode (-1);
		AvlNode maxLeftChild=null;
		parent=node;
		
		if(node==null)//firstly , insertion process must doing (there is no node in tree)
			return;
			
		deletedData=findNode(data,node);//minimum value of the right tree of the will be deleted node
		
		if(deletedData!=null)
			node=deletedData;

		else//wrong process.This value is not in the tree
			return;
		
		if(node.getLeft()==null && node.getRight()==null)//this value is end node
		{
			parent=findParent(parent, node);//find parent
			if(parent!=null)
			{
				if(node.getElement() < parent.getElement())//doing null father's child is enough
					parent.setLeft(null);
				else
					parent.setRight(null);
			}
			else
			{
				tree.setRoot(null);
				return;	
			}
		}
		else if(node.getRight()==null && node.getLeft()!=null)//there is no right child of the node
		{
			parent=findParent(parent, node);//find parent
			
			if(parent!=null)
			{
				if(node.getElement() < parent.getElement())//the left children must not off work.Assess them to the father
					parent.setLeft(node.getLeft());
				else
					parent.setRight(node.getLeft());
			}
			else
			{
				tree.setRoot(node.getLeft());
				return;
			}
		}
		else if(node.getRight()!=null && node.getLeft()==null)///there is no left child of the node
		{
			parent=findParent(parent, node);//find parent
			if(parent!=null)
			{
				if(node.getElement() < parent.getElement())//the right children must not off work.Assess them to the father
					parent.setLeft(node.getRight());
				else
					parent.setRight(node.getRight());
			}
			else
			{
				//tree.setRoot(null);
				tree.setRoot(node.getRight());
				return;
			}
		}
		else if(node.getRight()!=null && node.getLeft()!=null)//left and right children is existing 
		{
			maxLeftChild=findMax(node);//find the minimum element of the right tree
			parent=findParent(parent, maxLeftChild);//find the parent of the minimum element
			if(maxLeftChild.getLeft()==null)//there is no right child
			{
				if(maxLeftChild.getElement() > parent.getElement())//doing null parent's child
					parent.setRight(null);
				else
					parent.setLeft(null);
			}
			else
			{
				AvlNode remainsRightChilds = maxLeftChild.getLeft();
				if(maxLeftChild.getElement() > parent.getElement())//assess the rest children to the father of the maximum node(found before)
				{										//because maximum value will locate in deleted node's location
					parent.setRight(null);
					parent.setRight(remainsRightChilds);
				}
				else
				{
					parent.setLeft(null);
					parent.setLeft(remainsRightChilds);
				}
			}
			node.setElement( maxLeftChild.getElement() );//update of the will be deleted node's content
		}	
	}//end of delete
	

	private static int deletenode(AvlTree tree , int data)
	{
		delete(tree,data,tree.getRoot());//delete process
		AvlNode b=control(tree.getRoot() , tree.getRoot());//look the balance of the new tree
		tree.setRoot(b);
		return 1;
	}//end of deleting
		
	
	private static int addnode(AvlTree tree,int data)
	{	
		AvlNode node=new AvlNode(-1);//create a new node
		node.setElement(data);//update the new node's content with to be given data 
		
		if(tree.getRoot()==null)//if the root is empty
			tree.setRoot(node);//make it the root of the tree
		else//if there is a root
		{	
			AvlNode b=insert(tree.getRoot(),data);//insertion process
			b=control(b,tree.getRoot());//balance new tree
			tree.setRoot(b);
		}	
		return 1;
	}//end of adding


	private static AvlNode findMax(AvlNode node)
	{
		AvlNode current = null;
		
		if(node.getRight() == null)//there is no node in left ,so there is no maximum value
			return null;
		else//look for left tree
			current = node.getLeft();
		
		if(node.getLeft()!=null)
		{
			while (current.getRight() != null) //walk in left,because minimum locate in left
				current = current.getRight();
		}
		return(current);
	}
	
	private static int max( int left, int right )//compare the left depth and right depth
    {
        return left > right ? left : right;
    }
	
	private static int height( AvlNode t )//find the depth of the to given node
    {
        return t == null ? -1 : t.getHeight();
    }
	private static void postorder(AvlNode root,BufferedWriter write){
		if(root!=null){
			
			postorder(root.getLeft(),write);//walk in left
			postorder(root.getRight(),write);//walk in right
			
			try {
				write.write(" "+root.getElement());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//print the tree  postorder
		}	
	}//end of postorder 
	
	private static void preorder(AvlNode root,BufferedWriter write){
		if(root!=null){
			try {
				write.write(" "+root.getElement());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//print the tree preorder
			
			preorder(root.getLeft(),write);//walk in left
			preorder(root.getRight(),write);//walk in right			
		}	
	}//end of preorder 
	
	private static void inorder(AvlNode root,BufferedWriter write){
		if(root!=null){		
			
			inorder(root.getLeft(),write);//walk in left
			try {
				write.write(" "+root.getElement());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//print the tree inorder
			inorder(root.getRight(),write);//walk in right
		}	
	}//end of inorder 

  
   private static AvlNode rotateLeft( AvlNode head )
   {
   	AvlNode newHead = head.getLeft();//new head must be in central 
       head.setLeft( newHead.getRight() );//new head's children assess to old node
       newHead.setRight(head);
       head.setHeight(max( height( head.getLeft() ), height( head.getRight() ) ) + 1 );//height calibration
       newHead.setHeight( max( height( newHead.getLeft() ), head.getHeight() ) + 1 );
       return newHead;
   }

  
   private static AvlNode rotateRight( AvlNode head )
   {
       AvlNode newHead = head.getRight();//new head must be in central
       head.setRight( newHead.getLeft() );//new head's children assess to old node
       newHead.setLeft(head);
       head.setHeight(max( height( head.getLeft() ), height( head.getRight() ) ) + 1 );//height calibration
       newHead.setHeight( max( height( newHead.getRight() ), head.getHeight() ) + 1 );
       return newHead;
   }

  
   private static AvlNode doubleRotateLeft( AvlNode head )
   {
   	head.setLeft( rotateRight( head.getLeft() ) );//firstly rotate right
       return rotateLeft( head );//than rotate left
   }


   private static AvlNode doubleRotateRight( AvlNode head )
   {
   	head.setRight( rotateLeft( head.getRight() ) );//firstly rotate left
       return rotateRight( head );//than rotate right
   }
}
