class Redeclaration
{
    public static void main( String[] a )
    {
        System.out.println( new Test().Start() );
    }
}
// This class contains an array of integers and
// methods to initialize, print and search the array
// using Binary Search

class Test
{
    int size ;

    public int Start()
    {
        return 0;
    }
}

class X
{
    public int F ( int i )
    {
        return i;
    }
}

class Y extends X
{
    public boolean F ( int i )
    {
        return i; // error double is not compatible with int in X::F(int)
    }
}
