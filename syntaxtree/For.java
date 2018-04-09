package syntaxtree;
import visitor.TypeVisitor;
import visitor.Visitor;

public class For extends Statement
{
    public ForInit fi;
    public Exp e;
    public StmtExprList sel;
    public Statement s;

    public For( ForInit fi, Exp e, StmtExprList sel, Statement s )
    {
        this.fi = fi;
        this.e = e;
        this.sel = sel;
        this.s = s;
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
