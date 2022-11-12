Q main(int arg){
    mutable int a = 0;
    while(a < 3) {
        print a;
        a = a + 1;
        if(a == 2){
            return 100000;
        }
    }

    return 99;
}