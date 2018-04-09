package syntaxtree;

import java.util.Vector;

public class AssignExprList
{
    private Vector<AssignExpr> list;

    public AssignExprList()
    {
        list = new Vector<AssignExpr>();
    }

    public void addElement( AssignExpr n )
    {
        list.addElement( n );
    }

    public AssignExpr elementAt( int i )
    {
        return ( AssignExpr )list.elementAt( i );
    }

    public int size()
    {
        return list.size();
    }

}
