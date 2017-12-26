import java.io.IOException;
import java.util.*;

public class JS_searchtree {

public static TreeNode root;
//public static ArrayList<TreeNode> TreeNodeList=new ArrayList();
static int TotalNumOfNode=0;
static Stack BranchList=new Stack();
static ArrayList<TreeNode> Temp;
static TreeNode Optimal;
public static int UpperBound=Integer.MAX_VALUE;
static Analysis NewAnalysis;
static int ID;
	public static void main(String[] args) throws IOException {
//*************** Input Data (start)************************************ 
		InputData.Initialize();
		InputData.Createoperation();
		InputData.InputDisjunctivegraph();
		InputData.InputConjunctivegraph();
		InputData.ConnectSandT(InputData.OperationNodeList_C);
		//System.out.println(Arrays.deepToString(InputData.processingtime));
		
//*************** Input Data (end)************************************ 	
	
		System.out.println("Conjuntions");
		for(Timelags TheT:InputData.TimelagsList_C)
		 {
			 System.out.print(TheT.get_from().get_ID()+","+TheT.get_to().get_ID()+"=> ");
			 System.out.println("["+TheT.get_mini()+","+TheT.get_maxi()+"]"); 
		 }
		System.out.println("Disjuntions");
		 for(Timelags TheT:InputData.TimelagsList_D)
		 {
			 System.out.print(TheT.get_from().get_ID()+","+TheT.get_to().get_ID()+"=> ");
		      System.out.println("["+TheT.get_mini()+","+TheT.get_maxi()+"]"); 
		 }
		
//*************** Branch and bound ***********************************
		//Create a analysis object to collect the times of each propositions valid
		long startTime = System.currentTimeMillis();
		NewAnalysis=new Analysis();
		Temp=new ArrayList();
		ID=1;
		UpperBound=Integer.MAX_VALUE;
		root=new TreeNode(InputData.InputSequence,InputData.OutputSequence,InputData.Clique,InputData.OperationNodeList_C,InputData.TimelagsList_C,InputData.TimelagsList_D,InputData.Start,InputData.Terminal);
        System.out.println(root.Get_Information());
        LongestPath.Calculate(true,root.get_Operationnodelist_C(),root);
        LongestPath.Calculate(false,root.get_Operationnodelist_C(),root);
        System.out.println("==================Starting time interval===================");
        for(OperationNode_Conjunctive op:root.get_Operationnodelist_C())
        {
       	 System.out.println(op.get_ID()+"=> "+op.get_startingtime());
        }
        
        //Create a list to branch
        boolean StopBranch=false;
        BranchList.push(root);
        do{
	        
 	     //Remove the treenode which has been branched from BranchList
 	     boolean Popout=true;
        	do{
        		if(BranchList.getTop()==null)
     	        {break;}
        		if(BranchList.getTop().get_Lowerbound1()>UpperBound||BranchList.getTop().get_isbranched()==true||BranchList.getTop().get_Disjunctions()==null||BranchList.getTop().getCmax()>UpperBound||BranchList.getTop().get_Disjunctions().size()==0)
	        {
        			BranchList.pop();
	    	}
	       else{
	    	   Popout=false;
	           }  
	     //System.out.println(BranchList.getTop().Get_Information());
 	     }while(Popout==true);
	    
 	     if(BranchList.getTop()!=null)
 	     {
 	    	System.out.println("Total number of treeNode= "+TotalNumOfNode);
 	    	System.out.println("BranchList SIZE=>"+BranchList.size());
 	    	//System.out.println("\n"+"****************************************************");
 	        //System.out.println(BranchList.getTop().Get_Information());
 	       // System.out.println("****************************************************");
        	
 	    		CreateTreeNode(BranchList.getTop());	
 	     }
	      //System.out.print("@@@="+BranchList.size());
        if(BranchList.getTop()==null)
        {StopBranch=true;}
        }while(StopBranch!=true);
        
        
	    //Print result
	    //System.out.println("Total Number of search tree node= "+TreeNodeList.size()+"\n"+"The optimal makespan ="+UpperBound);
	    if(Optimal!=null)
	    {
	    	double endTime = System.currentTimeMillis();
	    	double totTime = endTime - startTime;
	    	//LongestPath.Calculate(true,Optimal.get_Operationnodelist_C(),Optimal);
	    	//System.out.println("Total Num of node£º"+TotalNumOfNode);
	    	System.out.println(Optimal.Print_headsandtails());
	    	System.out.println(Optimal.Print_startingtimeinterval());
	    	System.out.println(NewAnalysis.PrintAnalysis());
	    	System.out.println("Total number of treeNode= "+TotalNumOfNode);
	    	System.out.println(Optimal.Get_Information());
	    	System.out.println("Using Time:" + totTime/1000);
	    }
	    else{
	    	double endTime = System.currentTimeMillis();
	    	double totTime = endTime - startTime;
	    	System.out.println(NewAnalysis.PrintAnalysis());
	    	System.out.println("Total number of treeNode= "+TotalNumOfNode);
	    	System.out.println("Using Time:" + totTime/1000);
	    }
	}

