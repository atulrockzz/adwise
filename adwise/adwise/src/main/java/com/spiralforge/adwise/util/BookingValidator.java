package com.spiralforge.adwise.util;

public interface BookingValidator<E, T> {

	Boolean validate(E e, T t);
}
