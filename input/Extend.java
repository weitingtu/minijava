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

class X extends Y
{
    public int foo()
    {
        x = this.y(); // The x and y should be resolved
        // to the x and y() in class Y
        return x;
    }

    public int y ( int x, int y ) // overriding
    {
        return 0;
    }

    public boolean y ( int x )    // overriding
    {
        return true;
    }

    public int y()   // overloading and overriding
    {
        return 0;
    }
}

class Y
{
    int x;
    public int y()
    {
        return 0;
    }
}

