import java.util.*;

public class LongestPath {
    static ArrayList<OperationNode_Conjunctive> schedulableList=new ArrayList<OperationNode_Conjunctive>();
	public static void Calculate(Boolean fromS,ArrayList<OperationNode_Conjunctive> NodeList, TreeNode theNode) {
		OperationNode_Conjunctive Start;
		schedulableList.clear();
		if(fromS)
		{
			//Calculate the longest path from start Node
			//initialize schedulable list
			int iterator=0;
			Start=theNode.get_Start();
			Start.scheduled=true;
		    for(OperationNode_Conjunctive Ope:Start.get_to())
		    {
		    	if(Ope.get_from().size()==1)
		    	{Ope.scheduled=true;
		        schedulableList.add(Ope);}
		    }
		    
		    //initialize END
		    Boolean KeepCalculate=true;
		    do{     
		    	 //if(schedulableList.size()==InputData.OperationList.size())
			        //{KeepCalculate=false;}
		    	//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@"+schedulableList.size());
		    	if(iterator==theNode.get_Timelags_C().size())
		    	 {KeepCalculate=false;}
		    	 iterator++;	
		    	for(OperationNode_Conjunctive Op:schedulableList)
		    	{
		    		
		    	    Op.mantainStatingTime(theNode);	   
		    		Op.get_operation().updateHeads(GetMaximum_heads(theNode.get_Timelags_C(),Op));
		    		//System.out.println(Op.get_ID()+" heads: "+Op.get_operation().get_heads()+"to: "+Op.get_to().size()+" "+Op.get_to().get(0).get_ID()+" from: "+Op.get_from().size()+" "+Op.get_from().get(0).get_ID());
		    		/*
		    		for(OperationNode_Conjunctive from:Op.get_to().get(0).get_from())
		    		{System.out.println(from.get_ID()+" "+from.scheduled);}
		    		*/
		    		Op.scheduled=true;
		    	}
		    	schedulableList.clear();
		    	for(OperationNode_Conjunctive theOp:NodeList)
			    	{
			    	
			    		if(MaintainList(fromS,theOp))
			    		{
			    		schedulableList.add(theOp);
			    		}
			    	}
		    	
		    }while(KeepCalculate);
		    theNode.UpdateCmax(GetMaximum_heads(theNode.get_Timelags_C(),theNode.get_Terminal()));
		 	//Test*************************************************
	    	/*
		    System.out.println("====================heads=======================");
		    for(OperationNode_Conjunctive Op:schedulableList)
			 {
	    		System.out.print(Op.get_ID()+"=> ");
	    		System.out.println(Op.get_operation().get_heads());
			 }
			 */
	    	//Test END*********************************************
		   
		}
		if(fromS==false)
		{
			int iterator=0;
			//Calculate the longest path from terminal node
			//initialize the schedulable list
			Start=theNode.get_Terminal();
			Start.scheduled_tail=true;
		    for(OperationNode_Conjunctive Ope:Start.get_from())
		    {
		    	if(Ope.get_to().size()==1)
		    	{Ope.scheduled_tail=true;
		        schedulableList.add(Ope);}
		    }
			//initialize END
			Boolean KeepCalculate=true;
		    do{ 
		    	 if(iterator==theNode.get_Timelags_C().size())
		    	 {KeepCalculate=false;}
		    	 iterator++;	
		    	 //System.out.println("schedulable List:");
		        for(OperationNode_Conjunctive Op:schedulableList)
		    	{
		        	//System.out.println(Op.get_ID());
		    	    Op.mantainStatingTime(theNode);	   
		    		Op.get_operation().updateTails(GetMaximum_tails(theNode.get_Timelags_C(),Op));
		    		Op.scheduled_tail=true;
		    	}
		    	schedulableList.clear();
		    	for(OperationNode_Conjunctive theOp:NodeList)
			    	{
			    		if(MaintainList(fromS,theOp))
			    		{
			    		schedulableList.add(theOp);
			    		}
			    	}	
		    }while(KeepCalculate);
		 	//Test*************************************************
		    /*
	    	System.out.println("====================Tails=======================");
		    for(OperationNode_Conjunctive Op:schedulableList)
			 {
	    		System.out.print(Op.get_ID()+"=> ");
	    		System.out.println(Op.get_operation().get_tails());
			 }
			 */
	    	//Test END*********************************************
		}
	
		
		
	}
	 private static int GetMaximum_heads(ArrayList<Timelags> TimelagList,OperationNode_Conjunctive op) {
		 int Maximum=0;
		for(OperationNode_Conjunctive fromOp:op.get_from())
		{	
		if(Maximum<fromOp.get_operation().get_heads()+
				fromOp.get_processingtime()+
				GetTimelag(TimelagList,fromOp.get_operation(),op.get_operation()).get_mini())	
		{Maximum=fromOp.get_operation().get_heads()+fromOp.get_processingtime()+GetTimelag(TimelagList,fromOp.get_operation(),op.get_operation()).get_mini();}
		}
		return Maximum;
	}
	 private static int GetMaximum_tails(ArrayList<Timelags> TimelagList,OperationNode_Conjunctive op) {
		 int Maximum=0;
		
		for(OperationNode_Conjunctive toOp:op.get_to())
		{
		if(Maximum<(toOp.get_operation().get_tails()+
				toOp.get_processingtime()+
				GetTimelag(TimelagList,op.get_operation(),toOp.get_operation()).get_mini()))	
		{
			Maximum=(toOp.get_operation().get_tails()+
					toOp.get_processingtime()+
					GetTimelag(TimelagList,op.get_operation(),toOp.get_operation()).get_mini());}
		}
		return Maximum;
	}
	private static Boolean MaintainList(Boolean FromS,OperationNode_Conjunctive theOpe)
	{
		Boolean schedulable = true;
		if (FromS == true) {
			if (theOpe.scheduled == true) {
				schedulable = false;
			}
			if (FromS == true && theOpe.scheduled == false) {
				// System.out.println(theOpe.get_ID());
				for (OperationNode_Conjunctive Ope : theOpe.get_from()) {
					if (Ope.scheduled == false) {
						// System.out.println(Ope.get_ID()+"is not calculate");
						schedulable = false;
					}
				}
			}
		}
		if (FromS == false) {
			if (theOpe.scheduled_tail == true) {
				schedulable = false;
			}
			if (FromS == false & theOpe.scheduled_tail == false) {
				for (OperationNode_Conjunctive Ope : theOpe.get_to()) {
					if (Ope.scheduled_tail == false) {
						schedulable = false;
					}
				}
			}
		}
		return schedulable;
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


