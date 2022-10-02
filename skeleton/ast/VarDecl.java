package ast;

public class VarDecl {

    public static final int INTTYPE = 1;

    final int type;
    final String identifier;

    public VarDecl(int type, String ident) {
        this.type = type;
        this.identifier = ident;
    }
}
