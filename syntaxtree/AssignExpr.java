package syntaxtree;

import visitor.TypeVisitor;
import visitor.Visitor;

public class AssignExpr extends StmtExpr
{
    public Identifier i;
    public Exp e;

    public AssignExpr( Identifier ai, Exp ae )
    {
        i = ai;
        e = ae;
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
