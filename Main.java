import syntaxtree.*;
import visitor.*;
import myparser.*;

public class Main
{
    public static void main( String [] args )
    {
        try
        {
            Program root = new MiniJavaParser( System.in ).Goal();

            // Build the symbol table
            // Check redeclaration
            BuildSymbolTableVisitor buildSymTab = new BuildSymbolTableVisitor();
            root.accept( buildSymTab );

            // Check unknown identifier
            root.accept( new UnknownIdentifierVisitor( buildSymTab.getSymTab() ) );

            // Find symbol usage
            if ( args.length == 1 )
            {
                System.out.println( "Naming and Scoping " + args[0] );
                SymbolUsageVisitor task1 =
                    new SymbolUsageVisitor( buildSymTab.getSymTab(), args[0] );
                root.accept( task1 );
            }

            // Type check
            TypeCheckVisitor typeCheck =
                new TypeCheckVisitor( buildSymTab.getSymTab() );
            root.accept( typeCheck );
        }
        catch ( ParseException e )
        {
            System.out.println( e.toString() );
        }
    }
}

