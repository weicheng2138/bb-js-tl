import ilog.concert.*;
import ilog.cplex.*;
import java.io.*;  
import java.util.ArrayList;
//import jxl.*;

public class LB_ATSP {
	public static void main(String[] args) {
		
	}
    public static int Calculate(int Machineindex, ArrayList<Operation> theClique, ArrayList<Timelags> disjunctions) {
        int n=theClique.size();                                                                   //number of cities
        String input;                                                            //input for xls.
        int row=theClique.size();                                                                 //row of xls.
        int column=theClique.size();                                                              //column of xls.
        double ATSP=0;
        boolean Stop=true;
        try{
            double distanceMetrix[][] = new double[row][column];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) 
                {
                	if(i!=j)
                	{
                    double distance = GetTimelag(disjunctions,theClique.get(i),theClique.get(j)).get_mini();
                    if(distance!=0)
                    {Stop=false;}
                    //System.out.println("**************>"+distance);
                    distanceMetrix[i][j] = distance;
                	}
                }
            }
            if(Stop==true)
            {return 0;}
            IloCplex TSPcplex = new IloCplex();                                  //model defined
            IloIntVar[][] x = new IloIntVar[n][n];                               //boolean variable for Xij
            IloIntVar[] u = TSPcplex.intVarArray(n, 0, n);                       //u variables for eliminating subtour
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    x[i][j] = TSPcplex.intVar(0, 1);
                }
            }
            
            for (int i = 0; i < n; i++) {                                        //ST ROW=1; summation x[i][j]=1 & i!=j 
                IloLinearNumExpr ST1Func = TSPcplex.linearNumExpr();
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        ST1Func.addTerm(1, x[i][j]);
                    }
                }
                TSPcplex.addEq(ST1Func, 1);
            }
            
            for (int j = 0; j < n; j++) {                                        //ST COLUMN=1; summation x[i][j]=1 & i!=j 
                IloLinearNumExpr ST2Func = TSPcplex.linearNumExpr();
                for (int i = 0; i < n; i++) {
                    if (i != j) {
                        ST2Func.addTerm(1, x[i][j]);
                    }
                }
                TSPcplex.addEq(ST2Func, 1);
            }
            
            for(int i=1; i<n; i++){                                              //ST for eliminating subtours
                for(int j=1; j<n; j++){                     
                       if(i!=j){
                       IloLinearNumExpr ST3Func = TSPcplex.linearNumExpr();
                       ST3Func.addTerm(1, u[i]);
                       ST3Func.addTerm(-1, u[j]);
                       ST3Func.addTerm(n, x[i][j]);
                       TSPcplex.addLe(ST3Func, n-1);
                       
                   }
                }
            }
            
            IloLinearNumExpr ObjectFunc = TSPcplex.linearNumExpr();               //Objective function
            for(int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    if(i!=j){
                        ObjectFunc.addTerm(distanceMetrix[i][j], x[i][j]);
                    }
                }
            }

            TSPcplex.addMinimize(ObjectFunc);
            if (TSPcplex.solve()) {
               // System.out.println("The optimal tour distance is " + TSPcplex.getObjValue());
                ATSP=TSPcplex.getObjValue();
                TSPcplex.end();
            }

        }
        catch (Exception e){
            //System.err.println("Concert exception caught: " + e);
	}
    return (int)ATSP;
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
