package com.larskroll.common

import org.javatuples._

object TupleConversions {
    implicit def pair2Tuple[A, B](v: Pair[A, B]) = (v.getValue0, v.getValue1)
    implicit def triplet2Tuple[A, B, C](v: Triplet[A, B, C]) = (v.getValue0, v.getValue1, v.getValue2)
    
}