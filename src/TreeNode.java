import java.util.ArrayList;
class TreeNode {
	//Search tree node connect
	private TreeNode ParentNode;
	private TreeNode Next;
    private ArrayList<TreeNode> ChildList;
    
    private ArrayList<Operation>[] InputSequence;
    private ArrayList<Operation>[] OutputSequence;
    private ArrayList<Operation>[] Clique;
    private ArrayList<Timelags> Disjunctions;
    private ArrayList<Timelags> Conjunctions;
    private ArrayList<OperationNode_Conjunctive> OperationNodeList_C;
    
    private int LowerBound1;
    private OperationNode_Conjunctive Start;
    private OperationNode_Conjunctive Terminal;
    private int Cmax;
	private boolean branched;
    private boolean infeasible;
    //For root node
	public TreeNode(ArrayList<Operation>[] IS,ArrayList<Operation>[] OS,ArrayList<Operation>[] C,ArrayList<OperationNode_Conjunctive> OperationNodeList,ArrayList<Timelags> conjunctions,ArrayList<Timelags> disjunctions,OperationNode_Conjunctive start,OperationNode_Conjunctive terminal)
	{
		this.InputSequence=IS;
		this.OutputSequence=OS;
		this.Clique=C;
		this.OperationNodeList_C=OperationNodeList;
		this.Disjunctions=disjunctions;
		this.Conjunctions=conjunctions;
		this.ChildList=new ArrayList();
		this.Start=start;
		this.Start.scheduled=true;
		this.Terminal=terminal;
		this.Cmax=0;
		this.Next=null;
		this.branched=false;
		this.infeasible=false;
		this.LowerBound1=0;
		//Maintain Cliques
	    for(Timelags thedisjunction:this.Disjunctions)
	    {
	 	if( getContain(thedisjunction.get_from(),this.Clique[thedisjunction.get_from().get_machine()])==false)
		{this.Clique[thedisjunction.get_from().get_machine()].add(thedisjunction.get_from());}
	    }
	    
	    
	    
		
	}
	
	//For search tree node
	public TreeNode(TreeNode parent, ArrayList<Operation>[] IS,ArrayList<Operation>[] OS,ArrayList<Operation>[] C,ArrayList<OperationNode_Conjunctive> OperationNodeList,ArrayList<Timelags> conjunctions, ArrayList<Timelags> disjunctions, OperationNode_Conjunctive start, OperationNode_Conjunctive terminal)
	{
		this.InputSequence=IS;
		this.OutputSequence=OS;
		this.Clique=C;
		this.OperationNodeList_C=OperationNodeList;
		this.Disjunctions=disjunctions;
		this.ChildList=new ArrayList();
		this.Conjunctions=conjunctions;
		this.Start=start;
		this.Terminal=terminal;
		this.Cmax=0;
		this.Next=null;
		this.branched=false;
		this.infeasible=false;
		this.LowerBound1=0;
        //Maitain Cliques
    	for(Timelags thedisjunction:this.Disjunctions)
     	{
		if( getContain(thedisjunction.get_from(),this.Clique[thedisjunction.get_from().get_machine()])==false)
		{this.Clique[thedisjunction.get_from().get_machine()].add(thedisjunction.get_from());}   
	    }
        
	
	}
	public ArrayList<Timelags> get_Timelags_C(){
		return this.Conjunctions;
	}
	
	public ArrayList<OperationNode_Conjunctive> get_SchedulableList(){
		ArrayList<OperationNode_Conjunctive> schedulableList=new ArrayList<OperationNode_Conjunctive>();
		//ArrayList<OperationNode_Conjunctive> scheduledList=new ArrayList<OperationNode_Conjunctive>();
		schedulableList.clear();
		for(OperationNode_Conjunctive Op:this.OperationNodeList_C)
		{
			boolean schedulable=true;
			for(OperationNode_Conjunctive from:Op.get_from())
			{
				if(from.get_scheduled()==false)
				{
					schedulable=false;
				}
			}
		    if(schedulable==true&&Op.get_scheduled()==false)
		    {schedulableList.add(Op);}
		}
		
		return schedulableList;
	}
	
	private static OperationNode_Conjunctive GetOperationNode(Operation theOp, ArrayList<OperationNode_Conjunctive> OperationNodeList) {
		OperationNode_Conjunctive theOperationNode=null;
		for(OperationNode_Conjunctive OpNode:OperationNodeList)
		{
			if(OpNode.get_operation().equalto(theOp))
			{
				theOperationNode=OpNode;
			
			}
		}
		return theOperationNode;
	}
	
    public OperationNode_Conjunctive get_Start(){
    	return this.Start;
    }
    
    public OperationNode_Conjunctive get_Terminal(){
    	return this.Terminal;
    }
    
