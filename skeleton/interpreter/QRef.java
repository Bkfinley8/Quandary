package interpreter;

class QRef extends QVal {
    QObj referent;
    public QRef(QObj ref){
        this.referent = ref;
    }

    public QObj getRef(){
        return this.referent;
    }

    @Override
    public String toString(){
        if(this.referent == null){
            return "nil";
        }
        return this.referent.toString();
    }
}
