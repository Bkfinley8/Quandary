package ast;

public class FormalDeclList extends ASTNode{

    NEmptyFormalDeclList list;

    public FormalDeclList(NEmptyFormalDeclList list, Location loc){
        super(loc);
        this.list = list;
    }

    public NEmptyFormalDeclList getList(){
        return this.list;
    }

    public boolean isEmpty(){
        return this.list == null;
    }
}
