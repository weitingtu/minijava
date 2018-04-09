package syntaxtree;

import visitor.TypeVisitor;
import visitor.Visitor;

public class LocalVarDeclaration extends ForInit
{
    public Type t;
    public AssignExprList al;
    public LocalVarDeclaration( Type at, AssignExprList aal )
    {
        t = at;
        al = aal;
    }


    @Override
    public void accept( Visitor v )
    {
        v.visit( this );
    }

    @Override
    public Type accept( TypeVisitor v )
    {
        return v.visit( this );
    }
}
