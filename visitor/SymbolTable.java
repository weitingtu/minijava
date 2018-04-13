package visitor;
import syntaxtree.*;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Vector;
import java.lang.Object;

enum TypeEnum
{
    INT_ARRAY, DOUBLE_ARRAY, BOOLEAN, INTEGER, DOUBLE, IDENTIFIER;
    static public TypeEnum getTypeEnum( Type t )
    {
        if ( t instanceof IntArrayType )
        {
            return TypeEnum.INT_ARRAY;
        }
        if ( t instanceof DoubleArrayType )
        {
            return TypeEnum.DOUBLE_ARRAY;
        }
        if ( t instanceof BooleanType )
        {
            return TypeEnum.BOOLEAN;
        }
        if ( t instanceof IntegerType )
        {
            return TypeEnum.INTEGER;
        }
        if ( t instanceof DoubleType )
        {
            return TypeEnum.DOUBLE;
        }
        if ( t instanceof IdentifierType )
        {
            return TypeEnum.IDENTIFIER;
        }
        System.exit( -1 ); // Panic!
        return TypeEnum.INT_ARRAY;
    }
}

// The global Symbol Table that maps class name to Class
class SymbolTable
{
    private Hashtable<String, Class> hashtable;

    public SymbolTable()
    {
        hashtable = new Hashtable<String, Class>();
    }

    // Register the class name and map it to a new class (with its supperclass)
    // Return false if there is a name conflicts. Otherwise return true.
    public boolean addClass( String id, int idRef, String parent )
    {
        if ( containsClass( id ) )
        {
            return false;
        }
        else
        {
            hashtable.put( id, new Class( id, idRef, parent ) );
        }
        return true;
    }

    // Return the Class that previously mapped to the specified name.
    // Return null if the specified is not found.
    public Class getClass( String id )
    {
        if ( containsClass( id ) )
        {
            return ( Class )hashtable.get( id );
        }
        else
        {
            return null;
        }
    }

    public boolean containsClass( String id )
    {
        return hashtable.containsKey( id );
    }

    // Given a variable "id" that is used in method "m" inside class "c",
    // return the type of the variable. It returns null if the variable
    // is not yet defined.
    // If "m" is null, check only fields in class "c" or in its ancestors.
    // If "c" is null, check only the variables declared in "m".
    public Type getVarType( Method m, Class c, String id )
    {

        if ( m != null )
        {
            // Check if the variable is one of the local variables
            if ( m.getVar( id ) != null )
            {
                return m.getVar( id ).type();
            }

            // Check if the variable is one of the formal parameters
            if ( m.getParam( id ) != null )
            {
                return m.getParam( id ).type();
            }
        }

        // Try to resolve the name against fields in class
        while ( c != null )
        {
            // Check if the variables is one of the fields in the current class
            if ( c.getVar( id ) != null )
            {
                return c.getVar( id ).type(); // Found!
            }
            else // Try its superclass (and their superclasses)
                if ( c.parent() == null )
                {
                    c = null;
                }
                else
                {
                    c = getClass( c.parent() );
                }
        }

        System.out.println( "Variable " + id
                            + " not defined in current scope" );
        System.exit( -1 ); // Panic!
        return null;
    }

    // Return the declared method defined in the class named "cName"
    // (or in one of its ancestors)
    public Vector<Method> getMethod( String id, String cName )
    {
        Vector<Method> results = new Vector<Method>();
        Class c = getClass( cName );

        if ( c == null )
        {
            System.out.println( "Class " + cName + " not defined" );
            System.exit( -1 ); // Panic!
        }

        // Try to find the declared method along the class hierarchy
        while ( c != null )
        {
            Set<MethodDef> keys = c.methoddefs.keySet();
            Iterator<MethodDef> itr = keys.iterator();
            while ( itr.hasNext() )
            {
                MethodDef md = itr.next();
                if ( md.id.equals( id ) )
                {
                    results.addElement( c.methoddefs.get( md ) );
                }
            }
            if ( c.parent() == null )
            {
                c = null;
            }
            else
            {
                c = getClass( c.parent() );
            }
        }

        if ( results.size() == 0 )
        {
            System.out.println( "Method " + id + " not defined in class " + cName );

            System.exit( -1 );
        }
        return results;
    }

