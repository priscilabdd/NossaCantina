
package com.senac.exceptions;


public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException(){
        super("Senha incorreta");
    }
}
