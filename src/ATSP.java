import java.util.ArrayList;


public class ATSP {

	private int Distance;
	public ATSP(int Index,ArrayList<Operation> clique)
	{
		int miniTL=InputData.MiniTL[Index];
		int a=clique.size()/2;
		int b=clique.size()%2;
	    int NumOfArc=a*2+b;
	    this.Distance=NumOfArc*miniTL;
	}
	
	
}