    public boolean checkMethod( String id, String cName, Type type, FormalList fl )
    {
        Vector<TypeEnum> params = new Vector<TypeEnum>();
        for ( int i = 0; i < fl.size(); i++ )
        {
            params.addElement( TypeEnum.getTypeEnum( fl.elementAt( i ).t ) );
        }

        Class c = getClass( cName );

        if ( c == null )
        {
            System.out.println( "Class " + cName + " not defined" );
            System.exit( -1 ); // Panic!
        }

        if ( c.containsMethod( id, params ) )
        {
            System.out.println( "Method " + id + " is already defined in class " + cName );
            return false;
        }

        while ( c != null )
        {
            if ( c.containsMethod( id, params ) )
            {
                Method m = c.getMethod( id, fl );
                if ( !compareTypes( type, m.type() ) )
                {
                    System.out.println( "return type of " + cName + "::"  + id + " is not compatible with "
                                        + c.getId() + "::" + m.getId() );
                    return false;
                }
            }

            if ( c.parent() == null )
            {
                c = null;
            }
            else
            {
                c = getClass( c.parent() );
            }
        }

        return true;
    }


    // Utility method to check if t1 is compatible with t2
    // or if t1 is a subclass of t2
    // Note: This method can be placed in another class
    public boolean compareTypes( Type t1, Type t2 )
    {
        if ( t1 == null || t2 == null ) { return false; }

        if ( t1 instanceof IntegerType && t2 instanceof  IntegerType )
        {
            return true;
        }
        if ( t1 instanceof DoubleType && t2 instanceof  DoubleType )
        {
            return true;
        }
        if ( t1 instanceof IntegerType && t2 instanceof  DoubleType )
        {
            return true;
        }
        if ( t1 instanceof DoubleType && t2 instanceof  IntegerType )
        {
            return true;
        }
        if ( t1 instanceof BooleanType && t2 instanceof  BooleanType )
        {
            return true;
        }
        if ( t1 instanceof IntArrayType && t2 instanceof IntArrayType )
        {
            return true;
        }
        if ( t1 instanceof DoubleArrayType && t2 instanceof DoubleArrayType )
        {
            return true;
        }

        // If both are classes
        if ( t1 instanceof IdentifierType && t2 instanceof IdentifierType )
        {
            IdentifierType i1 = ( IdentifierType )t1;
            IdentifierType i2 = ( IdentifierType )t2;

            Class c = getClass( i2.s );
            while ( c != null )
            {
                // If two classes has the same name
                if ( i1.s.equals( c.getId() ) )
                {
                    return true;
                }
                else   // Check the next class along the class heirachy
                {

                    if ( c.parent() == null )
                    {
                        return false;
                    }

                    c = getClass( c.parent() );
                }
            }
        }
        return false;
    }

} // SymbolTable

// Store all properties that describe a class
class Class
{

    String id;      // Class name
    int idRef;
    Hashtable<String, Method> methods;
    Hashtable<MethodDef, Method> methoddefs;
    Hashtable<String, Variable> fields;
    String parent;  // Superclass's name  (null if there is no superclass)
    Type type;      // An instance of Type that represents this class

    // Model a class named "id" that extend a class name "p"
    // "p" is null if class "id" does has extend any class
    public Class( String id, int idRef, String p )
    {
        this.id = id;
        this.idRef = idRef;
        parent = p;
        type = new IdentifierType( id );
        methods = new Hashtable<String, Method>();
        methoddefs = new Hashtable<MethodDef, Method>();
        fields = new Hashtable<String, Variable>();
    }

    public Class() {}

    public String getId() { return id; }

    public int getIdRef() { return idRef; }

    public Type type() { return type; }

    public TypeEnum getEnumType( Type t )
    {
        //if ( t instanceof IntArrayType )
        {
            return TypeEnum.INT_ARRAY;
        }
        //ARRAY, DOUBLE_ARRAY, BOOLEAN, INTEGER, DOUBLE, IDENTIFIER;

    }

    // Add a method defined in the current class by registering
    // its name along with its return type.
    // The other properties (parameters, local variables) of the method
    // will be added later
    //
    // Return false if there is a name conflict (among all method names only)
    public boolean addMethod( String id, String cName, int idRef, Type type, FormalList fl )
    {
        Vector<TypeEnum> params = new Vector<TypeEnum>();
        for ( int i = 0; i < fl.size(); i++ )
        {
            params.addElement( TypeEnum.getTypeEnum( fl.elementAt( i ).t ) );
        }

        if ( containsMethod( id, params ) )
        {
            //System.out.println( "Method " + id + " is already defined in class " + this.id );
            return false;
        }
        else
        {
            methods.put( id, new Method( id, cName, idRef, type ) );
            methoddefs.put( new MethodDef( id, params ), new Method( id, cName, idRef, type ) );
            return true;
        }
    }

