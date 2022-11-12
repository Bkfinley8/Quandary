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

    public void setLeft(QVal val){
        this.left = val;
    }

    public void setRight(QVal val){
        this.right = val;  
    }

    @Override
    public String toString(){
        if(this.right == null){
            return "(" + this.left.toString() + " . " + "nil)";
        }
        String ret = "";
        ret = "(" + this.left.toString() + " . " + this.right.toString() + ")";
        return ret;
    }


}
