package interpreter;
import java.util.concurrent.atomic.AtomicBoolean;

public class QObj {
    QVal left;
    QVal right;
    AtomicBoolean lock = new AtomicBoolean(false);
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

    public AtomicBoolean getLock(){
        return lock;
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
