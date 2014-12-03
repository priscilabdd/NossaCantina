
package com.senac.exceptions;


public class EmptyFieldException extends RuntimeException{
    public EmptyFieldException(){
        super("Preencher todos os campos");
    }
}
