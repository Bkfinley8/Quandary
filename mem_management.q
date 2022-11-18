Q int main(int arg){
    if(arg == 1){
        mutable int i = 0;
        Ref list = 0 . 0;
        Ref temp = list;
        while(i < 17){
            setRight(temp, (i . i));
            temp = (Ref)right(temp);
            i = i + 1;
        }
    } else if(arg == 2){
        Ref a;
        Ref b;
        mutable int i = 0;
        while (i < 9){
            a =  1.nil;
            b = 2.nil;
            setRight(a,b);
            setRight(b,a);
 
            i = i + 1;
        }
    }  else if(arg == 3){
        int i = 0;
        Ref list = 0 . 0;
        Ref temp = list;
        while(i < 16){
            setRight(temp, (i . i));
            temp = (Ref)right(temp);
            i = i + 1;
        }
        Ref x = nil . 2;
        free(x);   
    }  else if(arg == 4){
        Ref a;
        Ref b;
        mutable int i = 0;
        while (i < 9){
            a =  1.nil;
            b = 2.nil;
            setRight(a,b);
            setRight(b,a);
            i = i + 1;
        }
    }
    return 0;
}