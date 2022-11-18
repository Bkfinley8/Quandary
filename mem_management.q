mutable Q main(int arg){
    if(arg == 1){
        mutable int i = 0;
        mutable Ref list = 0 . nil;
        mutable Ref temp = list;
        while(i < 16){
            setRight(temp, (i . nil));
            temp = (Ref)right(temp);
            i = i + 1;
        }
    } else if(arg == 2){
        mutable int i = 0;
        while (i < 9){
            Ref a =  1.nil;
            Ref b = 2.nil;
            setRight(a,b);
            setRight(b,a);
 
            i = i + 1;
        }
    }  else if(arg == 3){
        mutable int i = 0;
        mutable Ref list = 0 . nil;
        mutable Ref temp = list;
        while(i < 14){
            setRight(temp, (i . nil));
            temp = (Ref)right(temp);
            i = i + 1;
        }
        Ref x = nil . 2;
        free(x);   
        Ref y = nil . 2;
    } else if(arg == 4){
        mutable int i = 0;
        while (i < 9){
            Ref a =  1.nil;
            Ref b = 2.nil;
            setRight(a,b);
            setRight(b,a);
 
            i = i + 1;
        }
    }
    return 0;
}