int isList(Q list) {
    if (isAtom(list) == 1) {
        if (isNil(list) == 1) {
            return 1;
        } else { 
            return 0;
        }    
    } else {
        Ref sublist = (Ref)list;
        return isList(right(sublist));
    }

    return nil;
}

Ref append(Ref left, Ref right) {
    if(isNil(left) == 1) {
        return right;
    } else {
        return (left(left) . append(right(left), right));
    }

    return nil;
}

Ref reverse(Ref list){
    if(isNil(list) == 1){
        return list;
    } else {
        return append(reverse(right(list)), (left(list) . nil));
    }
    return nil;
}

int isSorted(Ref list){

}