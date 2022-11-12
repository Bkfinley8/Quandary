package ast;

public class VarDecl extends ASTNode{
    final int type;
    final String identifier;
    boolean isMutable;

    public VarDecl(int type, String ident, boolean isMutable, Location loc) {
        super(loc);
        this.type = type;
        this.identifier = ident;
        this.isMutable = isMutable;
    }

    public int getType(){
        return this.type;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    public boolean isMutable(){
        return isMutable;
    }
}
