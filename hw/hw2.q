int isList(Q list) {
    if (isAtom(list) != 0) {
        if (isNil(list) != 0) {
            return 1;
        } else { 
            return 0;
        }    
    } else {
        return isList(right((Ref)list));
    }
    return -1;
}

Ref append(Ref leftList, Ref rightList) {
    if(isNil(leftList) != 0) {
        return rightList;
    } else {
        return ((Ref)left(leftList) . append((Ref)right(leftList), rightList));
    }
    return nil;
}

Ref reverse(Ref list){
    if(isNil(list) != 0){
        return list;
    } else {
        return append(reverse((Ref)right(list)), ((Ref)left(list) . nil));
    }
    return nil;
}

int isSorted(Ref lists){
    if(isNil(lists) != 0){
        return 1;
    } else {
        int oldLength = getLength((Ref)left(lists));
        if(isNil((Ref)right(lists)) == 0){
            Ref nextList = (Ref)left((Ref)right(lists));
            int newLength = getLength(nextList);
            if(oldLength <= newLength){
                return isSorted((Ref)right(lists));
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }
    return -1;
}

int getLength(Ref l){
    if(isNil(l) == 1){
        return 0;
    } else {
        return 1 + getLength((Ref)right(l));
    }
    return -1;
}