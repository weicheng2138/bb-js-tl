
public class Timelags {
private int minimum_T;
private int maximum_T;
private Operation from;
private Operation to;
public Timelags(Operation From,Operation To,int minimum,int maximum)
{
	this.from=From;
	this.to=To;
	this.minimum_T=minimum;
	this.maximum_T=maximum;
}
public Operation get_from()
{return this.from;}
public Operation get_to()
{return this.to;}
public int get_mini()
{return this.minimum_T;}
public int get_maxi()
{return this.maximum_T;}

}