	public ArrayList<OperationNode_Conjunctive> get_Operationnodelist_C(){
		return this.OperationNodeList_C;
	}
	
	public void Connect(TreeNode Parent) {
		this.ParentNode=Parent;
		ParentNode.ChildList.add(this);
	}

	public TreeNode get_ParentNode() {

		return this.ParentNode;
	}

	public ArrayList<TreeNode> get_ChildList()
	{
		return this.ChildList;
	}
	
    public ArrayList<Operation> get_InputSequence(int Index)
    {
    	return this.InputSequence[Index];
    }

    public ArrayList<Operation> get_OutputSequence(int Index)
    {
    	return this.OutputSequence[Index];
    }
    
    public ArrayList<Operation> get_Clique(int Index)
    {
    	return this.Clique[Index];
    }
    
    public String Get_Information()
    {
    	String Information="===========================The information============================"+"\n"+"Makespan= "+this.Cmax+" "+this.get_isbranched()+" Lower bound="+this.LowerBound1+"\n";
        
		for(int Machine=1;Machine<=InputData.NumOfMachine;Machine++)
		{
			Information+="The Machine"+Machine+"'s Input Sequence is ";
		  	for(Operation IS:this.InputSequence[Machine])
	    	{
	    		Information+=IS.get_ID();
	    	}
		  	Information+="\n"+"The Machine"+Machine+"'s Output Sequence is ";
	    	for(Operation OS:this.OutputSequence[Machine])
	    	{
	    		Information+=OS.get_ID();
	    	}
	    	
			Information+="\n"+"The Machine"+Machine+"'s clique=>";
			for(Operation theOpe:this.Clique[Machine])
			{
		    Information+=theOpe.get_ID()+" ";
			}
			Information+="\n";
			Information+="\n";
				}
		Information+="=====================================================================";
		
    return Information;
    }
    
    public ArrayList<Timelags> get_Disjunctions(){
    	return this.Disjunctions;
    }
    
    public String Print_disjunctions(){
    	String Print="";
    	Print+="================Disjunctions======================="+"\n";
    	for(Timelags theT:this.Disjunctions)
    	{
    		Print+=theT.get_from().get_ID()+","+theT.get_to().get_ID()+"=> "+"["+theT.get_mini()+","+theT.get_maxi()+"]"+"\n";
    	}
    return Print;
    }

    public String Print_conjunctions(){
    	String Print="";
    	Print+="================Conjunctions======================="+"\n";
    	for(Timelags theT:this.Conjunctions)
    	{
    		Print+=theT.get_from().get_ID()+","+theT.get_to().get_ID()+"=> "+"["+theT.get_mini()+","+theT.get_maxi()+"]"+"\n";
    	}
    return Print;
    }
    
    public String Print_headsandtails(){
    	String Print="";
    	Print+="================Heads======================="+"\n";
    	for(OperationNode_Conjunctive Op:this.OperationNodeList_C)
    	{
    	    Print+=Op.get_ID()+"=> "+Op.get_operation().get_heads()+"\n";	
    	}
    	Print+="================Tails======================="+"\n";
    	for(OperationNode_Conjunctive Op:this.OperationNodeList_C)
    	{
    	    Print+=Op.get_ID()+"=> "+Op.get_operation().get_tails()+"\n";	
    	}
    	return Print;
    }
    
    public String Print_startingtimeinterval(){
    	String Print="";
    	Print+="================Starting time Interval======================="+"\n";
        for(OperationNode_Conjunctive op:this.get_Operationnodelist_C())
        {
       	 Print+=op.get_ID()+"=> "+op.get_startingtime()+"\n";
        }
    	return Print;
    }
   
    public String Print_operationNodeList(){
    	String Print=" ";
    	Print+="================OperationNode======================="+"\n";
        for(OperationNode_Conjunctive op:this.get_Operationnodelist_C())
        {
       	 Print+=op.get_ID()+"=> scheduled="+op.get_scheduled()+"\n";
        }
    	return Print;
    }
    
    public void UpdateCmax(int makespan){
	this.Cmax=makespan;}
    
    public int getCmax(){
    	return this.Cmax;
    }
    
	public void setNext(TreeNode head) {
		this.Next=head;
	}

	public TreeNode getNext() {
		
		return this.Next;
	}

	public void isbranched() {
		this.branched=true;
		
	}
	
	public boolean infeasible(){
		return this.infeasible;
	}
	
	public boolean get_isbranched(){
		return this.branched;
	}

	public boolean getContain(Operation theOp,ArrayList<Operation> List)
	{
		boolean contain=false;
		for(Operation OP:List)
		{
			if(theOp.equalto(OP))
			{contain=true;}
		}
			return contain;
	}

