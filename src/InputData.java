import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class InputData {
	static int NumOfMachine = 6;
	static int NumOfJob = 6;
	static int [] MiniTL={0,0,0,0,0,0,0};
	static int[][] processingtime = new int[NumOfJob][NumOfMachine];
	static ArrayList<Operation>[] Clique = new ArrayList[NumOfMachine + 1];
	static ArrayList<Operation>[] InputSequence = new ArrayList[NumOfMachine + 1];
	static ArrayList<Operation>[] OutputSequence = new ArrayList[NumOfMachine + 1];

	static OperationNode_Conjunctive Start;
	static OperationNode_Conjunctive Terminal;

	static ArrayList<Operation> OperationList = new ArrayList<Operation>();
	static ArrayList<OperationNode_Disjunctive> OperationNodeList_D = new ArrayList<OperationNode_Disjunctive>();
	static ArrayList<OperationNode_Conjunctive> OperationNodeList_C = new ArrayList<OperationNode_Conjunctive>();
	static ArrayList<Timelags> TimelagsList_C = new ArrayList<Timelags>();
	static ArrayList<Timelags> TimelagsList_D = new ArrayList<Timelags>();
	public static void Initialize() {
		for (int num = 1; num < NumOfMachine + 1; num++) {
			ArrayList<Operation> List = new ArrayList<Operation>();
			ArrayList<Operation> input = new ArrayList<Operation>();
			ArrayList<Operation> output = new ArrayList<Operation>();
			Clique[num] = List;
			InputSequence[num] = input;
			OutputSequence[num] = output;
		}
	}

	public static void InputDisjunctivegraph() throws IOException {
		Operation thefrom;
		Operation theto;

		if (OperationList.size() == 0) {
			System.out.println("OperationList is empty");
		}
		File file = new File("C:/Users/user/Desktop/論文程式/timelags_dis.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		try {
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line); // get product ,
																// processing
																// time and
																// limited
																// waiting time

				try {
					thefrom = null;
					theto = null;
					if (st.countTokens() != 6) // The data file includes product
												// , processing time and limited
												// waiting time
					{
						throw new Exception();
					}
					int From_job = Integer.parseInt(st.nextToken()); // ArrayRowNum
																		// for #
																		// of
																		// products
					int From_machine = Integer.parseInt(st.nextToken());
					int To_job = Integer.parseInt(st.nextToken());
					int To_machine = Integer.parseInt(st.nextToken());
					int minimum = Integer.parseInt(st.nextToken());
					int maximum = Integer.parseInt(st.nextToken());
					// find the from and to operation
					for (Operation theoperation : OperationList) {
						if (From_job == theoperation.get_job()
								& From_machine == theoperation.get_machine()) {
							thefrom = theoperation;
						}
						if (To_job == theoperation.get_job()
								& To_machine == theoperation.get_machine()) {
							theto = theoperation;
						}
					}
					// add from and to
					for (OperationNode_Disjunctive fromnode : OperationNodeList_D) {
						if (fromnode.get_operation() == thefrom) {
							// System.out.println(thefrom.get_job()+","+thefrom.get_machine());
							for (OperationNode_Disjunctive tonode : OperationNodeList_D) {
								if (tonode.get_operation() == theto) {
									fromnode.get_to().add(tonode);
									tonode.get_from().add(fromnode);
								}
							}
						}
					}
					Timelags theTimelags = new Timelags(thefrom, theto,
							minimum, maximum);
					TimelagsList_D.add(theTimelags);
				} catch (Exception e) {
					System.err.println("Error: " + line);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void InputConjunctivegraph() throws IOException {
		Operation thefrom;
		Operation theto;

		if (OperationList.size() == 0) {
			System.out.println("OperationList is empty");
		}
		File file = new File("C:/Users/user/Desktop/論文程式/timelags_con.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		try {
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line); // get product ,
																// processing
																// time and
																// limited
																// waiting time

				try {
					thefrom = null;
					theto = null;
					if (st.countTokens() != 6) // The data file includes product
												// , processing time and limited
												// waiting time
					{
						throw new Exception();
					}
					int From_job = Integer.parseInt(st.nextToken()); // ArrayRowNum
																		// for #
																		// of
																		// products
					int From_machine = Integer.parseInt(st.nextToken());
					int To_job = Integer.parseInt(st.nextToken());
					int To_machine = Integer.parseInt(st.nextToken());
					int minimum = Integer.parseInt(st.nextToken());
					int maximum = Integer.parseInt(st.nextToken());
					// find the from and to operation
					for (Operation theoperation : OperationList) {
						if (From_job == theoperation.get_job()
								& From_machine == theoperation.get_machine()) {
							thefrom = theoperation;
						}
						if (To_job == theoperation.get_job()
								& To_machine == theoperation.get_machine()) {
							theto = theoperation;
						}
					}
					// add from and to
					for (OperationNode_Conjunctive fromnode : OperationNodeList_C) {

						if (fromnode.get_operation() == thefrom) {
							// System.out.println(thefrom.get_job()+","+thefrom.get_machine());
							for (OperationNode_Conjunctive tonode : OperationNodeList_C) {
								if (tonode.get_operation() == theto) {
									// System.out.print(thefrom.get_job()+","+thefrom.get_machine()+" ");
									// System.out.print(theto.get_job()+","+theto.get_machine()+" ");
									fromnode.get_to().add(tonode);
									tonode.get_from().add(fromnode);
									// System.out.print(fromnode.get_operation().get_job()+" "+fromnode.get_operation().get_machine()+" ");
									// System.out.println(fromnode.get_from().size()+" "+fromnode.get_to().size());
								}
							}
						}
					}
					Timelags theTimelags = new Timelags(thefrom, theto,
							minimum, maximum);
					TimelagsList_C.add(theTimelags);
				} catch (Exception e) {
					System.err.println("Error: " + line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void Createoperation() throws IOException {
		File file = new File("C:/Users/user/Desktop/論文程式/Processingtime.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		try {
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line); // get product ,
																// processing
																// time and
																// limited
																// waiting time

				try {
					if (st.countTokens() != 3) // The data file includes product
												// , processing time and limited
												// waiting time
					{
						throw new Exception();
					}
					int job = Integer.parseInt(st.nextToken()); // ArrayRowNum
																// for # of
																// products
					int machine = Integer.parseInt(st.nextToken());
					int Processingtime = Integer.parseInt(st.nextToken());
					// create Operation and OperationList
					Operation new_operation = new Operation(job, machine);
					OperationList.add(new_operation);
					// initialize the information of OperationNode
					ArrayList<OperationNode_Disjunctive> From_D = new ArrayList<OperationNode_Disjunctive>();
					ArrayList<OperationNode_Disjunctive> To_D = new ArrayList<OperationNode_Disjunctive>();
					ArrayList<OperationNode_Conjunctive> From_C = new ArrayList<OperationNode_Conjunctive>();
					ArrayList<OperationNode_Conjunctive> To_C = new ArrayList<OperationNode_Conjunctive>();

					OperationNode_Disjunctive new_OperationNode_D = new OperationNode_Disjunctive(
							new_operation, Processingtime, From_D, To_D);
					OperationNodeList_D.add(new_OperationNode_D);
					OperationNode_Conjunctive new_OperationNode_C = new OperationNode_Conjunctive(
							new_operation, Processingtime, From_C, To_C);
					OperationNodeList_C.add(new_OperationNode_C);
				} catch (Exception e) {
					System.err.println("Error: " + line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void ConnectSandT(ArrayList<OperationNode_Conjunctive> theList) throws IOException{
	Operation theOpe;
	ArrayList<OperationNode_Conjunctive> From_S=new ArrayList<OperationNode_Conjunctive>();
	ArrayList<OperationNode_Conjunctive> To_S=new ArrayList<OperationNode_Conjunctive>();
	ArrayList<OperationNode_Conjunctive> From_T=new ArrayList<OperationNode_Conjunctive>();
	ArrayList<OperationNode_Conjunctive> To_T=new ArrayList<OperationNode_Conjunctive>();
	Operation Startoperation=new Operation(0,0);
	Operation Terminaloperation=new Operation(NumOfJob+1,NumOfMachine+1);
	 Start=new OperationNode_Conjunctive(Startoperation,0,From_S,To_S);
	 Start.isScheduled();
	 Terminal=new OperationNode_Conjunctive(Terminaloperation,0,From_T,To_T);
	
	 
	 File file = new File("C:/Users/user/Desktop/論文程式/ConnectSandT.txt"); 
		FileReader fr = new FileReader(file);
	     BufferedReader br = new BufferedReader(fr);
	     try {    
	    	 String line;  
	         while( ( line = br.readLine( ) ) != null )
	         {
	             StringTokenizer st = new StringTokenizer( line ); // get product , processing time and limited waiting time

	             try
	             {
	            	 theOpe=null;
	                 if( st.countTokens( ) != 5)              // The data file includes product , processing time and limited waiting time 
	                     {throw new Exception( );}                     
	                 int ConnectType= Integer.parseInt( st.nextToken( ) ); // 0:Connect with S node 1:Connect with T Node                  
	                 int job=Integer.parseInt(st.nextToken());
	                 int machine= Integer.parseInt( st.nextToken( ) );
	                 int minimum=Integer.parseInt( st.nextToken( ) );
	                 int maximum=Integer.parseInt(st.nextToken());
	                 for(Operation thisOpe:OperationList)
	                 {
	                	if(thisOpe.get_job()==job&&thisOpe.get_machine()==machine)
	                	{
	                		theOpe=thisOpe;
	                	}
	                 }
	                 for(OperationNode_Conjunctive Ope:OperationNodeList_C)
	                 {
	                	 if(Ope.get_operation()==theOpe)
	                	 {
	                	     if(ConnectType==0)
	                	     {
	                		   Ope.get_from().add(Start);
	                		   Start.get_to().add(Ope);
	                		 
	                		   Timelags theTimelags =new Timelags(Start.get_operation(),Ope.get_operation(),minimum,maximum);
	                	       TimelagsList_C.add(theTimelags);
	                	     }
	                	     else
	                	     {
	                		   Ope.get_to().add(Terminal);
	                		   Terminal.get_from().add(Ope);
	                		   Timelags theTimelags =new Timelags(Ope.get_operation(),Terminal.get_operation(),minimum,maximum);
		                	   TimelagsList_C.add(theTimelags);
	                     	 }
	                	 }
	                 }
	                 
	             }
	             catch( Exception e )
	               { System.err.println( "Error: " + line ); }
	          }
	     }catch (FileNotFoundException e){
	    	 e.printStackTrace();
	    	 }	
}
}
