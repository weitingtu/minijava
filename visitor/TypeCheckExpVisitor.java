package visitor;
import syntaxtree.*;
import java.util.Vector;

public class TypeCheckExpVisitor extends TypeDepthFirstVisitor
{


    // Exp e1,e2;
    public Type visit( And n )
    {
        if ( ! ( n.e1.accept( this ) instanceof BooleanType ) )
        {
            System.out.println( "Left side of And must be of type boolean" );
            System.exit( -1 );
        }
        if ( ! ( n.e2.accept( this ) instanceof BooleanType ) )
        {
            System.out.println( "Right side of And must be of type boolean" );
            System.exit( -1 );
        }
        return new BooleanType();
    }

    // Exp e1,e2;
    public Type visit( LessThan n )
    {
        if ( ! ( ( n.e1.accept( this ) instanceof IntegerType )
                 || ( n.e1.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Left side of LessThan must be of type integer or double" );
            System.exit( -1 );
        }
        if ( ! ( ( n.e2.accept( this ) instanceof IntegerType )
                 || ( n.e2.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Right side of LessThan must be of type integer or double" );
            System.exit( -1 );
        }
        return new BooleanType();
    }

    // Exp e1,e2;
    public Type visit( Plus n )
    {
        if ( ! ( ( n.e1.accept( this ) instanceof IntegerType )
                 || ( n.e1.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Left side of PLus must be of type integer or double" );
            System.exit( -1 );
        }
        if ( ! ( ( n.e1.accept( this ) instanceof IntegerType )
                 || ( n.e1.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Right side of Plus must be of type integer or double" );
            System.exit( -1 );
        }
        return new IntegerType();
    }

    // Exp e1,e2;
    public Type visit( Minus n )
    {
        if ( ! ( ( n.e1.accept( this ) instanceof IntegerType )
                 || ( n.e1.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Left side of Minus must be of type integeri or double" );
            System.exit( -1 );
        }
        if ( ! ( ( n.e1.accept( this ) instanceof IntegerType )
                 || ( n.e1.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Right side of Minus must be of type integer or double" );
            System.exit( -1 );
        }
        return new IntegerType();
    }

    // Exp e1,e2;
    public Type visit( Times n )
    {
        if ( ! ( ( n.e1.accept( this ) instanceof IntegerType )
                 || ( n.e1.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Left side of Times must be of type integer" );
            System.exit( -1 );
        }
        if ( ! ( ( n.e1.accept( this ) instanceof IntegerType )
                 || ( n.e1.accept( this ) instanceof DoubleType ) ) )
        {
            System.out.println( "Right side of Times must be of type integer" );
            System.exit( -1 );
        }
        return new IntegerType();
    }

    // Exp e1,e2;
    // e1 [e2]
    public Type visit( ArrayLookup n )
    {
        if ( ! ( ( n.e1.accept( this ) instanceof IntArrayType )
                 || ( n.e1.accept( this ) instanceof DoubleArrayType ) ) )
        {
            System.out.println( "Left side of ArrayLength must be of type integer or double" );
            System.exit( -1 );
        }
        if ( ! ( n.e2.accept( this ) instanceof IntegerType ) )
        {
            System.out.println( "Right side of ArrayLookup must be of type integer" );
            System.exit( -1 );
        }
        if ( n.e1.accept( this ) instanceof IntArrayType )
        {
            return new IntegerType();
        }
        else { return new DoubleType(); }
    }

    // Exp e;
    public Type visit( ArrayLength n )
    {
        if ( ! ( ( n.e.accept( this ) instanceof IntArrayType )
                 || ( n.e.accept( this ) instanceof DoubleArrayType ) ) )
        {
            System.out.println( "Left side of ArrayLength must be of type integer or double" );
            System.exit( -1 );
        }
        return new IntegerType();
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public Type visit( Call n )
    {

        if ( ! ( n.e.accept( this ) instanceof IdentifierType ) )
        {
            System.out.println( "method " + n.i.toString()
                                + "called  on something that is not a" +
                                " class or Object." );
            System.exit( -1 );
        }

        String mname = n.i.toString();
        String cname = ( ( IdentifierType ) n.e.accept( this ) ).s;

        //Method calledMethod = TypeCheckVisitor.symbolTable.getMethod( mname, cname );
        Vector<Method> methods = TypeCheckVisitor.symbolTable.getMethod( mname, cname );

        /*Method calledMethod = methods.elementAt(0);

        if ( n.el.size() != calledMethod.params.size() )
        {
            System.out.println( "Argument number mismatch "
                                + "( " + n.el.size() + " != " + calledMethod.params.size()
                                + " ) Type Error in arguments passed to " + cname + "." + mname );
            System.exit( -1 );
        }

        for ( int i = 0; i < n.el.size(); i++ )
        {
            Type t1 = null;
            Type t2 = null;

            if ( calledMethod.getParamAt( i ) != null )
            {
                t1 = calledMethod.getParamAt( i ).type();
            }
            t2 = n.el.elementAt( i ).accept( this );
            if ( !TypeCheckVisitor.symbolTable.compareTypes( t1, t2 ) )
            {
                System.out.println( "Type Error in arguments passed to " +
                                    cname + "." + mname );
                System.exit( -1 );
            }
        }

        //return TypeCheckVisitor.symbolTable.getMethodType( mname, cname );
        return calledMethod.type();
        */
        Method calledMethod = null;
        for ( int j = 0; j < methods.size(); j++ )
        {
            calledMethod = methods.elementAt( j );

            if ( n.el.size() != calledMethod.params.size() )
            {
                calledMethod = null;
                continue;
            }

            for ( int i = 0; i < n.el.size(); i++ )
            {
                Type t1 = null;
                Type t2 = null;

                if ( calledMethod.getParamAt( i ) != null )
                {
                    t1 = calledMethod.getParamAt( i ).type();
                }
                t2 = n.el.elementAt( i ).accept( this );
                if ( !TypeCheckVisitor.symbolTable.compareTypes( t1, t2 ) )
                {
                    calledMethod = null;
                    break;
                }
            }
            if ( null != calledMethod )
            {
                break;
            }
        }
        if ( null == calledMethod )
        {
            System.out.println( "Type Error in arguments passed to " +
                                cname + "." + mname );
            System.exit( -1 );
        }
        return calledMethod.type();
    }

    // int i;
    public Type visit( IntegerLiteral n )
    {
        return new IntegerType();
    }

    // double d;
    public Type visit( DoubleLiteral n )
    {
        return new DoubleType();
    }

    public Type visit( True n )
    {
        return new BooleanType();
    }

    public Type visit( False n )
    {
        return new BooleanType();
    }

    // String s;
    public Type visit( IdentifierExp n )
    {
        return TypeCheckVisitor.symbolTable.getVarType( TypeCheckVisitor.currMethod,
                TypeCheckVisitor.currClass, n.s );
    }

    public Type visit( This n )
    {
        return TypeCheckVisitor.currClass.type();
    }

    // Exp e;
    public Type visit( NewArray n )
    {

        if ( ! ( n.e.accept( this ) instanceof IntegerType ) )
        {
            System.out.println( "Left side of NewArray must be of type integer" );
            System.exit( -1 );
        }
        return new IntArrayType();
    }

    // Exp e;
    public Type visit( NewDoubleArray n )
    {

        if ( ! ( n.e.accept( this ) instanceof IntegerType ) )
        {
            System.out.println( "Left side of NewDoubleArray must be of type integer" );
            System.exit( -1 );
        }
        return new DoubleArrayType();
    }

    // Identifier i;
    public Type visit( NewObject n )
    {
        return new IdentifierType( n.i.s );
    }

    // Exp e;
    public Type visit( Not n )
    {
        if ( ! ( n.e.accept( this ) instanceof BooleanType ) )
        {
            System.out.println( "Left side of Not must be of type integer" );
            System.exit( -1 );
        }
        return new BooleanType();
    }

}
//TypeCheckVisitor.






