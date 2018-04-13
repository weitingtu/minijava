class Redeclaration
{
    public static void main( String[] a )
    {
        System.out.println( new Test().Start( 10 ) );
    }
}
// This class contains an array of integers and
// methods to initialize, print and search the array
// using Binary Search

class Test
{
    int size ;

    public int Start( int sz )
    {
        int sz;
        return 0;
    }
}

class Test2
{
    int size ;
    int sz;

    public int Start( int size )
    {
        int sz;
        return 0;
    }
}

class Test3
{
    int size ;
    int sz;

    public int Start( int size )
    {
        int sz;
        int sz;
        return 0;
    }
}