    public Method getMethod( String id, FormalList fl )
    {
        Vector<TypeEnum> params = new Vector<TypeEnum>();
        for ( int i = 0; i < fl.size(); i++ )
        {
            params.addElement( TypeEnum.getTypeEnum( fl.elementAt( i ).t ) );
        }

        if ( containsMethod( id, params ) )
        {
            return ( Method )methoddefs.get( new  MethodDef( id, params ) );
        }
        else
        {
            return null;
        }
    }

    // Add a field
    // Return false if there is a name conflict (among all fields only)
    public boolean addVar( Identifier id, int idRef, Type type )
    {
        if ( fields.containsKey( id.s ) )
        {
            return false;
        }
        else
        {
            fields.put( id.s, new Variable( id, idRef, type ) );
            return true;
        }
    }

    // Return a field with the specified name
    public Variable getVar( String id )
    {
        if ( containsVar( id ) )
        {
            return ( Variable )fields.get( id );
        }
        else
        {
            return null;
        }
    }

    public boolean containsVar( String id )
    {
        return fields.containsKey( id );
    }

    public boolean containsMethod( String id, Vector<TypeEnum> params )
    {
        return methoddefs.containsKey( new MethodDef( id, params )  );
    }

    public String parent()
    {
        return parent;
    }
} // Class

// Store all properties that describe a variable
class Variable
{

    Identifier id;
    int idRef;
    Type type;

    public Variable( Identifier id, int idRef, Type type )
    {
        this.id = id;
        this.idRef = idRef;
        this.type = type;
    }

    public String id() { return id.s; }

    public Identifier i() { return id; }

    public int idRef() { return idRef; }

    public Type type() { return type; }

} // Variable

// Store all properties that describe a variable
class MethodDef
{
    String id;  // Method name
    Vector<TypeEnum> params;          // Formal parameters

    public MethodDef( String id, Vector<TypeEnum> params )
    {
        this.id = id;
        this.params = params;
    }

    @Override
    public int hashCode()
    {
        //System.out.println( "hash code " +id + " " + id.hashCode() + " " +params+ " " +params.hashCode() );
        //return id.hashCode() + params.hashCode();
        return id.hashCode();
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        MethodDef other = ( MethodDef ) obj;
        return id.equals( other.id ) && params.equals( other.params );
    }

}

class Method
{

    String id;  // Method name
    String cName;
    int idRef;
    Type type;  // Return type
    Vector<Variable> params;          // Formal parameters
    Hashtable<String, Variable> vars; // Local variables

    public Method( String id, String cladd_id, int idRef, Type type )
    {
        this.id = id;
        this.cName = cName;
        this.idRef = idRef;
        this.type = type;
        params = new Vector<Variable>();
        vars = new Hashtable<String, Variable>();
    }

    public String getId() { return id; }

    public int getIdRef() { return idRef; }

    public Type type() { return type; }

    // Add a formal parameter
    // Return false if there is a name conflict
    public boolean addParam( Identifier id, int idRef, Type type )
    {
        if ( containsParam( id.s ) )
        {
            return false;
        }
        else
        {
            params.addElement( new Variable( id, idRef, type ) );
            return true;
        }
    }

    public Enumeration getParams()
    {
        return params.elements();
    }

    // Return a formal parameter by position (i=0 means 1st parameter)
    public Variable getParamAt( int i )
    {
        if ( i < params.size() )
        {
            return ( Variable )params.elementAt( i );
        }
        else
        {
            return null;
        }
    }

    // Add a local variable
    // Return false if there is a name conflict
    public boolean addVar( Identifier id, int idRef, Type type )
    {
        if ( containsParam( id.s ) )
        {
            return false;
        }
        else if ( vars.containsKey( id.s ) )
        {
            return false;
        }
        else
        {
            vars.put( id.s, new Variable( id, idRef, type ) );
            return true;
        }
    }

    public boolean containsVar( String id )
    {
        return vars.containsKey( id );
    }

    public boolean containsParam( String id )
    {
        for ( int i = 0; i < params.size(); i++ )
        {
            if ( ( ( Variable )params.elementAt( i ) ).id().equals( id ) )
            {
                return true;
            }
        }
        return false;
    }

    public Variable getVar( String id )
    {
        if ( containsVar( id ) )
        {
            return ( Variable )vars.get( id );
        }
        else
        {
            return null;
        }
    }

    // Return a formal parameter by name
    public Variable getParam( String id )
    {
        for ( int i = 0; i < params.size(); i++ )
            if ( ( ( Variable )params.elementAt( i ) ).id().equals( id ) )
            {
                return ( Variable )( params.elementAt( i ) );
            }

        return null;
    }

    void printParams()
    {
        for ( int i = 0; i < params.size(); i++ )
        {
            System.out.print( ( ( Variable )params.elementAt( i ) ).id() );
        }
        System.out.println();
    }

} // Method


