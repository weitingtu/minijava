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

class C extends B
{
}

class B extends A
{
}

class A extends C
{
}

