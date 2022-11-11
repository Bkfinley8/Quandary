package interpreter;

public class QObj {
    QVal left;
    QVal right;
    public QObj(QVal l, QVal r){
        this.left = l;
        this.right = r;
    }

    public QVal getLeft(){
        return this.left;
    }

    public QVal getRight(){
        return this.right;
    }

    @Override
    public String toString(){
        String ret = "";
        ret = "(" + this.left.toString() + " . " + this.right.toString() + ")";
        return ret;
    }


}
