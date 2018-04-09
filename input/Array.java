class Array
{
    public static void main( String[] a )
    {
        System.out.println( new Test().Start( 20 ) );
    }
}
// This class contains an array of integers and
// methods to initialize, print and search the array
// using Binary Search

class Test
{
    int [] number;
    int i;
    double [] doubles;
    double d;

    public int Start( int sz )
    {
        number = new int[sz];
        i = number[0];
        number[1] = i;
        number[2] = 10;

        doubles = new double[sz];
        d = doubles[0];
        doubles[1] = d;
        doubles[2] = 10.0;

        d = number[0];
        number[1] = d;
        number[2] = 10.0;

        i = doubles[0];
        doubles[1] = i;
        doubles[2] = 10;

        return 0;
    }
}
