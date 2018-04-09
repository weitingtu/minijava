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
            //root.accept( new UnknownIdentifierVisitor( buildSymTab.getSymTab() ) );
            UnknownIdentifierVisitor unknownId = new UnknownIdentifierVisitor( buildSymTab.getSymTab() );
            root.accept( unknownId );

            if ( buildSymTab.getRedeclarationCount() > 0 || unknownId.getUnknownIdCount() > 0 )
            {
                System.out.println( "Syntax error" );
                System.exit( -1 );
            }

            if ( args.length == 1 )
            {
                // Find symbol usage
                System.out.println( "Naming and Scoping " + args[0] );
                SymbolUsageVisitor task1 =
                    new SymbolUsageVisitor( buildSymTab.getSymTab(), args[0] );
                root.accept( task1 );
            }
            else
            {
                // Type check
                TypeCheckVisitor typeCheck =
                    new TypeCheckVisitor( buildSymTab.getSymTab() );
                root.accept( typeCheck );
            }
        }
        catch ( ParseException e )
        {
            System.out.println( e.toString() );
        }
    }
}

