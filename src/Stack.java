
public class Stack {

	private TreeNode head = null;

	public boolean isEmpty() {
		return head == null;
	}

	public TreeNode getTop() {
		return this.head;
	}

	public void push(TreeNode node) {
		if (this.isEmpty()) {
			head = node;
		} else {
			node.setNext(head);
			head = node;
		}
	}

	public void pop() {
		head = head.getNext();
	}
	public int size(){
		int count=0;
		boolean STOP=false;
		TreeNode floatNode;
		if(this.head!=null)
		{
			count++;
			floatNode=this.head;
			do{
				if(floatNode.getNext()!=null)
				{
				    count++;
					floatNode=floatNode.getNext();
				}
				else
				{STOP=true;}
			}while(STOP==false);
		}
		return count;
	}
}