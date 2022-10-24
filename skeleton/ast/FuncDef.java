package ast;

public class FuncDef extends ASTNode {
    VarDecl varDecl;
    FormalDeclList formalDeclList;
    StatementList stmtList;

    public FuncDef(VarDecl varDecl, FormalDeclList formalDeclList, StatementList stmtList, Location loc){
        super(loc);
        this.varDecl = varDecl;
        this.formalDeclList = formalDeclList;
        this.stmtList = stmtList;
    }
}
