import java.util.ArrayList;
public class OperationNode_Disjunctive {
private Operation operation;
private int processingtime;
private ArrayList<OperationNode_Disjunctive> from;
private ArrayList<OperationNode_Disjunctive> to;
private Timelags timelags;

public OperationNode_Disjunctive(Operation Operation,int Processingtime, ArrayList<OperationNode_Disjunctive> From, ArrayList<OperationNode_Disjunctive> To){
	this.operation=Operation;
	this.processingtime=Processingtime;
	this.from=From;
	this.to=To;

}
public Operation get_operation(){
	return this.operation;
}
public int get_processingtime(){
	return this.processingtime;
}
public ArrayList<OperationNode_Disjunctive> get_from(){
	return this.from;
}
public ArrayList<OperationNode_Disjunctive> get_to(){
	return this.to;
}
public String get_ID(){
	String operation;
	operation="("+this.operation.get_job()+","+this.operation.get_machine()+")";
	return operation;
}
}
