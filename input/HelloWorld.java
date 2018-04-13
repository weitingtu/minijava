/* HelloWorld.java
 */

public class HelloWorld
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        boolean a = true;
        boolean b = false;
        if ( a && b )
        {
            //a = 1;
        }
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
    public int h( int x ) { return 0; } // #7
}

class A extends B
{
    public int f( int x )    { return 0; } // #1
    public int f( double x ) { return 0; } // #2
    public int g( double x ) { return 0; } // #3
    //public boolean h( int x ) { return true; } // #6 (Error: Have different return type
    // against the overridden method in B)
    public int bar()
    {
        double b = 0.0;
        int a = 0;
        a = this.f( 1 );     // Resolve to method #1
        a = this.f( 1 + b ); // Resolve to method #2
        a = this.g( 1 );     // Resolve to method #4
        a = this.g( 1 + b ); // Resolve to method #3
        a = this.f( 1, 1 );  // Resolve to method #5

        return 0;
    }
}
