package org.example.iec61850.datatypes.common;


import lombok.Getter;
import lombok.Setter;

/**
 * Должен содержать сведения об инициаторе последнего изменения атрибута данных
 */

@Getter @Setter
public class Originator extends Data{
    private Attribute<OrCat> orCat = new Attribute<>(); // Категория инициатора
    private Attribute<String> orIdent = new Attribute<>(); // Адрес инициатора

    public enum OrCat{
        NOT_SUPPORTED, BAY_CONTROL, STATION_CONTROL,
        REMOTE_CONTROL, AUTOMATIC_BAY, AUTOMATIC_STATION,
        AUTOMATIC_REMOTE, MAINTENANCE, PROCESS
    }
}
