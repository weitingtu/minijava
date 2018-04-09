package syntaxtree;
import java.util.Vector;

import syntaxtree.StmtExpr;
import visitor.TypeVisitor;
import visitor.Visitor;

public class StmtExprList extends ForInit
{
    private Vector<StmtExpr> list;

    public StmtExprList()
    {
        list = new Vector<StmtExpr>();
    }

    public void addElement( StmtExpr n )
    {
        list.addElement( n );
    }

    public StmtExpr elementAt( int i )
    {
        return ( StmtExpr )list.elementAt( i );
    }

    public int size()
    {
        return list.size();
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
