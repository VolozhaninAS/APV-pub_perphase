package org.example.iec61850.datatypes.common;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Quality extends Data {
    private Attribute<VALIDITY> validity = new Attribute<>(); // Идентификатор качества
    private Attribute<Boolean> overflow = new Attribute<>(false); // Переполнение
    private Attribute<Boolean> outOfRange = new Attribute<>(false); //  За пределами предопределенного диапазона
    private Attribute<Boolean> badReference = new Attribute<>(false); // Источник информации не откалиброван
    private Attribute<Boolean> oscillatory = new Attribute<>(false); // Колебательный идентификатор качества
    private Attribute<Boolean> failure = new Attribute<>(false); // Повреждение
    private Attribute<Boolean> oldData = new Attribute<>(false); // Обновление давно не производилось
    private Attribute<Boolean> inconsistent = new Attribute<>(false); // Несогласованный
    private Attribute<Boolean> inaccurate = new Attribute<>(false); // Неудовлетворительная точность
    private SOURCE source = SOURCE.PROCESS; // Идентификатор происхождения значения (получен из процесса)
    private Attribute<Boolean> test = new Attribute<>(false); // Для указания тестового значения
    private Attribute<Boolean> operatorBlocked = new Attribute<>(false); // Оператор блокирует дальнейшие обновления

    public enum VALIDITY{
        GOOD, INVALID, RESERVED, QUESTIONABLE
    }
    public enum SOURCE{
        PROCESS, SUBSTITUTED
    }
}
