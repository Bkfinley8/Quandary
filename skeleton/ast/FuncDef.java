package ast;

public class FuncDef extends ASTNode {
    VarDecl returnDecl;
    FormalDeclList formalDeclList;
    StatementList stmtList;

    public FuncDef(VarDecl varDecl, FormalDeclList formalDeclList, StatementList stmtList, Location loc){
        super(loc);
        this.returnDecl = varDecl;
        this.formalDeclList = formalDeclList;
        this.stmtList = stmtList;
    }

    public VarDecl getReturnDecl(){
        return this.returnDecl;
    }

    public FormalDeclList getFormalDeclList(){
        return this.formalDeclList;
    }

    public StatementList getStatementList(){
        return this.stmtList;
    }

    public boolean hasDeclList(){
        return this.formalDeclList != null;
    }
}
