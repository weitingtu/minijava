package visitor;
import syntaxtree.*;

public class UnknownIdentifierVisitor extends DepthFirstVisitor
{

    static Class currClass;
    static Method currMethod;
    static SymbolTable symbolTable;
    private int idRef;
    private int unknownIdCount;

    public int getUnknownIdCount()
    {
        return unknownIdCount;
    }

    public UnknownIdentifierVisitor( SymbolTable s )
    {
        symbolTable = s;
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit( MainClass n )
    {
        String i1 = n.i1.toString();
        currClass = symbolTable.getClass( i1 );

        // ignore main parameter checking
        //n.i2.accept( this );
        n.s.accept( this );
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit( ClassDeclSimple n )
    {
        String id = n.i.toString();
        currClass = symbolTable.getClass( id );
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
        //n.j.accept( this );
        if ( !symbolTable.containsClass( n.j.toString() ) )
        {
            System.out.println( n.j.toString() + ": Unknown identifier in ClassDeclExtends ( " + n.j.beginLine + "," + n.j.beginColumn + " )" );
            unknownIdCount++;
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
        //currMethod = currClass.getMethod( id );
        currMethod = currClass.getMethod( id, n.fl );
        /*for ( int i = 0; i < n.fl.size(); i++ )
        {
            n.fl.elementAt( i ).accept( this );
        }*/
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

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit( Call n )
    {
        n.e.accept( this );
        // Ignore method name check
        //n.i.accept( this );
        for ( int i = 0; i < n.el.size(); i++ )
        {
            n.el.elementAt( i ).accept( this );
        }
    }

    // String s;
    public void visit( IdentifierExp n )
    {
        if ( !find( n.s, currClass, currMethod ) )
        {
            // <Identifier’s value>: Unknown identifier (<Location of the identifier>)
            System.out.println( n.s + ": Unknown identifier in IdentifierExp ( " + n.beginLine + "," + n.beginColumn + " )" );
            unknownIdCount++;
        }
    }

    // String s;
    public void visit( Identifier n )
    {
        if ( !find( n.s, currClass, currMethod ) )
        {
            // <Identifier’s value>: Unknown identifier (<Location of the identifier>)
            System.out.println( n.s + ": Unknown identifier in Identifier ( " + n.beginLine + "," + n.beginColumn + " )" );
            unknownIdCount++;
        }
    }

    private boolean find( String id, Method m )
    {
        if ( m == null )
        {
            return false;
        }

        return m.containsParam( id ) || m.containsVar( id );
    }

    private boolean find( String id, Class c )
    {
        if ( c == null )
        {
            return false;
        }

        if ( c.containsVar( id ) )
        {
            return true;
        }
        if ( c.parent() == null )
        {
            return false;
        }
        return find( id, symbolTable.getClass( c.parent() ) );
    }

    private boolean find( String id, Class c, Method m )
    {
        if ( find( id, m ) )
        {
            return true;
        }
        return find( id, c );
    }
}



