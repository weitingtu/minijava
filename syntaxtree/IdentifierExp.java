package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class IdentifierExp extends Exp
{
    public String s;

    /** The line number of the first character of this Token. */
    public int beginLine;
    /** The column number of the first character of this Token. */
    public int beginColumn;
    /** The line number of the last character of this Token. */
    public int endLine;
    /** The column number of the last character of this Token. */
    public int endColumn;

    public IdentifierExp( String as, int br, int bc, int er, int ec )
    {
        s = as;
        beginLine = br;
        beginColumn = bc;
        beginLine = br;
        beginColumn = bc;
    }

    public IdentifierExp( String as )
    {
        s = as;
    }

    public void accept( Visitor v )
    {
        v.visit( this );
    }

    public Type accept( TypeVisitor v )
    {
        return v.visit( this );
    }
}
