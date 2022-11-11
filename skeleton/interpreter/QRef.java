package interpreter;

class QRef extends QVal {
    QObj referent;
    public QRef(QObj ref){
        this.referent = ref;
    }

    @Override
    public String toString(){
        return this.referent.toString();
    }
}
