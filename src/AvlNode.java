
public class AvlNode {	//this is the node of tree
	AvlNode left;	
	AvlNode right;
	int element;	 //for keeping the number of the data
	int height;
	
	AvlNode(int newdata){
		this.element=newdata;
		height=0;
		left=null;
		right=null;
	}
	
	public AvlNode getLeft() {
		return left;
	}

	public void setLeft(AvlNode left) {
		this.left = left;
	}

	public AvlNode getRight() {
		return right;
	}

	public void setRight(AvlNode right) {
		this.right = right;
	}

	public int getElement() {
		return element;
	}

	public void setElement(int element) {
		this.element = element;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}