	public static void CreateTreeNode(TreeNode ParentNode)
	{
		//Determined the target machine which achieved the minimum of tOfOmega
		int TOmega=get_tOfOmega(ParentNode.get_SchedulableList());
		System.out.println("TOmega= "+TOmega);
		System.out.println("===========schedulable List==========");
		for(OperationNode_Conjunctive op:ParentNode.get_SchedulableList())
		{
			System.out.println("QQQ=>"+op.get_ID());
		}
		//Determined the target machines
		ArrayList<Integer> TargetMachineList =new ArrayList<Integer>();
		for(OperationNode_Conjunctive theop:ParentNode.get_SchedulableList())
		{
			if(theop.get_operation().get_heads()+theop.get_processingtime()<=TOmega)
			{
				if(TargetMachineList.contains(theop.get_operation().get_machine())==false)
				TargetMachineList.add(theop.get_operation().get_machine());
			}
		}
	    
	    //Initialize the OmegaPrime(Input Candidate), Output Candidate and NumOfChildNode
		ArrayList<Operation>[] OmegaPrime=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Operation>[] OutputCandidate=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Integer> NumOfChildNode = new ArrayList<Integer>();
		for(int index=0;index<=InputData.NumOfMachine;index++)
		{NumOfChildNode.add(0);}
		for(Integer MachineIndex:TargetMachineList)
	    {
	    	OmegaPrime[MachineIndex]=new ArrayList();
	    	OutputCandidate[MachineIndex]=new ArrayList();
	    }
	    
	    
	    //Maintain the OmegaPrime
	    for(OperationNode_Conjunctive theop:ParentNode.get_SchedulableList())
	    {
	    	Operation theOperation=theop.get_operation();
	        if(TargetMachineList.contains(theOperation.get_machine())&&theOperation.get_heads()<=TOmega)
	        	OmegaPrime[theOperation.get_machine()].add(theOperation);
	    }
	    
	    //Maintain the Output candidate(S)
	    for(Integer MachineIndex:TargetMachineList)
	    {
	    	OutputCandidate[MachineIndex].addAll(ParentNode.get_Clique(MachineIndex));
	    }
	    
	    //Calculate the number of chilenode
	    if(ParentNode.get_ChildList().size()==0)
	    {
	      int SumOfChild;
	       for(Integer MachineIndex:TargetMachineList)
	       {
	    	SumOfChild=OmegaPrime[MachineIndex].size()*OutputCandidate[MachineIndex].size();
	    	NumOfChildNode.add(MachineIndex,SumOfChild);
	       }
	    }
	    
	    //Select the pairs of input and output
	    for(Integer Index:TargetMachineList)
	    {
	    	boolean GotIO=false;
	    	
	    	//Apply Proposition 10
	    	for(Operation CriOp:ParentNode.get_Clique(Index))
	    	{
	    		
	    		if(LowerboundWK(CriOp,ParentNode,Index)+GetOperationNode(CriOp,ParentNode.get_Operationnodelist_C()).get_processingtime()>UpperBound)
	    		{
	    			System.out.println("Operation "+CriOp.get_ID()+" is a input or output");
	    			
	    			//Operation OpR=ParentNode.get_OutputSequence(Index).get(0);
	    			Operation OpK=null;
	    			if(ParentNode.get_InputSequence(Index).size()!=0)
	    			 {OpK=ParentNode.get_InputSequence(Index).get(ParentNode.get_InputSequence(Index).size()-1);}
	    			
			//==================================Immediate determine input======================
	    			//Apply Proposition5 to find Input
	    			if(OpK!=null)
	    			{
	    				
	    			if (GetOperationNode(OpK,
							ParentNode.get_Operationnodelist_C())
							.get_operation().get_heads()
							+ GetOperationNode(OpK,
									ParentNode.get_Operationnodelist_C())
									.get_processingtime()
							+ GetTimelag(ParentNode.get_Timelags_C(), OpK,
									CriOp).get_mini() > GetOperationNode(CriOp,
								ParentNode.get_Operationnodelist_C())
							.get_UBOfStartingtime()) {
						GotIO=true;
						NewAnalysis.Update(12,ParentNode);
						//NewAnalysis.Update(5,ParentNode.get_Disjunctions().size());
						BrachingProcess(ParentNode,Index,CriOp);
						break;
					}
					
	    			}
	    			//Apply Proposition6 to find input
	    			
	    			if(Lowerbound_givenOP(CriOp,ParentNode,Index)+LB_ATSP.Calculate(Index, ParentNode.get_Clique(Index), ParentNode.get_Disjunctions())>UpperBound)
	    			{
						GotIO=true;
						NewAnalysis.Update(12,ParentNode);
						//NewAnalysis.Update(6,ParentNode.get_Disjunctions().size());
						BrachingProcess(ParentNode,Index,CriOp);
						break;
	    			}
	    			
	    			//Apply Proposition8 to find input
	    			
	    			int totalPTWO=0;
	    			int minihead=Integer.MAX_VALUE;
	    			for(Operation op:ParentNode.get_Clique(Index))
	    			{
	    				if(op.equalto(CriOp)==false)
	    				{
	    					totalPTWO+=GetOperationNode(op,ParentNode.get_Operationnodelist_C()).get_processingtime();
	    				    if(GetOperationNode(op,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()<minihead)
	    				    {minihead=GetOperationNode(op,ParentNode.get_Operationnodelist_C()).get_operation().get_heads();}
	    				}
	    			    
	    			}
	    			
	    			if(GetOperationNode(CriOp,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime()<totalPTWO+minihead)
	    			{
	    				
						GotIO=true;
						NewAnalysis.Update(12,ParentNode);
						//NewAnalysis.Update(8,ParentNode.get_Disjunctions().size());
						BrachingProcess(ParentNode,Index,CriOp);
						break;
	    			}
	    			
	       //==================================Immediate determine output======================
	    	    		/*	
	    			//Apply Proposition5 to fine Output
					if (GetOperationNode(CriOp,
							ParentNode.get_Operationnodelist_C())
							.get_operation().get_heads()
							+ GetOperationNode(CriOp,
									ParentNode.get_Operationnodelist_C())
									.get_processingtime()
							+ GetTimelag(ParentNode.get_Disjunctions(), CriOp,
									OpR).get_mini() > GetOperationNode(OpR,
								ParentNode.get_Operationnodelist_C())
							.get_UBOfStartingtime()) {
						isInput = false;
						GotIO=true;
						break;
					}
	    			*/
	    	
	    			//Apply Proposition6 to fine output
	    			
	    			 if(Lowerbound_givenIP(CriOp,ParentNode,Index)+LB_ATSP.Calculate(Index, ParentNode.get_Clique(Index), ParentNode.get_Disjunctions())>UpperBound)
	    			 {
	    				 GotIO=true;
						 NewAnalysis.Update(11,ParentNode);
						 BrachingProcess(ParentNode,Index,CriOp,false);
						 break;
	    			 }
	    			
	    		}
	    		
	    	}
	    
	    	if(GotIO==true)
	    	{
	    		 break;
	    	}
	    	
	    	System.out.println("input:"+OmegaPrime[Index].size()+", output:"+OutputCandidate[Index].size());
	    	for(Operation Input:OmegaPrime[Index])
	       {
	    		
	    //======================Proposition6 (a)=========================
	    	
	    		if(Lowerbound_givenIP(Input,ParentNode,Index)+LB_ATSP.Calculate(Index, ParentNode.get_Clique(Index), ParentNode.get_Disjunctions())>UpperBound)
	    	  {
	    		  NewAnalysis.Update(6,ParentNode);
	    		  continue;
	    	  }
	    	  
	    //======================Proposition6 (a) (END)=========================
	    	  
	    	  if(OutputCandidate[Index].size()==0)
	    	  {
	    		 BrachingProcess(ParentNode,Index,Input);
	    	  }
	    
	    	 
	    	  for(Operation Output:OutputCandidate[Index])
	    	  {
	    			
	    		  if(Input.equalto(Output)==false)
	    		  {
	    			  boolean TabuPair=false;
	    			  System.out.println("======================= Input: "+Input.get_ID()+"Output: "+Output.get_ID()+"=================================");
	    			  //Total processing time of clique[Index]
	    			  int SumOfPTWO=0;
	    			  int SumOfPT=0;
	    			  int mini_tail=Integer.MAX_VALUE;
	    			  int mini_head=Integer.MAX_VALUE;
	    			  
	    			  for(Operation Op_C:ParentNode.get_Clique(Index))
	    			  { 
	    				 SumOfPT+=GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_processingtime();
	    				 if(Op_C!=Output)
	    				 {SumOfPTWO+=GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_processingtime();}
	    			     if(GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_operation().get_tails()<mini_tail)
	    			     {mini_tail=GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_operation().get_tails();}
	    			     if(GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()<mini_head)
	    			     {mini_head=GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_operation().get_heads();}
	    			  }
	    		
	    			   //========================Proposition 7=======================================
		    			if(GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()>UpperBound-SumOfPT-mini_tail)
		    			{
		    				System.out.println("Pro7");
		    				NewAnalysis.Update(7,ParentNode);
		    				TabuPair=true;
		    				continue;
		    			}
			    		//========================Proposition 7 (END)=================================
		    			
		    			
		    			//========================Proposition 8=======================================
		    			if(GetOperationNode(Output,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime()<SumOfPTWO+mini_head)
		    			{
		    				System.out.println("Pro8");
		    				NewAnalysis.Update(8,ParentNode);
		    				TabuPair=true;
		    				continue;
		    			}
		    			
		    			//========================Proposition 8 (END)=================================
		    			
		    			//========================Proposition 3(a)=======================================
		    			  for(Operation Op_C:ParentNode.get_Clique(Index))
		    			  {
		     
		    				  if(Op_C.equalto(Input)==false&&Op_C.equalto(Output)==false)
	    					  {
		    					  if((GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()+GetTimelag(ParentNode.get_Disjunctions(),Input,Op_C).get_mini()+ GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_processingtime()+GetTimelag(ParentNode.get_Disjunctions(),Op_C,Output).get_mini())>(GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime()+GetTimelag(ParentNode.get_Disjunctions(),Input,Output).get_maxi()))
		    				     {
		    					  System.out.println(ParentNode.Print_startingtimeinterval());
		    					  System.out.println(GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()+","+GetTimelag(ParentNode.get_Disjunctions(),Input,Op_C).get_mini()+","+GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_processingtime()+","+GetTimelag(ParentNode.get_Disjunctions(),Op_C,Output).get_mini());
		    					  System.out.println(GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime()+","+GetTimelag(ParentNode.get_Disjunctions(),Input,Output).get_maxi());
		    					  NewAnalysis.Update(3,ParentNode);
		    					  TabuPair=true;  
		    				     }
	    					  }
		    			  }
		    			  if(TabuPair==true)
		    			  {continue;}
		    			  //========================Proposition 3(a) (END)=================================
		    		      
		    			  
		    			//========================Proposition 3(b)=======================================
		    			  for(Operation Op_C:ParentNode.get_Clique(Index))
		    			  {
		    				  if(Op_C.equalto(Input)==false&&Op_C.equalto(Output)==false&&(GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime()+GetTimelag(ParentNode.get_Disjunctions(),Input,Op_C).get_maxi()+GetOperationNode(Op_C,ParentNode.get_Operationnodelist_C()).get_processingtime()+GetTimelag(ParentNode.get_Disjunctions(),Op_C,Output).get_maxi())<(GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()+GetTimelag(ParentNode.get_Disjunctions(),Input,Op_C).get_mini()))
		    				  {
		    					  System.out.println("Pro3.b");
		    					  NewAnalysis.Update(3,ParentNode);
		    					//TabuPair=true;  
		    				  }
		    			  }
		    			  if(TabuPair==true)
		    			  {continue;}
		    			  
		    			//========================Proposition 3(b) (END)=================================
	    			  
		    			  //========================Proposition 6(b)==========================
	    			  if(Lowerbound_givenOP(Output,ParentNode,Index)+LB_ATSP.Calculate(Index, ParentNode.get_Clique(Index), ParentNode.get_Disjunctions())>UpperBound)
		    			{
	    				  System.out.println("Pro6.b");
	    				  NewAnalysis.Update(6,ParentNode);
							TabuPair=true;
							continue;
		    			}
	    			//========================Proposition 6(b) (END)==========================
	
	
	    			//========================Proposition 4=======================================
	    			  if(GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()+SumOfPTWO+LB_ATSP.Calculate(Index, ParentNode.get_Clique(Index), ParentNode.get_Disjunctions())>GetOperationNode(Output,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime())
    			      {
	    				  System.out.println("??????????????????????????????????????????????????????????????");
	    				  System.out.println("P4£º");
	    				  System.out.println("ID"+Input.get_ID()+Input.get_heads()+","+SumOfPTWO+","+LB_ATSP.Calculate(Index, ParentNode.get_Clique(Index), ParentNode.get_Disjunctions()));
	    				  System.out.println("Out ID"+Output.get_ID()+GetOperationNode(Output,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime());
	    				  NewAnalysis.Update(4,ParentNode);
	    				  TabuPair=true;
	    				  //System.exit(0);
	    				  continue;
    			      }
	    			//========================Proposition 4 (END)=================================
	  	
	    			  
	    			//========================Proposition 5(a)=======================================
	    				  if((GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()+GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_processingtime()+GetTimelag(ParentNode.get_Disjunctions(),Input,Output).get_mini())>GetOperationNode(Output,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime())
	    				  {
	    					  System.out.println("Pro5.a");
	    					  System.out.println((GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_operation().get_heads()+GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_processingtime()+GetTimelag(ParentNode.get_Disjunctions(),Input,Output).get_mini())+","+GetOperationNode(Output,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime());
	    					  
	    					  NewAnalysis.Update(5,ParentNode);
		    				  TabuPair=true;
		    				  continue;
	    				  }	    				  
		    		//========================Proposition 5(a) (END)=================================
	    			
	    			
	    			//========================Proposition 5(b)=======================================
	    				  if((GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime()+GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_processingtime()+GetTimelag(ParentNode.get_Disjunctions(),Input,Output).get_maxi())<GetOperationNode(Output,ParentNode.get_Operationnodelist_C()).get_operation().get_heads())
	    				  {
	    					  System.out.println("Pro5.b");
	    					  System.out.println((GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_UBOfStartingtime()+"+"+GetOperationNode(Input,ParentNode.get_Operationnodelist_C()).get_processingtime()+"+"+GetTimelag(ParentNode.get_Disjunctions(),Input,Output).get_maxi())+","+GetOperationNode(Output,ParentNode.get_Operationnodelist_C()).get_operation().get_heads());
	    					  NewAnalysis.Update(5,ParentNode);
		    				  TabuPair=true;
		    				  continue;
	    				  }	    				  
		    		//========================Proposition 5(b) (END)=================================
	    			
	    			if(TabuPair==false)
	    		   {BrachingProcess(ParentNode,Index,Input,Output);}
	    		  
	    		  }
	    		}
	       }
	    }
	    ParentNode.isbranched();
	    
	    	}
	
	//*********************For output branch*****************************
	private static void BrachingProcess(TreeNode parentNode, Integer MachineIndex,
			Operation output, boolean b) {
		ArrayList<Operation>[] New_IS=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Operation>[] New_OS=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Operation>[] New_C=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Timelags> New_disjunctions=new ArrayList();
		ArrayList<Timelags> New_conjunctions=new ArrayList();
		ArrayList<OperationNode_Conjunctive> New_OperationNodeList=new ArrayList();
	
		//Initialize the IS, OS, Clique
		for(int index=1;index<=InputData.NumOfMachine;index++)
		{
			New_IS[index]=new ArrayList<Operation>();
			New_OS[index]=new ArrayList<Operation>();
			New_C[index]=new ArrayList<Operation>();
		}
		
		//Copy the IS, OS, Clique and Conjunctions from parent node
		for(Timelags Timelag:parentNode.get_Timelags_C())
		{
			New_conjunctions.add(Timelag);
		}
		
		//IS and OS
		boolean havescheduled=false;
		for(int Index=1;Index<=InputData.NumOfMachine;Index++)
		{
		    for(Operation inop:parentNode.get_InputSequence(Index))
		    {
		    	New_IS[Index].add(inop);
		    	if(inop.equalto(output))
		    	{havescheduled=true;}
		    }
		    for(Operation outop:parentNode.get_OutputSequence(Index))
		    {
		    New_OS[Index].add(outop);
		    if(outop.equalto(output))
	    	{havescheduled=true;}
		    }
		}
	        
	    //Maintain the IS, OS, disjunctions
		if(havescheduled!=true)
		{New_OS[output.get_machine()].add(output);}
		
		
		for(Timelags disjunction:parentNode.get_Disjunctions())
		{
			//Maintain the conjunctions start from inputs
			if(disjunction.get_to().equalto(output))
			{New_conjunctions.add(disjunction);}
			
			//Maintain the conjunction go to outputs
			if(disjunction.get_from().equalto(output)==false&&disjunction.get_to().equalto(output)==false)
			{New_disjunctions.add(disjunction);}	
		}
		
		New_OperationNodeList=CopyOperationList(parentNode.get_Operationnodelist_C(),New_conjunctions);
		GetOperationNode(output,New_OperationNodeList).isScheduled();
		//Initialize Start, Terminal
		ArrayList<OperationNode_Conjunctive> From_S=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> To_S=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> From_T=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> To_T=new ArrayList<OperationNode_Conjunctive>();
		Operation Start =new Operation(0,0);
		Operation Terminal =new Operation(InputData.NumOfJob+1,InputData.NumOfMachine+1);
		OperationNode_Conjunctive NewStart=new OperationNode_Conjunctive(Start,0,From_S,To_S);
		NewStart.isScheduled();
		OperationNode_Conjunctive NewTerminal=new OperationNode_Conjunctive(Terminal,0,From_T,To_T);
        for(OperationNode_Conjunctive Toop:parentNode.get_Start().get_to())
        {
        	NewStart.Addto(GetOperationNode(Toop.get_operation(),New_OperationNodeList));
        	GetOperationNode(Toop.get_operation(),New_OperationNodeList).Addfrom(NewStart);
        }
        for(OperationNode_Conjunctive Fop:parentNode.get_Terminal().get_from())
        {
        	NewTerminal.Addfrom(GetOperationNode(Fop.get_operation(),New_OperationNodeList));
        	GetOperationNode(Fop.get_operation(),New_OperationNodeList).Addto(NewTerminal);
        }
		
        //Add the operation into IS, when the clique only contain the one operation
        
		TreeNode NewNode=new TreeNode(parentNode,New_IS,New_OS,New_C,New_OperationNodeList,New_conjunctions,New_disjunctions,NewStart,NewTerminal);
		TotalNumOfNode++;
		//TreeNodeList.add(NewNode);
		if(NewNode.get_Lowerbound1()>UpperBound)
		{
			NewAnalysis.Update(2,NewNode);
		}
		if(NewNode.get_Lowerbound1()<UpperBound&&NewNode.getCmax()>UpperBound)
		{
			NewAnalysis.Update(15,NewNode);
		}
		ID++;
		LongestPath.Calculate(true,NewNode.get_Operationnodelist_C(),NewNode);
        LongestPath.Calculate(false,NewNode.get_Operationnodelist_C(),NewNode);
		NewNode.LowerBound1();
        NewNode.feasibleCheck();
        NewNode.Connect(parentNode);
        
        System.out.println("Current optimal = "+UpperBound);
    	System.out.println("This Cmax= "+NewNode.getCmax());
    	
        if(NewNode.infeasible()!=true&&NewNode.get_Disjunctions().size()==0&&NewNode.getCmax()<UpperBound)
        {
        	UpperBound=NewNode.getCmax();
            Optimal=NewNode;
         }
        
        if(NewNode.infeasible()!=true&&NewNode.get_Lowerbound1()<UpperBound&&NewNode.getCmax()<UpperBound&&NewNode.get_Disjunctions().size()!=0)
		{
        	System.out.println("NewNode in!!");
        	BranchList.push(NewNode);
        }
		
	}

	//***************************For input branch********************************
	private static void BrachingProcess(TreeNode parentNode, Integer Machineindex,
			Operation input) {
		ArrayList<Operation>[] New_IS=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Operation>[] New_OS=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Operation>[] New_C=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Timelags> New_disjunctions=new ArrayList();
		ArrayList<Timelags> New_conjunctions=new ArrayList();
		ArrayList<OperationNode_Conjunctive> New_OperationNodeList=new ArrayList();
	
		//Initialize the IS, OS, Clique
		for(int index=1;index<=InputData.NumOfMachine;index++)
		{
			New_IS[index]=new ArrayList<Operation>();
			New_OS[index]=new ArrayList<Operation>();
			New_C[index]=new ArrayList<Operation>();
		}
		
		//Copy the IS, OS, Clique and Conjunctions from parent node
		for(Timelags Timelag:parentNode.get_Timelags_C())
		{
			New_conjunctions.add(Timelag);
		}
		
		//IS and OS
		boolean havescheduled=false;
		for(int Index=1;Index<=InputData.NumOfMachine;Index++)
		{
		    for(Operation inop:parentNode.get_InputSequence(Index))
		    {
		    	New_IS[Index].add(inop);
		    	if(inop.equalto(input))
		    	{havescheduled=true;}
		    }
		    for(Operation outop:parentNode.get_OutputSequence(Index))
		    {
		    New_OS[Index].add(outop);
		    if(outop.equalto(input))
	    	{havescheduled=true;}
		    }
		}
	        
	    //Maintain the IS, OS, disjunctions
		if(havescheduled!=true)
		{New_IS[input.get_machine()].add(input);}
		
		
		for(Timelags disjunction:parentNode.get_Disjunctions())
		{
			//Maintain the conjunctions start from inputs
			if(disjunction.get_from().equalto(input))
			{New_conjunctions.add(disjunction);}
			
			//Maintain the conjunction go to outputs
			if(disjunction.get_from().equalto(input)==false&&disjunction.get_to().equalto(input)==false)
			{New_disjunctions.add(disjunction);}	
		}
		
		New_OperationNodeList=CopyOperationList(parentNode.get_Operationnodelist_C(),New_conjunctions);
		GetOperationNode(input,New_OperationNodeList).isScheduled();
		//Initialize Start, Terminal
		ArrayList<OperationNode_Conjunctive> From_S=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> To_S=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> From_T=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> To_T=new ArrayList<OperationNode_Conjunctive>();
		Operation Start =new Operation(0,0);
		Operation Terminal =new Operation(InputData.NumOfJob+1,InputData.NumOfMachine+1);
		OperationNode_Conjunctive NewStart=new OperationNode_Conjunctive(Start,0,From_S,To_S);
		NewStart.isScheduled();
		OperationNode_Conjunctive NewTerminal=new OperationNode_Conjunctive(Terminal,0,From_T,To_T);
        for(OperationNode_Conjunctive Toop:parentNode.get_Start().get_to())
        {
        	NewStart.Addto(GetOperationNode(Toop.get_operation(),New_OperationNodeList));
        	GetOperationNode(Toop.get_operation(),New_OperationNodeList).Addfrom(NewStart);
        }
        for(OperationNode_Conjunctive Fop:parentNode.get_Terminal().get_from())
        {
        	NewTerminal.Addfrom(GetOperationNode(Fop.get_operation(),New_OperationNodeList));
        	GetOperationNode(Fop.get_operation(),New_OperationNodeList).Addto(NewTerminal);
        }
		
        //Add the operation into IS, when the clique only contain the one operation
        
		TreeNode NewNode=new TreeNode(parentNode,New_IS,New_OS,New_C,New_OperationNodeList,New_conjunctions,New_disjunctions,NewStart,NewTerminal);
		TotalNumOfNode++;
		//TreeNodeList.add(NewNode);
		if(NewNode.get_Lowerbound1()>UpperBound)
		{
			NewAnalysis.Update(2,NewNode);
		}
		if(NewNode.get_Lowerbound1()<UpperBound&&NewNode.getCmax()>UpperBound)
		{
			NewAnalysis.Update(15,NewNode);
		}
		ID++;
		LongestPath.Calculate(true,NewNode.get_Operationnodelist_C(),NewNode);
        LongestPath.Calculate(false,NewNode.get_Operationnodelist_C(),NewNode);
		NewNode.LowerBound1();
        NewNode.feasibleCheck();
        NewNode.Connect(parentNode);
        
        System.out.println("Current optimal = "+UpperBound);
    	System.out.println("This Cmax= "+NewNode.getCmax());
    	
        if(NewNode.infeasible()!=true&&NewNode.get_Disjunctions().size()==0&&NewNode.getCmax()<UpperBound)
        {
        	UpperBound=NewNode.getCmax();
            Optimal=NewNode;
         }
        
        if(NewNode.get_Lowerbound1()<UpperBound&&NewNode.infeasible()!=true&&NewNode.getCmax()<UpperBound&&NewNode.get_Disjunctions().size()!=0)
		{
        	System.out.println("NewNode in!!");
        	BranchList.push(NewNode);
        }
		
	}

	//*********************************for pairs branch**************************** 
	private static void BrachingProcess(TreeNode parentNode, Integer MachineIndex, Operation input,
			Operation output) {
		ArrayList<Operation>[] New_IS=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Operation>[] New_OS=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Operation>[] New_C=new ArrayList[InputData.NumOfMachine+1];
		ArrayList<Timelags> New_disjunctions=new ArrayList();
		ArrayList<Timelags> New_conjunctions=new ArrayList();
		ArrayList<OperationNode_Conjunctive> New_OperationNodeList=new ArrayList();
	
		//Initialize the IS, OS, Clique
		for(int index=1;index<=InputData.NumOfMachine;index++)
		{
			New_IS[index]=new ArrayList<Operation>();
			New_OS[index]=new ArrayList<Operation>();
			New_C[index]=new ArrayList<Operation>();
		}
		
		//Copy the IS, OS, Clique and Conjunctions from parent node
		for(Timelags Timelag:parentNode.get_Timelags_C())
		{
			New_conjunctions.add(Timelag);
		}
		
		//IS and OS
		for(int Index=1;Index<=InputData.NumOfMachine;Index++)
		{
		    for(Operation inop:parentNode.get_InputSequence(Index))
		    {
			New_IS[Index].add(inop);
		    }
		    for(Operation outop:parentNode.get_OutputSequence(Index))
		    {
		    New_OS[Index].add(outop);
		    }
		}
	        
	    //Maintain the IS, OS, disjunctions
		if(output!=null)
		{New_IS[input.get_machine()].add(input);
		 New_OS[output.get_machine()].add(0,output);
		}
		
		for(Timelags disjunction:parentNode.get_Disjunctions())
		{
			//Maintain the conjunctions start from inputs
			if(disjunction.get_from().equalto(input))
			{New_conjunctions.add(disjunction);}
			
			//Maintain the conjunction go to outputs
			
			 if(disjunction.get_to().equalto(output)&&containTimelags(disjunction,New_conjunctions)==false)
			 {New_conjunctions.add(disjunction);}
			
			if(disjunction.get_from().equalto(input)==false&&disjunction.get_to().equalto(input)==false&&disjunction.get_from().equalto(output)==false&&disjunction.get_to().equalto(output)==false)
			{New_disjunctions.add(disjunction);}	
		}
		
		New_OperationNodeList=CopyOperationList(parentNode.get_Operationnodelist_C(),New_conjunctions);
		GetOperationNode(input,New_OperationNodeList).isScheduled();
		//Initialize Start, Terminal
		ArrayList<OperationNode_Conjunctive> From_S=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> To_S=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> From_T=new ArrayList<OperationNode_Conjunctive>();
		ArrayList<OperationNode_Conjunctive> To_T=new ArrayList<OperationNode_Conjunctive>();
		Operation Start =new Operation(0,0);
		Operation Terminal =new Operation(InputData.NumOfJob+1,InputData.NumOfMachine+1);
		OperationNode_Conjunctive NewStart=new OperationNode_Conjunctive(Start,0,From_S,To_S);
		NewStart.isScheduled();
		OperationNode_Conjunctive NewTerminal=new OperationNode_Conjunctive(Terminal,0,From_T,To_T);
        for(OperationNode_Conjunctive Toop:parentNode.get_Start().get_to())
        {
        	NewStart.Addto(GetOperationNode(Toop.get_operation(),New_OperationNodeList));
        	GetOperationNode(Toop.get_operation(),New_OperationNodeList).Addfrom(NewStart);
        }
        for(OperationNode_Conjunctive Fop:parentNode.get_Terminal().get_from())
        {
        	NewTerminal.Addfrom(GetOperationNode(Fop.get_operation(),New_OperationNodeList));
        	GetOperationNode(Fop.get_operation(),New_OperationNodeList).Addto(NewTerminal);
        }
		
        //Add the operation into IS, when the clique only contain the one operation
       /*
        if(parentNode.get_Clique(MachineIndex).size()==3)
        {
        	for(Operation op:parentNode.get_Clique(MachineIndex))
        	{
        		if(op.equalto(input)==false&&op.equalto(output)==false)
        		{New_IS[MachineIndex].add(op);}
        		
        	}
        }
        */
        
		TreeNode NewNode=new TreeNode(parentNode,New_IS,New_OS,New_C,New_OperationNodeList,New_conjunctions,New_disjunctions,NewStart,NewTerminal);
		//TreeNodeList.add(NewNode);
		TotalNumOfNode++;
		ID++;
		LongestPath.Calculate(true,NewNode.get_Operationnodelist_C(),NewNode);
        LongestPath.Calculate(false,NewNode.get_Operationnodelist_C(),NewNode);
		NewNode.LowerBound1();
		if(NewNode.get_Lowerbound1()>UpperBound)
		{
			NewAnalysis.Update(2,NewNode);
		}
		if(NewNode.get_Lowerbound1()<UpperBound&&NewNode.getCmax()>UpperBound)
		{
			NewAnalysis.Update(15,NewNode);
		}
        NewNode.feasibleCheck();
        NewNode.Connect(parentNode);
        
        System.out.println("Current optimal = "+UpperBound);
    	System.out.println("This Cmax= "+NewNode.getCmax());
    	
        if(NewNode.infeasible()!=true&&NewNode.get_Disjunctions().size()==0&&NewNode.getCmax()<UpperBound)
        {
        	UpperBound=NewNode.getCmax();
            Optimal=NewNode;
         }
        if(NewNode.infeasible()==true)
        {
        	System.out.println("Infeasible!!"); 	
        }
        if(NewNode.get_Lowerbound1()<UpperBound&&NewNode.infeasible()!=true&&NewNode.getCmax()<UpperBound&&NewNode.getCmax()<UpperBound&&NewNode.get_Disjunctions().size()!=0)
		{
        	System.out.println("NewNode in!!");
        	BranchList.push(NewNode);
        }
	}
	
	private static int LowerboundWK(Operation criOp, TreeNode parentNode, Integer index) {
		int Minihead=Integer.MAX_VALUE;
	    int Minitail=Integer.MAX_VALUE;
	    int SumOfPTWK=0;
        int lowerbound=Integer.MAX_VALUE;
	    
		for(Operation theop:parentNode.get_Clique(index))
		{
			if(theop.equalto(criOp)==false)
			{
			 SumOfPTWK+=GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_processingtime();
			 if(GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_heads()<Minihead)	
			 {Minihead=GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_heads();}
			 if(GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_tails()<Minitail)	
			 {Minitail=GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_tails();}
			}
		}
		lowerbound=(Minihead+SumOfPTWK+Minitail);
		return lowerbound;
	}

	private static int Lowerbound_givenOP(Operation criOp, TreeNode parentNode, Integer index) {
		int Minihead=Integer.MAX_VALUE;
	    int fixedtail=GetOperationNode(criOp,parentNode.get_Operationnodelist_C()).get_operation().get_tails();
	    int SumOfPT=0;
        int lowerbound=Integer.MAX_VALUE;
	    
		for(Operation theop:parentNode.get_Clique(index))
		{
			SumOfPT+=GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_processingtime();
			if(theop.equalto(criOp)==false)
			{
			 if(GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_heads()<Minihead)	
			 {Minihead=GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_heads();}
			}
		}
		lowerbound=(Minihead+SumOfPT+fixedtail);
		return lowerbound;
	}
	
	private static int Lowerbound_givenIP(Operation input, TreeNode parentNode, Integer index) {
		int fixedhead=GetOperationNode(input,parentNode.get_Operationnodelist_C()).get_operation().get_heads();
	    int Minitail=Integer.MAX_VALUE;
	    int SumOfPT=0;
        int lowerbound=Integer.MAX_VALUE;
	    
		for(Operation theop:parentNode.get_Clique(index))
		{
			SumOfPT+=GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_processingtime();
			if(theop.equalto(input)==false)
			{
			 if(GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_tails()<Minitail)	
			 {Minitail=GetOperationNode(theop,parentNode.get_Operationnodelist_C()).get_operation().get_tails();}
			}
		}
		lowerbound=(fixedhead+SumOfPT+Minitail);
		return lowerbound;
	}

	private static ArrayList<OperationNode_Conjunctive> CopyOperationList(
			ArrayList<OperationNode_Conjunctive> parentList, ArrayList<Timelags> Newconjunctions) {
		ArrayList<OperationNode_Conjunctive> thisList=new ArrayList();
		thisList.clear();
		//initialize the operationNode list
	
		for(OperationNode_Conjunctive theop:parentList)
		{
			Operation newop=new Operation(theop.get_operation().get_job(),theop.get_operation().get_machine());
			ArrayList<OperationNode_Conjunctive> newfrom=new ArrayList();
			ArrayList<OperationNode_Conjunctive> newto=new ArrayList();
			OperationNode_Conjunctive newOpNode=new OperationNode_Conjunctive(newop,theop.get_processingtime(),newfrom,newto);
		    if(theop.get_scheduled()==true)
		    {newOpNode.isScheduled();}
			thisList.add(newOpNode);
		}
		for(Timelags theT:Newconjunctions)
		{
			Operation from=theT.get_from();
			Operation to=theT.get_to();
			
			
			if(from.equalto(InputData.Start.get_operation())==false&&to.equalto(InputData.Terminal.get_operation())==false)
			{
		    GetOperationNode(from,thisList).Addto(GetOperationNode(to,thisList));
		    GetOperationNode(to,thisList).Addfrom(GetOperationNode(from,thisList));
			}
		}
		return thisList;
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

	public static int get_tOfOmega(ArrayList<OperationNode_Conjunctive> Omega){
		int tofomega=Integer.MAX_VALUE;
		for(OperationNode_Conjunctive op:Omega)
		{
		  if(op.get_operation().get_heads()+op.get_processingtime()<tofomega)
		  {tofomega=op.get_operation().get_heads()+op.get_processingtime();}
		}
		return tofomega;
	}

	public static boolean containTimelags(Timelags theLag,ArrayList<Timelags> List)
	{
		boolean contain=false;
		for(Timelags T:List)
		{
			if(theLag.get_from().equalto(T.get_from())&&theLag.get_to().equalto(T.get_to()))
					{contain=true;}
		}
		return contain;
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

	public static int ATSP(int Index,ArrayList<Operation> clique)
	{
		int miniTL=InputData.MiniTL[Index];
		int a=clique.size()/2;
		int b=clique.size()%2;
	    int NumOfArc=a*2+b;
	    int Distance=NumOfArc*miniTL;
	return Distance;
	}
    
}
