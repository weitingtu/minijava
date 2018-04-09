package syntaxtree;

import visitor.TypeVisitor;
import visitor.Visitor;

public class ArrayAssignExpr extends StmtExpr
{
    public Identifier i;
    public Exp idx;
    public Exp e;
    public ArrayAssignExpr( Identifier ai, Exp aIdx, Exp ae )
    {
        i = ai;
        idx = aIdx;
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
