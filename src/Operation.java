
public class Operation {
	private int job;
	private int machine;
	private int heads;
	private int tails;
public Operation(int job,int machine){
	this.job=job;
	this.machine=machine;
	this.heads=0;
	this.tails=0;
	
}
public int get_job(){
	return this.job;
}
public int get_machine(){
	return this.machine;
}
public String get_ID()
{
	String ID;
	ID="("+this.job+","+this.machine+")";
	return ID;
}

public void updateHeads(int New){
	this.heads=New;
}

public void updateTails(int New){
	this.tails=New;
}
public int get_heads(){
	return this.heads;
}

public int get_tails(){
	return this.tails;
}

public boolean equalto(Operation theop){
	boolean identical=false;
	if(theop==null)
	{}
	else if(this.job==theop.get_job()&&this.machine==theop.machine)
	{
		identical=true;
	}
	return identical;
}

}
