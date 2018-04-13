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

class C
{
    public int f( int x, int y ) { return 0; } // #5
}

class B extends C
{
    public int g( int x ) { return 0; } // #4
    public int h( int x ) { return 0; } // #7
}

class A extends B
{
    public int f( int x )    { return 0; } // #1
    public int f( double x ) { return 0; } // #2
    public int g( double x ) { return 0; } // #3
    public int g( int x ) { return 0; } // #4
    public int h( int x ) { return 0; } // #7
    public boolean h( int x ) { return 0; } // #7
    // against the overridden method in B)
    public int bar()
    {
        double b;
        int a;
        a = this.f( 1 );     // Resolve to method #1
        a = this.f( 1 + b ); // Resolve to method #2
        a = this.g( 1 );     // Resolve to method #4
        a = this.g( 1 + b ); // Resolve to method #3
        a = this.f( 1, 1 );  // Resolve to method #5

        return 0;
    }
}
