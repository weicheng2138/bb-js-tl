import java.util.ArrayList;


public class OperationNode_Conjunctive {
	private static Interval InitialInterval =new Interval(0,0);
	private Operation operation;
	private int processingtime;
	private Interval startInterval;
	private ArrayList<OperationNode_Conjunctive> from;
	private ArrayList<OperationNode_Conjunctive> to;
	public boolean scheduled; //apply in calculation of longest path
	public boolean scheduled_tail;
	private boolean scheduled_Node; //determined the schedulable operation node
	
	public OperationNode_Conjunctive(Operation Operation,int Processingtime, ArrayList<OperationNode_Conjunctive> From, ArrayList<OperationNode_Conjunctive> To) {
		this.operation=Operation;
		this.processingtime=Processingtime;
		this.from=From;
		this.to=To;
		this.scheduled=false;  //Heads calculation
		this.scheduled_tail=false; //Tails calculation
		this.scheduled_Node=false;
	    this.startInterval=InitialInterval;
	}
	public Operation get_operation(){
		return this.operation;
	}
	public int get_processingtime(){
		return this.processingtime;
	}
	public boolean get_scheduled(){
		return this.scheduled_Node;
	}
	public void isScheduled(){
		this.scheduled_Node=true;
	}
	public ArrayList<OperationNode_Conjunctive> get_from(){
		return this.from;
	}
	public ArrayList<OperationNode_Conjunctive> get_to(){
		return this.to;
	}
	public String get_ID(){
		String operation;
		operation="("+this.operation.get_job()+","+this.operation.get_machine()+")";
		return operation;
	}
	public void Addfrom(OperationNode_Conjunctive newfrom)
	{
		this.from.add(newfrom);
	}
	public void Addto(OperationNode_Conjunctive newto)
	{
		this.to.add(newto);
	}
	
		public void mantainStatingTime(TreeNode theNode){
		long MaximumOfLB=0;
		long MinimumOfUB=Integer.MAX_VALUE;
		//According to 2.1.2, take the predecessor with maximum completion time 
		//System.out.println(this.from.size());
		/*
		   for(OperationNode_Conjunctive op:theNode.get_Operationnodelist_C())
	        {
	        System.out.println("!!="+op.get_ID()+op.get_from().size());
	        for(OperationNode_Conjunctive the:op.get_from())
	          {System.out.print("<"+the.get_ID()+">");}
	        System.out.println("");
	        }
	        */
		for(OperationNode_Conjunctive thisOpe:this.from)
		{
			/*
			System.out.println("this="+this.operation.get_ID()+"from size: "+this.from.size());
			System.out.println("from: "+thisOpe.get_operation().get_ID());
		    System.out.println("UB="+MinimumOfUB);
		    */
			if(thisOpe.get_LBOfStartingtime()+thisOpe.get_processingtime()+GetTimelag(theNode,thisOpe.get_operation(),this.operation).get_mini()>=MaximumOfLB)
			{ 
				MaximumOfLB=thisOpe.get_LBOfStartingtime()+thisOpe.get_processingtime()+GetTimelag(theNode,thisOpe.get_operation(),this.operation).get_mini();}
			if(thisOpe.get_UBOfStartingtime()+thisOpe.get_processingtime()+GetTimelag(theNode,thisOpe.get_operation(),this.operation).get_maxi()<=MinimumOfUB)
			{
				MinimumOfUB=thisOpe.get_UBOfStartingtime()+thisOpe.get_processingtime()+GetTimelag(theNode,thisOpe.get_operation(),this.operation).get_maxi();}
		}
		Interval NewInterval=new Interval(MaximumOfLB,MinimumOfUB);
		this.startInterval=NewInterval;
	}
		
	public long get_LBOfStartingtime(){
		return this.startInterval.get_start();
	}
	
	public long get_UBOfStartingtime(){
		return this.startInterval.get_finish();
	}
	
    public String get_startingtime(){
    	String startingtime;
    	startingtime="["+startInterval.get_start()+","+startInterval.get_finish()+"]";
    	return startingtime;
    }
    private static Timelags GetTimelag(TreeNode theNode,Operation from,Operation to)
	{
		Timelags theTimelag = null;
		for(Timelags T:theNode.get_Timelags_C())
		{
			if(T.get_from().equalto(from)&T.get_to().equalto(to))
		     {
				theTimelag=T;
		     }
		}
	return theTimelag;
	}
}