    public boolean isDone(){
    	boolean Done=true;
    	for(int index=1;index<=InputData.NumOfMachine;index++)
    	{
    		if(this.Clique[index].size()!=0)
    		{
    			Done=false;
    		}
    	}
    	return Done;
    }

    public void feasibleCheck() {
		int counter=0;
		for(OperationNode_Conjunctive OP:this.OperationNodeList_C)
		{
			if(OP.get_LBOfStartingtime()>OP.get_UBOfStartingtime())
			{
				this.infeasible=true;
			break;
			}
		    if(OP.get_operation().get_heads()==0)
		    {counter++;}
		    /*
		    for(OperationNode_Conjunctive Op_F:OP.get_from())
		    {
		    	if(GetTimelag(this.Disjunctions,Op_F.get_operation(),OP.get_operation())!=null)
		    	{
		    	if(Op_F.get_UBOfStartingtime()+GetTimelag(this.Disjunctions,Op_F.get_operation(),OP.get_operation()).get_maxi()<OP.get_LBOfStartingtime())
		    	{
		    		this.infeasible=true;
		    	    break;
		    	}
		    	if(Op_F.get_LBOfStartingtime()+GetTimelag(this.Disjunctions,Op_F.get_operation(),OP.get_operation()).get_mini()>OP.get_UBOfStartingtime())
		    	{
		    		this.infeasible=true;
		    	    break;
		    	}
		    	} 
		    	else{
		    		if(Op_F.get_UBOfStartingtime()+GetTimelag(this.Conjunctions,Op_F.get_operation(),OP.get_operation()).get_maxi()<OP.get_LBOfStartingtime())
			    	{
			    		this.infeasible=true;
			    	    break;
			    	}
		    		if(Op_F.get_LBOfStartingtime()+GetTimelag(this.Conjunctions,Op_F.get_operation(),OP.get_operation()).get_mini()>OP.get_UBOfStartingtime())
			    	{
			    		this.infeasible=true;
			    	    break;
			    	}
		    	}
		    }
		    */
		    
		}
		if(counter>InputData.NumOfJob)
		{this.infeasible=true;}
	} 
	
	public int get_Lowerbound1(){
		return this.LowerBound1;
	}
	
	public void Upperbound(){
		
		
	}
	
	public int ATSP(int Index,ArrayList<Operation> clique)
	{
		int miniTL=InputData.MiniTL[Index];
		int a=clique.size()/2;
		int b=clique.size()%2;
	    int NumOfArc=a*2+b;
	    int Distance=NumOfArc*miniTL;
	return Distance;
	}
	
    public void LowerBound1(){
    	ArrayList<Integer> LowerBound=new ArrayList();
    	for(int index=1;index<=InputData.NumOfMachine;index++)
    	{LowerBound.add(0);}
    	for(int index=1;index<=InputData.NumOfMachine;index++)
    	{ 
    		int theLowerbound=0;
    		int miniheads=Integer.MAX_VALUE;
    		int minitails=Integer.MAX_VALUE;
    		int TotalPT=0;
    		for(Operation op:this.Clique[index])
    		{
    			if(GetOperationNode(op,this.OperationNodeList_C).get_operation().get_heads()<miniheads)
    			{miniheads=GetOperationNode(op,this.OperationNodeList_C).get_operation().get_heads();}
    			if(GetOperationNode(op,this.OperationNodeList_C).get_operation().get_tails()<minitails)
    			{minitails=GetOperationNode(op,this.OperationNodeList_C).get_operation().get_tails();}
    		    TotalPT+=GetOperationNode(op,this.OperationNodeList_C).get_processingtime();
    		    //System.out.print(this.Print_headsandtails());
    		    //System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+miniheads+" ,"+minitails);
    		}
    	    if(miniheads!=Integer.MAX_VALUE&&minitails!=Integer.MAX_VALUE)
    		{
    	    	//theLowerbound+=miniheads+TotalPT+minitails+ATSP(index,this.get_Clique(index));
    	    	theLowerbound+=miniheads+TotalPT+minitails+LB_ATSP.Calculate(index, JS_searchtree.root.get_Clique(index), JS_searchtree.root.get_Disjunctions());
    		    LowerBound.add(index,theLowerbound);
    		}
    	}
    
    	for(Integer LB:LowerBound)
    	{
    		if(LB>this.LowerBound1)
    		{this.LowerBound1=LB;}
    	}
    }
    
    private static Timelags GetTimelag(ArrayList<Timelags> TimelagList,Operation from,Operation to)
    {
    	Timelags theTimelag = null;
	for(Timelags T:TimelagList)
	{
		if(T.get_from().equalto(from)&T.get_to().equalto(to))
	     {theTimelag=T;}	
	}
	return theTimelag;
} 
}
