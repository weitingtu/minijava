package visitor;
import syntaxtree.*;

public class SymbolUsageVisitor extends DepthFirstVisitor
{

    static Class currClass;
    static Method currMethod;
    static SymbolTable symbolTable;
    private String target;
    private int idRef;

    public SymbolUsageVisitor( SymbolTable s, String target )
    {
        symbolTable = s;
        this.target = target;
        idRef = 0;
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit( Program n )
    {
        n.m.accept( this );
        for ( int i = 0; i < n.cl.size(); i++ )
        {
            n.cl.elementAt( i ).accept( this );
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit( MainClass n )
    {
        String i1 = n.i1.toString();
        currClass = symbolTable.getClass( i1 );
        if ( target.equals( i1 ) )
        {
            print( currClass );
        }

        // ignore main parameter checking
        n.i2.accept( this );
        n.s.accept( this );
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit( ClassDeclSimple n )
    {
        String id = n.i.toString();
        currClass = symbolTable.getClass( id );
        if ( target.equals( id ) )
        {
            print( currClass );
        }
        for ( int i = 0; i < n.vl.size(); i++ )
        {
            n.vl.elementAt( i ).accept( this );
        }
        for ( int i = 0; i < n.ml.size(); i++ )
        {
            n.ml.elementAt( i ).accept( this );
        }
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit( ClassDeclExtends n )
    {
        String id = n.i.toString();
        currClass = symbolTable.getClass( id );
        if ( target.equals( id ) )
        {
            print( currClass );
        }
        n.j.accept( this );
        for ( int i = 0; i < n.vl.size(); i++ )
        {
            n.vl.elementAt( i ).accept( this );
        }
        for ( int i = 0; i < n.ml.size(); i++ )
        {
            n.ml.elementAt( i ).accept( this );
        }
    }

    // Type t;
    // Identifier i;
    public void visit( VarDecl n )
    {
        n.t.accept( this );
        //n.i.accept(this);
        if ( target.equals( n.i.toString() ) )
        {
            print( n.i.toString(), currClass, currMethod );
        }
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit( MethodDecl n )
    {
        n.t.accept( this );
        String id = n.i.toString();
        currMethod = currClass.getMethod( id );
        if ( target.equals( id ) )
        {
            print( currMethod, currClass );
        }
        for ( int i = 0; i < n.fl.size(); i++ )
        {
            n.fl.elementAt( i ).accept( this );
        }
        for ( int i = 0; i < n.vl.size(); i++ )
        {
            n.vl.elementAt( i ).accept( this );
        }
        for ( int i = 0; i < n.sl.size(); i++ )
        {
            n.sl.elementAt( i ).accept( this );
        }
        n.e.accept( this );
        currMethod = null;
    }

    // Type t;
    // Identifier i;
    public void visit( Formal n )
    {
        if ( target.equals( n.i.toString() ) )
        {
            print( n, currMethod, currClass );
        }
        //n.t.accept( this );
        //n.i.accept( this );
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit( Call n )
    {
        n.e.accept( this );
        n.i.accept( this );
        for ( int i = 0; i < n.el.size(); i++ )
        {
            n.el.elementAt( i ).accept( this );
        }
    }

    // String s;
    public void visit( IdentifierExp n )
    {
        if ( target.equals( n.s.toString() ) )
        {
            print( n );
        }
    }

    // String s;
    public void visit( Identifier n )
    {
        if ( target.equals( n.toString() ) )
        {
            print( n );
        }
    }

    private void print( Class c )
    {
        // IdRef-of-X, Class, Foo, Bar
        // IdRef-of-x, Class
        idRef = c.getIdRef();
        System.out.print( c.getId() + " " + c.getIdRef() + ", Class" );
        String parent = c.parent();
        while ( parent != null )
        {
            Class parentClass = symbolTable.getClass( parent );
            System.out.println( ", " + parentClass.getId() );
            parent = parentClass.parent();
        }
        System.out.println();
    }

    private String getTypeString( Type t )
    {
        if ( t instanceof IntArrayType )
        {
            return "int []";
        }
        if ( t instanceof DoubleArrayType )
        {
            return "double []";
        }
        if ( t instanceof BooleanType )
        {
            return "boolean";
        }
        if ( t instanceof IntegerType )
        {
            return "int";
        }
        if ( t instanceof DoubleType )
        {
            return "double";
        }
        if ( t instanceof IdentifierType )
        {
            return ((IdentifierType) t).s;
        }
        System.exit( -1 );
        return "";
    }

    private void print( String id, Class c, Method m )
    {
        Variable var = null;
        if ( m != null )
        {
            var = m.getVar( id );
            if ( var == null )
            {
                System.out.println( "Unable to get local " + id + " for " + c.getId() + "::" + m.getId() );
                System.exit( -1 );
            }
            idRef = var.idRef();
            // IdRef-of-X, Local, type-of-X, Bar::foo()
            System.out.println( var.id() + " " + var.idRef() + ", Local, " + getTypeString( var.type() ) + ", " + c.getId() + "::" +
                                m.getId() );
        }
        else if ( c != null && c.getVar( id ) != null )
        {
            var = c.getVar( id );
            if ( var == null )
            {
                System.out.println( "Unable to get data member " + id + " for " + c.getId() );
                System.exit( -1 );
            }
            idRef = var.idRef();
            // IdRef-of-X, Data member, type-of-X, Bar
            System.out.println( var.id() + " " + var.idRef() + ", Data member, " + getTypeString( var.type() ) + ", " + c.getId() );
        }
        else
        {
            System.out.println( "Print " + id + " with null class and method" );
            System.exit( -1 );
        }
    }

    private void print( Method m, Class c )
    {
        // IdRef-of-X, Bar, int (int a, int [] b, boolean c)
        // IdRef-of-X, Foo, int [] ()
        idRef = m.getIdRef();
        System.out.print( m.getId() + " " + m.getIdRef() + ", " + c.getId() + ", " + getTypeString( m.type() )  + " (" );
        int i = 0;
        Variable var = m.getParamAt( i );
        while ( var != null )
        {
            if ( i > 0 )
            {
                System.out.print( ", " );
            }
            System.out.print( getTypeString( var.type() ) + " " + var.id() );
            var = m.getParamAt( ++i );
        }
        System.out.println( ")" );
    }

    private void print( Formal f, Method m, Class c )
    {
        Variable var = m.getParam( f.i.toString() );
        if ( var == null )
        {
            System.out.println( "Unable to get parameter " + f.i.toString() + " for " + c.getId() + "::" + m.getId() );
            System.exit( -1 );
        }
        idRef = var.idRef();
        // IdRef-of-X, Param, type-of-X, Bar::foo()
        System.out.println( var.id() + " " + var.idRef() + ", Param, " + getTypeString( var.type() ) + ", " + c.getId() + "::" +
                            m.getId() );
    }

    private void print( IdentifierExp i )
    {
        // R,C: M
        System.out.println( i.beginLine + "," + i.beginColumn + ": " + i.s + " " + idRef );
    }

    private void print( Identifier i )
    {
        // R,C: M
        System.out.println( i.beginLine + "," + i.beginColumn + ": " + i.s + " " + idRef );
    }
}



