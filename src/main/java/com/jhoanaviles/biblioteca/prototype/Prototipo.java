package com.jhoanaviles.biblioteca.prototype;

/**
 * Interface genérica para el patrón creacional Prototype.
 * Define el contrato para la clonación de objetos en memoria.
 **/
public interface Prototipo<T> {
    T clonar();
}