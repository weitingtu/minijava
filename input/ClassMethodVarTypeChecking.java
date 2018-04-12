class Redeclaration
{
    public static void main( String[] a )
    {
        System.out.println( new foo().Start( 10 ) );
    }
}
// This class contains an array of integers and
// methods to initialize, print and search the array
// using Binary Search

class foo
{
    public int Start( int sz )
    {
        return 0;
    }

    public int foo()
    {
        int foo;

        foo bar;          // refer to class main
        foo = this.foo(); // refer to method
        foo = foo + foo;  // refer to variable
        return 0;
    }
}
