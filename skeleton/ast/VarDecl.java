package ast;

public class VarDecl{
    final int type;
    final String identifier;

    public VarDecl(int type, String ident) {
        this.type = type;
        this.identifier = ident;
    }

    public int getType(){
        return this.type;
    }

    public String getIdentifier(){
        return this.identifier;
    }
}
