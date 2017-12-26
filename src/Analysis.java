
public class Analysis {
private double AmountOfPro2;
private double AmountOfPro3;
private double AmountOfPro4;
private double AmountOfPro5;
private double AmountOfPro6;
private double AmountOfPro7;
private double AmountOfPro8;
private double AmountOfPro11;
private double AmountOfPro12;
private double AmountOfPro15;
private double AmountOfDis2;
private double AmountOfDis3;
private double AmountOfDis4;
private double AmountOfDis5;
private double AmountOfDis6;
private double AmountOfDis7;
private double AmountOfDis8;
private double AmountOfDis11;
private double AmountOfDis12;
private double AmountOfDis15;
private double total;

public Analysis(){
	this.AmountOfPro2=0;
	this.AmountOfPro3=0;
	this.AmountOfPro4=0;
	this.AmountOfPro5=0;
	this.AmountOfPro6=0;
	this.AmountOfPro7=0;
	this.AmountOfPro8=0;
	this.AmountOfPro11=0;
	this.AmountOfPro12=0;
	this.AmountOfPro15=0;
	this.AmountOfDis2=0;
	this.AmountOfDis3=0;
	this.AmountOfDis4=0;
	this.AmountOfDis5=0;
	this.AmountOfDis6=0;
	this.AmountOfDis7=0;
	this.AmountOfDis8=0;
	this.AmountOfDis11=0;
	this.AmountOfDis12=0;
	this.AmountOfDis15=0;
	
	this.total=0;
}

 public void Update(int Proposition,TreeNode Parent)
 { 
	 double unscheduled=1;
	 switch(Proposition)
	 {
	
	 case 2:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro2+=unscheduled;
		 //this.AmountOfPro2++; 
		 //this.AmountOfDis2+=NumOfDis;
		 break;
	 case 3:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro3+=unscheduled;
		 //this.AmountOfPro3++;
	      //this.AmountOfDis3+=NumOfDis;
	      break;
	 case 4:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro4+=unscheduled; 
	      //this.AmountOfPro4++;
	      //this.AmountOfDis4+=NumOfDis;
	      break;
	 case 5:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro5+=unscheduled; 
	      //this.AmountOfPro5++;
	      //this.AmountOfDis5+=NumOfDis;
	      break;
	 case 6:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro6+=unscheduled;
	      //this.AmountOfPro6++;
	      //this.AmountOfDis6+=NumOfDis;
	      break;
	 case 7:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro7+=unscheduled; 
		  //this.AmountOfPro7++;
	      //this.AmountOfDis7+=NumOfDis;
	      break;
	 case 8:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro8+=unscheduled; 
		 //this.AmountOfPro8++;
	      //this.AmountOfDis8+=NumOfDis;
	      break;
	 case 11:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro11+=unscheduled;  
		 //this.AmountOfPro11++;
	     // this.AmountOfDis11+=NumOfDis;
	      break;
	 case 12:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro12+=unscheduled;  
		 //this.AmountOfPro12++;
	      //this.AmountOfDis12+=NumOfDis;
	      break;
	 case 15:
		 for(int index=1;index<=InputData.NumOfMachine;index++)
		 {
			 unscheduled=1;
			 if(Parent.get_Clique(index).size()!=0)
			 {
		     unscheduled=rfc(Parent.get_Clique(index).size());
		     }
			 unscheduled*=unscheduled;
		 }
			 this.AmountOfPro15+=unscheduled;  
		  //this.AmountOfPro15++;
	      //this.AmountOfDis15+=NumOfDis;
	      break;
	      
	 default:
		  break; 
	 }
 }

 public String PrintAnalysis(){
	 String Print="";
	 double Total=this.AmountOfPro2+this.AmountOfPro3+this.AmountOfPro4+this.AmountOfPro5+this.AmountOfPro6+this.AmountOfPro7+this.AmountOfPro8+this.AmountOfPro11+this.AmountOfPro12+this.AmountOfPro15;
	 Print="Total amount£º"+Total+"\n"+"Proposition 2 =>"+this.AmountOfPro2/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis2/this.AmountOfPro2+"\n\n"+"Proposition 3 =>"+(this.AmountOfPro3/Total)+"\n"+"The averange NumOfDis =>"+this.AmountOfDis3/this.AmountOfPro3+"\n\n"+"Proposition 4 =>"+(this.AmountOfPro4/Total)+"\n"+"The averange NumOfDis =>"+this.AmountOfDis4/this.AmountOfPro4+"\n\n"+"Proposition 5 =>"+this.AmountOfPro5/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis5/this.AmountOfPro5+"\n\n"+"Proposition 6 =>"+this.AmountOfPro6/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis6/this.AmountOfPro6+"\n\n"+"Proposition 7 =>"+this.AmountOfPro7/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis7/this.AmountOfPro7+"\n\n"+"Proposition 8 =>"+this.AmountOfPro8/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis8/this.AmountOfPro8+"\n\n"+"Proposition 11 =>"+this.AmountOfPro11/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis11/this.AmountOfPro11+"\n\n"+"Proposition 12 =>"+this.AmountOfPro12/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis12/this.AmountOfPro12+"\n\n"+"Proposition 15 =>"+this.AmountOfPro15/Total+"\n"+"The averange NumOfDis =>"+this.AmountOfDis15/this.AmountOfPro15;
	 
	 return Print;
 }

 public static double rfc(int g)
 {
switch (g)
{
case 1:
   return 1;
   //break;
default:
   return  g * rfc(g - 1);
}
 }
 
}
